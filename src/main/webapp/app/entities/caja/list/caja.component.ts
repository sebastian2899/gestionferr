import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICaja } from '../caja.model';
import { CajaService } from '../service/caja.service';
import { CajaDeleteDialogComponent } from '../delete/caja-delete-dialog.component';
import dayjs from 'dayjs';
import { AlertService } from 'app/core/util/alert.service';

@Component({
  selector: 'jhi-caja',
  templateUrl: './caja.component.html',
})
export class CajaComponent implements OnInit {
  cajas?: ICaja[];
  isLoading = false;
  estados = ['DEUDA', 'PAGADA'];
  estado?: string | null;
  fecha?: dayjs.Dayjs | null;

  constructor(protected cajaService: CajaService, protected modalService: NgbModal, protected alertService: AlertService) {}

  loadAll(): void {
    this.isLoading = true;

    this.cajaService.query().subscribe({
      next: (res: HttpResponse<ICaja[]>) => {
        this.isLoading = false;
        this.cajas = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  generarReporteCaja(): void {
    this.cajaService.reporteCaja().subscribe(
      (res: any) => {
        const file = new Blob([res], { type: 'aplication.pdf' });
        const Url = URL.createObjectURL(file);
        window.open(Url);
      },

      () => {
        this.alertService.addAlert({
          type: 'danger',
          message: 'Error al generar el reporte',
        });
      }
    );
  }

  cajasPorEstado(): void {
    if (this.estado) {
      this.cajaService.cajasPorEstado(this.estado).subscribe({
        next: (res: HttpResponse<ICaja[]>) => {
          this.cajas = res.body ?? [];
        },
        error: () => {
          this.cajas = [];
        },
      });
    }
    if (this.fecha) {
      this.cajaService.cajasPorFecha(this.fecha.toString()).subscribe({
        next: (res: HttpResponse<ICaja[]>) => {
          this.cajas = res.body ?? [];
        },
        error: () => {
          this.cajas = [];
        },
      });
    }
  }

  trackId(index: number, item: ICaja): number {
    return item.id!;
  }

  delete(caja: ICaja): void {
    const modalRef = this.modalService.open(CajaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.caja = caja;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
