import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IItemFacturaCompra, getItemFacturaCompraIdentifier } from '../item-factura-compra.model';

export type EntityResponseType = HttpResponse<IItemFacturaCompra>;
export type EntityArrayResponseType = HttpResponse<IItemFacturaCompra[]>;

@Injectable({ providedIn: 'root' })
export class ItemFacturaCompraService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/item-factura-compras');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(itemFacturaCompra: IItemFacturaCompra): Observable<EntityResponseType> {
    return this.http.post<IItemFacturaCompra>(this.resourceUrl, itemFacturaCompra, { observe: 'response' });
  }

  update(itemFacturaCompra: IItemFacturaCompra): Observable<EntityResponseType> {
    return this.http.put<IItemFacturaCompra>(
      `${this.resourceUrl}/${getItemFacturaCompraIdentifier(itemFacturaCompra) as number}`,
      itemFacturaCompra,
      { observe: 'response' }
    );
  }

  partialUpdate(itemFacturaCompra: IItemFacturaCompra): Observable<EntityResponseType> {
    return this.http.patch<IItemFacturaCompra>(
      `${this.resourceUrl}/${getItemFacturaCompraIdentifier(itemFacturaCompra) as number}`,
      itemFacturaCompra,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IItemFacturaCompra>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IItemFacturaCompra[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addItemFacturaCompraToCollectionIfMissing(
    itemFacturaCompraCollection: IItemFacturaCompra[],
    ...itemFacturaComprasToCheck: (IItemFacturaCompra | null | undefined)[]
  ): IItemFacturaCompra[] {
    const itemFacturaCompras: IItemFacturaCompra[] = itemFacturaComprasToCheck.filter(isPresent);
    if (itemFacturaCompras.length > 0) {
      const itemFacturaCompraCollectionIdentifiers = itemFacturaCompraCollection.map(
        itemFacturaCompraItem => getItemFacturaCompraIdentifier(itemFacturaCompraItem)!
      );
      const itemFacturaComprasToAdd = itemFacturaCompras.filter(itemFacturaCompraItem => {
        const itemFacturaCompraIdentifier = getItemFacturaCompraIdentifier(itemFacturaCompraItem);
        if (itemFacturaCompraIdentifier == null || itemFacturaCompraCollectionIdentifiers.includes(itemFacturaCompraIdentifier)) {
          return false;
        }
        itemFacturaCompraCollectionIdentifiers.push(itemFacturaCompraIdentifier);
        return true;
      });
      return [...itemFacturaComprasToAdd, ...itemFacturaCompraCollection];
    }
    return itemFacturaCompraCollection;
  }
}
