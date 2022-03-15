package com.gestionferr.app.web.rest;

import static com.gestionferr.app.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gestionferr.app.IntegrationTest;
import com.gestionferr.app.domain.FacturaCompra;
import com.gestionferr.app.domain.enumeration.TipoFacturaEnum;
import com.gestionferr.app.repository.FacturaCompraRepository;
import com.gestionferr.app.service.dto.FacturaCompraDTO;
import com.gestionferr.app.service.mapper.FacturaCompraMapper;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link FacturaCompraResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FacturaCompraResourceIT {

    private static final String DEFAULT_NUMERO_FACTURA = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO_FACTURA = "BBBBBBBBBB";

    private static final Instant DEFAULT_FECHA_CREACION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_CREACION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_INFO_CLIENTE = "AAAAAAAAAA";
    private static final String UPDATED_INFO_CLIENTE = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_VALOR_FACTURA = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALOR_FACTURA = new BigDecimal(2);

    private static final BigDecimal DEFAULT_VALOR_PAGADO = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALOR_PAGADO = new BigDecimal(2);

    private static final BigDecimal DEFAULT_VALOR_DEUDA = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALOR_DEUDA = new BigDecimal(2);

    private static final TipoFacturaEnum DEFAULT_TIPO_FACTURA = TipoFacturaEnum.CREDITO;
    private static final TipoFacturaEnum UPDATED_TIPO_FACTURA = TipoFacturaEnum.DEBITO;

    private static final Long DEFAULT_ID_PROOVEDOR = 1L;
    private static final Long UPDATED_ID_PROOVEDOR = 2L;

    private static final String ENTITY_API_URL = "/api/factura-compras";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FacturaCompraRepository facturaCompraRepository;

    @Autowired
    private FacturaCompraMapper facturaCompraMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFacturaCompraMockMvc;

    private FacturaCompra facturaCompra;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FacturaCompra createEntity(EntityManager em) {
        FacturaCompra facturaCompra = new FacturaCompra()
            .numeroFactura(DEFAULT_NUMERO_FACTURA)
            .fechaCreacion(DEFAULT_FECHA_CREACION)
            .infoCliente(DEFAULT_INFO_CLIENTE)
            .valorFactura(DEFAULT_VALOR_FACTURA)
            .valorPagado(DEFAULT_VALOR_PAGADO)
            .valorDeuda(DEFAULT_VALOR_DEUDA)
            .tipoFactura(DEFAULT_TIPO_FACTURA)
            .idProovedor(DEFAULT_ID_PROOVEDOR);
        return facturaCompra;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FacturaCompra createUpdatedEntity(EntityManager em) {
        FacturaCompra facturaCompra = new FacturaCompra()
            .numeroFactura(UPDATED_NUMERO_FACTURA)
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .infoCliente(UPDATED_INFO_CLIENTE)
            .valorFactura(UPDATED_VALOR_FACTURA)
            .valorPagado(UPDATED_VALOR_PAGADO)
            .valorDeuda(UPDATED_VALOR_DEUDA)
            .tipoFactura(UPDATED_TIPO_FACTURA)
            .idProovedor(UPDATED_ID_PROOVEDOR);
        return facturaCompra;
    }

    @BeforeEach
    public void initTest() {
        facturaCompra = createEntity(em);
    }

    @Test
    @Transactional
    void createFacturaCompra() throws Exception {
        int databaseSizeBeforeCreate = facturaCompraRepository.findAll().size();
        // Create the FacturaCompra
        FacturaCompraDTO facturaCompraDTO = facturaCompraMapper.toDto(facturaCompra);
        restFacturaCompraMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(facturaCompraDTO))
            )
            .andExpect(status().isCreated());

        // Validate the FacturaCompra in the database
        List<FacturaCompra> facturaCompraList = facturaCompraRepository.findAll();
        assertThat(facturaCompraList).hasSize(databaseSizeBeforeCreate + 1);
        FacturaCompra testFacturaCompra = facturaCompraList.get(facturaCompraList.size() - 1);
        assertThat(testFacturaCompra.getNumeroFactura()).isEqualTo(DEFAULT_NUMERO_FACTURA);
        assertThat(testFacturaCompra.getFechaCreacion()).isEqualTo(DEFAULT_FECHA_CREACION);
        assertThat(testFacturaCompra.getInfoCliente()).isEqualTo(DEFAULT_INFO_CLIENTE);
        assertThat(testFacturaCompra.getValorFactura()).isEqualByComparingTo(DEFAULT_VALOR_FACTURA);
        assertThat(testFacturaCompra.getValorPagado()).isEqualByComparingTo(DEFAULT_VALOR_PAGADO);
        assertThat(testFacturaCompra.getValorDeuda()).isEqualByComparingTo(DEFAULT_VALOR_DEUDA);
        assertThat(testFacturaCompra.getTipoFactura()).isEqualTo(DEFAULT_TIPO_FACTURA);
        assertThat(testFacturaCompra.getIdProovedor()).isEqualTo(DEFAULT_ID_PROOVEDOR);
    }

    @Test
    @Transactional
    void createFacturaCompraWithExistingId() throws Exception {
        // Create the FacturaCompra with an existing ID
        facturaCompra.setId(1L);
        FacturaCompraDTO facturaCompraDTO = facturaCompraMapper.toDto(facturaCompra);

        int databaseSizeBeforeCreate = facturaCompraRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFacturaCompraMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(facturaCompraDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FacturaCompra in the database
        List<FacturaCompra> facturaCompraList = facturaCompraRepository.findAll();
        assertThat(facturaCompraList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFacturaCompras() throws Exception {
        // Initialize the database
        facturaCompraRepository.saveAndFlush(facturaCompra);

        // Get all the facturaCompraList
        restFacturaCompraMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(facturaCompra.getId().intValue())))
            .andExpect(jsonPath("$.[*].numeroFactura").value(hasItem(DEFAULT_NUMERO_FACTURA)))
            .andExpect(jsonPath("$.[*].fechaCreacion").value(hasItem(DEFAULT_FECHA_CREACION.toString())))
            .andExpect(jsonPath("$.[*].infoCliente").value(hasItem(DEFAULT_INFO_CLIENTE)))
            .andExpect(jsonPath("$.[*].valorFactura").value(hasItem(sameNumber(DEFAULT_VALOR_FACTURA))))
            .andExpect(jsonPath("$.[*].valorPagado").value(hasItem(sameNumber(DEFAULT_VALOR_PAGADO))))
            .andExpect(jsonPath("$.[*].valorDeuda").value(hasItem(sameNumber(DEFAULT_VALOR_DEUDA))))
            .andExpect(jsonPath("$.[*].tipoFactura").value(hasItem(DEFAULT_TIPO_FACTURA.toString())))
            .andExpect(jsonPath("$.[*].idProovedor").value(hasItem(DEFAULT_ID_PROOVEDOR.intValue())));
    }

    @Test
    @Transactional
    void getFacturaCompra() throws Exception {
        // Initialize the database
        facturaCompraRepository.saveAndFlush(facturaCompra);

        // Get the facturaCompra
        restFacturaCompraMockMvc
            .perform(get(ENTITY_API_URL_ID, facturaCompra.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(facturaCompra.getId().intValue()))
            .andExpect(jsonPath("$.numeroFactura").value(DEFAULT_NUMERO_FACTURA))
            .andExpect(jsonPath("$.fechaCreacion").value(DEFAULT_FECHA_CREACION.toString()))
            .andExpect(jsonPath("$.infoCliente").value(DEFAULT_INFO_CLIENTE))
            .andExpect(jsonPath("$.valorFactura").value(sameNumber(DEFAULT_VALOR_FACTURA)))
            .andExpect(jsonPath("$.valorPagado").value(sameNumber(DEFAULT_VALOR_PAGADO)))
            .andExpect(jsonPath("$.valorDeuda").value(sameNumber(DEFAULT_VALOR_DEUDA)))
            .andExpect(jsonPath("$.tipoFactura").value(DEFAULT_TIPO_FACTURA.toString()))
            .andExpect(jsonPath("$.idProovedor").value(DEFAULT_ID_PROOVEDOR.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingFacturaCompra() throws Exception {
        // Get the facturaCompra
        restFacturaCompraMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFacturaCompra() throws Exception {
        // Initialize the database
        facturaCompraRepository.saveAndFlush(facturaCompra);

        int databaseSizeBeforeUpdate = facturaCompraRepository.findAll().size();

        // Update the facturaCompra
        FacturaCompra updatedFacturaCompra = facturaCompraRepository.findById(facturaCompra.getId()).get();
        // Disconnect from session so that the updates on updatedFacturaCompra are not directly saved in db
        em.detach(updatedFacturaCompra);
        updatedFacturaCompra
            .numeroFactura(UPDATED_NUMERO_FACTURA)
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .infoCliente(UPDATED_INFO_CLIENTE)
            .valorFactura(UPDATED_VALOR_FACTURA)
            .valorPagado(UPDATED_VALOR_PAGADO)
            .valorDeuda(UPDATED_VALOR_DEUDA)
            .tipoFactura(UPDATED_TIPO_FACTURA)
            .idProovedor(UPDATED_ID_PROOVEDOR);
        FacturaCompraDTO facturaCompraDTO = facturaCompraMapper.toDto(updatedFacturaCompra);

        restFacturaCompraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, facturaCompraDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(facturaCompraDTO))
            )
            .andExpect(status().isOk());

        // Validate the FacturaCompra in the database
        List<FacturaCompra> facturaCompraList = facturaCompraRepository.findAll();
        assertThat(facturaCompraList).hasSize(databaseSizeBeforeUpdate);
        FacturaCompra testFacturaCompra = facturaCompraList.get(facturaCompraList.size() - 1);
        assertThat(testFacturaCompra.getNumeroFactura()).isEqualTo(UPDATED_NUMERO_FACTURA);
        assertThat(testFacturaCompra.getFechaCreacion()).isEqualTo(UPDATED_FECHA_CREACION);
        assertThat(testFacturaCompra.getInfoCliente()).isEqualTo(UPDATED_INFO_CLIENTE);
        assertThat(testFacturaCompra.getValorFactura()).isEqualByComparingTo(UPDATED_VALOR_FACTURA);
        assertThat(testFacturaCompra.getValorPagado()).isEqualByComparingTo(UPDATED_VALOR_PAGADO);
        assertThat(testFacturaCompra.getValorDeuda()).isEqualByComparingTo(UPDATED_VALOR_DEUDA);
        assertThat(testFacturaCompra.getTipoFactura()).isEqualTo(UPDATED_TIPO_FACTURA);
        assertThat(testFacturaCompra.getIdProovedor()).isEqualTo(UPDATED_ID_PROOVEDOR);
    }

    @Test
    @Transactional
    void putNonExistingFacturaCompra() throws Exception {
        int databaseSizeBeforeUpdate = facturaCompraRepository.findAll().size();
        facturaCompra.setId(count.incrementAndGet());

        // Create the FacturaCompra
        FacturaCompraDTO facturaCompraDTO = facturaCompraMapper.toDto(facturaCompra);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFacturaCompraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, facturaCompraDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(facturaCompraDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FacturaCompra in the database
        List<FacturaCompra> facturaCompraList = facturaCompraRepository.findAll();
        assertThat(facturaCompraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFacturaCompra() throws Exception {
        int databaseSizeBeforeUpdate = facturaCompraRepository.findAll().size();
        facturaCompra.setId(count.incrementAndGet());

        // Create the FacturaCompra
        FacturaCompraDTO facturaCompraDTO = facturaCompraMapper.toDto(facturaCompra);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacturaCompraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(facturaCompraDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FacturaCompra in the database
        List<FacturaCompra> facturaCompraList = facturaCompraRepository.findAll();
        assertThat(facturaCompraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFacturaCompra() throws Exception {
        int databaseSizeBeforeUpdate = facturaCompraRepository.findAll().size();
        facturaCompra.setId(count.incrementAndGet());

        // Create the FacturaCompra
        FacturaCompraDTO facturaCompraDTO = facturaCompraMapper.toDto(facturaCompra);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacturaCompraMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(facturaCompraDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FacturaCompra in the database
        List<FacturaCompra> facturaCompraList = facturaCompraRepository.findAll();
        assertThat(facturaCompraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFacturaCompraWithPatch() throws Exception {
        // Initialize the database
        facturaCompraRepository.saveAndFlush(facturaCompra);

        int databaseSizeBeforeUpdate = facturaCompraRepository.findAll().size();

        // Update the facturaCompra using partial update
        FacturaCompra partialUpdatedFacturaCompra = new FacturaCompra();
        partialUpdatedFacturaCompra.setId(facturaCompra.getId());

        partialUpdatedFacturaCompra
            .numeroFactura(UPDATED_NUMERO_FACTURA)
            .valorPagado(UPDATED_VALOR_PAGADO)
            .valorDeuda(UPDATED_VALOR_DEUDA)
            .tipoFactura(UPDATED_TIPO_FACTURA)
            .idProovedor(UPDATED_ID_PROOVEDOR);

        restFacturaCompraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFacturaCompra.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFacturaCompra))
            )
            .andExpect(status().isOk());

        // Validate the FacturaCompra in the database
        List<FacturaCompra> facturaCompraList = facturaCompraRepository.findAll();
        assertThat(facturaCompraList).hasSize(databaseSizeBeforeUpdate);
        FacturaCompra testFacturaCompra = facturaCompraList.get(facturaCompraList.size() - 1);
        assertThat(testFacturaCompra.getNumeroFactura()).isEqualTo(UPDATED_NUMERO_FACTURA);
        assertThat(testFacturaCompra.getFechaCreacion()).isEqualTo(DEFAULT_FECHA_CREACION);
        assertThat(testFacturaCompra.getInfoCliente()).isEqualTo(DEFAULT_INFO_CLIENTE);
        assertThat(testFacturaCompra.getValorFactura()).isEqualByComparingTo(DEFAULT_VALOR_FACTURA);
        assertThat(testFacturaCompra.getValorPagado()).isEqualByComparingTo(UPDATED_VALOR_PAGADO);
        assertThat(testFacturaCompra.getValorDeuda()).isEqualByComparingTo(UPDATED_VALOR_DEUDA);
        assertThat(testFacturaCompra.getTipoFactura()).isEqualTo(UPDATED_TIPO_FACTURA);
        assertThat(testFacturaCompra.getIdProovedor()).isEqualTo(UPDATED_ID_PROOVEDOR);
    }

    @Test
    @Transactional
    void fullUpdateFacturaCompraWithPatch() throws Exception {
        // Initialize the database
        facturaCompraRepository.saveAndFlush(facturaCompra);

        int databaseSizeBeforeUpdate = facturaCompraRepository.findAll().size();

        // Update the facturaCompra using partial update
        FacturaCompra partialUpdatedFacturaCompra = new FacturaCompra();
        partialUpdatedFacturaCompra.setId(facturaCompra.getId());

        partialUpdatedFacturaCompra
            .numeroFactura(UPDATED_NUMERO_FACTURA)
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .infoCliente(UPDATED_INFO_CLIENTE)
            .valorFactura(UPDATED_VALOR_FACTURA)
            .valorPagado(UPDATED_VALOR_PAGADO)
            .valorDeuda(UPDATED_VALOR_DEUDA)
            .tipoFactura(UPDATED_TIPO_FACTURA)
            .idProovedor(UPDATED_ID_PROOVEDOR);

        restFacturaCompraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFacturaCompra.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFacturaCompra))
            )
            .andExpect(status().isOk());

        // Validate the FacturaCompra in the database
        List<FacturaCompra> facturaCompraList = facturaCompraRepository.findAll();
        assertThat(facturaCompraList).hasSize(databaseSizeBeforeUpdate);
        FacturaCompra testFacturaCompra = facturaCompraList.get(facturaCompraList.size() - 1);
        assertThat(testFacturaCompra.getNumeroFactura()).isEqualTo(UPDATED_NUMERO_FACTURA);
        assertThat(testFacturaCompra.getFechaCreacion()).isEqualTo(UPDATED_FECHA_CREACION);
        assertThat(testFacturaCompra.getInfoCliente()).isEqualTo(UPDATED_INFO_CLIENTE);
        assertThat(testFacturaCompra.getValorFactura()).isEqualByComparingTo(UPDATED_VALOR_FACTURA);
        assertThat(testFacturaCompra.getValorPagado()).isEqualByComparingTo(UPDATED_VALOR_PAGADO);
        assertThat(testFacturaCompra.getValorDeuda()).isEqualByComparingTo(UPDATED_VALOR_DEUDA);
        assertThat(testFacturaCompra.getTipoFactura()).isEqualTo(UPDATED_TIPO_FACTURA);
        assertThat(testFacturaCompra.getIdProovedor()).isEqualTo(UPDATED_ID_PROOVEDOR);
    }

    @Test
    @Transactional
    void patchNonExistingFacturaCompra() throws Exception {
        int databaseSizeBeforeUpdate = facturaCompraRepository.findAll().size();
        facturaCompra.setId(count.incrementAndGet());

        // Create the FacturaCompra
        FacturaCompraDTO facturaCompraDTO = facturaCompraMapper.toDto(facturaCompra);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFacturaCompraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, facturaCompraDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(facturaCompraDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FacturaCompra in the database
        List<FacturaCompra> facturaCompraList = facturaCompraRepository.findAll();
        assertThat(facturaCompraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFacturaCompra() throws Exception {
        int databaseSizeBeforeUpdate = facturaCompraRepository.findAll().size();
        facturaCompra.setId(count.incrementAndGet());

        // Create the FacturaCompra
        FacturaCompraDTO facturaCompraDTO = facturaCompraMapper.toDto(facturaCompra);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacturaCompraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(facturaCompraDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FacturaCompra in the database
        List<FacturaCompra> facturaCompraList = facturaCompraRepository.findAll();
        assertThat(facturaCompraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFacturaCompra() throws Exception {
        int databaseSizeBeforeUpdate = facturaCompraRepository.findAll().size();
        facturaCompra.setId(count.incrementAndGet());

        // Create the FacturaCompra
        FacturaCompraDTO facturaCompraDTO = facturaCompraMapper.toDto(facturaCompra);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacturaCompraMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(facturaCompraDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FacturaCompra in the database
        List<FacturaCompra> facturaCompraList = facturaCompraRepository.findAll();
        assertThat(facturaCompraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFacturaCompra() throws Exception {
        // Initialize the database
        facturaCompraRepository.saveAndFlush(facturaCompra);

        int databaseSizeBeforeDelete = facturaCompraRepository.findAll().size();

        // Delete the facturaCompra
        restFacturaCompraMockMvc
            .perform(delete(ENTITY_API_URL_ID, facturaCompra.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FacturaCompra> facturaCompraList = facturaCompraRepository.findAll();
        assertThat(facturaCompraList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
