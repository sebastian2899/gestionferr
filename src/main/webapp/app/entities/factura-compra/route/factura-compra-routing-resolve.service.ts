import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFacturaCompra, FacturaCompra } from '../factura-compra.model';
import { FacturaCompraService } from '../service/factura-compra.service';

@Injectable({ providedIn: 'root' })
export class FacturaCompraRoutingResolveService implements Resolve<IFacturaCompra> {
  constructor(protected service: FacturaCompraService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFacturaCompra> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((facturaCompra: HttpResponse<FacturaCompra>) => {
          if (facturaCompra.body) {
            return of(facturaCompra.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new FacturaCompra());
  }
}
