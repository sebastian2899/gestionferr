import dayjs from 'dayjs/esm';
import { TipoFacturaEnum } from 'app/entities/enumerations/tipo-factura-enum.model';

export interface IFacturaCompra {
  id?: number;
  numeroFactura?: string | null;
  fechaCreacion?: dayjs.Dayjs | null;
  infoCliente?: string | null;
  valorFactura?: number | null;
  valorPagado?: number | null;
  valorDeuda?: number | null;
  tipoFactura?: TipoFacturaEnum | null;
  idProovedor?: number | null;
}

export class FacturaCompra implements IFacturaCompra {
  constructor(
    public id?: number,
    public numeroFactura?: string | null,
    public fechaCreacion?: dayjs.Dayjs | null,
    public infoCliente?: string | null,
    public valorFactura?: number | null,
    public valorPagado?: number | null,
    public valorDeuda?: number | null,
    public tipoFactura?: TipoFacturaEnum | null,
    public idProovedor?: number | null
  ) {}
}

export function getFacturaCompraIdentifier(facturaCompra: IFacturaCompra): number | undefined {
  return facturaCompra.id;
}
