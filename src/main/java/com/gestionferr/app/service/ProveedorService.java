package com.gestionferr.app.service;

import com.gestionferr.app.service.dto.ProveedorDTO;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.gestionferr.app.domain.Proveedor}.
 */
public interface ProveedorService {
    /**
     * Save a proveedor.
     *
     * @param proveedorDTO the entity to save.
     * @return the persisted entity.
     */
    ProveedorDTO save(ProveedorDTO proveedorDTO);

    /**
     * Partially updates a proveedor.
     *
     * @param proveedorDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ProveedorDTO> partialUpdate(ProveedorDTO proveedorDTO);

    /**
     * Get all the proveedors.
     *
     * @return the list of entities.
     */
    List<ProveedorDTO> findAll();

    /**
     * Get the "id" proveedor.
     *
     * @param id the id of the entity.
     * @return the entity.
     * @throws ParseException
     */

    ProveedorDTO findOne(Long id) throws ParseException;

    /**
     * Delete the "id" proveedor.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    List<ProveedorDTO> proveedorFiltros(ProveedorDTO proveedor);
}
