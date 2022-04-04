import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IProducto, getProductoIdentifier } from '../producto.model';

export type EntityResponseType = HttpResponse<IProducto>;
export type EntityArrayResponseType = HttpResponse<IProducto[]>;

@Injectable({ providedIn: 'root' })
export class ProductoService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/productos');
  protected productoPorCategoriaUrl = this.applicationConfigService.getEndpointFor('api/productos-categoria');
  protected productoFiltroAutomaticoUrl = this.applicationConfigService.getEndpointFor('api/productos-filtro-automatico');
  protected productoAgotadosUrl = this.applicationConfigService.getEndpointFor('api/productos-agotados');
  protected productoCasiAgotadosUrl = this.applicationConfigService.getEndpointFor('api/productos-casi-agotados');
  protected actualizarPrecioProductosUrl = this.applicationConfigService.getEndpointFor('api/productos-precios-porcentaje');
  protected productosPorNombreUrl = this.applicationConfigService.getEndpointFor('api/productos-nombre');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(producto: IProducto): Observable<EntityResponseType> {
    return this.http.post<IProducto>(this.resourceUrl, producto, { observe: 'response' });
  }

  productosAgotados(): Observable<EntityArrayResponseType> {
    return this.http.get<IProducto[]>(this.productoAgotadosUrl, { observe: 'response' });
  }

  productosPorNombre(nombre: string): Observable<EntityArrayResponseType> {
    return this.http.get<IProducto[]>(`${this.productosPorNombreUrl}/${nombre}`, { observe: 'response' });
  }

  productosCasiAgotados(): Observable<EntityArrayResponseType> {
    return this.http.get<IProducto[]>(this.productoCasiAgotadosUrl, { observe: 'response' });
  }

  productosFiltroAutomatico(codigo: number): Observable<EntityArrayResponseType> {
    return this.http.get<IProducto[]>(`${this.productoFiltroAutomaticoUrl}/${codigo}`, { observe: 'response' });
  }

  productosPorCategoria(idCategoria: number): Observable<EntityArrayResponseType> {
    return this.http.get<IProducto[]>(`${this.productoPorCategoriaUrl}/${idCategoria}`, { observe: 'response' });
  }

  update(producto: IProducto): Observable<EntityResponseType> {
    return this.http.put<IProducto>(`${this.resourceUrl}/${getProductoIdentifier(producto) as number}`, producto, { observe: 'response' });
  }

  partialUpdate(producto: IProducto): Observable<EntityResponseType> {
    return this.http.patch<IProducto>(`${this.resourceUrl}/${getProductoIdentifier(producto) as number}`, producto, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IProducto>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IProducto[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  actualizarPrecioProductos(opcion: string, porcentaje: number): Observable<HttpResponse<{}>> {
    return this.http.get(`${this.actualizarPrecioProductosUrl}/${opcion}/${porcentaje}`, { observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addProductoToCollectionIfMissing(productoCollection: IProducto[], ...productosToCheck: (IProducto | null | undefined)[]): IProducto[] {
    const productos: IProducto[] = productosToCheck.filter(isPresent);
    if (productos.length > 0) {
      const productoCollectionIdentifiers = productoCollection.map(productoItem => getProductoIdentifier(productoItem)!);
      const productosToAdd = productos.filter(productoItem => {
        const productoIdentifier = getProductoIdentifier(productoItem);
        if (productoIdentifier == null || productoCollectionIdentifiers.includes(productoIdentifier)) {
          return false;
        }
        productoCollectionIdentifiers.push(productoIdentifier);
        return true;
      });
      return [...productosToAdd, ...productoCollection];
    }
    return productoCollection;
  }
}
