import { HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import dayjs from 'dayjs';
import { IFacturaVentaFechas } from '../factura-fechas';
import { FacturaVentaService } from '../service/factura-venta.service';

@Component({
  selector: 'jhi-factura-fechas',
  templateUrl: './factura-fechas.component.html',
  styleUrls: ['./factura-fechas.component.scss'],
})
export class FacturaFechasComponent implements OnInit {
  valoresFactura?: IFacturaVentaFechas | null;
  fechaInicio?: dayjs.Dayjs;
  fechaFin?: dayjs.Dayjs;

  constructor(protected facturaService: FacturaVentaService) {
    this.fechaInicio = dayjs().startOf('day');
    this.fechaFin = dayjs().startOf('day');
  }

  ngOnInit(): void {
    this.fechaInicio = dayjs().startOf('day');
    this.fechaFin = dayjs().startOf('day');
  }

  consultarValores(): void {
    if (this.fechaInicio && this.fechaFin) {
      this.facturaService.valoresFacturaMes(this.fechaInicio.toString(), this.fechaFin.toString()).subscribe({
        next: (res: HttpResponse<IFacturaVentaFechas>) => {
          this.valoresFactura = res.body;
        },
        error: () => {
          this.valoresFactura = null;
        },
      });
    }
  }

  previusState(): void {
    window.history.back();
  }
}
