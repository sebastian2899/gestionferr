import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFacturaCompra, getFacturaCompraIdentifier } from '../factura-compra.model';
import { IRegistroFacturaCompra } from '../registroFacturaCompra';

export type EntityResponseType = HttpResponse<IFacturaCompra>;
export type BooleanResponseType = HttpResponse<boolean>;
export type EntityArrayResponseType = HttpResponse<IFacturaCompra[]>;
export type RegistroFacturaCompra = HttpResponse<IRegistroFacturaCompra>;

@Injectable({ providedIn: 'root' })
export class FacturaCompraService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/factura-compras');
  protected facturassPorFiltroUrl = this.applicationConfigService.getEndpointFor('api/factura-compras-filtro');
  protected registroFacturaCompraUrl = this.applicationConfigService.getEndpointFor('api/factura-compra-mes');
  protected facturasPorFechaUrl = this.applicationConfigService.getEndpointFor('api/facturas-compra-fecha');
  protected validarFacturaCompraUrl = this.applicationConfigService.getEndpointFor('api/factura-compra-validar-numero');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(facturaCompra: IFacturaCompra): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(facturaCompra);
    return this.http
      .post<IFacturaCompra>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  validarFacturaCompra(numeroFactura: string): Observable<BooleanResponseType> {
    return this.http.get<boolean>(`${this.validarFacturaCompraUrl}/${numeroFactura}`, { observe: 'response' });
  }

  registroFacturaMes(fechaInicio: string, fechaFin: string): Observable<RegistroFacturaCompra> {
    return this.http.get<IRegistroFacturaCompra>(`${this.registroFacturaCompraUrl}/${fechaInicio}/${fechaFin}`, { observe: 'response' });
  }

  facturasCompraPorFecha(fecha: string): Observable<EntityArrayResponseType> {
    return this.http
      .get<IFacturaCompra[]>(`${this.facturasPorFechaUrl}/${fecha}`, { observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  facturaPorFiltro(facturaCompra: IFacturaCompra): Observable<EntityArrayResponseType> {
    return this.http
      .post<IFacturaCompra[]>(this.facturassPorFiltroUrl, facturaCompra, { observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  update(facturaCompra: IFacturaCompra): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(facturaCompra);
    return this.http
      .put<IFacturaCompra>(`${this.resourceUrl}/${getFacturaCompraIdentifier(facturaCompra) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(facturaCompra: IFacturaCompra): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(facturaCompra);
    return this.http
      .patch<IFacturaCompra>(`${this.resourceUrl}/${getFacturaCompraIdentifier(facturaCompra) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IFacturaCompra>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IFacturaCompra[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addFacturaCompraToCollectionIfMissing(
    facturaCompraCollection: IFacturaCompra[],
    ...facturaComprasToCheck: (IFacturaCompra | null | undefined)[]
  ): IFacturaCompra[] {
    const facturaCompras: IFacturaCompra[] = facturaComprasToCheck.filter(isPresent);
    if (facturaCompras.length > 0) {
      const facturaCompraCollectionIdentifiers = facturaCompraCollection.map(
        facturaCompraItem => getFacturaCompraIdentifier(facturaCompraItem)!
      );
      const facturaComprasToAdd = facturaCompras.filter(facturaCompraItem => {
        const facturaCompraIdentifier = getFacturaCompraIdentifier(facturaCompraItem);
        if (facturaCompraIdentifier == null || facturaCompraCollectionIdentifiers.includes(facturaCompraIdentifier)) {
          return false;
        }
        facturaCompraCollectionIdentifiers.push(facturaCompraIdentifier);
        return true;
      });
      return [...facturaComprasToAdd, ...facturaCompraCollection];
    }
    return facturaCompraCollection;
  }

  protected convertDateFromClient(facturaCompra: IFacturaCompra): IFacturaCompra {
    return Object.assign({}, facturaCompra, {
      fechaCreacion: facturaCompra.fechaCreacion?.isValid() ? facturaCompra.fechaCreacion.toJSON() : undefined,
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
      res.body.forEach((facturaCompra: IFacturaCompra) => {
        facturaCompra.fechaCreacion = facturaCompra.fechaCreacion ? dayjs(facturaCompra.fechaCreacion) : undefined;
      });
    }
    return res;
  }
}
