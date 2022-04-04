import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICaja, getCajaIdentifier } from '../caja.model';
import { ICajaFechas } from '../caja-fechas/caja-fechas';

export type EntityResponseType = HttpResponse<ICaja>;
export type EntityArrayResponseType = HttpResponse<ICaja[]>;
export type NumberType = HttpResponse<number>;
export type CajaFechasType = HttpResponse<ICajaFechas>;
export type CajaByteType = HttpResponse<any>;

@Injectable({ providedIn: 'root' })
export class CajaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/cajas');
  protected ValorDiaResourceUrl = this.applicationConfigService.getEndpointFor('api/valorCajaDia');
  protected cajasPorEstadoUrl = this.applicationConfigService.getEndpointFor('api/cajas-filtro-estado');
  protected cajasPorFechaUrl = this.applicationConfigService.getEndpointFor('api/cajas-por-fecha');
  protected cajasValoresPorFechaUrl = this.applicationConfigService.getEndpointFor('api/cajas-valores-meses');
  protected cajasReporteUrl = this.applicationConfigService.getEndpointFor('api/cajas-reporte');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(caja: ICaja): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(caja);
    return this.http
      .post<ICaja>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  cajasPorEstado(estado: string): Observable<EntityArrayResponseType> {
    return this.http
      .get<ICaja[]>(`${this.cajasPorEstadoUrl}/${estado}`, { observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  reporteCaja(): Observable<any> {
    const httpOption = { responseType: 'arrayBuffer' as 'json' };
    return this.http.get<any>(this.cajasReporteUrl, httpOption);
  }

  cajasPorFecha(fecha: string): Observable<EntityArrayResponseType> {
    return this.http
      .get<ICaja[]>(`${this.cajasPorFechaUrl}/${fecha}`, { observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  cajasValoresFechas(fechaInicio: string, fechaFin: string): Observable<CajaFechasType> {
    return this.http.get<ICajaFechas>(`${this.cajasValoresPorFechaUrl}/${fechaInicio}/${fechaFin}`, { observe: 'response' });
  }

  update(caja: ICaja): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(caja);
    return this.http
      .put<ICaja>(`${this.resourceUrl}/${getCajaIdentifier(caja) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(caja: ICaja): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(caja);
    return this.http
      .patch<ICaja>(`${this.resourceUrl}/${getCajaIdentifier(caja) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICaja>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  valorCajaDia(): Observable<NumberType> {
    return this.http.get<number>(this.ValorDiaResourceUrl, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICaja[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCajaToCollectionIfMissing(cajaCollection: ICaja[], ...cajasToCheck: (ICaja | null | undefined)[]): ICaja[] {
    const cajas: ICaja[] = cajasToCheck.filter(isPresent);
    if (cajas.length > 0) {
      const cajaCollectionIdentifiers = cajaCollection.map(cajaItem => getCajaIdentifier(cajaItem)!);
      const cajasToAdd = cajas.filter(cajaItem => {
        const cajaIdentifier = getCajaIdentifier(cajaItem);
        if (cajaIdentifier == null || cajaCollectionIdentifiers.includes(cajaIdentifier)) {
          return false;
        }
        cajaCollectionIdentifiers.push(cajaIdentifier);
        return true;
      });
      return [...cajasToAdd, ...cajaCollection];
    }
    return cajaCollection;
  }

  protected convertDateFromClient(caja: ICaja): ICaja {
    return Object.assign({}, caja, {
      fechaCreacion: caja.fechaCreacion?.isValid() ? caja.fechaCreacion.toJSON() : undefined,
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
      res.body.forEach((caja: ICaja) => {
        caja.fechaCreacion = caja.fechaCreacion ? dayjs(caja.fechaCreacion) : undefined;
      });
    }
    return res;
  }
}
