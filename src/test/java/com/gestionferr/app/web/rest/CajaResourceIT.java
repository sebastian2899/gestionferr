package com.gestionferr.app.web.rest;

import static com.gestionferr.app.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gestionferr.app.IntegrationTest;
import com.gestionferr.app.domain.Caja;
import com.gestionferr.app.domain.enumeration.TipoEstadoEnum;
import com.gestionferr.app.repository.CajaRepository;
import com.gestionferr.app.service.dto.CajaDTO;
import com.gestionferr.app.service.mapper.CajaMapper;
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
 * Integration tests for the {@link CajaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CajaResourceIT {

    private static final Instant DEFAULT_FECHA_CREACION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_CREACION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final BigDecimal DEFAULT_VALOR_VENTA_DIA = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALOR_VENTA_DIA = new BigDecimal(2);

    private static final BigDecimal DEFAULT_VALOR_REGISTRADO_DIA = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALOR_REGISTRADO_DIA = new BigDecimal(2);

    private static final TipoEstadoEnum DEFAULT_ESTADO = TipoEstadoEnum.PAGADA;
    private static final TipoEstadoEnum UPDATED_ESTADO = TipoEstadoEnum.DEUDA;

    private static final BigDecimal DEFAULT_DIFERENCIA = new BigDecimal(1);
    private static final BigDecimal UPDATED_DIFERENCIA = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/cajas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CajaRepository cajaRepository;

    @Autowired
    private CajaMapper cajaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCajaMockMvc;

    private Caja caja;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Caja createEntity(EntityManager em) {
        Caja caja = new Caja()
            .fechaCreacion(DEFAULT_FECHA_CREACION)
            .valorVentaDia(DEFAULT_VALOR_VENTA_DIA)
            .valorRegistradoDia(DEFAULT_VALOR_REGISTRADO_DIA)
            .estado(DEFAULT_ESTADO)
            .diferencia(DEFAULT_DIFERENCIA);
        return caja;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Caja createUpdatedEntity(EntityManager em) {
        Caja caja = new Caja()
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .valorVentaDia(UPDATED_VALOR_VENTA_DIA)
            .valorRegistradoDia(UPDATED_VALOR_REGISTRADO_DIA)
            .estado(UPDATED_ESTADO)
            .diferencia(UPDATED_DIFERENCIA);
        return caja;
    }

    @BeforeEach
    public void initTest() {
        caja = createEntity(em);
    }

    @Test
    @Transactional
    void createCaja() throws Exception {
        int databaseSizeBeforeCreate = cajaRepository.findAll().size();
        // Create the Caja
        CajaDTO cajaDTO = cajaMapper.toDto(caja);
        restCajaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cajaDTO)))
            .andExpect(status().isCreated());

        // Validate the Caja in the database
        List<Caja> cajaList = cajaRepository.findAll();
        assertThat(cajaList).hasSize(databaseSizeBeforeCreate + 1);
        Caja testCaja = cajaList.get(cajaList.size() - 1);
        assertThat(testCaja.getFechaCreacion()).isEqualTo(DEFAULT_FECHA_CREACION);
        assertThat(testCaja.getValorVentaDia()).isEqualByComparingTo(DEFAULT_VALOR_VENTA_DIA);
        assertThat(testCaja.getValorRegistradoDia()).isEqualByComparingTo(DEFAULT_VALOR_REGISTRADO_DIA);
        assertThat(testCaja.getEstado()).isEqualTo(DEFAULT_ESTADO);
        assertThat(testCaja.getDiferencia()).isEqualByComparingTo(DEFAULT_DIFERENCIA);
    }

    @Test
    @Transactional
    void createCajaWithExistingId() throws Exception {
        // Create the Caja with an existing ID
        caja.setId(1L);
        CajaDTO cajaDTO = cajaMapper.toDto(caja);

        int databaseSizeBeforeCreate = cajaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCajaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cajaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Caja in the database
        List<Caja> cajaList = cajaRepository.findAll();
        assertThat(cajaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCajas() throws Exception {
        // Initialize the database
        cajaRepository.saveAndFlush(caja);

        // Get all the cajaList
        restCajaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(caja.getId().intValue())))
            .andExpect(jsonPath("$.[*].fechaCreacion").value(hasItem(DEFAULT_FECHA_CREACION.toString())))
            .andExpect(jsonPath("$.[*].valorVentaDia").value(hasItem(sameNumber(DEFAULT_VALOR_VENTA_DIA))))
            .andExpect(jsonPath("$.[*].valorRegistradoDia").value(hasItem(sameNumber(DEFAULT_VALOR_REGISTRADO_DIA))))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())))
            .andExpect(jsonPath("$.[*].diferencia").value(hasItem(sameNumber(DEFAULT_DIFERENCIA))));
    }

    @Test
    @Transactional
    void getCaja() throws Exception {
        // Initialize the database
        cajaRepository.saveAndFlush(caja);

        // Get the caja
        restCajaMockMvc
            .perform(get(ENTITY_API_URL_ID, caja.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(caja.getId().intValue()))
            .andExpect(jsonPath("$.fechaCreacion").value(DEFAULT_FECHA_CREACION.toString()))
            .andExpect(jsonPath("$.valorVentaDia").value(sameNumber(DEFAULT_VALOR_VENTA_DIA)))
            .andExpect(jsonPath("$.valorRegistradoDia").value(sameNumber(DEFAULT_VALOR_REGISTRADO_DIA)))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO.toString()))
            .andExpect(jsonPath("$.diferencia").value(sameNumber(DEFAULT_DIFERENCIA)));
    }

    @Test
    @Transactional
    void getNonExistingCaja() throws Exception {
        // Get the caja
        restCajaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCaja() throws Exception {
        // Initialize the database
        cajaRepository.saveAndFlush(caja);

        int databaseSizeBeforeUpdate = cajaRepository.findAll().size();

        // Update the caja
        Caja updatedCaja = cajaRepository.findById(caja.getId()).get();
        // Disconnect from session so that the updates on updatedCaja are not directly saved in db
        em.detach(updatedCaja);
        updatedCaja
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .valorVentaDia(UPDATED_VALOR_VENTA_DIA)
            .valorRegistradoDia(UPDATED_VALOR_REGISTRADO_DIA)
            .estado(UPDATED_ESTADO)
            .diferencia(UPDATED_DIFERENCIA);
        CajaDTO cajaDTO = cajaMapper.toDto(updatedCaja);

        restCajaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cajaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cajaDTO))
            )
            .andExpect(status().isOk());

        // Validate the Caja in the database
        List<Caja> cajaList = cajaRepository.findAll();
        assertThat(cajaList).hasSize(databaseSizeBeforeUpdate);
        Caja testCaja = cajaList.get(cajaList.size() - 1);
        assertThat(testCaja.getFechaCreacion()).isEqualTo(UPDATED_FECHA_CREACION);
        assertThat(testCaja.getValorVentaDia()).isEqualByComparingTo(UPDATED_VALOR_VENTA_DIA);
        assertThat(testCaja.getValorRegistradoDia()).isEqualByComparingTo(UPDATED_VALOR_REGISTRADO_DIA);
        assertThat(testCaja.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testCaja.getDiferencia()).isEqualByComparingTo(UPDATED_DIFERENCIA);
    }

    @Test
    @Transactional
    void putNonExistingCaja() throws Exception {
        int databaseSizeBeforeUpdate = cajaRepository.findAll().size();
        caja.setId(count.incrementAndGet());

        // Create the Caja
        CajaDTO cajaDTO = cajaMapper.toDto(caja);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCajaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cajaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cajaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Caja in the database
        List<Caja> cajaList = cajaRepository.findAll();
        assertThat(cajaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCaja() throws Exception {
        int databaseSizeBeforeUpdate = cajaRepository.findAll().size();
        caja.setId(count.incrementAndGet());

        // Create the Caja
        CajaDTO cajaDTO = cajaMapper.toDto(caja);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCajaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cajaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Caja in the database
        List<Caja> cajaList = cajaRepository.findAll();
        assertThat(cajaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCaja() throws Exception {
        int databaseSizeBeforeUpdate = cajaRepository.findAll().size();
        caja.setId(count.incrementAndGet());

        // Create the Caja
        CajaDTO cajaDTO = cajaMapper.toDto(caja);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCajaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cajaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Caja in the database
        List<Caja> cajaList = cajaRepository.findAll();
        assertThat(cajaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCajaWithPatch() throws Exception {
        // Initialize the database
        cajaRepository.saveAndFlush(caja);

        int databaseSizeBeforeUpdate = cajaRepository.findAll().size();

        // Update the caja using partial update
        Caja partialUpdatedCaja = new Caja();
        partialUpdatedCaja.setId(caja.getId());

        partialUpdatedCaja.fechaCreacion(UPDATED_FECHA_CREACION);

        restCajaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCaja.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCaja))
            )
            .andExpect(status().isOk());

        // Validate the Caja in the database
        List<Caja> cajaList = cajaRepository.findAll();
        assertThat(cajaList).hasSize(databaseSizeBeforeUpdate);
        Caja testCaja = cajaList.get(cajaList.size() - 1);
        assertThat(testCaja.getFechaCreacion()).isEqualTo(UPDATED_FECHA_CREACION);
        assertThat(testCaja.getValorVentaDia()).isEqualByComparingTo(DEFAULT_VALOR_VENTA_DIA);
        assertThat(testCaja.getValorRegistradoDia()).isEqualByComparingTo(DEFAULT_VALOR_REGISTRADO_DIA);
        assertThat(testCaja.getEstado()).isEqualTo(DEFAULT_ESTADO);
        assertThat(testCaja.getDiferencia()).isEqualByComparingTo(DEFAULT_DIFERENCIA);
    }

    @Test
    @Transactional
    void fullUpdateCajaWithPatch() throws Exception {
        // Initialize the database
        cajaRepository.saveAndFlush(caja);

        int databaseSizeBeforeUpdate = cajaRepository.findAll().size();

        // Update the caja using partial update
        Caja partialUpdatedCaja = new Caja();
        partialUpdatedCaja.setId(caja.getId());

        partialUpdatedCaja
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .valorVentaDia(UPDATED_VALOR_VENTA_DIA)
            .valorRegistradoDia(UPDATED_VALOR_REGISTRADO_DIA)
            .estado(UPDATED_ESTADO)
            .diferencia(UPDATED_DIFERENCIA);

        restCajaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCaja.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCaja))
            )
            .andExpect(status().isOk());

        // Validate the Caja in the database
        List<Caja> cajaList = cajaRepository.findAll();
        assertThat(cajaList).hasSize(databaseSizeBeforeUpdate);
        Caja testCaja = cajaList.get(cajaList.size() - 1);
        assertThat(testCaja.getFechaCreacion()).isEqualTo(UPDATED_FECHA_CREACION);
        assertThat(testCaja.getValorVentaDia()).isEqualByComparingTo(UPDATED_VALOR_VENTA_DIA);
        assertThat(testCaja.getValorRegistradoDia()).isEqualByComparingTo(UPDATED_VALOR_REGISTRADO_DIA);
        assertThat(testCaja.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testCaja.getDiferencia()).isEqualByComparingTo(UPDATED_DIFERENCIA);
    }

    @Test
    @Transactional
    void patchNonExistingCaja() throws Exception {
        int databaseSizeBeforeUpdate = cajaRepository.findAll().size();
        caja.setId(count.incrementAndGet());

        // Create the Caja
        CajaDTO cajaDTO = cajaMapper.toDto(caja);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCajaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cajaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cajaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Caja in the database
        List<Caja> cajaList = cajaRepository.findAll();
        assertThat(cajaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCaja() throws Exception {
        int databaseSizeBeforeUpdate = cajaRepository.findAll().size();
        caja.setId(count.incrementAndGet());

        // Create the Caja
        CajaDTO cajaDTO = cajaMapper.toDto(caja);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCajaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cajaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Caja in the database
        List<Caja> cajaList = cajaRepository.findAll();
        assertThat(cajaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCaja() throws Exception {
        int databaseSizeBeforeUpdate = cajaRepository.findAll().size();
        caja.setId(count.incrementAndGet());

        // Create the Caja
        CajaDTO cajaDTO = cajaMapper.toDto(caja);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCajaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(cajaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Caja in the database
        List<Caja> cajaList = cajaRepository.findAll();
        assertThat(cajaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCaja() throws Exception {
        // Initialize the database
        cajaRepository.saveAndFlush(caja);

        int databaseSizeBeforeDelete = cajaRepository.findAll().size();

        // Delete the caja
        restCajaMockMvc
            .perform(delete(ENTITY_API_URL_ID, caja.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Caja> cajaList = cajaRepository.findAll();
        assertThat(cajaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
