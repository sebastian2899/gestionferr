import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AbonoComponent } from './list/abono.component';
import { AbonoDetailComponent } from './detail/abono-detail.component';
import { AbonoUpdateComponent } from './update/abono-update.component';
import { AbonoDeleteDialogComponent } from './delete/abono-delete-dialog.component';
import { AbonoRoutingModule } from './route/abono-routing.module';
import { MatMenuModule } from '@angular/material/menu';
import { MatIconModule } from '@angular/material/icon';

@NgModule({
  imports: [SharedModule, AbonoRoutingModule, MatMenuModule, MatIconModule],
  declarations: [AbonoComponent, AbonoDetailComponent, AbonoUpdateComponent, AbonoDeleteDialogComponent],
  entryComponents: [AbonoDeleteDialogComponent],
})
export class AbonoModule {}
