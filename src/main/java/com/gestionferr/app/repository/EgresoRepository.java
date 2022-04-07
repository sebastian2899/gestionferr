package com.gestionferr.app.repository;

import com.gestionferr.app.domain.Egreso;
import java.math.BigDecimal;
import java.util.List;
import java.util.List;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Egreso entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EgresoRepository extends JpaRepository<Egreso, Long> {
    @Query("SELECT SUM(e.valorEgreso) FROM Egreso e WHERE TO_CHAR(e.fechaCreacion, 'dd/MM/yyyy') = :fecha")
    BigDecimal valorEgresoDia(@Param("fecha") String fecha);

    @Query("SELECT e FROM Egreso e WHERE TO_CHAR(e.fechaCreacion, 'dd/MM/yyyy') =:fecha")
    List<Egreso> egresoDiario(@Param("fecha") String fecha);

    @Query("SELECT e FROM Egreso e WHERE TO_CHAR(e.fechaCreacion, 'yyyy-MM-dd') =:fecha")
    List<Egreso> egresoPorFecha(@Param("fecha") String fecha);
}
