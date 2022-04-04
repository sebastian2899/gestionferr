import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, FormControl } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IFacturaCompra, FacturaCompra } from '../factura-compra.model';
import { FacturaCompraService } from '../service/factura-compra.service';
import { TipoFacturaEnum } from 'app/entities/enumerations/tipo-factura-enum.model';
import { ProveedorService } from 'app/entities/proveedor/service/proveedor.service';
import { IProveedor } from 'app/entities/proveedor/proveedor.model';
import { IProducto } from 'app/entities/producto/producto.model';
import { ProductoService } from 'app/entities/producto/service/producto.service';
import { IItemFacturaCompra, ItemFacturaCompra } from 'app/entities/item-factura-compra/item-factura-compra.model';
import { AlertService } from 'app/core/util/alert.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'jhi-factura-compra-update',
  templateUrl: './factura-compra-update.component.html',
})
export class FacturaCompraUpdateComponent implements OnInit {
  @ViewChild('mensajeValidacionFactura', { static: true }) content: ElementRef | undefined;

  isSaving = false;
  tipoFacturaEnumValues = Object.keys(TipoFacturaEnum);
  proveedores?: IProveedor[] | null;
  producto?: IProducto | null;
  cantidad?: number | null;
  productos?: IProducto[] | null;
  productoSeleccionado?: IProducto | null;
  productoItemSeleccionado?: IItemFacturaCompra | null;
  productosSeleccionados?: IItemFacturaCompra[] = [];
  productoNuevo = true;
  validarEditar = false;
  numFactura?: string | null;

  editForm = this.fb.group({
    id: [],
    numeroFactura: [],
    fechaCreacion: [],
    infoCliente: [],
    producto: new FormControl(),
    cantidad: new FormControl(),
    valorFactura: [],
    valorPagado: [],
    valorDeuda: [],
    tipoFactura: [],
    idProovedor: [],
    estado: [],
  });

  constructor(
    protected facturaCompraService: FacturaCompraService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    protected proveedorServide: ProveedorService,
    protected productoService: ProductoService,
    protected alertService: AlertService,
    protected modalService: NgbModal
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ facturaCompra }) => {
      if (facturaCompra.id === undefined) {
        const today = dayjs().startOf('day');
        facturaCompra.fechaCreacion = today;
        this.validarEditar = false;
      } else {
        this.productosSeleccionados = facturaCompra.itemsFacturaCompra;
        this.validarEditar = true;
      }

      this.updateForm(facturaCompra);
    });
    this.consultarProveedores();
    this.consultarProductos();
  }

  previousState(): void {
    window.history.back();
  }

  consultarProveedores(): void {
    this.proveedorServide.query().subscribe({
      next: (res: HttpResponse<IProveedor[]>) => {
        this.proveedores = res.body ?? [];
      },
      error: () => {
        this.proveedores = [];
      },
    });
  }

  seleccionarProducto(): void {
    const idProducto = this.editForm.get(['producto'])!.value;
    this.productos?.forEach(element => {
      if (element.id === Number(idProducto)) {
        this.productoSeleccionado = element;
      }
    });
    const cantidad = this.editForm.get(['cantidad'])!.value;

    if (idProducto && cantidad) {
      if (this.productosSeleccionados && this.productosSeleccionados.length > 0) {
        for (let i = 0; i < this.productosSeleccionados.length; i++) {
          if (this.productosSeleccionados[i].idProducto === Number(idProducto)) {
            this.productosSeleccionados[i].cantidad! += Number(cantidad);
            const cantSumar = Number(cantidad) * this.productoSeleccionado!.precio!;
            this.productosSeleccionados[i].precio = this.productosSeleccionados[i].precio! + Number(cantSumar);
            this.productoNuevo = false;
            break;
          } else {
            this.productoNuevo = true;
          }
        }
      }

      if (this.productoSeleccionado && this.productoNuevo) {
        this.productoItemSeleccionado = new ItemFacturaCompra();
        this.productoItemSeleccionado.cantidadTotalProducto = this.productoSeleccionado.cantidad;
        this.productoItemSeleccionado.idProducto = this.productoSeleccionado.id;
        this.productoItemSeleccionado.nombreProducto = this.productoSeleccionado.nombre;
        this.productoItemSeleccionado.precioProducto = this.productoSeleccionado.precio;
        this.productoItemSeleccionado.cantidad = cantidad;
        this.productoItemSeleccionado.precio = this.productoItemSeleccionado.precioProducto! * Number(cantidad);

        this.productosSeleccionados?.push(this.productoItemSeleccionado);
      }

      this.editForm.get(['producto'])?.setValue(null);
      this.editForm.get(['cantidad'])?.setValue(null);
      this.calcularValores();
    } else {
      this.alertService.addAlert({
        type: 'warning',
        message: 'Seleccione un producto y una cantidad valida',
      });
    }
  }

  calcularValores(): void {
    let valorFactura = 0;
    this.productosSeleccionados?.forEach(element => {
      valorFactura += element.precio!;
    });

    this.editForm.get(['valorFactura'])?.setValue(valorFactura);
    this.editForm.get(['valorDeuda'])?.setValue(valorFactura);
    this.editForm.get(['estado'])?.setValue('DEUDA');

    const valorPagado = this.editForm.get(['valorPagado'])!.value;

    if (valorPagado) {
      const valorDeuda = valorFactura - valorPagado;

      this.editForm.get(['valorDeuda'])?.setValue(valorDeuda);
      let mensaje = '';
      valorDeuda === 0 ? (mensaje = 'PAGADA') : (mensaje = 'DEUDA');

      this.editForm.get(['estado'])?.setValue(mensaje);
    }
  }

  eliminarProducto(producto: IItemFacturaCompra): void {
    const index = this.productosSeleccionados!.indexOf(producto);
    if (index >= 0) {
      this.productosSeleccionados?.splice(index, 1);
    }
    this.calcularValores();
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

  validarFacturaCompraSave(): void {
    const numeroFactura = this.editForm.get(['numeroFactura'])!.value;

    this.facturaCompraService.validarFacturaCompra(numeroFactura).subscribe({
      next: (res: HttpResponse<boolean>) => {
        this.numFactura = numeroFactura;
        const respValidation = res.body;
        if (respValidation) {
          this.modalService.open(this.content);
        } else {
          this.save();
        }
      },
      error: () => {
        this.numFactura = null;
      },
    });
  }

  save(): void {
    this.isSaving = true;
    const facturaCompra = this.createFromForm();
    if (facturaCompra.id !== undefined) {
      this.subscribeToSaveResponse(this.facturaCompraService.update(facturaCompra));
    } else {
      facturaCompra.itemsFacturaCompra = this.productosSeleccionados;
      this.subscribeToSaveResponse(this.facturaCompraService.create(facturaCompra));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFacturaCompra>>): void {
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

  protected updateForm(facturaCompra: IFacturaCompra): void {
    this.editForm.patchValue({
      id: facturaCompra.id,
      numeroFactura: facturaCompra.numeroFactura,
      fechaCreacion: facturaCompra.fechaCreacion ? facturaCompra.fechaCreacion.format(DATE_TIME_FORMAT) : null,
      infoCliente: facturaCompra.infoCliente,
      valorFactura: facturaCompra.valorFactura,
      valorPagado: facturaCompra.valorPagado,
      valorDeuda: facturaCompra.valorDeuda,
      tipoFactura: facturaCompra.tipoFactura,
      idProovedor: facturaCompra.idProovedor,
      estado: facturaCompra.estado,
    });
  }

  protected createFromForm(): IFacturaCompra {
    return {
      ...new FacturaCompra(),
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
      idProovedor: this.editForm.get(['idProovedor'])!.value,
      estado: this.editForm.get(['estado'])!.value,
    };
  }
}
