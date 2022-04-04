import { HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import dayjs from 'dayjs';
import { CajaService } from '../service/caja.service';
import { ICajaFechas } from './caja-fechas';

@Component({
  selector: 'jhi-caja-fechas',
  templateUrl: './caja-fechas.component.html',
  styleUrls: ['./caja-fechas.component.scss'],
})
export class CajaFechasComponent implements OnInit {
  fechaInicio?: dayjs.Dayjs | null;
  fechaFin?: dayjs.Dayjs | null;
  valorVendidoDia?: number | null;
  valorRegistradoDia?: number | null;
  diferencia?: number | null;
  cajaFechas?: ICajaFechas | null;

  constructor(protected cajaService: CajaService, protected ngbModal: NgbModal) {}

  ngOnInit(): void {
    this.fechaInicio = dayjs().startOf('day');
    this.fechaFin = dayjs().startOf('day');
  }

  consultarValoresCaja(): void {
    if (this.fechaInicio && this.fechaFin) {
      this.cajaService.cajasValoresFechas(this.fechaInicio.toString(), this.fechaFin.toString()).subscribe({
        next: (res: HttpResponse<ICajaFechas>) => {
          this.cajaFechas = res.body;
          this.valorVendidoDia = this.cajaFechas?.valorTotalDia;
          this.valorRegistradoDia = this.cajaFechas?.valorRegistradoDia;
          this.diferencia = this.cajaFechas?.diferencia;
        },
        error: () => {
          this.cajaFechas = null;
        },
      });
    }
  }

  previusState(): void {
    window.history.back();
  }
}
