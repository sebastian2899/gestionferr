package com.gestionferr.app.repository;

import com.gestionferr.app.domain.Proveedor;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Proveedor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {
    @Query(
        "SELECT f.numeroFactura,f.fechaCreacion,f.valorFactura,f.valorDeuda FROM FacturaCompra f WHERE f.idProovedor = :idProveedor" +
        " AND f.valorDeuda > 0"
    )
    List<Object[]> facturasProveedor(@Param("idProveedor") Long idProveedor);

    @Query("SELECT p FROM Proveedor p WHERE p.id = :id")
    Proveedor proveedorPorId(@Param("id") Long id);
}
