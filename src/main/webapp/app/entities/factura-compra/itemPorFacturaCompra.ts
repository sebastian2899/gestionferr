export interface IItemPorFacturaCompra {
  numeroFactura?: string | null;
  fecha?: String | null;
  valorFactura?: number | null;
  valorDeuda?: number | null;
}

export class ItemPorFacturaCompra implements IItemPorFacturaCompra {
  constructor(
    public numeroFactura?: string | null,
    public fecha?: string | null,
    public valorFactura?: number | null,
    public valorDeuda?: number | null
  ) {}
}
