package com.gestionferr.app.service.impl;

import com.gestionferr.app.domain.ItemFacturaCompra;
import com.gestionferr.app.repository.ItemFacturaCompraRepository;
import com.gestionferr.app.service.ItemFacturaCompraService;
import com.gestionferr.app.service.dto.ItemFacturaCompraDTO;
import com.gestionferr.app.service.mapper.ItemFacturaCompraMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ItemFacturaCompra}.
 */
@Service
@Transactional
public class ItemFacturaCompraServiceImpl implements ItemFacturaCompraService {

    private final Logger log = LoggerFactory.getLogger(ItemFacturaCompraServiceImpl.class);

    private final ItemFacturaCompraRepository itemFacturaCompraRepository;

    private final ItemFacturaCompraMapper itemFacturaCompraMapper;

    public ItemFacturaCompraServiceImpl(
        ItemFacturaCompraRepository itemFacturaCompraRepository,
        ItemFacturaCompraMapper itemFacturaCompraMapper
    ) {
        this.itemFacturaCompraRepository = itemFacturaCompraRepository;
        this.itemFacturaCompraMapper = itemFacturaCompraMapper;
    }

    @Override
    public ItemFacturaCompraDTO save(ItemFacturaCompraDTO itemFacturaCompraDTO) {
        log.debug("Request to save ItemFacturaCompra : {}", itemFacturaCompraDTO);
        ItemFacturaCompra itemFacturaCompra = itemFacturaCompraMapper.toEntity(itemFacturaCompraDTO);
        itemFacturaCompra = itemFacturaCompraRepository.save(itemFacturaCompra);
        return itemFacturaCompraMapper.toDto(itemFacturaCompra);
    }

    @Override
    public Optional<ItemFacturaCompraDTO> partialUpdate(ItemFacturaCompraDTO itemFacturaCompraDTO) {
        log.debug("Request to partially update ItemFacturaCompra : {}", itemFacturaCompraDTO);

        return itemFacturaCompraRepository
            .findById(itemFacturaCompraDTO.getId())
            .map(existingItemFacturaCompra -> {
                itemFacturaCompraMapper.partialUpdate(existingItemFacturaCompra, itemFacturaCompraDTO);

                return existingItemFacturaCompra;
            })
            .map(itemFacturaCompraRepository::save)
            .map(itemFacturaCompraMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemFacturaCompraDTO> findAll() {
        log.debug("Request to get all ItemFacturaCompras");
        return itemFacturaCompraRepository
            .findAll()
            .stream()
            .map(itemFacturaCompraMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ItemFacturaCompraDTO> findOne(Long id) {
        log.debug("Request to get ItemFacturaCompra : {}", id);
        return itemFacturaCompraRepository.findById(id).map(itemFacturaCompraMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ItemFacturaCompra : {}", id);
        itemFacturaCompraRepository.deleteById(id);
    }
}
