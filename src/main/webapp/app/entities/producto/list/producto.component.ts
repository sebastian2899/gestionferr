import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IProducto } from '../producto.model';
import { ProductoService } from '../service/producto.service';
import { ProductoDeleteDialogComponent } from '../delete/producto-delete-dialog.component';
import { DataUtils } from 'app/core/util/data-util.service';
import { ICategoria } from 'app/entities/categoria/categoria.model';
import { CategoriaService } from 'app/entities/categoria/service/categoria.service';
import { AlertService } from 'app/core/util/alert.service';

@Component({
  selector: 'jhi-producto',
  templateUrl: './producto.component.html',
})
export class ProductoComponent implements OnInit {
  @ViewChild('mensajeProductoPorcentajes', { static: true }) content: ElementRef | undefined;

  productos?: IProducto[];
  productosAgotados?: IProducto[];
  productosCasiAgotados?: IProducto[];
  isLoading = false;
  categorias?: ICategoria[] | null;
  idCategoria?: number | null;
  mostrarProductosAgotados?: boolean | null;
  botonProductosAgotados = false;
  mensajePorcentaje?: string | null;
  aumento = false;
  decremento = false;
  cantidadAumentar?: number | null;
  cantidadDecrementar?: number | null;
  nombreProducto?: string | null;

  constructor(
    protected productoService: ProductoService,
    protected dataUtils: DataUtils,
    protected modalService: NgbModal,
    protected categoriaService: CategoriaService,
    protected alertService: AlertService
  ) {}

  loadAll(): void {
    this.isLoading = true;

    this.productoService.query().subscribe({
      next: (res: HttpResponse<IProducto[]>) => {
        this.isLoading = false;
        this.productos = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
    this.consultarCategorias();
    this.productosAgotadosMethod();
  }

  mensajeProductosPorcentajes(): void {
    this.mensajePorcentaje =
      'En los enlaces podra elegir entre aumentar todo el precio de los productos o por el contrario' +
      ' disminuir el precio, tenga en cuénta que se aplicará el descuénto que usted digíte para todos los productos, tomando en cuénta' +
      ' el precio de cada producto, si desea hacer un cambio no general para un producto en específico, diríjase a la sección editar' +
      ' de el producto al que desea modificarle el precio.';

    this.modalService.open(this.content);
  }

  productosAgotadosMethod(): void {
    this.productoService.productosAgotados().subscribe({
      next: (res: HttpResponse<IProducto[]>) => {
        this.productosAgotados = res.body ?? [];
        if (this.productosAgotados.length > 0) {
          this.botonProductosAgotados = true;
        }
      },
      error: () => {
        this.productosAgotados = [];
      },
    });

    this.productoService.productosCasiAgotados().subscribe({
      next: (resp: HttpResponse<IProducto[]>) => {
        this.productosCasiAgotados = resp.body ?? [];
        if (this.productosCasiAgotados.length > 0) {
          this.botonProductosAgotados = true;
        }
      },
      error: () => {
        this.productosCasiAgotados = [];
      },
    });
  }
  OpcionesPorcentjaeDefecto(): void {
    this.aumento = false;
    this.decremento = false;
  }

  aplicarPorcentajePrecio(): void {
    if (this.aumento) {
      if (this.cantidadAumentar! <= 0) {
        this.alertService.addAlert({
          type: 'danger',
          message: 'Por favor ingrese un porcentáje valido.',
        });
      } else {
        this.productoService.actualizarPrecioProductos('aumento', this.cantidadAumentar!).subscribe(() => {
          this.alertService.addAlert({
            type: 'success',
            message: 'Precio de los productos Actualizados Correctamente.',
          });
          this.loadAll();
          this.aumento = false;
        });
      }
    } else if (this.decremento) {
      if (this.cantidadDecrementar! <= 0) {
        this.alertService.addAlert({
          type: 'danger',
          message: 'Por favor ingrese un porcentáje valido.',
        });
      } else {
        this.productoService.actualizarPrecioProductos('decremento', this.cantidadDecrementar!).subscribe(() => {
          this.alertService.addAlert({
            type: 'success',
            message: 'Precio de los productos Actualizados Correctamente.',
          });
          this.loadAll();
          this.decremento = false;
        });
      }
    }
  }

  consultarCategorias(): void {
    this.categoriaService.query().subscribe({
      next: (res: HttpResponse<ICategoria[]>) => {
        this.categorias = res.body ?? [];
      },
      error: () => {
        this.categorias = [];
      },
    });
  }

  agregarPorcentajePrecios(opcion: string): void {
    if (opcion === 'aumento') {
      this.aumento = true;
      this.decremento = false;
    } else if (opcion === 'decremento') {
      this.aumento = false;
      this.decremento = true;
    }
  }

  ocultarAgotados(codigo: number): void {
    if (codigo === 1) {
      this.mostrarProductosAgotados = false;
    } else {
      this.mostrarProductosAgotados = true;
    }
  }

  productosPorNombre(): void {
    if (this.nombreProducto) {
      this.productoService.productosPorNombre(this.nombreProducto).subscribe({
        next: (res: HttpResponse<IProducto[]>) => {
          this.productos = res.body ?? [];
        },
        error: () => {
          this.productos = [];
        },
      });
    } else {
      this.loadAll();
    }
  }

  productosPorCategoria(): void {
    if (this.idCategoria) {
      this.productoService.productosPorCategoria(this.idCategoria).subscribe({
        next: (res: HttpResponse<IProducto[]>) => {
          this.productos = res.body ?? [];
        },
        error: () => {
          this.productos = [];
        },
      });
    } else {
      this.loadAll();
    }
  }

  previusState(): void {
    this.modalService.dismissAll();
  }

  filtroProductoAutomatico(codigo?: number): void {
    this.productoService.productosFiltroAutomatico(codigo!).subscribe({
      next: (res: HttpResponse<IProducto[]>) => {
        this.productos = res.body ?? [];
      },
      error: () => {
        this.productos = [];
      },
    });
  }

  trackId(index: number, item: IProducto): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(producto: IProducto): void {
    const modalRef = this.modalService.open(ProductoDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.producto = producto;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
