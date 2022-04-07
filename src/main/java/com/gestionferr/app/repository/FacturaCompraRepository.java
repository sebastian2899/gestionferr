package com.gestionferr.app.repository;

import com.gestionferr.app.domain.Abono;
import com.gestionferr.app.domain.FacturaCompra;
import com.gestionferr.app.domain.ItemFacturaCompra;
import java.time.Instant;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the FacturaCompra entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FacturaCompraRepository extends JpaRepository<FacturaCompra, Long> {
    @Query("SELECT f FROM FacturaCompra f WHERE f.id = :id")
    FacturaCompra facuraPorId(@Param("id") Long id);

    @Query("SELECT i FROM ItemFacturaCompra i WHERE i.idFacturaCompra = :id ")
    List<ItemFacturaCompra> itemsPorFactura(@Param("id") Long id);

    @Query("SELECT p.nombre FROM Producto p WHERE p.id =:id")
    String nombreProductoPorId(@Param("id") Long id);

    @Query("SELECT p.nombre FROM Proveedor p WHERE p.id =:id")
    String nombreProovedroPorId(@Param("id") Long id);

    @Query(
        "SELECT SUM(f.valorFactura),SUM(f.valorPagado),SUM(f.valorDeuda) FROM FacturaCompra f WHERE TO_CHAR(f.fechaCreacion, 'yyyy-MM-dd') " +
        "BETWEEN :fechaInicio AND :fechaFin"
    )
    List<Object[]> valoresFacturaMes(@Param("fechaInicio") String fechaInicio, @Param("fechaFin") String fechaFin);

    @Query("SELECT COUNT(*) FROM FacturaCompra f WHERE f.numeroFactura = :numeroFactura")
    int validarNumeroFactura(@Param("numeroFactura") String numeroFactura);

    @Query("SELECT f FROM FacturaCompra f WHERE f.fechaCreacion BETWEEN :fechaInicio AND :fechaFin")
    List<FacturaCompra> facturaCompraReport(@Param("fechaInicio") Instant fechaInicio, @Param("fechaFin") Instant fechaFin);

    @Query("SELECT p.nombre,p.numeroContacto,p.numeroCC FROM Proveedor p WHERE p.id=:id")
    List<String[]> proveedorFactura(@Param("id") Long id);

    @Query("SELECT  a FROM Abono a WHERE a.idFactura =:idFactura AND a.tipoFactura = 'Factura Compra'")
    List<Abono> abonoFacturaCompra(@Param("idFactura") Long idFactura);
}
