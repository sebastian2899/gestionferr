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

@Component({
  selector: 'jhi-abono-update',
  templateUrl: './abono-update.component.html',
})
export class AbonoUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    idFactura: [],
    fechaCreacion: [],
    valorAbono: [],
    tipoFactura: [],
  });

  constructor(protected abonoService: AbonoService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ abono }) => {
      if (abono.id === undefined) {
        const today = dayjs().startOf('day');
        abono.fechaCreacion = today;
      }

      this.updateForm(abono);
    });
  }

  previousState(): void {
    window.history.back();
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
      idFactura: this.editForm.get(['idFactura'])!.value,
      fechaCreacion: this.editForm.get(['fechaCreacion'])!.value
        ? dayjs(this.editForm.get(['fechaCreacion'])!.value, DATE_TIME_FORMAT)
        : undefined,
      valorAbono: this.editForm.get(['valorAbono'])!.value,
      tipoFactura: this.editForm.get(['tipoFactura'])!.value,
    };
  }
}
