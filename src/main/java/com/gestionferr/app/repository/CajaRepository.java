package com.gestionferr.app.repository;

import com.gestionferr.app.domain.Caja;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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

    @Query("SELECT c FROM Caja c WHERE TO_CHAR(c.fechaCreacion, 'yyyy-MM-dd') =:fecha")
    List<Caja> cajasPorFecha(@Param("fecha") String fecha);

    @Query(
        "SELECT SUM(c.valorVentaDia),SUM(c.valorRegistradoDia),SUM(c.diferencia) FROM Caja c WHERE TO_CHAR(c.fechaCreacion, 'yyyy-MM-dd') BETWEEN " +
        ":fechaInicio AND :fechaFin"
    )
    List<Object[]> valoresCajaMeses(@Param("fechaInicio") String fechaInicio, @Param("fechaFin") String fechaFin);

    @Query("SELECT c FROM Caja c WHERE c.fechaCreacion BETWEEN :fechaInicio AND :fechaFin")
    List<Caja> cajaPorFechasReport(@Param("fechaInicio") Instant fechaInicio, @Param("fechaFin") Instant fechaFin);
}
