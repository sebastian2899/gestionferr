package com.gestionferr.app.repository;

import com.gestionferr.app.domain.Cliente;
import com.gestionferr.app.domain.FacturaVenta;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Cliente entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    @Query(
        "SELECT f.id,f.fechaCreacion,f.numeroFactura,f.valorFactura,f.valorPagado,f.valorDeuda FROM" +
        " FacturaVenta f WHERE f.idCliente=:id"
    )
    List<Object[]> datosFacturaVentaCliente(@Param("id") Long id);

    @Query("SELECT c FROM Cliente c WHERE c.id =:id")
    Cliente clientePorId(@Param("id") Long id);
}
