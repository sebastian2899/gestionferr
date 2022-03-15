package com.gestionferr.app.web.rest;

import com.gestionferr.app.repository.ItemFacturaCompraRepository;
import com.gestionferr.app.service.ItemFacturaCompraService;
import com.gestionferr.app.service.dto.ItemFacturaCompraDTO;
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
 * REST controller for managing {@link com.gestionferr.app.domain.ItemFacturaCompra}.
 */
@RestController
@RequestMapping("/api")
public class ItemFacturaCompraResource {

    private final Logger log = LoggerFactory.getLogger(ItemFacturaCompraResource.class);

    private static final String ENTITY_NAME = "itemFacturaCompra";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ItemFacturaCompraService itemFacturaCompraService;

    private final ItemFacturaCompraRepository itemFacturaCompraRepository;

    public ItemFacturaCompraResource(
        ItemFacturaCompraService itemFacturaCompraService,
        ItemFacturaCompraRepository itemFacturaCompraRepository
    ) {
        this.itemFacturaCompraService = itemFacturaCompraService;
        this.itemFacturaCompraRepository = itemFacturaCompraRepository;
    }

    /**
     * {@code POST  /item-factura-compras} : Create a new itemFacturaCompra.
     *
     * @param itemFacturaCompraDTO the itemFacturaCompraDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new itemFacturaCompraDTO, or with status {@code 400 (Bad Request)} if the itemFacturaCompra has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/item-factura-compras")
    public ResponseEntity<ItemFacturaCompraDTO> createItemFacturaCompra(@RequestBody ItemFacturaCompraDTO itemFacturaCompraDTO)
        throws URISyntaxException {
        log.debug("REST request to save ItemFacturaCompra : {}", itemFacturaCompraDTO);
        if (itemFacturaCompraDTO.getId() != null) {
            throw new BadRequestAlertException("A new itemFacturaCompra cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ItemFacturaCompraDTO result = itemFacturaCompraService.save(itemFacturaCompraDTO);
        return ResponseEntity
            .created(new URI("/api/item-factura-compras/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /item-factura-compras/:id} : Updates an existing itemFacturaCompra.
     *
     * @param id the id of the itemFacturaCompraDTO to save.
     * @param itemFacturaCompraDTO the itemFacturaCompraDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated itemFacturaCompraDTO,
     * or with status {@code 400 (Bad Request)} if the itemFacturaCompraDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the itemFacturaCompraDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/item-factura-compras/{id}")
    public ResponseEntity<ItemFacturaCompraDTO> updateItemFacturaCompra(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ItemFacturaCompraDTO itemFacturaCompraDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ItemFacturaCompra : {}, {}", id, itemFacturaCompraDTO);
        if (itemFacturaCompraDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, itemFacturaCompraDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!itemFacturaCompraRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ItemFacturaCompraDTO result = itemFacturaCompraService.save(itemFacturaCompraDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, itemFacturaCompraDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /item-factura-compras/:id} : Partial updates given fields of an existing itemFacturaCompra, field will ignore if it is null
     *
     * @param id the id of the itemFacturaCompraDTO to save.
     * @param itemFacturaCompraDTO the itemFacturaCompraDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated itemFacturaCompraDTO,
     * or with status {@code 400 (Bad Request)} if the itemFacturaCompraDTO is not valid,
     * or with status {@code 404 (Not Found)} if the itemFacturaCompraDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the itemFacturaCompraDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/item-factura-compras/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ItemFacturaCompraDTO> partialUpdateItemFacturaCompra(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ItemFacturaCompraDTO itemFacturaCompraDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ItemFacturaCompra partially : {}, {}", id, itemFacturaCompraDTO);
        if (itemFacturaCompraDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, itemFacturaCompraDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!itemFacturaCompraRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ItemFacturaCompraDTO> result = itemFacturaCompraService.partialUpdate(itemFacturaCompraDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, itemFacturaCompraDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /item-factura-compras} : get all the itemFacturaCompras.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of itemFacturaCompras in body.
     */
    @GetMapping("/item-factura-compras")
    public List<ItemFacturaCompraDTO> getAllItemFacturaCompras() {
        log.debug("REST request to get all ItemFacturaCompras");
        return itemFacturaCompraService.findAll();
    }

    /**
     * {@code GET  /item-factura-compras/:id} : get the "id" itemFacturaCompra.
     *
     * @param id the id of the itemFacturaCompraDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the itemFacturaCompraDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/item-factura-compras/{id}")
    public ResponseEntity<ItemFacturaCompraDTO> getItemFacturaCompra(@PathVariable Long id) {
        log.debug("REST request to get ItemFacturaCompra : {}", id);
        Optional<ItemFacturaCompraDTO> itemFacturaCompraDTO = itemFacturaCompraService.findOne(id);
        return ResponseUtil.wrapOrNotFound(itemFacturaCompraDTO);
    }

    /**
     * {@code DELETE  /item-factura-compras/:id} : delete the "id" itemFacturaCompra.
     *
     * @param id the id of the itemFacturaCompraDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/item-factura-compras/{id}")
    public ResponseEntity<Void> deleteItemFacturaCompra(@PathVariable Long id) {
        log.debug("REST request to delete ItemFacturaCompra : {}", id);
        itemFacturaCompraService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
