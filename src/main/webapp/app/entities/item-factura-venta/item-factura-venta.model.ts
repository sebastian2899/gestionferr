export interface IItemFacturaVenta {
  id?: number;
  idFacturaVenta?: number | null;
  idProducto?: number | null;
  cantidad?: number | null;
  precio?: number | null;
  nombreProducto?: string | null;
  cantidadTotalProducto?: number | null;
  precioProducto?: number | null;
}

export class ItemFacturaVenta implements IItemFacturaVenta {
  constructor(
    public id?: number,
    public idFacturaVenta?: number | null,
    public idProducto?: number | null,
    public cantidad?: number | null,
    public precio?: number | null,
    public nombreProducto?: string | null,
    public cantidadTotalProducto?: number | null,
    public precioProducto?: number | null
  ) {}
}

export function getItemFacturaVentaIdentifier(itemFacturaVenta: IItemFacturaVenta): number | undefined {
  return itemFacturaVenta.id;
}
