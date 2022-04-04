package com.gestionferr.app.web.rest;

import com.gestionferr.app.repository.FacturaVentaRepository;
import com.gestionferr.app.service.FacturaVentaService;
import com.gestionferr.app.service.dto.FacturaVentaDTO;
import com.gestionferr.app.service.dto.FacturaVentasFechaDTO;
import com.gestionferr.app.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.gestionferr.app.domain.FacturaVenta}.
 */
@RestController
@RequestMapping("/api")
public class FacturaVentaResource {

    private final Logger log = LoggerFactory.getLogger(FacturaVentaResource.class);

    private static final String ENTITY_NAME = "facturaVenta";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FacturaVentaService facturaVentaService;

    private final FacturaVentaRepository facturaVentaRepository;

    public FacturaVentaResource(FacturaVentaService facturaVentaService, FacturaVentaRepository facturaVentaRepository) {
        this.facturaVentaService = facturaVentaService;
        this.facturaVentaRepository = facturaVentaRepository;
    }

    /**
     * {@code POST  /factura-ventas} : Create a new facturaVenta.
     *
     * @param facturaVentaDTO the facturaVentaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new facturaVentaDTO, or with status {@code 400 (Bad Request)} if the facturaVenta has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/factura-ventas")
    public ResponseEntity<FacturaVentaDTO> createFacturaVenta(@RequestBody FacturaVentaDTO facturaVentaDTO) throws URISyntaxException {
        log.debug("REST request to save FacturaVenta : {}", facturaVentaDTO);
        if (facturaVentaDTO.getId() != null) {
            throw new BadRequestAlertException("A new facturaVenta cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FacturaVentaDTO result = facturaVentaService.save(facturaVentaDTO);
        return ResponseEntity
            .created(new URI("/api/factura-ventas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PostMapping("/facturaFiltros")
    public ResponseEntity<List<FacturaVentaDTO>> facturasFiltro(@RequestBody FacturaVentaDTO facturaVentaDTO) throws URISyntaxException {
        log.debug("REST request to get all factures per filters");

        /*
         * if(facturaVentaDTO == null) { throw new
         * BadRequestAlertException("factura Venta cannot already have an boddy",
         * ENTITY_NAME, "boddy not exist"); }
         */

        List<FacturaVentaDTO> facturasFiltro = facturaVentaService.facturasPorFiltro(facturaVentaDTO);

        return ResponseEntity.ok().body(facturasFiltro);
    }

    @GetMapping("/facturaFechas/{fecha}")
    public ResponseEntity<List<FacturaVentaDTO>> facturaPorFecha(@PathVariable String fecha) {
        log.debug("REST Request to get all factura ventas per date filters");

        List<FacturaVentaDTO> facturaPorFecha = facturaVentaService.facturasPorFecha(fecha);

        return ResponseEntity.ok().body(facturaPorFecha);
    }

    /**
     * {@code PUT  /factura-ventas/:id} : Updates an existing facturaVenta.
     *
     * @param id the id of the facturaVentaDTO to save.
     * @param facturaVentaDTO the facturaVentaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated facturaVentaDTO,
     * or with status {@code 400 (Bad Request)} if the facturaVentaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the facturaVentaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/factura-ventas/{id}")
    public ResponseEntity<FacturaVentaDTO> updateFacturaVenta(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FacturaVentaDTO facturaVentaDTO
    ) throws URISyntaxException {
        log.debug("REST request to update FacturaVenta : {}, {}", id, facturaVentaDTO);
        if (facturaVentaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, facturaVentaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!facturaVentaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FacturaVentaDTO result = facturaVentaService.save(facturaVentaDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, facturaVentaDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /factura-ventas/:id} : Partial updates given fields of an existing facturaVenta, field will ignore if it is null
     *
     * @param id the id of the facturaVentaDTO to save.
     * @param facturaVentaDTO the facturaVentaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated facturaVentaDTO,
     * or with status {@code 400 (Bad Request)} if the facturaVentaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the facturaVentaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the facturaVentaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/factura-ventas/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FacturaVentaDTO> partialUpdateFacturaVenta(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FacturaVentaDTO facturaVentaDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update FacturaVenta partially : {}, {}", id, facturaVentaDTO);
        if (facturaVentaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, facturaVentaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!facturaVentaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FacturaVentaDTO> result = facturaVentaService.partialUpdate(facturaVentaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, facturaVentaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /factura-ventas} : get all the facturaVentas.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of facturaVentas in body.
     */
    @GetMapping("/factura-ventas")
    public List<FacturaVentaDTO> getAllFacturaVentas() {
        log.debug("REST request to get all FacturaVentas");
        return facturaVentaService.findAll();
    }

    @GetMapping("/valor-factura-mes/{fechaInicio}/{fechaFin}")
    public ResponseEntity<FacturaVentasFechaDTO> valoresFacturaFecha(@PathVariable String fechaInicio, @PathVariable String fechaFin) {
        log.debug("REST request to get values of facuta venta per dates");

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        FacturaVentasFechaDTO factura = null;

        try {
            Instant fechaIni = format.parse(fechaInicio.substring(0, fechaInicio.indexOf("T"))).toInstant();
            Instant fechaFn = format.parse(fechaFin.substring(0, fechaFin.indexOf("T"))).toInstant();
            factura = facturaVentaService.valoresFacturaFecha(fechaIni, fechaFn);
        } catch (ParseException e) {
            e.printStackTrace();
            e.getMessage();
        }

        return ResponseEntity.ok().body(factura);
    }

    @GetMapping("/factura-venta-validar-factura-numero/{numeroFactura}")
    public ResponseEntity<Boolean> validarFacturaVenta(@PathVariable String numeroFactura) {
        log.debug("REST request to validation save of facturaCompra");

        Boolean resp = facturaVentaService.ValidarFacturaVentanumeroFactura(numeroFactura);

        return new ResponseEntity<Boolean>(resp, null, HttpStatus.OK);
    }

    @GetMapping("/reporte-factura-venta")
    public ResponseEntity<byte[]> reporteMensualFacturaVenta() {
        log.debug("REST Requesto to generate monthly report");

        byte[] reporte = facturaVentaService.generarReporteFacturaVentasMensual();

        return ResponseEntity.ok().body(reporte);
    }

    /**
     * {@code GET  /factura-ventas/:id} : get the "id" facturaVenta.
     *
     * @param id the id of the facturaVentaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the facturaVentaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/factura-ventas/{id}")
    public ResponseEntity<FacturaVentaDTO> getFacturaVenta(@PathVariable Long id) {
        log.debug("REST request to get FacturaVenta : {}", id);
        FacturaVentaDTO facturaVentaDTO = facturaVentaService.facturaVentaPorId(id);
        return ResponseEntity.ok().body(facturaVentaDTO);
    }

    /**
     * {@code DELETE  /factura-ventas/:id} : delete the "id" facturaVenta.
     *
     * @param id the id of the facturaVentaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/factura-ventas/{id}")
    public ResponseEntity<Void> deleteFacturaVenta(@PathVariable Long id) {
        log.debug("REST request to delete FacturaVenta : {}", id);
        facturaVentaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
