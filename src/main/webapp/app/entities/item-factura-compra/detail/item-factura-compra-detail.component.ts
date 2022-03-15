import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IItemFacturaCompra } from '../item-factura-compra.model';

@Component({
  selector: 'jhi-item-factura-compra-detail',
  templateUrl: './item-factura-compra-detail.component.html',
})
export class ItemFacturaCompraDetailComponent implements OnInit {
  itemFacturaCompra: IItemFacturaCompra | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ itemFacturaCompra }) => {
      this.itemFacturaCompra = itemFacturaCompra;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
