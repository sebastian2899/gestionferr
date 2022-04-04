package com.gestionferr.app.service.impl;

import com.gestionferr.app.config.Constants;
import com.gestionferr.app.domain.Caja;
import com.gestionferr.app.domain.enumeration.TipoEstadoEnum;
import com.gestionferr.app.repository.CajaRepository;
import com.gestionferr.app.service.CajaService;
import com.gestionferr.app.service.dto.CajaDTO;
import com.gestionferr.app.service.dto.CajaFechasDTO;
import com.gestionferr.app.service.mapper.CajaMapper;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Caja}.
 */
@Service
@Transactional
public class CajaServiceImpl implements CajaService {

    private final Logger log = LoggerFactory.getLogger(CajaServiceImpl.class);

    private final CajaRepository cajaRepository;

    private final CajaMapper cajaMapper;

    @PersistenceContext
    private EntityManager entityManager;

    public CajaServiceImpl(CajaRepository cajaRepository, CajaMapper cajaMapper) {
        this.cajaRepository = cajaRepository;
        this.cajaMapper = cajaMapper;
    }

    @Override
    public CajaDTO save(CajaDTO cajaDTO) {
        log.debug("Request to save Caja : {}", cajaDTO);
        Caja caja = cajaMapper.toEntity(cajaDTO);
        caja = cajaRepository.save(caja);
        return cajaMapper.toDto(caja);
    }

    @Override
    public Optional<CajaDTO> partialUpdate(CajaDTO cajaDTO) {
        log.debug("Request to partially update Caja : {}", cajaDTO);

        return cajaRepository
            .findById(cajaDTO.getId())
            .map(existingCaja -> {
                cajaMapper.partialUpdate(existingCaja, cajaDTO);

                return existingCaja;
            })
            .map(cajaRepository::save)
            .map(cajaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CajaDTO> findAll() {
        log.debug("Request to get all Cajas");
        return cajaRepository.findAll().stream().map(cajaMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CajaDTO> findOne(Long id) {
        log.debug("Request to get Caja : {}", id);
        return cajaRepository.findById(id).map(cajaMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Caja : {}", id);
        cajaRepository.deleteById(id);
    }

    @Override
    public BigDecimal valoresDia() {
        log.debug("Request to get value day");

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String fecha = format.format(new Date());

        BigDecimal valorDiaFacturaVenta = cajaRepository.valorPagadoDia(fecha);

        if (valorDiaFacturaVenta == null) {
            valorDiaFacturaVenta = BigDecimal.ZERO;
        }

        BigDecimal valorAbonoDia = cajaRepository.valorABonoDia(fecha);

        if (valorAbonoDia == null) {
            valorAbonoDia = BigDecimal.ZERO;
        }

        BigDecimal valorTotal = valorDiaFacturaVenta.add(valorAbonoDia);

        return valorTotal;
    }

    @Override
    public List<CajaDTO> cajasPorFiltro(String estado) throws ParseException {
        log.debug("Resquest to get all cajas per estado:", estado);

        StringBuilder sb = new StringBuilder();

        sb.append(Constants.CAJAS_BASE);
        List<Caja> cajas = new ArrayList<>();

        if (estado != null && !estado.isEmpty()) {
            sb.append(Constants.CAJA_POR_ESTADO);
        }

        Query q = entityManager.createNativeQuery(sb.toString());
        q.setParameter("estado", estado);
        List<Object[]> cajasPorEstado = q.getResultList();

        Caja caja = null;

        for (Object[] item : cajasPorEstado) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            caja = new Caja();
            caja.setId(Long.parseLong(item[0].toString()));
            Instant fecha = format.parse(item[1].toString().substring(0, item[1].toString().indexOf(" "))).toInstant();
            caja.setFechaCreacion(fecha);
            caja.setValorVentaDia(new BigDecimal(item[2].toString()));
            caja.setValorRegistradoDia(new BigDecimal(item[3].toString()));
            caja.setEstado(TipoEstadoEnum.valueOf(item[4].toString()));
            caja.setDiferencia(new BigDecimal(item[5].toString()));

            cajas.add(caja);
        }

        return cajas.stream().map(cajaMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public List<CajaDTO> cajasPorFecha(String fecha) {
        log.debug("Request to get all cajas per date", fecha);

        String fechaFormat = fecha.substring(0, fecha.indexOf("T"));

        List<Caja> cajasPorFecha = cajaRepository.cajasPorFecha(fechaFormat);

        return cajasPorFecha.stream().map(cajaMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public CajaFechasDTO valoresCajaPorFechas(String fechaInicio, String fechaFin) {
        log.debug("Request to get all cajas per dates");

        String fechaInicioFormat = fechaInicio.substring(0, fechaInicio.indexOf("T"));
        String fechaFinFormat = fechaFin.substring(0, fechaFin.indexOf("T"));

        CajaFechasDTO cajaFechas = null;
        List<Object[]> cajasPorFechas = cajaRepository.valoresCajaMeses(fechaInicioFormat, fechaFinFormat);

        for (Object[] dato : cajasPorFechas) {
            cajaFechas = new CajaFechasDTO();
            cajaFechas.setValorTotalDia(new BigDecimal(dato[0].toString()));
            cajaFechas.setValorRegistradoDia(new BigDecimal(dato[1].toString()));
            cajaFechas.setDiferencia(new BigDecimal(dato[2].toString()));
        }

        return cajaFechas;
    }

    @Override
    public byte[] reporteCaja() {
        log.debug("Request to generate report of caja per dates");

        Calendar fechaInicio = Calendar.getInstance();
        fechaInicio.add(Calendar.MONTH, -1);

        Instant fechaInicioInstant = fechaInicio.toInstant();
        Instant fechaFin = Instant.now();

        List<Caja> cajasPorFecha = cajaRepository.cajaPorFechasReport(fechaInicioInstant, fechaFin);

        Document document = null;
        String nombreDocumento = null;

        try {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

            document = new Document();
            nombreDocumento = "Caja Reporte.pdf";
            FileOutputStream fileOutput = new FileOutputStream(nombreDocumento);

            PdfWriter.getInstance(document, fileOutput);
            document.open();

            Date fechaInicioDate = Date.from(fechaInicioInstant);
            String fechaInicioString = format.format(fechaInicioDate);

            Date fechaFinDate = Date.from(fechaFin);
            String fechaFinString = format.format(fechaFinDate);

            Paragraph titulo = new Paragraph("Reporte PDF Caja Por Fechas");
            titulo.setAlignment(1);

            document.add(titulo);

            Paragraph fecha1 = new Paragraph(fechaInicioString);
            Paragraph fecha2 = new Paragraph(fechaFinString);

            document.add(Chunk.NEWLINE);

            fecha1.setAlignment(2);
            fecha2.setAlignment(2);

            document.add(fecha1);
            document.add(fecha2);

            document.close();

            return FileUtils.readFileToByteArray(new File(nombreDocumento));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
