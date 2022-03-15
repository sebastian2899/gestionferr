import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { FacturaCompraComponent } from './list/factura-compra.component';
import { FacturaCompraDetailComponent } from './detail/factura-compra-detail.component';
import { FacturaCompraUpdateComponent } from './update/factura-compra-update.component';
import { FacturaCompraDeleteDialogComponent } from './delete/factura-compra-delete-dialog.component';
import { FacturaCompraRoutingModule } from './route/factura-compra-routing.module';

@NgModule({
  imports: [SharedModule, FacturaCompraRoutingModule],
  declarations: [FacturaCompraComponent, FacturaCompraDetailComponent, FacturaCompraUpdateComponent, FacturaCompraDeleteDialogComponent],
  entryComponents: [FacturaCompraDeleteDialogComponent],
})
export class FacturaCompraModule {}
