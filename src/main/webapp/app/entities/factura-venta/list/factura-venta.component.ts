import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { FacturaVenta, IFacturaVenta } from '../factura-venta.model';
import { FacturaVentaService } from '../service/factura-venta.service';
import { FacturaVentaDeleteDialogComponent } from '../delete/factura-venta-delete-dialog.component';
import { StateStorageService } from 'app/core/auth/state-storage.service';
import { Router } from '@angular/router';
import dayjs from 'dayjs/esm';
import { AlertService } from 'app/core/util/alert.service';
import { ClienteService } from 'app/entities/cliente/service/cliente.service';
import { ICliente } from 'app/entities/cliente/cliente.model';

@Component({
  selector: 'jhi-factura-venta',
  templateUrl: './factura-venta.component.html',
})
export class FacturaVentaComponent implements OnInit {
  facturaVentas?: IFacturaVenta[];
  facturaVenta?: IFacturaVenta | null;
  isLoading = false;
  numeroFactura = '';
  nombreCliente = '';
  estado = '';
  fecha?: dayjs.Dayjs | null;
  cliente?: ICliente | null;

  constructor(
    protected facturaVentaService: FacturaVentaService,
    protected modalService: NgbModal,
    protected storageService: StateStorageService,
    protected route: Router,
    protected alert: AlertService,
    protected clienteService: ClienteService
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

  generearReporteMensual(): void {
    this.facturaVentaService.generarReporteMensual().subscribe(
      (res: any) => {
        const file = new Blob([res], { type: 'application/pdf' });
        const fileURL = URL.createObjectURL(file);
        window.open(fileURL);
      },

      () => {
        this.alert.addAlert({
          type: 'danger',
          message: 'Error al generar el reporte',
        });
      }
    );
  }

  facturasFiltro(): void {
    this.facturaVenta = new FacturaVenta();
    this.facturaVenta.numeroFactura = this.numeroFactura;
    this.facturaVenta.infoCliente = this.nombreCliente;
    this.facturaVenta.estado = this.estado;

    this.facturaVentaService.facturaFiltros(this.facturaVenta).subscribe({
      next: (res: HttpResponse<IFacturaVenta[]>) => {
        this.facturaVentas = res.body ?? [];
      },
      error: () => {
        this.facturaVentas = [];
      },
    });
  }

  filtrarPorFecha(): void {
    if (this.fecha) {
      this.facturaVentaService.facturaFecha(this.fecha.toString()).subscribe({
        next: (res: HttpResponse<IFacturaVenta[]>) => {
          this.facturaVentas = res.body ?? [];
        },
        error: () => {
          this.facturaVentas = [];
        },
      });
    }
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
