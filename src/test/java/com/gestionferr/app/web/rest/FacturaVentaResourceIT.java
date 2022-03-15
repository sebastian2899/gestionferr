package com.gestionferr.app.web.rest;

import static com.gestionferr.app.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gestionferr.app.IntegrationTest;
import com.gestionferr.app.domain.FacturaVenta;
import com.gestionferr.app.domain.enumeration.TipoFacturaEnum;
import com.gestionferr.app.repository.FacturaVentaRepository;
import com.gestionferr.app.service.dto.FacturaVentaDTO;
import com.gestionferr.app.service.mapper.FacturaVentaMapper;
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
 * Integration tests for the {@link FacturaVentaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FacturaVentaResourceIT {

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

    private static final Long DEFAULT_ID_CLIENTE = 1L;
    private static final Long UPDATED_ID_CLIENTE = 2L;

    private static final String ENTITY_API_URL = "/api/factura-ventas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FacturaVentaRepository facturaVentaRepository;

    @Autowired
    private FacturaVentaMapper facturaVentaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFacturaVentaMockMvc;

    private FacturaVenta facturaVenta;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FacturaVenta createEntity(EntityManager em) {
        FacturaVenta facturaVenta = new FacturaVenta()
            .numeroFactura(DEFAULT_NUMERO_FACTURA)
            .fechaCreacion(DEFAULT_FECHA_CREACION)
            .infoCliente(DEFAULT_INFO_CLIENTE)
            .valorFactura(DEFAULT_VALOR_FACTURA)
            .valorPagado(DEFAULT_VALOR_PAGADO)
            .valorDeuda(DEFAULT_VALOR_DEUDA)
            .tipoFactura(DEFAULT_TIPO_FACTURA)
            .idCliente(DEFAULT_ID_CLIENTE);
        return facturaVenta;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FacturaVenta createUpdatedEntity(EntityManager em) {
        FacturaVenta facturaVenta = new FacturaVenta()
            .numeroFactura(UPDATED_NUMERO_FACTURA)
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .infoCliente(UPDATED_INFO_CLIENTE)
            .valorFactura(UPDATED_VALOR_FACTURA)
            .valorPagado(UPDATED_VALOR_PAGADO)
            .valorDeuda(UPDATED_VALOR_DEUDA)
            .tipoFactura(UPDATED_TIPO_FACTURA)
            .idCliente(UPDATED_ID_CLIENTE);
        return facturaVenta;
    }

    @BeforeEach
    public void initTest() {
        facturaVenta = createEntity(em);
    }

    @Test
    @Transactional
    void createFacturaVenta() throws Exception {
        int databaseSizeBeforeCreate = facturaVentaRepository.findAll().size();
        // Create the FacturaVenta
        FacturaVentaDTO facturaVentaDTO = facturaVentaMapper.toDto(facturaVenta);
        restFacturaVentaMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(facturaVentaDTO))
            )
            .andExpect(status().isCreated());

        // Validate the FacturaVenta in the database
        List<FacturaVenta> facturaVentaList = facturaVentaRepository.findAll();
        assertThat(facturaVentaList).hasSize(databaseSizeBeforeCreate + 1);
        FacturaVenta testFacturaVenta = facturaVentaList.get(facturaVentaList.size() - 1);
        assertThat(testFacturaVenta.getNumeroFactura()).isEqualTo(DEFAULT_NUMERO_FACTURA);
        assertThat(testFacturaVenta.getFechaCreacion()).isEqualTo(DEFAULT_FECHA_CREACION);
        assertThat(testFacturaVenta.getInfoCliente()).isEqualTo(DEFAULT_INFO_CLIENTE);
        assertThat(testFacturaVenta.getValorFactura()).isEqualByComparingTo(DEFAULT_VALOR_FACTURA);
        assertThat(testFacturaVenta.getValorPagado()).isEqualByComparingTo(DEFAULT_VALOR_PAGADO);
        assertThat(testFacturaVenta.getValorDeuda()).isEqualByComparingTo(DEFAULT_VALOR_DEUDA);
        assertThat(testFacturaVenta.getTipoFactura()).isEqualTo(DEFAULT_TIPO_FACTURA);
        assertThat(testFacturaVenta.getIdCliente()).isEqualTo(DEFAULT_ID_CLIENTE);
    }

    @Test
    @Transactional
    void createFacturaVentaWithExistingId() throws Exception {
        // Create the FacturaVenta with an existing ID
        facturaVenta.setId(1L);
        FacturaVentaDTO facturaVentaDTO = facturaVentaMapper.toDto(facturaVenta);

        int databaseSizeBeforeCreate = facturaVentaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFacturaVentaMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(facturaVentaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FacturaVenta in the database
        List<FacturaVenta> facturaVentaList = facturaVentaRepository.findAll();
        assertThat(facturaVentaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFacturaVentas() throws Exception {
        // Initialize the database
        facturaVentaRepository.saveAndFlush(facturaVenta);

        // Get all the facturaVentaList
        restFacturaVentaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(facturaVenta.getId().intValue())))
            .andExpect(jsonPath("$.[*].numeroFactura").value(hasItem(DEFAULT_NUMERO_FACTURA)))
            .andExpect(jsonPath("$.[*].fechaCreacion").value(hasItem(DEFAULT_FECHA_CREACION.toString())))
            .andExpect(jsonPath("$.[*].infoCliente").value(hasItem(DEFAULT_INFO_CLIENTE)))
            .andExpect(jsonPath("$.[*].valorFactura").value(hasItem(sameNumber(DEFAULT_VALOR_FACTURA))))
            .andExpect(jsonPath("$.[*].valorPagado").value(hasItem(sameNumber(DEFAULT_VALOR_PAGADO))))
            .andExpect(jsonPath("$.[*].valorDeuda").value(hasItem(sameNumber(DEFAULT_VALOR_DEUDA))))
            .andExpect(jsonPath("$.[*].tipoFactura").value(hasItem(DEFAULT_TIPO_FACTURA.toString())))
            .andExpect(jsonPath("$.[*].idCliente").value(hasItem(DEFAULT_ID_CLIENTE.intValue())));
    }

    @Test
    @Transactional
    void getFacturaVenta() throws Exception {
        // Initialize the database
        facturaVentaRepository.saveAndFlush(facturaVenta);

        // Get the facturaVenta
        restFacturaVentaMockMvc
            .perform(get(ENTITY_API_URL_ID, facturaVenta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(facturaVenta.getId().intValue()))
            .andExpect(jsonPath("$.numeroFactura").value(DEFAULT_NUMERO_FACTURA))
            .andExpect(jsonPath("$.fechaCreacion").value(DEFAULT_FECHA_CREACION.toString()))
            .andExpect(jsonPath("$.infoCliente").value(DEFAULT_INFO_CLIENTE))
            .andExpect(jsonPath("$.valorFactura").value(sameNumber(DEFAULT_VALOR_FACTURA)))
            .andExpect(jsonPath("$.valorPagado").value(sameNumber(DEFAULT_VALOR_PAGADO)))
            .andExpect(jsonPath("$.valorDeuda").value(sameNumber(DEFAULT_VALOR_DEUDA)))
            .andExpect(jsonPath("$.tipoFactura").value(DEFAULT_TIPO_FACTURA.toString()))
            .andExpect(jsonPath("$.idCliente").value(DEFAULT_ID_CLIENTE.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingFacturaVenta() throws Exception {
        // Get the facturaVenta
        restFacturaVentaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFacturaVenta() throws Exception {
        // Initialize the database
        facturaVentaRepository.saveAndFlush(facturaVenta);

        int databaseSizeBeforeUpdate = facturaVentaRepository.findAll().size();

        // Update the facturaVenta
        FacturaVenta updatedFacturaVenta = facturaVentaRepository.findById(facturaVenta.getId()).get();
        // Disconnect from session so that the updates on updatedFacturaVenta are not directly saved in db
        em.detach(updatedFacturaVenta);
        updatedFacturaVenta
            .numeroFactura(UPDATED_NUMERO_FACTURA)
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .infoCliente(UPDATED_INFO_CLIENTE)
            .valorFactura(UPDATED_VALOR_FACTURA)
            .valorPagado(UPDATED_VALOR_PAGADO)
            .valorDeuda(UPDATED_VALOR_DEUDA)
            .tipoFactura(UPDATED_TIPO_FACTURA)
            .idCliente(UPDATED_ID_CLIENTE);
        FacturaVentaDTO facturaVentaDTO = facturaVentaMapper.toDto(updatedFacturaVenta);

        restFacturaVentaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, facturaVentaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(facturaVentaDTO))
            )
            .andExpect(status().isOk());

        // Validate the FacturaVenta in the database
        List<FacturaVenta> facturaVentaList = facturaVentaRepository.findAll();
        assertThat(facturaVentaList).hasSize(databaseSizeBeforeUpdate);
        FacturaVenta testFacturaVenta = facturaVentaList.get(facturaVentaList.size() - 1);
        assertThat(testFacturaVenta.getNumeroFactura()).isEqualTo(UPDATED_NUMERO_FACTURA);
        assertThat(testFacturaVenta.getFechaCreacion()).isEqualTo(UPDATED_FECHA_CREACION);
        assertThat(testFacturaVenta.getInfoCliente()).isEqualTo(UPDATED_INFO_CLIENTE);
        assertThat(testFacturaVenta.getValorFactura()).isEqualByComparingTo(UPDATED_VALOR_FACTURA);
        assertThat(testFacturaVenta.getValorPagado()).isEqualByComparingTo(UPDATED_VALOR_PAGADO);
        assertThat(testFacturaVenta.getValorDeuda()).isEqualByComparingTo(UPDATED_VALOR_DEUDA);
        assertThat(testFacturaVenta.getTipoFactura()).isEqualTo(UPDATED_TIPO_FACTURA);
        assertThat(testFacturaVenta.getIdCliente()).isEqualTo(UPDATED_ID_CLIENTE);
    }

    @Test
    @Transactional
    void putNonExistingFacturaVenta() throws Exception {
        int databaseSizeBeforeUpdate = facturaVentaRepository.findAll().size();
        facturaVenta.setId(count.incrementAndGet());

        // Create the FacturaVenta
        FacturaVentaDTO facturaVentaDTO = facturaVentaMapper.toDto(facturaVenta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFacturaVentaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, facturaVentaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(facturaVentaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FacturaVenta in the database
        List<FacturaVenta> facturaVentaList = facturaVentaRepository.findAll();
        assertThat(facturaVentaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFacturaVenta() throws Exception {
        int databaseSizeBeforeUpdate = facturaVentaRepository.findAll().size();
        facturaVenta.setId(count.incrementAndGet());

        // Create the FacturaVenta
        FacturaVentaDTO facturaVentaDTO = facturaVentaMapper.toDto(facturaVenta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacturaVentaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(facturaVentaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FacturaVenta in the database
        List<FacturaVenta> facturaVentaList = facturaVentaRepository.findAll();
        assertThat(facturaVentaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFacturaVenta() throws Exception {
        int databaseSizeBeforeUpdate = facturaVentaRepository.findAll().size();
        facturaVenta.setId(count.incrementAndGet());

        // Create the FacturaVenta
        FacturaVentaDTO facturaVentaDTO = facturaVentaMapper.toDto(facturaVenta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacturaVentaMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(facturaVentaDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FacturaVenta in the database
        List<FacturaVenta> facturaVentaList = facturaVentaRepository.findAll();
        assertThat(facturaVentaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFacturaVentaWithPatch() throws Exception {
        // Initialize the database
        facturaVentaRepository.saveAndFlush(facturaVenta);

        int databaseSizeBeforeUpdate = facturaVentaRepository.findAll().size();

        // Update the facturaVenta using partial update
        FacturaVenta partialUpdatedFacturaVenta = new FacturaVenta();
        partialUpdatedFacturaVenta.setId(facturaVenta.getId());

        partialUpdatedFacturaVenta
            .numeroFactura(UPDATED_NUMERO_FACTURA)
            .valorFactura(UPDATED_VALOR_FACTURA)
            .valorPagado(UPDATED_VALOR_PAGADO)
            .tipoFactura(UPDATED_TIPO_FACTURA);

        restFacturaVentaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFacturaVenta.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFacturaVenta))
            )
            .andExpect(status().isOk());

        // Validate the FacturaVenta in the database
        List<FacturaVenta> facturaVentaList = facturaVentaRepository.findAll();
        assertThat(facturaVentaList).hasSize(databaseSizeBeforeUpdate);
        FacturaVenta testFacturaVenta = facturaVentaList.get(facturaVentaList.size() - 1);
        assertThat(testFacturaVenta.getNumeroFactura()).isEqualTo(UPDATED_NUMERO_FACTURA);
        assertThat(testFacturaVenta.getFechaCreacion()).isEqualTo(DEFAULT_FECHA_CREACION);
        assertThat(testFacturaVenta.getInfoCliente()).isEqualTo(DEFAULT_INFO_CLIENTE);
        assertThat(testFacturaVenta.getValorFactura()).isEqualByComparingTo(UPDATED_VALOR_FACTURA);
        assertThat(testFacturaVenta.getValorPagado()).isEqualByComparingTo(UPDATED_VALOR_PAGADO);
        assertThat(testFacturaVenta.getValorDeuda()).isEqualByComparingTo(DEFAULT_VALOR_DEUDA);
        assertThat(testFacturaVenta.getTipoFactura()).isEqualTo(UPDATED_TIPO_FACTURA);
        assertThat(testFacturaVenta.getIdCliente()).isEqualTo(DEFAULT_ID_CLIENTE);
    }

    @Test
    @Transactional
    void fullUpdateFacturaVentaWithPatch() throws Exception {
        // Initialize the database
        facturaVentaRepository.saveAndFlush(facturaVenta);

        int databaseSizeBeforeUpdate = facturaVentaRepository.findAll().size();

        // Update the facturaVenta using partial update
        FacturaVenta partialUpdatedFacturaVenta = new FacturaVenta();
        partialUpdatedFacturaVenta.setId(facturaVenta.getId());

        partialUpdatedFacturaVenta
            .numeroFactura(UPDATED_NUMERO_FACTURA)
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .infoCliente(UPDATED_INFO_CLIENTE)
            .valorFactura(UPDATED_VALOR_FACTURA)
            .valorPagado(UPDATED_VALOR_PAGADO)
            .valorDeuda(UPDATED_VALOR_DEUDA)
            .tipoFactura(UPDATED_TIPO_FACTURA)
            .idCliente(UPDATED_ID_CLIENTE);

        restFacturaVentaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFacturaVenta.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFacturaVenta))
            )
            .andExpect(status().isOk());

        // Validate the FacturaVenta in the database
        List<FacturaVenta> facturaVentaList = facturaVentaRepository.findAll();
        assertThat(facturaVentaList).hasSize(databaseSizeBeforeUpdate);
        FacturaVenta testFacturaVenta = facturaVentaList.get(facturaVentaList.size() - 1);
        assertThat(testFacturaVenta.getNumeroFactura()).isEqualTo(UPDATED_NUMERO_FACTURA);
        assertThat(testFacturaVenta.getFechaCreacion()).isEqualTo(UPDATED_FECHA_CREACION);
        assertThat(testFacturaVenta.getInfoCliente()).isEqualTo(UPDATED_INFO_CLIENTE);
        assertThat(testFacturaVenta.getValorFactura()).isEqualByComparingTo(UPDATED_VALOR_FACTURA);
        assertThat(testFacturaVenta.getValorPagado()).isEqualByComparingTo(UPDATED_VALOR_PAGADO);
        assertThat(testFacturaVenta.getValorDeuda()).isEqualByComparingTo(UPDATED_VALOR_DEUDA);
        assertThat(testFacturaVenta.getTipoFactura()).isEqualTo(UPDATED_TIPO_FACTURA);
        assertThat(testFacturaVenta.getIdCliente()).isEqualTo(UPDATED_ID_CLIENTE);
    }

    @Test
    @Transactional
    void patchNonExistingFacturaVenta() throws Exception {
        int databaseSizeBeforeUpdate = facturaVentaRepository.findAll().size();
        facturaVenta.setId(count.incrementAndGet());

        // Create the FacturaVenta
        FacturaVentaDTO facturaVentaDTO = facturaVentaMapper.toDto(facturaVenta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFacturaVentaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, facturaVentaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(facturaVentaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FacturaVenta in the database
        List<FacturaVenta> facturaVentaList = facturaVentaRepository.findAll();
        assertThat(facturaVentaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFacturaVenta() throws Exception {
        int databaseSizeBeforeUpdate = facturaVentaRepository.findAll().size();
        facturaVenta.setId(count.incrementAndGet());

        // Create the FacturaVenta
        FacturaVentaDTO facturaVentaDTO = facturaVentaMapper.toDto(facturaVenta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacturaVentaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(facturaVentaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FacturaVenta in the database
        List<FacturaVenta> facturaVentaList = facturaVentaRepository.findAll();
        assertThat(facturaVentaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFacturaVenta() throws Exception {
        int databaseSizeBeforeUpdate = facturaVentaRepository.findAll().size();
        facturaVenta.setId(count.incrementAndGet());

        // Create the FacturaVenta
        FacturaVentaDTO facturaVentaDTO = facturaVentaMapper.toDto(facturaVenta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacturaVentaMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(facturaVentaDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FacturaVenta in the database
        List<FacturaVenta> facturaVentaList = facturaVentaRepository.findAll();
        assertThat(facturaVentaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFacturaVenta() throws Exception {
        // Initialize the database
        facturaVentaRepository.saveAndFlush(facturaVenta);

        int databaseSizeBeforeDelete = facturaVentaRepository.findAll().size();

        // Delete the facturaVenta
        restFacturaVentaMockMvc
            .perform(delete(ENTITY_API_URL_ID, facturaVenta.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FacturaVenta> facturaVentaList = facturaVentaRepository.findAll();
        assertThat(facturaVentaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
