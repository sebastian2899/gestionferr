package com.gestionferr.app.service.impl;

import com.gestionferr.app.domain.FacturaCompra;
import com.gestionferr.app.repository.FacturaCompraRepository;
import com.gestionferr.app.service.FacturaCompraService;
import com.gestionferr.app.service.dto.FacturaCompraDTO;
import com.gestionferr.app.service.mapper.FacturaCompraMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link FacturaCompra}.
 */
@Service
@Transactional
public class FacturaCompraServiceImpl implements FacturaCompraService {

    private final Logger log = LoggerFactory.getLogger(FacturaCompraServiceImpl.class);

    private final FacturaCompraRepository facturaCompraRepository;

    private final FacturaCompraMapper facturaCompraMapper;

    public FacturaCompraServiceImpl(FacturaCompraRepository facturaCompraRepository, FacturaCompraMapper facturaCompraMapper) {
        this.facturaCompraRepository = facturaCompraRepository;
        this.facturaCompraMapper = facturaCompraMapper;
    }

    @Override
    public FacturaCompraDTO save(FacturaCompraDTO facturaCompraDTO) {
        log.debug("Request to save FacturaCompra : {}", facturaCompraDTO);
        FacturaCompra facturaCompra = facturaCompraMapper.toEntity(facturaCompraDTO);
        facturaCompra = facturaCompraRepository.save(facturaCompra);
        return facturaCompraMapper.toDto(facturaCompra);
    }

    @Override
    public Optional<FacturaCompraDTO> partialUpdate(FacturaCompraDTO facturaCompraDTO) {
        log.debug("Request to partially update FacturaCompra : {}", facturaCompraDTO);

        return facturaCompraRepository
            .findById(facturaCompraDTO.getId())
            .map(existingFacturaCompra -> {
                facturaCompraMapper.partialUpdate(existingFacturaCompra, facturaCompraDTO);

                return existingFacturaCompra;
            })
            .map(facturaCompraRepository::save)
            .map(facturaCompraMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FacturaCompraDTO> findAll() {
        log.debug("Request to get all FacturaCompras");
        return facturaCompraRepository.findAll().stream().map(facturaCompraMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FacturaCompraDTO> findOne(Long id) {
        log.debug("Request to get FacturaCompra : {}", id);
        return facturaCompraRepository.findById(id).map(facturaCompraMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete FacturaCompra : {}", id);
        facturaCompraRepository.deleteById(id);
    }
}
