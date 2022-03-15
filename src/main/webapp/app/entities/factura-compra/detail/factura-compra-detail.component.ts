import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFacturaCompra } from '../factura-compra.model';

@Component({
  selector: 'jhi-factura-compra-detail',
  templateUrl: './factura-compra-detail.component.html',
})
export class FacturaCompraDetailComponent implements OnInit {
  facturaCompra: IFacturaCompra | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ facturaCompra }) => {
      this.facturaCompra = facturaCompra;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
