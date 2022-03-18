package com.gestionferr.app.config;

/**
 * Application constants.
 */
public final class Constants {

    // Regex for acceptable logins
    public static final String LOGIN_REGEX = "^(?>[a-zA-Z0-9!$&*+=?^_`{|}~.-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*)|(?>[_.@A-Za-z0-9-]+)$";

    public static final String SYSTEM = "system";
    public static final String DEFAULT_LANGUAGE = "es";

    //VENTAS
    public static final String RESTAR_PRODUCTOS_SELECCIONADOS = "UPDATE FROM Producto SET cantidad=cantidad-:cantidad WHERE id=:id";

    public static final String SUMAR_PRODUCTOS_FACTURA_REVERTIDA = "UPDATE FROM Producto SET cantidad=cantidad+:cantidad" + " WHERE id=:id";

    //CATEGORIA
    public static final String CONSULTAR_NOMBRE_CATEGORIA = "SELECT c.categoriaProducto FROM Categoria c WHERE c.id = :id";

    //ABONO
    public static final String CONSULTAR_ABONOS_POR_FACTURAVENTA =
        "SELECT a FROM Abono a WHERE a.idFactura=:idFactura AND " + "a.tipoFactura = 'Factura Venta'";

    public static final String ELIMINAR_ABONOS_FACTURA = "DELETE FROM Abono WHERE idFactura=:id AND tipoFactura = 'Factura Venta'";

    public static final String ELIMINAR_ITEMS_POR_FACTURA = "DELETE FROM ItemFacturaVenta WHERE idFacturaVenta =:id";

    private Constants() {}
}
