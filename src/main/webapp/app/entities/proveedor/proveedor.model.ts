import { TipoIdentificacionEnum } from 'app/entities/enumerations/tipo-identificacion-enum.model';
import { TipoProveedorEnum } from 'app/entities/enumerations/tipo-proveedor-enum.model';
import dayjs from 'dayjs';
import { IItemPorFacturaCompra } from '../factura-compra/itemPorFacturaCompra';

export interface IProveedor {
  fechaCreacion?: dayjs.Dayjs | null;
  id?: number;
  nombre?: string | null;
  numeroContacto?: string | null;
  email?: string | null;
  tipoIdentificacion?: TipoIdentificacionEnum | null;
  numeroCC?: string | null;
  tipoProveedor?: TipoProveedorEnum | null;
  facturasProovedor?: IItemPorFacturaCompra[] | null;
}

export class Proveedor implements IProveedor {
  constructor(
    public fechaCreacion?: dayjs.Dayjs | null,
    public id?: number,
    public nombre?: string | null,
    public numeroContacto?: string | null,
    public email?: string | null,
    public tipoIdentificacion?: TipoIdentificacionEnum | null,
    public numeroCC?: string | null,
    public tipoProveedor?: TipoProveedorEnum | null,
    public facturasProovedor?: IItemPorFacturaCompra[] | null
  ) {}
}

export function getProveedorIdentifier(proveedor: IProveedor): number | undefined {
  return proveedor.id;
}
