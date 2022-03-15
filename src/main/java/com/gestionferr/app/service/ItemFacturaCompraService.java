package com.gestionferr.app.service;

import com.gestionferr.app.service.dto.ItemFacturaCompraDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.gestionferr.app.domain.ItemFacturaCompra}.
 */
public interface ItemFacturaCompraService {
    /**
     * Save a itemFacturaCompra.
     *
     * @param itemFacturaCompraDTO the entity to save.
     * @return the persisted entity.
     */
    ItemFacturaCompraDTO save(ItemFacturaCompraDTO itemFacturaCompraDTO);

    /**
     * Partially updates a itemFacturaCompra.
     *
     * @param itemFacturaCompraDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ItemFacturaCompraDTO> partialUpdate(ItemFacturaCompraDTO itemFacturaCompraDTO);

    /**
     * Get all the itemFacturaCompras.
     *
     * @return the list of entities.
     */
    List<ItemFacturaCompraDTO> findAll();

    /**
     * Get the "id" itemFacturaCompra.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ItemFacturaCompraDTO> findOne(Long id);

    /**
     * Delete the "id" itemFacturaCompra.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
