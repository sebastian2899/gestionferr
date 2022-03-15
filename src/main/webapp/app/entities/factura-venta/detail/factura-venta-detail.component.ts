import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFacturaVenta } from '../factura-venta.model';

@Component({
  selector: 'jhi-factura-venta-detail',
  templateUrl: './factura-venta-detail.component.html',
})
export class FacturaVentaDetailComponent implements OnInit {
  facturaVenta: IFacturaVenta | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ facturaVenta }) => {
      this.facturaVenta = facturaVenta;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
