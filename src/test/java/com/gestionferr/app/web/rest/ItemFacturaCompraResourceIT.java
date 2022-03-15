package com.gestionferr.app.web.rest;

import static com.gestionferr.app.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gestionferr.app.IntegrationTest;
import com.gestionferr.app.domain.ItemFacturaCompra;
import com.gestionferr.app.repository.ItemFacturaCompraRepository;
import com.gestionferr.app.service.dto.ItemFacturaCompraDTO;
import com.gestionferr.app.service.mapper.ItemFacturaCompraMapper;
import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ItemFacturaCompraResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ItemFacturaCompraResourceIT {

    private static final Long DEFAULT_ID_FACTURA_COMPRA = 1L;
    private static final Long UPDATED_ID_FACTURA_COMPRA = 2L;

    private static final Long DEFAULT_ID_PRODUCTO = 1L;
    private static final Long UPDATED_ID_PRODUCTO = 2L;

    private static final Long DEFAULT_CANTIDAD = 1L;
    private static final Long UPDATED_CANTIDAD = 2L;

    private static final BigDecimal DEFAULT_PRECIO = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRECIO = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/item-factura-compras";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ItemFacturaCompraRepository itemFacturaCompraRepository;

    @Autowired
    private ItemFacturaCompraMapper itemFacturaCompraMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restItemFacturaCompraMockMvc;

    private ItemFacturaCompra itemFacturaCompra;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ItemFacturaCompra createEntity(EntityManager em) {
        ItemFacturaCompra itemFacturaCompra = new ItemFacturaCompra()
            .idFacturaCompra(DEFAULT_ID_FACTURA_COMPRA)
            .idProducto(DEFAULT_ID_PRODUCTO)
            .cantidad(DEFAULT_CANTIDAD)
            .precio(DEFAULT_PRECIO);
        return itemFacturaCompra;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ItemFacturaCompra createUpdatedEntity(EntityManager em) {
        ItemFacturaCompra itemFacturaCompra = new ItemFacturaCompra()
            .idFacturaCompra(UPDATED_ID_FACTURA_COMPRA)
            .idProducto(UPDATED_ID_PRODUCTO)
            .cantidad(UPDATED_CANTIDAD)
            .precio(UPDATED_PRECIO);
        return itemFacturaCompra;
    }

    @BeforeEach
    public void initTest() {
        itemFacturaCompra = createEntity(em);
    }

    @Test
    @Transactional
    void createItemFacturaCompra() throws Exception {
        int databaseSizeBeforeCreate = itemFacturaCompraRepository.findAll().size();
        // Create the ItemFacturaCompra
        ItemFacturaCompraDTO itemFacturaCompraDTO = itemFacturaCompraMapper.toDto(itemFacturaCompra);
        restItemFacturaCompraMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(itemFacturaCompraDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ItemFacturaCompra in the database
        List<ItemFacturaCompra> itemFacturaCompraList = itemFacturaCompraRepository.findAll();
        assertThat(itemFacturaCompraList).hasSize(databaseSizeBeforeCreate + 1);
        ItemFacturaCompra testItemFacturaCompra = itemFacturaCompraList.get(itemFacturaCompraList.size() - 1);
        assertThat(testItemFacturaCompra.getIdFacturaCompra()).isEqualTo(DEFAULT_ID_FACTURA_COMPRA);
        assertThat(testItemFacturaCompra.getIdProducto()).isEqualTo(DEFAULT_ID_PRODUCTO);
        assertThat(testItemFacturaCompra.getCantidad()).isEqualTo(DEFAULT_CANTIDAD);
        assertThat(testItemFacturaCompra.getPrecio()).isEqualByComparingTo(DEFAULT_PRECIO);
    }

    @Test
    @Transactional
    void createItemFacturaCompraWithExistingId() throws Exception {
        // Create the ItemFacturaCompra with an existing ID
        itemFacturaCompra.setId(1L);
        ItemFacturaCompraDTO itemFacturaCompraDTO = itemFacturaCompraMapper.toDto(itemFacturaCompra);

        int databaseSizeBeforeCreate = itemFacturaCompraRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restItemFacturaCompraMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(itemFacturaCompraDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemFacturaCompra in the database
        List<ItemFacturaCompra> itemFacturaCompraList = itemFacturaCompraRepository.findAll();
        assertThat(itemFacturaCompraList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllItemFacturaCompras() throws Exception {
        // Initialize the database
        itemFacturaCompraRepository.saveAndFlush(itemFacturaCompra);

        // Get all the itemFacturaCompraList
        restItemFacturaCompraMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(itemFacturaCompra.getId().intValue())))
            .andExpect(jsonPath("$.[*].idFacturaCompra").value(hasItem(DEFAULT_ID_FACTURA_COMPRA.intValue())))
            .andExpect(jsonPath("$.[*].idProducto").value(hasItem(DEFAULT_ID_PRODUCTO.intValue())))
            .andExpect(jsonPath("$.[*].cantidad").value(hasItem(DEFAULT_CANTIDAD.intValue())))
            .andExpect(jsonPath("$.[*].precio").value(hasItem(sameNumber(DEFAULT_PRECIO))));
    }

    @Test
    @Transactional
    void getItemFacturaCompra() throws Exception {
        // Initialize the database
        itemFacturaCompraRepository.saveAndFlush(itemFacturaCompra);

        // Get the itemFacturaCompra
        restItemFacturaCompraMockMvc
            .perform(get(ENTITY_API_URL_ID, itemFacturaCompra.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(itemFacturaCompra.getId().intValue()))
            .andExpect(jsonPath("$.idFacturaCompra").value(DEFAULT_ID_FACTURA_COMPRA.intValue()))
            .andExpect(jsonPath("$.idProducto").value(DEFAULT_ID_PRODUCTO.intValue()))
            .andExpect(jsonPath("$.cantidad").value(DEFAULT_CANTIDAD.intValue()))
            .andExpect(jsonPath("$.precio").value(sameNumber(DEFAULT_PRECIO)));
    }

    @Test
    @Transactional
    void getNonExistingItemFacturaCompra() throws Exception {
        // Get the itemFacturaCompra
        restItemFacturaCompraMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewItemFacturaCompra() throws Exception {
        // Initialize the database
        itemFacturaCompraRepository.saveAndFlush(itemFacturaCompra);

        int databaseSizeBeforeUpdate = itemFacturaCompraRepository.findAll().size();

        // Update the itemFacturaCompra
        ItemFacturaCompra updatedItemFacturaCompra = itemFacturaCompraRepository.findById(itemFacturaCompra.getId()).get();
        // Disconnect from session so that the updates on updatedItemFacturaCompra are not directly saved in db
        em.detach(updatedItemFacturaCompra);
        updatedItemFacturaCompra
            .idFacturaCompra(UPDATED_ID_FACTURA_COMPRA)
            .idProducto(UPDATED_ID_PRODUCTO)
            .cantidad(UPDATED_CANTIDAD)
            .precio(UPDATED_PRECIO);
        ItemFacturaCompraDTO itemFacturaCompraDTO = itemFacturaCompraMapper.toDto(updatedItemFacturaCompra);

        restItemFacturaCompraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, itemFacturaCompraDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(itemFacturaCompraDTO))
            )
            .andExpect(status().isOk());

        // Validate the ItemFacturaCompra in the database
        List<ItemFacturaCompra> itemFacturaCompraList = itemFacturaCompraRepository.findAll();
        assertThat(itemFacturaCompraList).hasSize(databaseSizeBeforeUpdate);
        ItemFacturaCompra testItemFacturaCompra = itemFacturaCompraList.get(itemFacturaCompraList.size() - 1);
        assertThat(testItemFacturaCompra.getIdFacturaCompra()).isEqualTo(UPDATED_ID_FACTURA_COMPRA);
        assertThat(testItemFacturaCompra.getIdProducto()).isEqualTo(UPDATED_ID_PRODUCTO);
        assertThat(testItemFacturaCompra.getCantidad()).isEqualTo(UPDATED_CANTIDAD);
        assertThat(testItemFacturaCompra.getPrecio()).isEqualByComparingTo(UPDATED_PRECIO);
    }

    @Test
    @Transactional
    void putNonExistingItemFacturaCompra() throws Exception {
        int databaseSizeBeforeUpdate = itemFacturaCompraRepository.findAll().size();
        itemFacturaCompra.setId(count.incrementAndGet());

        // Create the ItemFacturaCompra
        ItemFacturaCompraDTO itemFacturaCompraDTO = itemFacturaCompraMapper.toDto(itemFacturaCompra);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restItemFacturaCompraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, itemFacturaCompraDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(itemFacturaCompraDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemFacturaCompra in the database
        List<ItemFacturaCompra> itemFacturaCompraList = itemFacturaCompraRepository.findAll();
        assertThat(itemFacturaCompraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchItemFacturaCompra() throws Exception {
        int databaseSizeBeforeUpdate = itemFacturaCompraRepository.findAll().size();
        itemFacturaCompra.setId(count.incrementAndGet());

        // Create the ItemFacturaCompra
        ItemFacturaCompraDTO itemFacturaCompraDTO = itemFacturaCompraMapper.toDto(itemFacturaCompra);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemFacturaCompraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(itemFacturaCompraDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemFacturaCompra in the database
        List<ItemFacturaCompra> itemFacturaCompraList = itemFacturaCompraRepository.findAll();
        assertThat(itemFacturaCompraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamItemFacturaCompra() throws Exception {
        int databaseSizeBeforeUpdate = itemFacturaCompraRepository.findAll().size();
        itemFacturaCompra.setId(count.incrementAndGet());

        // Create the ItemFacturaCompra
        ItemFacturaCompraDTO itemFacturaCompraDTO = itemFacturaCompraMapper.toDto(itemFacturaCompra);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemFacturaCompraMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(itemFacturaCompraDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ItemFacturaCompra in the database
        List<ItemFacturaCompra> itemFacturaCompraList = itemFacturaCompraRepository.findAll();
        assertThat(itemFacturaCompraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateItemFacturaCompraWithPatch() throws Exception {
        // Initialize the database
        itemFacturaCompraRepository.saveAndFlush(itemFacturaCompra);

        int databaseSizeBeforeUpdate = itemFacturaCompraRepository.findAll().size();

        // Update the itemFacturaCompra using partial update
        ItemFacturaCompra partialUpdatedItemFacturaCompra = new ItemFacturaCompra();
        partialUpdatedItemFacturaCompra.setId(itemFacturaCompra.getId());

        partialUpdatedItemFacturaCompra.idFacturaCompra(UPDATED_ID_FACTURA_COMPRA).idProducto(UPDATED_ID_PRODUCTO).precio(UPDATED_PRECIO);

        restItemFacturaCompraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedItemFacturaCompra.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedItemFacturaCompra))
            )
            .andExpect(status().isOk());

        // Validate the ItemFacturaCompra in the database
        List<ItemFacturaCompra> itemFacturaCompraList = itemFacturaCompraRepository.findAll();
        assertThat(itemFacturaCompraList).hasSize(databaseSizeBeforeUpdate);
        ItemFacturaCompra testItemFacturaCompra = itemFacturaCompraList.get(itemFacturaCompraList.size() - 1);
        assertThat(testItemFacturaCompra.getIdFacturaCompra()).isEqualTo(UPDATED_ID_FACTURA_COMPRA);
        assertThat(testItemFacturaCompra.getIdProducto()).isEqualTo(UPDATED_ID_PRODUCTO);
        assertThat(testItemFacturaCompra.getCantidad()).isEqualTo(DEFAULT_CANTIDAD);
        assertThat(testItemFacturaCompra.getPrecio()).isEqualByComparingTo(UPDATED_PRECIO);
    }

    @Test
    @Transactional
    void fullUpdateItemFacturaCompraWithPatch() throws Exception {
        // Initialize the database
        itemFacturaCompraRepository.saveAndFlush(itemFacturaCompra);

        int databaseSizeBeforeUpdate = itemFacturaCompraRepository.findAll().size();

        // Update the itemFacturaCompra using partial update
        ItemFacturaCompra partialUpdatedItemFacturaCompra = new ItemFacturaCompra();
        partialUpdatedItemFacturaCompra.setId(itemFacturaCompra.getId());

        partialUpdatedItemFacturaCompra
            .idFacturaCompra(UPDATED_ID_FACTURA_COMPRA)
            .idProducto(UPDATED_ID_PRODUCTO)
            .cantidad(UPDATED_CANTIDAD)
            .precio(UPDATED_PRECIO);

        restItemFacturaCompraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedItemFacturaCompra.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedItemFacturaCompra))
            )
            .andExpect(status().isOk());

        // Validate the ItemFacturaCompra in the database
        List<ItemFacturaCompra> itemFacturaCompraList = itemFacturaCompraRepository.findAll();
        assertThat(itemFacturaCompraList).hasSize(databaseSizeBeforeUpdate);
        ItemFacturaCompra testItemFacturaCompra = itemFacturaCompraList.get(itemFacturaCompraList.size() - 1);
        assertThat(testItemFacturaCompra.getIdFacturaCompra()).isEqualTo(UPDATED_ID_FACTURA_COMPRA);
        assertThat(testItemFacturaCompra.getIdProducto()).isEqualTo(UPDATED_ID_PRODUCTO);
        assertThat(testItemFacturaCompra.getCantidad()).isEqualTo(UPDATED_CANTIDAD);
        assertThat(testItemFacturaCompra.getPrecio()).isEqualByComparingTo(UPDATED_PRECIO);
    }

    @Test
    @Transactional
    void patchNonExistingItemFacturaCompra() throws Exception {
        int databaseSizeBeforeUpdate = itemFacturaCompraRepository.findAll().size();
        itemFacturaCompra.setId(count.incrementAndGet());

        // Create the ItemFacturaCompra
        ItemFacturaCompraDTO itemFacturaCompraDTO = itemFacturaCompraMapper.toDto(itemFacturaCompra);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restItemFacturaCompraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, itemFacturaCompraDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(itemFacturaCompraDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemFacturaCompra in the database
        List<ItemFacturaCompra> itemFacturaCompraList = itemFacturaCompraRepository.findAll();
        assertThat(itemFacturaCompraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchItemFacturaCompra() throws Exception {
        int databaseSizeBeforeUpdate = itemFacturaCompraRepository.findAll().size();
        itemFacturaCompra.setId(count.incrementAndGet());

        // Create the ItemFacturaCompra
        ItemFacturaCompraDTO itemFacturaCompraDTO = itemFacturaCompraMapper.toDto(itemFacturaCompra);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemFacturaCompraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(itemFacturaCompraDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemFacturaCompra in the database
        List<ItemFacturaCompra> itemFacturaCompraList = itemFacturaCompraRepository.findAll();
        assertThat(itemFacturaCompraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamItemFacturaCompra() throws Exception {
        int databaseSizeBeforeUpdate = itemFacturaCompraRepository.findAll().size();
        itemFacturaCompra.setId(count.incrementAndGet());

        // Create the ItemFacturaCompra
        ItemFacturaCompraDTO itemFacturaCompraDTO = itemFacturaCompraMapper.toDto(itemFacturaCompra);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemFacturaCompraMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(itemFacturaCompraDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ItemFacturaCompra in the database
        List<ItemFacturaCompra> itemFacturaCompraList = itemFacturaCompraRepository.findAll();
        assertThat(itemFacturaCompraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteItemFacturaCompra() throws Exception {
        // Initialize the database
        itemFacturaCompraRepository.saveAndFlush(itemFacturaCompra);

        int databaseSizeBeforeDelete = itemFacturaCompraRepository.findAll().size();

        // Delete the itemFacturaCompra
        restItemFacturaCompraMockMvc
            .perform(delete(ENTITY_API_URL_ID, itemFacturaCompra.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ItemFacturaCompra> itemFacturaCompraList = itemFacturaCompraRepository.findAll();
        assertThat(itemFacturaCompraList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
