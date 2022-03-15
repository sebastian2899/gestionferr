export interface IProducto {
  id?: number;
  nombre?: string | null;
  precio?: number | null;
  cantidad?: number | null;
  fotoContentType?: string | null;
  foto?: string | null;
  descripcion?: string | null;
  idCategoria?: number | null;
  nombreCategoria?: string | null;
}

export class Producto implements IProducto {
  constructor(
    public id?: number,
    public nombre?: string | null,
    public precio?: number | null,
    public cantidad?: number | null,
    public fotoContentType?: string | null,
    public foto?: string | null,
    public descripcion?: string | null,
    public idCategoria?: number | null,
    public nombreCategoria?: string | null
  ) {}
}

export function getProductoIdentifier(producto: IProducto): number | undefined {
  return producto.id;
}
