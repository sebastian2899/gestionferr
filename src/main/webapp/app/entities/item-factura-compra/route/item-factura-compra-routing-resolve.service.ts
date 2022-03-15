import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IItemFacturaCompra, ItemFacturaCompra } from '../item-factura-compra.model';
import { ItemFacturaCompraService } from '../service/item-factura-compra.service';

@Injectable({ providedIn: 'root' })
export class ItemFacturaCompraRoutingResolveService implements Resolve<IItemFacturaCompra> {
  constructor(protected service: ItemFacturaCompraService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IItemFacturaCompra> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((itemFacturaCompra: HttpResponse<ItemFacturaCompra>) => {
          if (itemFacturaCompra.body) {
            return of(itemFacturaCompra.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ItemFacturaCompra());
  }
}
