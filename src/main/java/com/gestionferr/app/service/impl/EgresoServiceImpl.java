package com.gestionferr.app.service.impl;

import com.gestionferr.app.domain.Egreso;
import com.gestionferr.app.repository.EgresoRepository;
import com.gestionferr.app.service.EgresoService;
import com.gestionferr.app.service.dto.EgresoDTO;
import com.gestionferr.app.service.mapper.EgresoMapper;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Egreso}.
 */
@Service
@Transactional
public class EgresoServiceImpl implements EgresoService {

    private final Logger log = LoggerFactory.getLogger(EgresoServiceImpl.class);

    private final EgresoRepository egresoRepository;

    private final EgresoMapper egresoMapper;

    public EgresoServiceImpl(EgresoRepository egresoRepository, EgresoMapper egresoMapper) {
        this.egresoRepository = egresoRepository;
        this.egresoMapper = egresoMapper;
    }

    @Override
    public EgresoDTO save(EgresoDTO egresoDTO) {
        log.debug("Request to save Egreso : {}", egresoDTO);
        Egreso egreso = egresoMapper.toEntity(egresoDTO);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

        Date dateActual = new Date();
        String fechaActualFormat = format.format(dateActual);

        Date fechaEgreso = Date.from(egreso.getFechaCreacion());
        String fechaEgresoFormat = format.format(fechaEgreso);

        if (fechaActualFormat.equalsIgnoreCase(fechaEgresoFormat)) {
            egreso.setEstadoEditar("editable");
        } else {
            egreso.setEstadoEditar("no editable");
        }

        egreso = egresoRepository.save(egreso);
        return egresoMapper.toDto(egreso);
    }

    @Scheduled(cron = "0 0 9 * * *")
    public void cambiarEstadoEgresoEditar() {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date dateActual = new Date();
        String fechaActualFormat = format.format(dateActual);

        List<Egreso> egresos = egresoRepository.findAll();

        for (Egreso egreso : egresos) {
            Date fechaEgreso = Date.from(egreso.getFechaCreacion());
            String fechaEgresoFormat = format.format(fechaEgreso);

            if (fechaActualFormat.equalsIgnoreCase(fechaEgresoFormat)) {
                egreso.setEstadoEditar("editable");
            } else {
                egreso.setEstadoEditar("no editable");
            }
        }
    }

    @Override
    public Optional<EgresoDTO> partialUpdate(EgresoDTO egresoDTO) {
        log.debug("Request to partially update Egreso : {}", egresoDTO);

        return egresoRepository
            .findById(egresoDTO.getId())
            .map(existingEgreso -> {
                egresoMapper.partialUpdate(existingEgreso, egresoDTO);

                return existingEgreso;
            })
            .map(egresoRepository::save)
            .map(egresoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EgresoDTO> findAll() {
        log.debug("Request to get all Egresos");
        return egresoRepository.findAll().stream().map(egresoMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EgresoDTO> findOne(Long id) {
        log.debug("Request to get Egreso : {}", id);
        return egresoRepository.findById(id).map(egresoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Egreso : {}", id);
        egresoRepository.deleteById(id);
    }

    @Override
    public BigDecimal valorDiarioEgreso() {
        log.debug("Request to get daily value of egreso");
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date fechaActual = new Date();

        String fechaFormat = format.format(fechaActual);

        BigDecimal valorDiarioEgreso = egresoRepository.valorEgresoDia(fechaFormat);

        if (valorDiarioEgreso == null) {
            valorDiarioEgreso = BigDecimal.ZERO;
        }

        return valorDiarioEgreso;
    }

    @Override
    public List<EgresoDTO> egresoDiario() {
        log.debug("Request to get all daily egreso");

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();

        String fechaActual = format.format(date);

        List<Egreso> dailyEgreso = egresoRepository.egresoDiario(fechaActual);

        return dailyEgreso.stream().map(egresoMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public List<EgresoDTO> egresosFecha(String fecha) {
        log.debug("Request to get egresos per date");

        String fechaFormat = fecha.substring(0, fecha.indexOf("T"));

        List<Egreso> egresosFecha = egresoRepository.egresoPorFecha(fechaFormat);

        return egresosFecha.stream().map(egresoMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }
}
