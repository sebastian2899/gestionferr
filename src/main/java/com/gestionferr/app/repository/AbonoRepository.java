package com.gestionferr.app.repository;

import com.gestionferr.app.domain.Abono;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Abono entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AbonoRepository extends JpaRepository<Abono, Long> {
    @Query("SELECT a FROM Abono a WHERE a.idFactura = :id AND a.tipoFactura = 'Factura Venta'")
    List<Abono> abonosPorFactura(@Param("id") Long id);
}
