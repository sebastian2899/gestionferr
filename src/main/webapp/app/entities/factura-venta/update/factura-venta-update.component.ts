import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, FormControl } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IFacturaVenta, FacturaVenta } from '../factura-venta.model';
import { FacturaVentaService } from '../service/factura-venta.service';
import { TipoFacturaEnum } from 'app/entities/enumerations/tipo-factura-enum.model';
import { ClienteService } from 'app/entities/cliente/service/cliente.service';
import { ICliente } from 'app/entities/cliente/cliente.model';
import { IProducto } from 'app/entities/producto/producto.model';
import { ProductoService } from 'app/entities/producto/service/producto.service';
import { IItemFacturaVenta, ItemFacturaVenta } from 'app/entities/item-factura-venta/item-factura-venta.model';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'jhi-factura-venta-update',
  templateUrl: './factura-venta-update.component.html',
})
export class FacturaVentaUpdateComponent implements OnInit {
  @ViewChild('cantidadInvalida', { static: true }) content: ElementRef | undefined;
  @ViewChild('alertaProductoVacio', { static: true }) content2: ElementRef | undefined;

  isSaving = false;
  tipoFacturaEnumValues = Object.keys(TipoFacturaEnum);
  clienteNoRegistrado = true;
  productos?: IProducto[] | null;
  clientes?: ICliente[] | null;
  producto?: IProducto | null;
  cantidad?: number | null;
  productoSeleccionado?: IProducto | null;
  productosSeleccionados: IItemFacturaVenta[] = [];
  itemFacturaVenta?: IItemFacturaVenta | null;
  productoNuevo = true;
  mensajeCantidadInvalida?: string | null;
  mensajeAlertaProductoVacio = 'Por favor seleccione un producto y una cantidad';
  validarValorDeuda?: boolean | null;
  mensajeValorDeuda = 'Para guardar la factura, asegurese de no estar debiendo un valor al cliente.';
  titulo?: string | null;

  editForm = this.fb.group({
    id: [],
    numeroFactura: [],
    fechaCreacion: [],
    infoCliente: [],
    valorFactura: [],
    producto: new FormControl(),
    cantidad: new FormControl(),
    valorPagado: [],
    valorDeuda: [],
    tipoFactura: [],
    idCliente: [],
    estado: [],
  });

  constructor(
    protected facturaVentaService: FacturaVentaService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    protected clienteService: ClienteService,
    protected productoService: ProductoService,
    protected modalService: NgbModal
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ facturaVenta }) => {
      if (facturaVenta.id === undefined) {
        const today = dayjs().startOf('day');
        facturaVenta.fechaCreacion = today;
        this.titulo = 'Crear Factura';
      } else {
        this.titulo = 'Actualizar Factura';
      }

      this.updateForm(facturaVenta);
    });
    this.consultarProductos();
    this.consultarClientes();
  }

  consultarProductos(): void {
    this.productoService.query().subscribe({
      next: (res: HttpResponse<IProducto[]>) => {
        this.productos = res.body ?? [];
      },
      error: () => {
        this.productos = [];
      },
    });
  }

  consultarClientes(): void {
    this.clienteService.query().subscribe({
      next: (res: HttpResponse<ICliente[]>) => {
        this.clientes = res.body ?? [];
      },
      error: () => {
        this.clientes = [];
      },
    });
  }

  previousState(): void {
    window.history.back();
  }

  agregarProducto(): void {
    const idProducto = this.editForm.get(['producto'])!.value;
    this.productos?.forEach(element => {
      if (Number(element.id) === Number(idProducto)) {
        this.productoSeleccionado = element;
      }
    });
    const cantidad1 = this.editForm.get(['cantidad'])!.value;

    if (idProducto && cantidad1) {
      if (this.productosSeleccionados.length > 0 && this.itemFacturaVenta?.idProducto) {
        for (let i = 0; i < this.productosSeleccionados.length; i++) {
          if (this.productosSeleccionados[i].idProducto === Number(idProducto)) {
            this.productosSeleccionados[i].cantidad += cantidad1;
            const cantidadMensaje = Number(cantidad1) + this.productosSeleccionados[i].cantidad!;
            // SE VALIDA QUE LA CANTIDAD NUEVA NO SEA MAYOR A LA CANTIDAD TOTAL DE PRODUCTOS

            if (this.productosSeleccionados[i].cantidad! > this.productosSeleccionados[i].cantidadTotalProducto!) {
              this.mensajeCantidadInvalida = `No se puede agregar el producto ${String(
                this.productosSeleccionados[i].nombreProducto!.toUpperCase()
              )},
                    ya que solo existen ${String(this.productosSeleccionados[i].cantidadTotalProducto)}, y se estan seleccionando 
                   ${String(cantidadMensaje)}, por favor ingrese una cantidad valida.`;
              this.productosSeleccionados[i].cantidad! -= cantidad1;
              this.modalService.open(this.content);
              this.productoNuevo = false;
              break;
            } else {
              const precio = cantidad1 * this.productosSeleccionados[i].precioProducto!;
              this.productosSeleccionados[i].precio! += precio;
              this.productoNuevo = false;
              break;
            }
          } else {
            this.productoNuevo = true;
          }
        }
      }

      if (this.productoSeleccionado && this.productoNuevo) {
        this.itemFacturaVenta = new ItemFacturaVenta();
        const cantidad = this.editForm.get(['cantidad'])!.value;
        const precio = cantidad * this.productoSeleccionado.precio!;
        this.itemFacturaVenta.idProducto = this.productoSeleccionado.id;
        this.itemFacturaVenta.nombreProducto = this.productoSeleccionado.nombre;
        this.itemFacturaVenta.precioProducto = this.productoSeleccionado.precio;
        this.itemFacturaVenta.cantidadTotalProducto = this.productoSeleccionado.cantidad;
        this.itemFacturaVenta.cantidad = cantidad;
        this.itemFacturaVenta.precio = precio;

        if (cantidad > this.itemFacturaVenta.cantidadTotalProducto!) {
          this.mensajeCantidadInvalida = `No se puede agregar el producto ${String(this.itemFacturaVenta.nombreProducto?.toUpperCase())},
           ya que solo existen ${String(this.itemFacturaVenta.cantidadTotalProducto)}, y se estan seleccionando 
          ${String((this.itemFacturaVenta.cantidad = cantidad))}, por favor ingrese una cantidad valida.`;
          this.modalService.open(this.content);
        } else {
          this.productosSeleccionados.push(this.itemFacturaVenta);
        }
      }
    } else {
      this.modalService.open(this.content2);
    }

    this.editForm.get(['producto'])?.setValue(null);
    this.editForm.get(['cantidad'])?.setValue(null);
    this.calcularValores();
  }

  eliminarProductoSeleccionado(itemFactura: IItemFacturaVenta): void {
    const index = this.productosSeleccionados.indexOf(itemFactura);
    if (index >= 0) {
      this.productosSeleccionados.splice(index, 1);
    }
    this.calcularValores();
  }

  calcularValores(): void {
    let valorFactura = 0;
    this.productosSeleccionados.forEach(element => {
      valorFactura += element.precio!;
    });
    this.editForm.get(['valorFactura'])?.setValue(valorFactura);
    this.editForm.get(['valorDeuda'])?.setValue(valorFactura);
    this.editForm.get(['estado'])?.setValue('DEUDA');

    if (valorFactura === 0 || !valorFactura) {
      this.editForm.get(['estado'])?.setValue(null);
    }
  }

  restarValores(): void {
    const valorFactura = this.editForm.get(['valorFactura'])!.value;
    const valorPagado = this.editForm.get(['valorPagado'])!.value;

    const valorDeuda = valorFactura - valorPagado;

    this.editForm.get(['valorDeuda'])?.setValue(valorDeuda);
    let mensaje = '';
    valorDeuda === 0 ? (mensaje = 'PAGADA') : (mensaje = 'DEUDA');
    valorDeuda < 0 ? (this.validarValorDeuda = true) : (this.validarValorDeuda = false);

    this.editForm.get(['estado'])?.setValue(mensaje);
  }

  validarClienteRegistrado(): void {
    this.clienteNoRegistrado = false;
    const infoCliente = this.editForm.get(['infoCliente'])!.value;
    this.editForm.get(['infoCliente'])?.setValue(infoCliente);
  }

  save(): void {
    this.isSaving = true;
    const facturaVenta = this.createFromForm();
    if (facturaVenta.id !== undefined) {
      this.subscribeToSaveResponse(this.facturaVentaService.update(facturaVenta));
    } else {
      facturaVenta.itemsPorFactura = this.productosSeleccionados;
      this.subscribeToSaveResponse(this.facturaVentaService.create(facturaVenta));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFacturaVenta>>): void {
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

  protected updateForm(facturaVenta: IFacturaVenta): void {
    this.editForm.patchValue({
      id: facturaVenta.id,
      numeroFactura: facturaVenta.numeroFactura,
      fechaCreacion: facturaVenta.fechaCreacion ? facturaVenta.fechaCreacion.format(DATE_TIME_FORMAT) : null,
      infoCliente: facturaVenta.infoCliente,
      valorFactura: facturaVenta.valorFactura,
      valorPagado: facturaVenta.valorPagado,
      valorDeuda: facturaVenta.valorDeuda,
      tipoFactura: facturaVenta.tipoFactura,
      idCliente: facturaVenta.idCliente,
      estado: facturaVenta.estado,
    });
  }

  protected createFromForm(): IFacturaVenta {
    return {
      ...new FacturaVenta(),
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
      idCliente: this.editForm.get(['idCliente'])!.value,
      estado: this.editForm.get(['estado'])!.value,
    };
  }
}
