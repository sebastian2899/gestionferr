import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAbono, getAbonoIdentifier } from '../abono.model';

export type EntityResponseType = HttpResponse<IAbono>;
export type EntityArrayResponseType = HttpResponse<IAbono[]>;

@Injectable({ providedIn: 'root' })
export class AbonoService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/abonos');
  protected AbonosPorFacturaUrl = this.applicationConfigService.getEndpointFor('api/abonosFactura');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(abono: IAbono): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(abono);
    return this.http
      .post<IAbono>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(abono: IAbono): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(abono);
    return this.http
      .put<IAbono>(`${this.resourceUrl}/${getAbonoIdentifier(abono) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(abono: IAbono): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(abono);
    return this.http
      .patch<IAbono>(`${this.resourceUrl}/${getAbonoIdentifier(abono) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IAbono>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAbono[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  abonosPorFactura(id: number, codigo: number): Observable<EntityArrayResponseType> {
    return this.http
      .get<IAbono[]>(`${this.AbonosPorFacturaUrl}/${id}/${codigo}`, { observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addAbonoToCollectionIfMissing(abonoCollection: IAbono[], ...abonosToCheck: (IAbono | null | undefined)[]): IAbono[] {
    const abonos: IAbono[] = abonosToCheck.filter(isPresent);
    if (abonos.length > 0) {
      const abonoCollectionIdentifiers = abonoCollection.map(abonoItem => getAbonoIdentifier(abonoItem)!);
      const abonosToAdd = abonos.filter(abonoItem => {
        const abonoIdentifier = getAbonoIdentifier(abonoItem);
        if (abonoIdentifier == null || abonoCollectionIdentifiers.includes(abonoIdentifier)) {
          return false;
        }
        abonoCollectionIdentifiers.push(abonoIdentifier);
        return true;
      });
      return [...abonosToAdd, ...abonoCollection];
    }
    return abonoCollection;
  }

  protected convertDateFromClient(abono: IAbono): IAbono {
    return Object.assign({}, abono, {
      fechaCreacion: abono.fechaCreacion?.isValid() ? abono.fechaCreacion.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.fechaCreacion = res.body.fechaCreacion ? dayjs(res.body.fechaCreacion) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((abono: IAbono) => {
        abono.fechaCreacion = abono.fechaCreacion ? dayjs(abono.fechaCreacion) : undefined;
      });
    }
    return res;
  }
}
