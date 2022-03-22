import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ICliente, Cliente } from '../cliente.model';
import { ClienteService } from '../service/cliente.service';
import { TipoIdentificacionEnum } from 'app/entities/enumerations/tipo-identificacion-enum.model';

@Component({
  selector: 'jhi-cliente-update',
  templateUrl: './cliente-update.component.html',
})
export class ClienteUpdateComponent implements OnInit {
  isSaving = false;
  tipoIdentificacionEnumValues = Object.keys(TipoIdentificacionEnum);
  titulo?: string | null;

  editForm = this.fb.group({
    id: [],
    nombre: [],
    numeroContacto: [],
    email: [],
    tipoIdentificacion: [],
    numeroCC: [],
  });

  constructor(protected clienteService: ClienteService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cliente }) => {
      if (cliente.id !== undefined) {
        this.titulo = 'Actualizar Cliente';
      } else {
        this.titulo = 'Crear Cliente';
      }
      this.updateForm(cliente);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const cliente = this.createFromForm();
    if (cliente.id !== undefined) {
      this.subscribeToSaveResponse(this.clienteService.update(cliente));
    } else {
      this.subscribeToSaveResponse(this.clienteService.create(cliente));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICliente>>): void {
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

  protected updateForm(cliente: ICliente): void {
    this.editForm.patchValue({
      id: cliente.id,
      nombre: cliente.nombre,
      numeroContacto: cliente.numeroContacto,
      email: cliente.email,
      tipoIdentificacion: cliente.tipoIdentificacion,
      numeroCC: cliente.numeroCC,
    });
  }

  protected createFromForm(): ICliente {
    return {
      ...new Cliente(),
      id: this.editForm.get(['id'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
      numeroContacto: this.editForm.get(['numeroContacto'])!.value,
      email: this.editForm.get(['email'])!.value,
      tipoIdentificacion: this.editForm.get(['tipoIdentificacion'])!.value,
      numeroCC: this.editForm.get(['numeroCC'])!.value,
    };
  }
}
