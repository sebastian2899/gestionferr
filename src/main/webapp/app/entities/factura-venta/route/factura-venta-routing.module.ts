import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FacturaVentaComponent } from '../list/factura-venta.component';
import { FacturaVentaDetailComponent } from '../detail/factura-venta-detail.component';
import { FacturaVentaUpdateComponent } from '../update/factura-venta-update.component';
import { FacturaVentaRoutingResolveService } from './factura-venta-routing-resolve.service';
import { FacturaFechasComponent } from '../factura-fechas/factura-fechas.component';

const facturaVentaRoute: Routes = [
  {
    path: '',
    component: FacturaVentaComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FacturaVentaDetailComponent,
    resolve: {
      facturaVenta: FacturaVentaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FacturaVentaUpdateComponent,
    resolve: {
      facturaVenta: FacturaVentaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':factura-fechas',
    component: FacturaFechasComponent,
    resolve: {
      facturaVenta: FacturaVentaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FacturaVentaUpdateComponent,
    resolve: {
      facturaVenta: FacturaVentaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(facturaVentaRoute)],
  exports: [RouterModule],
})
export class FacturaVentaRoutingModule {}
