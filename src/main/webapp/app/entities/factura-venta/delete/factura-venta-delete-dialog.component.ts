import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFacturaVenta } from '../factura-venta.model';
import { FacturaVentaService } from '../service/factura-venta.service';

@Component({
  templateUrl: './factura-venta-delete-dialog.component.html',
})
export class FacturaVentaDeleteDialogComponent {
  facturaVenta?: IFacturaVenta;

  constructor(protected facturaVentaService: FacturaVentaService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.facturaVentaService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
