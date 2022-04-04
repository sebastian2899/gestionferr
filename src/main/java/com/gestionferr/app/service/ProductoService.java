package com.gestionferr.app.service;

import com.gestionferr.app.service.dto.ProductoDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.gestionferr.app.domain.Producto}.
 */
public interface ProductoService {
    /**
     * Save a producto.
     *
     * @param productoDTO the entity to save.
     * @return the persisted entity.
     */
    ProductoDTO save(ProductoDTO productoDTO);

    /**
     * Partially updates a producto.
     *
     * @param productoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ProductoDTO> partialUpdate(ProductoDTO productoDTO);

    /**
     * Get all the productos.
     *
     * @return the list of entities.
     */
    List<ProductoDTO> findAll();

    /**
     * Get the "id" producto.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ProductoDTO> findOne(Long id);

    /**
     * Delete the "id" producto.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    List<ProductoDTO> productosPorCategoria(Long idCategoria);

    List<ProductoDTO> productosFiltroAutomatico(int codigo);

    List<ProductoDTO> productosAgotados();

    List<ProductoDTO> productosCasiAgotados();

    void actualizarPrecioProductos(String opcion, Long porcentaje);

    List<ProductoDTO> productosPorNombre(String nombre);
}
