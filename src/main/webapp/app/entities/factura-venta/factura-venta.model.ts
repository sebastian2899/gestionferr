import dayjs from 'dayjs/esm';
import { TipoFacturaEnum } from 'app/entities/enumerations/tipo-factura-enum.model';
import { IItemFacturaVenta } from '../item-factura-venta/item-factura-venta.model';

export interface IFacturaVenta {
  id?: number;
  numeroFactura?: string | null;
  fechaCreacion?: dayjs.Dayjs | null;
  infoCliente?: string | null;
  valorFactura?: number | null;
  valorPagado?: number | null;
  valorDeuda?: number | null;
  tipoFactura?: TipoFacturaEnum | null;
  itemsPorFactura?: IItemFacturaVenta[] | null;
  idCliente?: number | null;
  estado?: string | null;
}

export class FacturaVenta implements IFacturaVenta {
  constructor(
    public id?: number,
    public numeroFactura?: string | null,
    public fechaCreacion?: dayjs.Dayjs | null,
    public infoCliente?: string | null,
    public valorFactura?: number | null,
    public valorPagado?: number | null,
    public valorDeuda?: number | null,
    public itemsPorF?: IItemFacturaVenta[] | null,
    public tipoFactura?: TipoFacturaEnum | null,
    public idCliente?: number | null,
    public estado?: string | null
  ) {}
}

export function getFacturaVentaIdentifier(facturaVenta: IFacturaVenta): number | undefined {
  return facturaVenta.id;
}
