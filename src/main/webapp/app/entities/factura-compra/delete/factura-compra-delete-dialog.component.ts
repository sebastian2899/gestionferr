import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFacturaCompra } from '../factura-compra.model';
import { FacturaCompraService } from '../service/factura-compra.service';

@Component({
  templateUrl: './factura-compra-delete-dialog.component.html',
})
export class FacturaCompraDeleteDialogComponent {
  facturaCompra?: IFacturaCompra;

  constructor(protected facturaCompraService: FacturaCompraService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.facturaCompraService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
