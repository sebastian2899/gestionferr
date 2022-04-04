import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FacturaCompraComponent } from '../list/factura-compra.component';
import { FacturaCompraDetailComponent } from '../detail/factura-compra-detail.component';
import { FacturaCompraUpdateComponent } from '../update/factura-compra-update.component';
import { FacturaCompraRoutingResolveService } from './factura-compra-routing-resolve.service';
import { RegistroFacturaCompraComponent } from '../registro-factura-compra/registro-factura-compra.component';

const facturaCompraRoute: Routes = [
  {
    path: '',
    component: FacturaCompraComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FacturaCompraDetailComponent,
    resolve: {
      facturaCompra: FacturaCompraRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FacturaCompraUpdateComponent,
    resolve: {
      facturaCompra: FacturaCompraRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'registro-factura-compra',
    component: RegistroFacturaCompraComponent,
    resolve: {
      facturaCompra: FacturaCompraRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FacturaCompraUpdateComponent,
    resolve: {
      facturaCompra: FacturaCompraRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(facturaCompraRoute)],
  exports: [RouterModule],
})
export class FacturaCompraRoutingModule {}
