export interface IItemFacturaCompra {
  id?: number;
  idFacturaCompra?: number | null;
  idProducto?: number | null;
  cantidad?: number | null;
  precio?: number | null;
  cantidadTotalProducto?: number | null;
  precioProducto?: number | null;
  nombreProducto?: string | null;
}

export class ItemFacturaCompra implements IItemFacturaCompra {
  constructor(
    public id?: number,
    public idFacturaCompra?: number | null,
    public idProducto?: number | null,
    public cantidad?: number | null,
    public precio?: number | null,
    public cantidadTotalProducto?: number | null,
    public nombreProducto?: string | null,
    public precioProducto?: number | null
  ) {}
}

export function getItemFacturaCompraIdentifier(itemFacturaCompra: IItemFacturaCompra): number | undefined {
  return itemFacturaCompra.id;
}
