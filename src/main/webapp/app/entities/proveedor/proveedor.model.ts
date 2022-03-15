import { TipoIdentificacionEnum } from 'app/entities/enumerations/tipo-identificacion-enum.model';
import { TipoProveedorEnum } from 'app/entities/enumerations/tipo-proveedor-enum.model';

export interface IProveedor {
  id?: number;
  nombre?: string | null;
  numeroContacto?: string | null;
  email?: string | null;
  tipoIdentificacion?: TipoIdentificacionEnum | null;
  numeroCC?: string | null;
  tipoProveedor?: TipoProveedorEnum | null;
}

export class Proveedor implements IProveedor {
  constructor(
    public id?: number,
    public nombre?: string | null,
    public numeroContacto?: string | null,
    public email?: string | null,
    public tipoIdentificacion?: TipoIdentificacionEnum | null,
    public numeroCC?: string | null,
    public tipoProveedor?: TipoProveedorEnum | null
  ) {}
}

export function getProveedorIdentifier(proveedor: IProveedor): number | undefined {
  return proveedor.id;
}
