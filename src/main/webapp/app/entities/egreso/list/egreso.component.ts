import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IEgreso } from '../egreso.model';
import { EgresoService } from '../service/egreso.service';
import { EgresoDeleteDialogComponent } from '../delete/egreso-delete-dialog.component';
import dayjs from 'dayjs';

@Component({
  selector: 'jhi-egreso',
  templateUrl: './egreso.component.html',
})
export class EgresoComponent implements OnInit {
  egresos?: IEgreso[];
  isLoading = false;
  valorDiario?: number | null;
  fecha?: dayjs.Dayjs | null;

  constructor(protected egresoService: EgresoService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.egresoService.query().subscribe({
      next: (res: HttpResponse<IEgreso[]>) => {
        this.isLoading = false;
        this.egresos = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
    this.consultarValorEgresoDia();
  }

  consultarEgresosfecha(): void {
    if (this.fecha) {
      this.egresoService.consultarEgresoFecha(this.fecha.toString()).subscribe({
        next: (res: HttpResponse<IEgreso[]>) => {
          this.egresos = res.body ?? [];
        },
        error: () => {
          this.egresos = [];
        },
      });
    }
  }

  consultarEgresoDiario(): void {
    this.egresoService.egresoDiario().subscribe({
      next: (res: HttpResponse<IEgreso[]>) => {
        this.egresos = res.body ?? [];
      },
      error: () => {
        this.egresos = [];
      },
    });
  }

  consultarValorEgresoDia(): void {
    this.egresoService.valorDiarioEgreso().subscribe({
      next: (res: HttpResponse<number>) => {
        this.valorDiario = res.body;
      },
      error: () => {
        this.valorDiario = 0;
      },
    });
  }

  trackId(index: number, item: IEgreso): number {
    return item.id!;
  }

  delete(egreso: IEgreso): void {
    const modalRef = this.modalService.open(EgresoDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.egreso = egreso;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
