package com.gestionferr.app.web.rest;

import com.gestionferr.app.repository.FacturaCompraRepository;
import com.gestionferr.app.service.FacturaCompraService;
import com.gestionferr.app.service.dto.FacturaCompraDTO;
import com.gestionferr.app.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.gestionferr.app.domain.FacturaCompra}.
 */
@RestController
@RequestMapping("/api")
public class FacturaCompraResource {

    private final Logger log = LoggerFactory.getLogger(FacturaCompraResource.class);

    private static final String ENTITY_NAME = "facturaCompra";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FacturaCompraService facturaCompraService;

    private final FacturaCompraRepository facturaCompraRepository;

    public FacturaCompraResource(FacturaCompraService facturaCompraService, FacturaCompraRepository facturaCompraRepository) {
        this.facturaCompraService = facturaCompraService;
        this.facturaCompraRepository = facturaCompraRepository;
    }

    /**
     * {@code POST  /factura-compras} : Create a new facturaCompra.
     *
     * @param facturaCompraDTO the facturaCompraDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new facturaCompraDTO, or with status {@code 400 (Bad Request)} if the facturaCompra has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/factura-compras")
    public ResponseEntity<FacturaCompraDTO> createFacturaCompra(@RequestBody FacturaCompraDTO facturaCompraDTO) throws URISyntaxException {
        log.debug("REST request to save FacturaCompra : {}", facturaCompraDTO);
        if (facturaCompraDTO.getId() != null) {
            throw new BadRequestAlertException("A new facturaCompra cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FacturaCompraDTO result = facturaCompraService.save(facturaCompraDTO);
        return ResponseEntity
            .created(new URI("/api/factura-compras/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /factura-compras/:id} : Updates an existing facturaCompra.
     *
     * @param id the id of the facturaCompraDTO to save.
     * @param facturaCompraDTO the facturaCompraDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated facturaCompraDTO,
     * or with status {@code 400 (Bad Request)} if the facturaCompraDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the facturaCompraDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/factura-compras/{id}")
    public ResponseEntity<FacturaCompraDTO> updateFacturaCompra(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FacturaCompraDTO facturaCompraDTO
    ) throws URISyntaxException {
        log.debug("REST request to update FacturaCompra : {}, {}", id, facturaCompraDTO);
        if (facturaCompraDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, facturaCompraDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!facturaCompraRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FacturaCompraDTO result = facturaCompraService.save(facturaCompraDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, facturaCompraDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /factura-compras/:id} : Partial updates given fields of an existing facturaCompra, field will ignore if it is null
     *
     * @param id the id of the facturaCompraDTO to save.
     * @param facturaCompraDTO the facturaCompraDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated facturaCompraDTO,
     * or with status {@code 400 (Bad Request)} if the facturaCompraDTO is not valid,
     * or with status {@code 404 (Not Found)} if the facturaCompraDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the facturaCompraDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/factura-compras/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FacturaCompraDTO> partialUpdateFacturaCompra(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FacturaCompraDTO facturaCompraDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update FacturaCompra partially : {}, {}", id, facturaCompraDTO);
        if (facturaCompraDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, facturaCompraDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!facturaCompraRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FacturaCompraDTO> result = facturaCompraService.partialUpdate(facturaCompraDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, facturaCompraDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /factura-compras} : get all the facturaCompras.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of facturaCompras in body.
     */
    @GetMapping("/factura-compras")
    public List<FacturaCompraDTO> getAllFacturaCompras() {
        log.debug("REST request to get all FacturaCompras");
        return facturaCompraService.findAll();
    }

    /**
     * {@code GET  /factura-compras/:id} : get the "id" facturaCompra.
     *
     * @param id the id of the facturaCompraDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the facturaCompraDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/factura-compras/{id}")
    public ResponseEntity<FacturaCompraDTO> getFacturaCompra(@PathVariable Long id) {
        log.debug("REST request to get FacturaCompra : {}", id);
        Optional<FacturaCompraDTO> facturaCompraDTO = facturaCompraService.findOne(id);
        return ResponseUtil.wrapOrNotFound(facturaCompraDTO);
    }

    /**
     * {@code DELETE  /factura-compras/:id} : delete the "id" facturaCompra.
     *
     * @param id the id of the facturaCompraDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/factura-compras/{id}")
    public ResponseEntity<Void> deleteFacturaCompra(@PathVariable Long id) {
        log.debug("REST request to delete FacturaCompra : {}", id);
        facturaCompraService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
