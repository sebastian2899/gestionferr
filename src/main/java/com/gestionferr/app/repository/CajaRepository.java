package com.gestionferr.app.repository;

import com.gestionferr.app.domain.Caja;
import java.math.BigDecimal;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Caja entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CajaRepository extends JpaRepository<Caja, Long> {
    @Query("SELECT SUM(f.valorPagado) FROM FacturaVenta f WHERE TO_CHAR(f.fechaCreacion, 'dd/MM/yyyy')=:fecha")
    BigDecimal valorPagadoDia(@Param("fecha") String fecha);

    @Query("SELECT SUM(a.valorAbono) FROM Abono a WHERE TO_CHAR(a.fechaCreacion, 'dd/MM/yyyy')=:fecha AND tipoFactura = 'Factura Venta'")
    BigDecimal valorABonoDia(@Param("fecha") String fecha);
}
