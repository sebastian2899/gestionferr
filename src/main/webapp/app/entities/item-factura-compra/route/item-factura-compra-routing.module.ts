import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ItemFacturaCompraComponent } from '../list/item-factura-compra.component';
import { ItemFacturaCompraDetailComponent } from '../detail/item-factura-compra-detail.component';
import { ItemFacturaCompraUpdateComponent } from '../update/item-factura-compra-update.component';
import { ItemFacturaCompraRoutingResolveService } from './item-factura-compra-routing-resolve.service';

const itemFacturaCompraRoute: Routes = [
  {
    path: '',
    component: ItemFacturaCompraComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ItemFacturaCompraDetailComponent,
    resolve: {
      itemFacturaCompra: ItemFacturaCompraRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ItemFacturaCompraUpdateComponent,
    resolve: {
      itemFacturaCompra: ItemFacturaCompraRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ItemFacturaCompraUpdateComponent,
    resolve: {
      itemFacturaCompra: ItemFacturaCompraRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(itemFacturaCompraRoute)],
  exports: [RouterModule],
})
export class ItemFacturaCompraRoutingModule {}
