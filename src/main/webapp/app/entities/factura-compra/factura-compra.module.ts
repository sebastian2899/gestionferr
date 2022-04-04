import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { FacturaCompraComponent } from './list/factura-compra.component';
import { FacturaCompraDetailComponent } from './detail/factura-compra-detail.component';
import { FacturaCompraUpdateComponent } from './update/factura-compra-update.component';
import { FacturaCompraDeleteDialogComponent } from './delete/factura-compra-delete-dialog.component';
import { FacturaCompraRoutingModule } from './route/factura-compra-routing.module';
import { MatIconModule } from '@angular/material/icon';
import { RegistroFacturaCompraComponent } from './registro-factura-compra/registro-factura-compra.component';

@NgModule({
  imports: [SharedModule, FacturaCompraRoutingModule, MatIconModule],
  declarations: [
    FacturaCompraComponent,
    FacturaCompraDetailComponent,
    FacturaCompraUpdateComponent,
    FacturaCompraDeleteDialogComponent,
    RegistroFacturaCompraComponent,
  ],
  entryComponents: [FacturaCompraDeleteDialogComponent],
})
export class FacturaCompraModule {}
