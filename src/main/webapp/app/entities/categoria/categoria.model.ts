export interface ICategoria {
  id?: number;
  categoriaProducto?: string | null;
  descripcion?: string | null;
}

export class Categoria implements ICategoria {
  constructor(public id?: number, public categoriaProducto?: string | null, public descripcion?: string | null) {}
}

export function getCategoriaIdentifier(categoria: ICategoria): number | undefined {
  return categoria.id;
}
