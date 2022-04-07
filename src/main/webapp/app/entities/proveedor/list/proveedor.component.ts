import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IProveedor, Proveedor } from '../proveedor.model';
import { ProveedorService } from '../service/proveedor.service';
import { ProveedorDeleteDialogComponent } from '../delete/proveedor-delete-dialog.component';
import { TipoProveedorEnum } from 'app/entities/enumerations/tipo-proveedor-enum.model';

@Component({
  selector: 'jhi-proveedor',
  templateUrl: './proveedor.component.html',
})
export class ProveedorComponent implements OnInit {
  proveedors?: IProveedor[];
  isLoading = false;
  nombre?: string | null;
  numeroContacto?: string | null;
  numeroCC?: string | null;
  tipoProveedor = Object.keys(TipoProveedorEnum);
  tipoProveedor2?: TipoProveedorEnum | null;
  proveedor?: IProveedor | null;

  constructor(protected proveedorService: ProveedorService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.proveedorService.query().subscribe({
      next: (res: HttpResponse<IProveedor[]>) => {
        this.isLoading = false;
        this.proveedors = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  proveedoresFiltro(): void {
    this.proveedor = new Proveedor();

    this.proveedor.nombre = this.nombre;
    this.proveedor.numeroContacto = this.numeroContacto;
    this.proveedor.numeroCC = this.numeroCC;
    this.proveedor.tipoProveedor = this.tipoProveedor2;

    this.proveedorService.proveedoresFiltro(this.proveedor).subscribe({
      next: (res: HttpResponse<IProveedor[]>) => {
        this.proveedors = res.body ?? [];
      },
      error: () => {
        this.proveedors = [];
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IProveedor): number {
    return item.id!;
  }

  delete(proveedor: IProveedor): void {
    const modalRef = this.modalService.open(ProveedorDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.proveedor = proveedor;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
