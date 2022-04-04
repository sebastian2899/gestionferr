package com.gestionferr.app.config;

/**
 * Application constants.
 */
public final class Constants {

    // Regex for acceptable logins
    public static final String LOGIN_REGEX = "^(?>[a-zA-Z0-9!$&*+=?^_`{|}~.-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*)|(?>[_.@A-Za-z0-9-]+)$";

    public static final String SYSTEM = "system";
    public static final String DEFAULT_LANGUAGE = "es";

    //VENTAS////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static final String RESTAR_PRODUCTOS_SELECCIONADOS = "UPDATE FROM Producto SET cantidad=cantidad-:cantidad WHERE id=:id";

    public static final String SUMAR_PRODUCTOS_FACTURA_REVERTIDA = "UPDATE FROM Producto SET cantidad=cantidad+:cantidad" + " WHERE id=:id";

    public static final String FACTURA_FILTRO_GENERAL = "SELECT f FROM FacturaVenta f WHERE f.id IS NOT NULL";

    public static final String FACTURA_FILTRO_NUMERO_FACTURA = " AND f.numeroFactura LIKE :numeroFactura";

    public static final String FACTURA_FILTRO_NOMBRE_CLIENTE = " AND UPPER(f.infoCliente) LIKE :nombreCliente";

    public static final String FACTURA_FILTRO_ESTADO_FACTURA = " AND UPPER(f.estado) = :estado";

    public static final String FACTURA_FILTRO_FECHA_FACTURA =
        "SELECT f FROM FacturaVenta f WHERE TO_CHAR(f.fechaCreacion, 'yyyy-MM-dd')=:fecha";

    public static final String CONSULTAR_VALORES_FACTURA_MENSUAL =
        "SELECT SUM(f.valorFactura),SUM(f.valorPagado),SUM(f.valorDeuda) " +
        "FROM FacturaVenta f WHERE f.fechaCreacion BETWEEN :fechaInicio AND :fechaFin";

    public static final String CONSULTAR_FACUTRAS_POR_FECHAS =
        "SELECT f FROM FacturaVenta f WHERE f.fechaCreacion BETWEEN :fechaInicio and :fechaFin";

    //////COMPRAS///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static final String FACTURA_COMPRA_BASE = "SELECT f FROM FacturaCompra f WHERE f.id IS NOT NULL";

    public static final String FACTURA_COMPRA_NUMERO_FACTURA = " AND f.numeroFactura LIKE :numeroFactura";

    public static final String FACTURA_COMPRA_INFO_PROVEEDOR = " AND UPPER(f.infoCliente) LIKE :infoProveedor";

    public static final String FACTURA_COMPRA__ESTADO = " AND UPPER(f.estado) LIKE :estado";

    public static final String FACTURA_COMPRA_FECHA =
        "SELECT f FROM FacturaCompra f WHERE TO_CHAR(f.fechaCreacion,'yyyy-MM-dd')" + " = :fecha";

    public static final String FACTURA_COMPRA_ORDER_BY = "SELECT f FROM FacturaCompra f ORDER BY f.fechaCreacion DESC";

    public static final String ELIMINAR_ITEMS_FACTURA_COMPRA = "DELETE FROM ItemFacturaCompra i WHERE i.idFacturaCompra =:idFactura";

    //CATEGORIA/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static final String CONSULTAR_NOMBRE_CATEGORIA = "SELECT c.categoriaProducto FROM Categoria c WHERE c.id =:id";

    //ABONO
    public static final String CONSULTAR_ABONOS_POR_FACTURAVENTA =
        "SELECT a FROM Abono a WHERE a.idFactura=:idFactura AND " + "a.tipoFactura = 'Factura Venta'";

    public static final String CONSULTAR_ABONO_POR_FACTURA_COMPRA =
        "SELECT a FROM Abono a WHERE a.idFactura=:idFactura AND a.tipoFactura" + " = 'Factura Compra'";

    public static final String ELIMINAR_ABONOS_FACTURA = "DELETE FROM Abono WHERE idFactura=:id AND tipoFactura = 'Factura Venta'";

    public static final String ELIMINAR_ITEMS_POR_FACTURA = "DELETE FROM ItemFacturaVenta WHERE idFacturaVenta =:id";

    //CAJA////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static final String ELIMINAR_VALOR_CAJA_ABONO_FECHA =
        "UPDATE FROM Caja SET valorVentaDia=valorVentaDia-:valorAbono WHERE" + " TO_CHAR(fechaCreacion, 'dd/MM/yyyy') =:fechaCreacion";

    public static final String ELIMINAR_VALOR_CAJA_FACTURA_FECHA =
        "UPDATE FROM Caja SET valorVentaDia=valorVentaDia-:valorFactura WHERE" + " TO_CHAR(fechaCreacion, 'dd/MM/yyyy') = :fechaCreacion";

    public static final String ELIMINAR_CAJA_VALOR_CERO =
        "DELETE FROM Caja WHERE TO_CHAR(fechaCreacion, 'dd/MM/yyyy') =:fecha AND valorVentaDia = 0";

    public static final String CAJAS_BASE = "SELECT * FROM caja WHERE id IS NOT NULL";

    public static final String CAJA_POR_FECHA = " AND TO_CHAR(c.fechaCreacion, 'yyyy-MM-dd') = :fecha";

    public static final String CAJA_POR_ESTADO = " AND estado =:estado";

    //CLIENTES
    public static final String CLIENTE_BASE = "SELECT c FROM Cliente c WHERE c.id IS NOT NULL";
    public static final String CLIENTE_FILTRO_NOMBRE = " AND UPPER(c.nombre) LIKE :nombre";
    public static final String CLIENTE_FILTRO_NUMCC = " AND UPPER(c.numeroCC) LIKE :numeroCC";

    private Constants() {}
}
