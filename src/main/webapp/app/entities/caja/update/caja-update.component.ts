import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ICaja, Caja } from '../caja.model';
import { CajaService } from '../service/caja.service';
import { TipoEstadoEnum } from 'app/entities/enumerations/tipo-estado-enum.model';
import { AlertService } from 'app/core/util/alert.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'jhi-caja-update',
  templateUrl: './caja-update.component.html',
})
export class CajaUpdateComponent implements OnInit {
  @ViewChild('mensajeValidacionCaja', { static: true }) content: ElementRef | undefined;

  isSaving = false;
  tipoEstadoEnumValues = Object.keys(TipoEstadoEnum);
  valorCajaDia?: number | null;
  mensajePrecioMayor = false;

  editForm = this.fb.group({
    id: [],
    fechaCreacion: [],
    valorVentaDia: [],
    valorRegistradoDia: [],
    estado: [],
    diferencia: [],
  });

  constructor(
    protected cajaService: CajaService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    protected alerService: AlertService,
    protected modal: NgbModal
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ caja }) => {
      if (caja.id === undefined) {
        const today = dayjs().startOf('day');
        caja.fechaCreacion = today;
      }

      this.updateForm(caja);
    });

    this.consultarValorDia();
  }

  previousState(): void {
    window.history.back();
  }

  consultarValorDia(): void {
    this.cajaService.valorCajaDia().subscribe({
      next: (res: HttpResponse<number>) => {
        this.valorCajaDia = res.body;
        if (this.valorCajaDia) {
          this.editForm.get(['valorVentaDia'])?.setValue(this.valorCajaDia);
          this.editForm.get(['estado'])?.setValue('DEUDA');
          this.editForm.get(['diferencia'])?.setValue(this.valorCajaDia);
        } else {
          this.editForm.get(['valorVentaDia'])?.setValue(0);
        }
      },
      error: () => {
        this.valorCajaDia = null;
      },
    });
  }

  calcularValores(): void {
    const valorRegistrado = this.editForm.get(['valorRegistradoDia'])!.value;
    this.mensajePrecioMayor = false;
    if (this.valorCajaDia) {
      const diferencia = this.valorCajaDia - valorRegistrado;
      this.editForm.get(['diferencia'])?.setValue(diferencia);

      let estado = '';
      diferencia === 0 ? (estado = 'PAGADA') : (estado = 'DEUDA');

      this.editForm.get(['estado'])?.setValue(estado);

      if (diferencia < 0) {
        this.mensajePrecioMayor = true;
      }
    }
  }

  validarCreacionCaja(): void {
    this.cajaService.validarCreacionCaja().subscribe({
      next: (res: HttpResponse<boolean>) => {
        const boolean = res.body;
        if (boolean) {
          this.modal.open(this.content);
        } else {
          this.save();
        }
      },
      error: () => {
        this.alerService.addAlert({
          type: 'danger',
          message: 'Error al validar la caja',
        });
      },
    });
  }

  previousStateModal(): void {
    this.modal.dismissAll();
  }

  save(): void {
    this.isSaving = true;
    const caja = this.createFromForm();
    if (caja.id !== undefined) {
      this.subscribeToSaveResponse(this.cajaService.update(caja));
    } else {
      this.subscribeToSaveResponse(this.cajaService.create(caja));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICaja>>): void {
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

  protected updateForm(caja: ICaja): void {
    this.editForm.patchValue({
      id: caja.id,
      fechaCreacion: caja.fechaCreacion ? caja.fechaCreacion.format(DATE_TIME_FORMAT) : null,
      valorVentaDia: caja.valorVentaDia,
      valorRegistradoDia: caja.valorRegistradoDia,
      estado: caja.estado,
      diferencia: caja.diferencia,
    });
  }

  protected createFromForm(): ICaja {
    return {
      ...new Caja(),
      id: this.editForm.get(['id'])!.value,
      fechaCreacion: this.editForm.get(['fechaCreacion'])!.value
        ? dayjs(this.editForm.get(['fechaCreacion'])!.value, DATE_TIME_FORMAT)
        : undefined,
      valorVentaDia: this.editForm.get(['valorVentaDia'])!.value,
      valorRegistradoDia: this.editForm.get(['valorRegistradoDia'])!.value,
      estado: this.editForm.get(['estado'])!.value,
      diferencia: this.editForm.get(['diferencia'])!.value,
    };
  }
}
