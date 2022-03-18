import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IFacturaVenta } from '../factura-venta.model';
import { FacturaVentaService } from '../service/factura-venta.service';
import { FacturaVentaDeleteDialogComponent } from '../delete/factura-venta-delete-dialog.component';
import { StateStorageService } from 'app/core/auth/state-storage.service';
import { Router } from '@angular/router';

@Component({
  selector: 'jhi-factura-venta',
  templateUrl: './factura-venta.component.html',
})
export class FacturaVentaComponent implements OnInit {
  facturaVentas?: IFacturaVenta[];
  isLoading = false;

  constructor(
    protected facturaVentaService: FacturaVentaService,
    protected modalService: NgbModal,
    protected storageService: StateStorageService,
    protected route: Router
  ) {}

  loadAll(): void {
    this.isLoading = true;

    this.facturaVentaService.query().subscribe({
      next: (res: HttpResponse<IFacturaVenta[]>) => {
        this.isLoading = false;
        this.facturaVentas = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  pasoParametroFactura(idFactura: number): void {
    this.storageService.pasoParametroFactura(idFactura);
    this.route.navigate(['abono/new']);
  }

  pasoParametroAbonosPorFactura(idFactura: number): void {
    this.storageService.pasoParametroFactura(idFactura);
    this.route.navigate(['abono']);
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IFacturaVenta): number {
    return item.id!;
  }

  delete(facturaVenta: IFacturaVenta): void {
    const modalRef = this.modalService.open(FacturaVentaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.facturaVenta = facturaVenta;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
