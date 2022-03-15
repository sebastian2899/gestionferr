import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'producto',
        data: { pageTitle: 'gestionferrApp.producto.home.title' },
        loadChildren: () => import('./producto/producto.module').then(m => m.ProductoModule),
      },
      {
        path: 'factura-venta',
        data: { pageTitle: 'gestionferrApp.facturaVenta.home.title' },
        loadChildren: () => import('./factura-venta/factura-venta.module').then(m => m.FacturaVentaModule),
      },
      {
        path: 'factura-compra',
        data: { pageTitle: 'gestionferrApp.facturaCompra.home.title' },
        loadChildren: () => import('./factura-compra/factura-compra.module').then(m => m.FacturaCompraModule),
      },
      {
        path: 'cliente',
        data: { pageTitle: 'gestionferrApp.cliente.home.title' },
        loadChildren: () => import('./cliente/cliente.module').then(m => m.ClienteModule),
      },
      {
        path: 'proveedor',
        data: { pageTitle: 'gestionferrApp.proveedor.home.title' },
        loadChildren: () => import('./proveedor/proveedor.module').then(m => m.ProveedorModule),
      },
      {
        path: 'item-factura-compra',
        data: { pageTitle: 'gestionferrApp.itemFacturaCompra.home.title' },
        loadChildren: () => import('./item-factura-compra/item-factura-compra.module').then(m => m.ItemFacturaCompraModule),
      },
      {
        path: 'item-factura-venta',
        data: { pageTitle: 'gestionferrApp.itemFacturaVenta.home.title' },
        loadChildren: () => import('./item-factura-venta/item-factura-venta.module').then(m => m.ItemFacturaVentaModule),
      },
      {
        path: 'caja',
        data: { pageTitle: 'gestionferrApp.caja.home.title' },
        loadChildren: () => import('./caja/caja.module').then(m => m.CajaModule),
      },
      {
        path: 'abono',
        data: { pageTitle: 'gestionferrApp.abono.home.title' },
        loadChildren: () => import('./abono/abono.module').then(m => m.AbonoModule),
      },
      {
        path: 'egreso',
        data: { pageTitle: 'gestionferrApp.egreso.home.title' },
        loadChildren: () => import('./egreso/egreso.module').then(m => m.EgresoModule),
      },
      {
        path: 'categoria',
        data: { pageTitle: 'gestionferrApp.categoria.home.title' },
        loadChildren: () => import('./categoria/categoria.module').then(m => m.CategoriaModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
