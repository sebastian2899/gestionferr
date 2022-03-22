import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { FacturaVentaComponent } from './list/factura-venta.component';
import { FacturaVentaDetailComponent } from './detail/factura-venta-detail.component';
import { FacturaVentaUpdateComponent } from './update/factura-venta-update.component';
import { FacturaVentaDeleteDialogComponent } from './delete/factura-venta-delete-dialog.component';
import { FacturaVentaRoutingModule } from './route/factura-venta-routing.module';
import { MatIconModule } from '@angular/material/icon';
import { FacturaFechasComponent } from './factura-fechas/factura-fechas.component';

@NgModule({
  imports: [SharedModule, FacturaVentaRoutingModule, MatIconModule],
  declarations: [
    FacturaVentaComponent,
    FacturaVentaDetailComponent,
    FacturaVentaUpdateComponent,
    FacturaVentaDeleteDialogComponent,
    FacturaFechasComponent,
  ],
  entryComponents: [FacturaVentaDeleteDialogComponent],
})
export class FacturaVentaModule {}
