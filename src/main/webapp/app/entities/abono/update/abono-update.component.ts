import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IAbono, Abono } from '../abono.model';
import { AbonoService } from '../service/abono.service';
import { StateStorageService } from 'app/core/auth/state-storage.service';
import { FacturaVentaService } from 'app/entities/factura-venta/service/factura-venta.service';
import { IFacturaVenta } from 'app/entities/factura-venta/factura-venta.model';
import { FacturaCompraService } from 'app/entities/factura-compra/service/factura-compra.service';
import { IFacturaCompra } from 'app/entities/factura-compra/factura-compra.model';

@Component({
  selector: 'jhi-abono-update',
  templateUrl: './abono-update.component.html',
})
export class AbonoUpdateComponent implements OnInit {
  // @ViewChild('precioInvalido',{static:true}) content: ElementRef | undefined;

  isSaving = false;
  idFactura?: number | null;
  idFacturaCompra?: number | null;
  tipoFactura?: string | null;
  validarPrecioAbono?: boolean | null;
  valorDeuda?: number | null;

  editForm = this.fb.group({
    id: [],
    idFactura: [],
    fechaCreacion: [],
    valorAbono: [],
    tipoFactura: [],
    valorDeuda: [],
  });

  constructor(
    protected abonoService: AbonoService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    protected storageService: StateStorageService,
    protected facturaVentaService: FacturaVentaService,
    protected facturaCompraService: FacturaCompraService
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ abono }) => {
      if (abono.id === undefined) {
        const today = dayjs().startOf('day');
        abono.fechaCreacion = today;
      }

      this.updateForm(abono);
    });
    this.idFactura = this.storageService.getParametroFactura();
    this.asignartipoFactura();
  }

  previousState(): void {
    window.history.back();
  }

  asignartipoFactura(): void {
    if (this.idFactura) {
      this.facturaVentaService.find(this.idFactura).subscribe({
        next: (res: HttpResponse<IFacturaVenta>) => {
          const factura = res.body;
          if (factura) {
            this.valorDeuda = factura.valorDeuda;
            const tipoFactura = 'Factura Venta';
            this.editForm.get(['tipoFactura'])?.setValue(tipoFactura);
            this.editForm.get(['valorDeuda'])?.setValue(this.valorDeuda);
          }
        },
        error: () => {
          this.tipoFactura = null;
        },
      });
      this.storageService.clearFactura();
    } else {
      this.idFacturaCompra = this.storageService.getParametroFacturaCompra();
      this.facturaCompraService.find(this.idFacturaCompra!).subscribe({
        next: (res: HttpResponse<IFacturaCompra>) => {
          const factura = res.body;
          this.valorDeuda = factura?.valorDeuda;
          const tipoFactura = 'Factura Compra';
          this.editForm.get(['tipoFactura'])?.setValue(tipoFactura);
          this.editForm.get(['valorDeuda'])?.setValue(this.valorDeuda);
        },
      });
      this.storageService.clearFacturaCompra();
    }
  }

  restarValores(): void {
    const valorAbono = this.editForm.get(['valorAbono'])!.value;

    if (this.valorDeuda! < valorAbono) {
      this.validarPrecioAbono = true;
    } else {
      this.validarPrecioAbono = false;
    }

    const deuda = Number(this.valorDeuda) - Number(valorAbono);
    this.editForm.get(['valorDeuda'])?.setValue(deuda);

    if (valorAbono === null || valorAbono === undefined) {
      this.editForm.get(['valorDeuda'])?.setValue(this.valorDeuda);
      this.validarPrecioAbono = false;
    }
  }

  save(): void {
    this.isSaving = true;
    const abono = this.createFromForm();
    if (abono.id !== undefined) {
      this.subscribeToSaveResponse(this.abonoService.update(abono));
    } else {
      this.subscribeToSaveResponse(this.abonoService.create(abono));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAbono>>): void {
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

  protected updateForm(abono: IAbono): void {
    this.editForm.patchValue({
      id: abono.id,
      idFactura: abono.idFactura,
      fechaCreacion: abono.fechaCreacion ? abono.fechaCreacion.format(DATE_TIME_FORMAT) : null,
      valorAbono: abono.valorAbono,
      tipoFactura: abono.tipoFactura,
    });
  }

  protected createFromForm(): IAbono {
    return {
      ...new Abono(),
      id: this.editForm.get(['id'])!.value,
      idFactura: this.idFactura,
      fechaCreacion: this.editForm.get(['fechaCreacion'])!.value
        ? dayjs(this.editForm.get(['fechaCreacion'])!.value, DATE_TIME_FORMAT)
        : undefined,
      valorAbono: this.editForm.get(['valorAbono'])!.value,
      tipoFactura: this.editForm.get(['tipoFactura'])!.value,
    };
  }
}
