import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IItemFacturaCompra } from '../item-factura-compra.model';
import { ItemFacturaCompraService } from '../service/item-factura-compra.service';
import { ItemFacturaCompraDeleteDialogComponent } from '../delete/item-factura-compra-delete-dialog.component';

@Component({
  selector: 'jhi-item-factura-compra',
  templateUrl: './item-factura-compra.component.html',
})
export class ItemFacturaCompraComponent implements OnInit {
  itemFacturaCompras?: IItemFacturaCompra[];
  isLoading = false;

  constructor(protected itemFacturaCompraService: ItemFacturaCompraService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.itemFacturaCompraService.query().subscribe({
      next: (res: HttpResponse<IItemFacturaCompra[]>) => {
        this.isLoading = false;
        this.itemFacturaCompras = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IItemFacturaCompra): number {
    return item.id!;
  }

  delete(itemFacturaCompra: IItemFacturaCompra): void {
    const modalRef = this.modalService.open(ItemFacturaCompraDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.itemFacturaCompra = itemFacturaCompra;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
