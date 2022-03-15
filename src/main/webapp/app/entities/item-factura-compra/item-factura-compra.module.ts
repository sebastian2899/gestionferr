import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ItemFacturaCompraComponent } from './list/item-factura-compra.component';
import { ItemFacturaCompraDetailComponent } from './detail/item-factura-compra-detail.component';
import { ItemFacturaCompraUpdateComponent } from './update/item-factura-compra-update.component';
import { ItemFacturaCompraDeleteDialogComponent } from './delete/item-factura-compra-delete-dialog.component';
import { ItemFacturaCompraRoutingModule } from './route/item-factura-compra-routing.module';

@NgModule({
  imports: [SharedModule, ItemFacturaCompraRoutingModule],
  declarations: [
    ItemFacturaCompraComponent,
    ItemFacturaCompraDetailComponent,
    ItemFacturaCompraUpdateComponent,
    ItemFacturaCompraDeleteDialogComponent,
  ],
  entryComponents: [ItemFacturaCompraDeleteDialogComponent],
})
export class ItemFacturaCompraModule {}
