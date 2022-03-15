package com.gestionferr.app.service.impl;

import com.gestionferr.app.domain.Abono;
import com.gestionferr.app.repository.AbonoRepository;
import com.gestionferr.app.service.AbonoService;
import com.gestionferr.app.service.dto.AbonoDTO;
import com.gestionferr.app.service.mapper.AbonoMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Abono}.
 */
@Service
@Transactional
public class AbonoServiceImpl implements AbonoService {

    private final Logger log = LoggerFactory.getLogger(AbonoServiceImpl.class);

    private final AbonoRepository abonoRepository;

    private final AbonoMapper abonoMapper;

    public AbonoServiceImpl(AbonoRepository abonoRepository, AbonoMapper abonoMapper) {
        this.abonoRepository = abonoRepository;
        this.abonoMapper = abonoMapper;
    }

    @Override
    public AbonoDTO save(AbonoDTO abonoDTO) {
        log.debug("Request to save Abono : {}", abonoDTO);
        Abono abono = abonoMapper.toEntity(abonoDTO);
        abono = abonoRepository.save(abono);
        return abonoMapper.toDto(abono);
    }

    @Override
    public Optional<AbonoDTO> partialUpdate(AbonoDTO abonoDTO) {
        log.debug("Request to partially update Abono : {}", abonoDTO);

        return abonoRepository
            .findById(abonoDTO.getId())
            .map(existingAbono -> {
                abonoMapper.partialUpdate(existingAbono, abonoDTO);

                return existingAbono;
            })
            .map(abonoRepository::save)
            .map(abonoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AbonoDTO> findAll() {
        log.debug("Request to get all Abonos");
        return abonoRepository.findAll().stream().map(abonoMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AbonoDTO> findOne(Long id) {
        log.debug("Request to get Abono : {}", id);
        return abonoRepository.findById(id).map(abonoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Abono : {}", id);
        abonoRepository.deleteById(id);
    }
}
