import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { Cliente, ICliente } from '../cliente.model';
import { ClienteService } from '../service/cliente.service';
import { ClienteDeleteDialogComponent } from '../delete/cliente-delete-dialog.component';

@Component({
  selector: 'jhi-cliente',
  templateUrl: './cliente.component.html',
})
export class ClienteComponent implements OnInit {
  clientes?: ICliente[];
  isLoading = false;
  cliente?: ICliente | null;
  nombre = '';
  numeroCC = '';

  constructor(protected clienteService: ClienteService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.clienteService.query().subscribe({
      next: (res: HttpResponse<ICliente[]>) => {
        this.isLoading = false;
        this.clientes = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  filtrarCliente(): void {
    this.cliente = new Cliente();
    this.cliente.nombre = this.nombre;
    this.cliente.numeroCC = this.numeroCC;

    this.clienteService.clientesFiltro(this.cliente).subscribe({
      next: (res: HttpResponse<ICliente[]>) => {
        this.clientes = res.body ?? [];
      },
      error: () => {
        this.clientes = [];
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ICliente): number {
    return item.id!;
  }

  delete(cliente: ICliente): void {
    const modalRef = this.modalService.open(ClienteDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.cliente = cliente;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
