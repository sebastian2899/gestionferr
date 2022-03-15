import dayjs from 'dayjs/esm';

export interface IAbono {
  id?: number;
  idFactura?: number | null;
  fechaCreacion?: dayjs.Dayjs | null;
  valorAbono?: number | null;
  tipoFactura?: string | null;
}

export class Abono implements IAbono {
  constructor(
    public id?: number,
    public idFactura?: number | null,
    public fechaCreacion?: dayjs.Dayjs | null,
    public valorAbono?: number | null,
    public tipoFactura?: string | null
  ) {}
}

export function getAbonoIdentifier(abono: IAbono): number | undefined {
  return abono.id;
}
