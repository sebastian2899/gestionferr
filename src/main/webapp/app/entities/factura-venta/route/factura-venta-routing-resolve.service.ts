import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFacturaVenta, FacturaVenta } from '../factura-venta.model';
import { FacturaVentaService } from '../service/factura-venta.service';

@Injectable({ providedIn: 'root' })
export class FacturaVentaRoutingResolveService implements Resolve<IFacturaVenta> {
  constructor(protected service: FacturaVentaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFacturaVenta> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((facturaVenta: HttpResponse<FacturaVenta>) => {
          if (facturaVenta.body) {
            return of(facturaVenta.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new FacturaVenta());
  }
}
