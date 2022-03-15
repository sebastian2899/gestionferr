import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IProveedor, Proveedor } from '../proveedor.model';
import { ProveedorService } from '../service/proveedor.service';
import { TipoIdentificacionEnum } from 'app/entities/enumerations/tipo-identificacion-enum.model';
import { TipoProveedorEnum } from 'app/entities/enumerations/tipo-proveedor-enum.model';

@Component({
  selector: 'jhi-proveedor-update',
  templateUrl: './proveedor-update.component.html',
})
export class ProveedorUpdateComponent implements OnInit {
  isSaving = false;
  tipoIdentificacionEnumValues = Object.keys(TipoIdentificacionEnum);
  tipoProveedorEnumValues = Object.keys(TipoProveedorEnum);

  editForm = this.fb.group({
    id: [],
    nombre: [],
    numeroContacto: [],
    email: [],
    tipoIdentificacion: [],
    numeroCC: [],
    tipoProveedor: [],
  });

  constructor(protected proveedorService: ProveedorService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ proveedor }) => {
      this.updateForm(proveedor);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const proveedor = this.createFromForm();
    if (proveedor.id !== undefined) {
      this.subscribeToSaveResponse(this.proveedorService.update(proveedor));
    } else {
      this.subscribeToSaveResponse(this.proveedorService.create(proveedor));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProveedor>>): void {
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

  protected updateForm(proveedor: IProveedor): void {
    this.editForm.patchValue({
      id: proveedor.id,
      nombre: proveedor.nombre,
      numeroContacto: proveedor.numeroContacto,
      email: proveedor.email,
      tipoIdentificacion: proveedor.tipoIdentificacion,
      numeroCC: proveedor.numeroCC,
      tipoProveedor: proveedor.tipoProveedor,
    });
  }

  protected createFromForm(): IProveedor {
    return {
      ...new Proveedor(),
      id: this.editForm.get(['id'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
      numeroContacto: this.editForm.get(['numeroContacto'])!.value,
      email: this.editForm.get(['email'])!.value,
      tipoIdentificacion: this.editForm.get(['tipoIdentificacion'])!.value,
      numeroCC: this.editForm.get(['numeroCC'])!.value,
      tipoProveedor: this.editForm.get(['tipoProveedor'])!.value,
    };
  }
}
