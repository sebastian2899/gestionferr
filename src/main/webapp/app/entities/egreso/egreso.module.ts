import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { EgresoComponent } from './list/egreso.component';
import { EgresoDetailComponent } from './detail/egreso-detail.component';
import { EgresoUpdateComponent } from './update/egreso-update.component';
import { EgresoDeleteDialogComponent } from './delete/egreso-delete-dialog.component';
import { EgresoRoutingModule } from './route/egreso-routing.module';
import { MatIconModule } from '@angular/material/icon';

@NgModule({
  imports: [SharedModule, EgresoRoutingModule, MatIconModule],
  declarations: [EgresoComponent, EgresoDetailComponent, EgresoUpdateComponent, EgresoDeleteDialogComponent],
  entryComponents: [EgresoDeleteDialogComponent],
})
export class EgresoModule {}
