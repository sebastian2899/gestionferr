import { HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import dayjs from 'dayjs/esm';
import { IRegistroFacturaCompra } from '../registroFacturaCompra';
import { FacturaCompraService } from '../service/factura-compra.service';

@Component({
  selector: 'jhi-registro-factura-compra',
  templateUrl: './registro-factura-compra.component.html',
  styleUrls: ['./registro-factura-compra.component.scss'],
})
export class RegistroFacturaCompraComponent implements OnInit {
  valores?: boolean | null;
  fechaInicio?: dayjs.Dayjs | null;
  fechaFin?: dayjs.Dayjs | null;
  valoresMes?: IRegistroFacturaCompra | null;
  valorFactura?: number | null;
  valorPagado?: number | null;
  valorDeuda?: number | null;

  constructor(protected facturasCompraService: FacturaCompraService) {
    this.fechaInicio = dayjs().startOf('day');
    this.fechaFin = dayjs().startOf('day');
  }

  ngOnInit(): void {
    this.fechaInicio = dayjs().startOf('day');
    this.fechaFin = dayjs().startOf('day');
  }

  consultarValoresFacturaMes(): void {
    this.facturasCompraService.registroFacturaMes(this.fechaInicio!.toString(), this.fechaFin!.toString()).subscribe({
      next: (res: HttpResponse<IRegistroFacturaCompra>) => {
        this.valoresMes = res.body;
        if (this.valoresMes) {
          this.valorFactura = this.valoresMes.valorFactura;
          this.valorPagado = this.valoresMes.valorPagado;
          this.valorDeuda = this.valoresMes.valorDeuda;
          this.valores = true;
        }
      },
      error: () => {
        this.valoresMes = null;
      },
    });
  }
}
