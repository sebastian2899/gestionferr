package com.gestionferr.app.repository;

import com.gestionferr.app.domain.FacturaVenta;
import com.gestionferr.app.domain.ItemFacturaVenta;
import com.gestionferr.app.domain.Producto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the FacturaVenta entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FacturaVentaRepository extends JpaRepository<FacturaVenta, Long> {
    @Query("SELECT f FROM FacturaVenta f WHERE f.id = :id")
    FacturaVenta facturaVentaPorId(@Param("id") Long id);

    @Query("SELECT i.idProducto,i.cantidad,i.precio FROM ItemFacturaVenta i WHERE i.idFacturaVenta =:id")
    List<Object[]> itemsFacturaVenta(@Param("id") Long id);

    @Query("SELECT i FROM ItemFacturaVenta i WHERE i.idFacturaVenta =:id")
    List<ItemFacturaVenta> allItemsFacturaVenta(@Param("id") Long id);

    @Query("SELECT p FROM Producto p WHERE p.id =:id")
    Producto productoPorId(@Param("id") Long id);

    @Query("SELECT c.nombre FROM Cliente c WHERE c.id=:id")
    String nombreCliente(@Param("id") Long id);

    @Query("SELECT p.nombre FROM Producto p WHERE p.id=:id")
    String nombreProducto(@Param("id") Long id);
}
