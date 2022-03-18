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

  constructor(protected abonoService: AbonoService, protected modalService: NgbModal, protected storageService: StateStorageService) {}

  loadAll(): void {
    this.isLoading = true;

    if (this.idFactura) {
      this.abonoService.abonosPorFactura(this.idFactura).subscribe({
        next: (res: HttpResponse<IAbono[]>) => {
          this.isLoading = false;
          this.abonos = res.body ?? [];
        },
        error: () => {
          this.isLoading = false;
        },
      });
    }
  }

  ngOnInit(): void {
    this.idFactura = this.storageService.getParametroFactura();
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
