import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IFacturaCompra, FacturaCompra } from '../factura-compra.model';
import { FacturaCompraService } from '../service/factura-compra.service';
import { TipoFacturaEnum } from 'app/entities/enumerations/tipo-factura-enum.model';

@Component({
  selector: 'jhi-factura-compra-update',
  templateUrl: './factura-compra-update.component.html',
})
export class FacturaCompraUpdateComponent implements OnInit {
  isSaving = false;
  tipoFacturaEnumValues = Object.keys(TipoFacturaEnum);

  editForm = this.fb.group({
    id: [],
    numeroFactura: [],
    fechaCreacion: [],
    infoCliente: [],
    valorFactura: [],
    valorPagado: [],
    valorDeuda: [],
    tipoFactura: [],
    idProovedor: [],
  });

  constructor(protected facturaCompraService: FacturaCompraService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ facturaCompra }) => {
      if (facturaCompra.id === undefined) {
        const today = dayjs().startOf('day');
        facturaCompra.fechaCreacion = today;
      }

      this.updateForm(facturaCompra);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const facturaCompra = this.createFromForm();
    if (facturaCompra.id !== undefined) {
      this.subscribeToSaveResponse(this.facturaCompraService.update(facturaCompra));
    } else {
      this.subscribeToSaveResponse(this.facturaCompraService.create(facturaCompra));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFacturaCompra>>): void {
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

  protected updateForm(facturaCompra: IFacturaCompra): void {
    this.editForm.patchValue({
      id: facturaCompra.id,
      numeroFactura: facturaCompra.numeroFactura,
      fechaCreacion: facturaCompra.fechaCreacion ? facturaCompra.fechaCreacion.format(DATE_TIME_FORMAT) : null,
      infoCliente: facturaCompra.infoCliente,
      valorFactura: facturaCompra.valorFactura,
      valorPagado: facturaCompra.valorPagado,
      valorDeuda: facturaCompra.valorDeuda,
      tipoFactura: facturaCompra.tipoFactura,
      idProovedor: facturaCompra.idProovedor,
    });
  }

  protected createFromForm(): IFacturaCompra {
    return {
      ...new FacturaCompra(),
      id: this.editForm.get(['id'])!.value,
      numeroFactura: this.editForm.get(['numeroFactura'])!.value,
      fechaCreacion: this.editForm.get(['fechaCreacion'])!.value
        ? dayjs(this.editForm.get(['fechaCreacion'])!.value, DATE_TIME_FORMAT)
        : undefined,
      infoCliente: this.editForm.get(['infoCliente'])!.value,
      valorFactura: this.editForm.get(['valorFactura'])!.value,
      valorPagado: this.editForm.get(['valorPagado'])!.value,
      valorDeuda: this.editForm.get(['valorDeuda'])!.value,
      tipoFactura: this.editForm.get(['tipoFactura'])!.value,
      idProovedor: this.editForm.get(['idProovedor'])!.value,
    };
  }
}
