import dayjs from 'dayjs/esm';
import { TipoEstadoEnum } from 'app/entities/enumerations/tipo-estado-enum.model';

export interface ICaja {
  id?: number;
  fechaCreacion?: dayjs.Dayjs | null;
  valorVentaDia?: number | null;
  valorRegistradoDia?: number | null;
  estado?: TipoEstadoEnum | null;
  diferencia?: number | null;
}

export class Caja implements ICaja {
  constructor(
    public id?: number,
    public fechaCreacion?: dayjs.Dayjs | null,
    public valorVentaDia?: number | null,
    public valorRegistradoDia?: number | null,
    public estado?: TipoEstadoEnum | null,
    public diferencia?: number | null
  ) {}
}

export function getCajaIdentifier(caja: ICaja): number | undefined {
  return caja.id;
}
