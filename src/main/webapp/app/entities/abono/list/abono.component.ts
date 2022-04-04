import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IAbono } from '../abono.model';
import { AbonoService } from '../service/abono.service';
import { AbonoDeleteDialogComponent } from '../delete/abono-delete-dialog.component';
import { StateStorageService } from 'app/core/auth/state-storage.service';

@Component({
  selector: 'jhi-abono',
  templateUrl: './abono.component.html',
})
export class AbonoComponent implements OnInit {
  abonos?: IAbono[];
  isLoading = false;
  idFactura?: number | null;
  idFacturaCompra?: number | null;

  constructor(protected abonoService: AbonoService, protected modalService: NgbModal, protected storageService: StateStorageService) {}

  loadAll(): void {
    this.isLoading = true;

    if (this.idFactura) {
      const codigo = 1;
      this.abonoService.abonosPorFactura(this.idFactura, codigo).subscribe({
        next: (res: HttpResponse<IAbono[]>) => {
          this.isLoading = false;
          this.abonos = res.body ?? [];
        },
        error: () => {
          this.isLoading = false;
        },
      });
      this.storageService.clearFactura();
    } else {
      this.idFacturaCompra = this.storageService.getParametroFacturaCompra();
      if (this.idFacturaCompra) {
        const codigo = 2;
        this.abonoService.abonosPorFactura(this.idFacturaCompra, codigo).subscribe({
          next: (res: HttpResponse<IAbono[]>) => {
            this.isLoading = false;
            this.abonos = res.body ?? [];
          },
          error: () => {
            this.isLoading = false;
          },
        });
        this.storageService.clearFacturaCompra();
      }
    }
  }

  ngOnInit(): void {
    this.idFactura = this.storageService.getParametroFactura();
    this.idFacturaCompra = this.storageService.getParametroFacturaCompra();
    this.loadAll();
  }

  trackId(index: number, item: IAbono): number {
    return item.id!;
  }

  delete(abono: IAbono): void {
    const modalRef = this.modalService.open(AbonoDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.abono = abono;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
