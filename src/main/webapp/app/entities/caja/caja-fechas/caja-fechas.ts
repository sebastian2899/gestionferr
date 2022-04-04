export interface ICajaFechas {
  valorTotalDia?: number | null;
  valorRegistradoDia?: number | null;
  diferencia?: number | null;
}

export class CajasFechas implements ICajaFechas {
  constructor(public valorTotalDia?: number | null, public valorRegistradoDia?: number | null, public diferencia?: number | null) {}
}
