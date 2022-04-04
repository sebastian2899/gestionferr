import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { FacturaCompra, IFacturaCompra } from '../factura-compra.model';
import { FacturaCompraService } from '../service/factura-compra.service';
import { FacturaCompraDeleteDialogComponent } from '../delete/factura-compra-delete-dialog.component';
import { StateStorageService } from 'app/core/auth/state-storage.service';
import { Router } from '@angular/router';
import dayjs from 'dayjs';

@Component({
  selector: 'jhi-factura-compra',
  templateUrl: './factura-compra.component.html',
})
export class FacturaCompraComponent implements OnInit {
  facturaCompras?: IFacturaCompra[];
  isLoading = false;
  numeroFactura = '';
  infoProveedor = '';
  estados = ['PAGADA', 'DEUDA'];
  estado?: string | null;
  fecha?: dayjs.Dayjs | null;
  facturaCompra?: IFacturaCompra | null;

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

  facturasFiltro(): void {
    this.facturaCompra = new FacturaCompra();
    this.facturaCompra.numeroFactura = this.numeroFactura;
    this.facturaCompra.infoCliente = this.infoProveedor;
    this.facturaCompra.estado = this.estado;

    this.facturaCompraService.facturaPorFiltro(this.facturaCompra).subscribe({
      next: (res: HttpResponse<IFacturaCompra[]>) => {
        this.facturaCompras = res.body ?? [];
      },
      error: () => {
        this.facturaCompras = [];
      },
    });
  }

  facturasPorFecha(): void {
    if (this.fecha) {
      this.facturaCompraService.facturasCompraPorFecha(this.fecha.toString()).subscribe({
        next: (res: HttpResponse<IFacturaCompra[]>) => {
          this.facturaCompras = res.body ?? [];
        },
        error: () => {
          this.facturaCompras = [];
        },
      });
    }
  }

  pasoParametroFacturaCompra(id: number): void {
    this.storageService.pasoParametroFacturaCompra(id);
    this.router.navigate(['abono/new']);
  }

  pasoParametroFacturaCompraAbono(id: number): void {
    this.storageService.pasoParametroFacturaCompra(id);
    this.router.navigate(['abono']);
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
