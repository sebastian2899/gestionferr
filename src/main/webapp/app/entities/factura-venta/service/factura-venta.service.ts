import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFacturaVenta, getFacturaVentaIdentifier } from '../factura-venta.model';

export type EntityResponseType = HttpResponse<IFacturaVenta>;
export type EntityArrayResponseType = HttpResponse<IFacturaVenta[]>;

@Injectable({ providedIn: 'root' })
export class FacturaVentaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/factura-ventas');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(facturaVenta: IFacturaVenta): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(facturaVenta);
    return this.http
      .post<IFacturaVenta>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(facturaVenta: IFacturaVenta): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(facturaVenta);
    return this.http
      .put<IFacturaVenta>(`${this.resourceUrl}/${getFacturaVentaIdentifier(facturaVenta) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(facturaVenta: IFacturaVenta): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(facturaVenta);
    return this.http
      .patch<IFacturaVenta>(`${this.resourceUrl}/${getFacturaVentaIdentifier(facturaVenta) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IFacturaVenta>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IFacturaVenta[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addFacturaVentaToCollectionIfMissing(
    facturaVentaCollection: IFacturaVenta[],
    ...facturaVentasToCheck: (IFacturaVenta | null | undefined)[]
  ): IFacturaVenta[] {
    const facturaVentas: IFacturaVenta[] = facturaVentasToCheck.filter(isPresent);
    if (facturaVentas.length > 0) {
      const facturaVentaCollectionIdentifiers = facturaVentaCollection.map(
        facturaVentaItem => getFacturaVentaIdentifier(facturaVentaItem)!
      );
      const facturaVentasToAdd = facturaVentas.filter(facturaVentaItem => {
        const facturaVentaIdentifier = getFacturaVentaIdentifier(facturaVentaItem);
        if (facturaVentaIdentifier == null || facturaVentaCollectionIdentifiers.includes(facturaVentaIdentifier)) {
          return false;
        }
        facturaVentaCollectionIdentifiers.push(facturaVentaIdentifier);
        return true;
      });
      return [...facturaVentasToAdd, ...facturaVentaCollection];
    }
    return facturaVentaCollection;
  }

  protected convertDateFromClient(facturaVenta: IFacturaVenta): IFacturaVenta {
    return Object.assign({}, facturaVenta, {
      fechaCreacion: facturaVenta.fechaCreacion?.isValid() ? facturaVenta.fechaCreacion.toJSON() : undefined,
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
      res.body.forEach((facturaVenta: IFacturaVenta) => {
        facturaVenta.fechaCreacion = facturaVenta.fechaCreacion ? dayjs(facturaVenta.fechaCreacion) : undefined;
      });
    }
    return res;
  }
}
