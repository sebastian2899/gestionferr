export interface IFacturaVentaFechas {
  valorTotal?: number | null;
  valorPagado?: number | null;
  diferencia?: number | null;
}

export class FacturaVentaFechas implements IFacturaVentaFechas {
  constructor(public valorTotal?: number | null, public valorPagado?: number | null, public diferencia?: number | null) {}
}
