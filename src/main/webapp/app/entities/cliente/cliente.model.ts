import { TipoIdentificacionEnum } from 'app/entities/enumerations/tipo-identificacion-enum.model';
import { IFacturaVenta } from '../factura-venta/factura-venta.model';

export interface ICliente {
  id?: number;
  nombre?: string | null;
  numeroContacto?: string | null;
  email?: string | null;
  tipoIdentificacion?: TipoIdentificacionEnum | null;
  numeroCC?: string | null;
  facturasCliente?: IFacturaVenta[] | null;
}

export class Cliente implements ICliente {
  constructor(
    public id?: number,
    public nombre?: string | null,
    public numeroContacto?: string | null,
    public email?: string | null,
    public tipoIdentificacion?: TipoIdentificacionEnum | null,
    public numeroCC?: string | null,
    public facturasCliente?: IFacturaVenta[] | null
  ) {}
}

export function getClienteIdentifier(cliente: ICliente): number | undefined {
  return cliente.id;
}
