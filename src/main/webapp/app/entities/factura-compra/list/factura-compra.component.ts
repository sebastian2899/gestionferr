import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IFacturaCompra } from '../factura-compra.model';
import { FacturaCompraService } from '../service/factura-compra.service';
import { FacturaCompraDeleteDialogComponent } from '../delete/factura-compra-delete-dialog.component';
import { StateStorageService } from 'app/core/auth/state-storage.service';
import { Router } from '@angular/router';

@Component({
  selector: 'jhi-factura-compra',
  templateUrl: './factura-compra.component.html',
})
export class FacturaCompraComponent implements OnInit {
  facturaCompras?: IFacturaCompra[];
  isLoading = false;

  constructor(
    protected facturaCompraService: FacturaCompraService,
    protected modalService: NgbModal,
    protected storageService: StateStorageService,
    protected router: Router
  ) {}

  loadAll(): void {
    this.isLoading = true;

    this.facturaCompraService.query().subscribe({
      next: (res: HttpResponse<IFacturaCompra[]>) => {
        this.isLoading = false;
        this.facturaCompras = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  pasoParametroFacturaCompra(id: number): void {
    this.storageService.pasoParametroFacturaCompra(id);
    this.router.navigate(['abono/new']);
  }

  trackId(index: number, item: IFacturaCompra): number {
    return item.id!;
  }

  delete(facturaCompra: IFacturaCompra): void {
    const modalRef = this.modalService.open(FacturaCompraDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.facturaCompra = facturaCompra;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
