export interface IClienteFactura {
  idFacturaVenta?: number | null;
  fechaCreacion?: string | null;
  numeroFactura?: string | null;
  valorFactura?: number | null;
  valorPagado?: number | null;
  valorDeuda?: number | null;
}

export class ClienteFactura implements IClienteFactura {
  constructor(
    public idFacturaVenta?: number | null,
    public fechaCreacion?: string | null,
    public numeroFactura?: string | null,
    public valorFactura?: number | null,
    public valorPagado?: number | null,
    public valorDeuda?: number | null
  ) {}
}
