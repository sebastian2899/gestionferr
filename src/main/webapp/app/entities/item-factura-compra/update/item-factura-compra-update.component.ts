import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IItemFacturaCompra, ItemFacturaCompra } from '../item-factura-compra.model';
import { ItemFacturaCompraService } from '../service/item-factura-compra.service';

@Component({
  selector: 'jhi-item-factura-compra-update',
  templateUrl: './item-factura-compra-update.component.html',
})
export class ItemFacturaCompraUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    idFacturaCompra: [],
    idProducto: [],
    cantidad: [],
    precio: [],
  });

  constructor(
    protected itemFacturaCompraService: ItemFacturaCompraService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ itemFacturaCompra }) => {
      this.updateForm(itemFacturaCompra);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const itemFacturaCompra = this.createFromForm();
    if (itemFacturaCompra.id !== undefined) {
      this.subscribeToSaveResponse(this.itemFacturaCompraService.update(itemFacturaCompra));
    } else {
      this.subscribeToSaveResponse(this.itemFacturaCompraService.create(itemFacturaCompra));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IItemFacturaCompra>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(itemFacturaCompra: IItemFacturaCompra): void {
    this.editForm.patchValue({
      id: itemFacturaCompra.id,
      idFacturaCompra: itemFacturaCompra.idFacturaCompra,
      idProducto: itemFacturaCompra.idProducto,
      cantidad: itemFacturaCompra.cantidad,
      precio: itemFacturaCompra.precio,
    });
  }

  protected createFromForm(): IItemFacturaCompra {
    return {
      ...new ItemFacturaCompra(),
      id: this.editForm.get(['id'])!.value,
      idFacturaCompra: this.editForm.get(['idFacturaCompra'])!.value,
      idProducto: this.editForm.get(['idProducto'])!.value,
      cantidad: this.editForm.get(['cantidad'])!.value,
      precio: this.editForm.get(['precio'])!.value,
    };
  }
}
