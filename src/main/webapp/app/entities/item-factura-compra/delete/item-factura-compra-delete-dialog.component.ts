import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IItemFacturaCompra } from '../item-factura-compra.model';
import { ItemFacturaCompraService } from '../service/item-factura-compra.service';

@Component({
  templateUrl: './item-factura-compra-delete-dialog.component.html',
})
export class ItemFacturaCompraDeleteDialogComponent {
  itemFacturaCompra?: IItemFacturaCompra;

  constructor(protected itemFacturaCompraService: ItemFacturaCompraService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.itemFacturaCompraService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
