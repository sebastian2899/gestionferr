package com.gestionferr.app.service;

import com.gestionferr.app.service.dto.FacturaVentaDTO;
import com.gestionferr.app.service.dto.FacturaVentasFechaDTO;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.gestionferr.app.domain.FacturaVenta}.
 */
public interface FacturaVentaService {
    /**
     * Save a facturaVenta.
     *
     * @param facturaVentaDTO the entity to save.
     * @return the persisted entity.
     */
    FacturaVentaDTO save(FacturaVentaDTO facturaVentaDTO);

    /**
     * Partially updates a facturaVenta.
     *
     * @param facturaVentaDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FacturaVentaDTO> partialUpdate(FacturaVentaDTO facturaVentaDTO);

    /**
     * Get all the facturaVentas.
     *
     * @return the list of entities.
     */
    List<FacturaVentaDTO> findAll();

    /**
     * Get the "id" facturaVenta.
     *
     * @param id the id of the entity.
     * @return the entity.
     */

    FacturaVentaDTO facturaVentaPorId(Long id);

    /**
     * Delete the "id" facturaVenta.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    List<FacturaVentaDTO> facturasPorFiltro(FacturaVentaDTO facturaVentaDTO);

    List<FacturaVentaDTO> facturasPorFecha(String fecha);

    FacturaVentasFechaDTO valoresFacturaFecha(Instant fechaInicio, Instant fechaFin);

    byte[] generarReporteFacturaVentasMensual();
}
