export interface IRegistroFacturaCompra {
  valorFactura?: number | null;
  valorPagado?: number | null;
  valorDeuda?: number | null;
}

export class RegistroFacturaCompra {
  constructor(public valorFactura?: number | null, public valorPagado?: number | null, public valorDeuda?: number | null) {}
}
