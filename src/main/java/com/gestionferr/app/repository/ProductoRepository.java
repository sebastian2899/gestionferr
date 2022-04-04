package com.gestionferr.app.repository;

import com.gestionferr.app.domain.Producto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Producto entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    @Query("SELECT c.categoriaProducto FROM Categoria c WHERE c.id=:id")
    String nombreCategoria(@Param("id") Long id);

    @Query("SELECT p FROM Producto p WHERE p.idCategoria =:idCategoria")
    List<Producto> productosPorCategoria(@Param("idCategoria") Long idCategoria);

    @Query("SELECT p FROM Producto p ORDER BY p.nombre")
    List<Producto> productosOrderNombre();

    @Query("SELECT p FROM Producto p ORDER BY p.precio ASC")
    List<Producto> productosOrderPrecio();

    @Query("SELECT p FROM Producto p WHERE p.cantidad = 0")
    List<Producto> productosAgotados();

    @Query("SELECT p FROM Producto p WHERE p.cantidad > 0 AND p.cantidad <= 10")
    List<Producto> productosCasiAgotados();

    @Query("SELECT p FROM Producto p WHERE UPPER(p.nombre) LIKE :nombre")
    List<Producto> productosPorNombre(@Param("nombre") String nombre);
}
