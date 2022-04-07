package com.gestionferr.app.service;

import com.gestionferr.app.service.dto.FacturaCompraDTO;
import com.gestionferr.app.service.dto.RegistroFacturaCompraDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.gestionferr.app.domain.FacturaCompra}.
 */
public interface FacturaCompraService {
    /**
     * Save a facturaCompra.
     *
     * @param facturaCompraDTO the entity to save.
     * @return the persisted entity.
     */
    FacturaCompraDTO save(FacturaCompraDTO facturaCompraDTO);

    /**
     * Partially updates a facturaCompra.
     *
     * @param facturaCompraDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FacturaCompraDTO> partialUpdate(FacturaCompraDTO facturaCompraDTO);

    /**
     * Get all the facturaCompras.
     *
     * @return the list of entities.
     */
    List<FacturaCompraDTO> findAll();

    /**
     * Get the "id" facturaCompra.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    FacturaCompraDTO findOne(Long id);

    /**
     * Delete the "id" facturaCompra.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    List<FacturaCompraDTO> facturasCompraFiltro(FacturaCompraDTO facturaCompra);

    RegistroFacturaCompraDTO valoresFacturaCompraMes(String fechaInicio, String fechaFin);

    List<FacturaCompraDTO> facturaPorFecha(String fecha);

    Boolean validarNumeroFacturaSave(String numeroFactura);

    byte[] facturaCompraReport();
}
