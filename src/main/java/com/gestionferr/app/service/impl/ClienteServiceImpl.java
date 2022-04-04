package com.gestionferr.app.service.impl;

import com.gestionferr.app.config.Constants;
import com.gestionferr.app.domain.Cliente;
import com.gestionferr.app.domain.FacturaVenta;
import com.gestionferr.app.repository.ClienteRepository;
import com.gestionferr.app.service.ClienteService;
import com.gestionferr.app.service.dto.ClienteDTO;
import com.gestionferr.app.service.mapper.ClienteMapper;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Cliente}.
 */
@Service
@Transactional
public class ClienteServiceImpl implements ClienteService {

    private final Logger log = LoggerFactory.getLogger(ClienteServiceImpl.class);

    private final ClienteRepository clienteRepository;

    private final ClienteMapper clienteMapper;

    public ClienteServiceImpl(ClienteRepository clienteRepository, ClienteMapper clienteMapper) {
        this.clienteRepository = clienteRepository;
        this.clienteMapper = clienteMapper;
    }

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public ClienteDTO save(ClienteDTO clienteDTO) {
        log.debug("Request to save Cliente : {}", clienteDTO);
        Cliente cliente = clienteMapper.toEntity(clienteDTO);
        cliente = clienteRepository.save(cliente);
        return clienteMapper.toDto(cliente);
    }

    @Override
    public Optional<ClienteDTO> partialUpdate(ClienteDTO clienteDTO) {
        log.debug("Request to partially update Cliente : {}", clienteDTO);

        return clienteRepository
            .findById(clienteDTO.getId())
            .map(existingCliente -> {
                clienteMapper.partialUpdate(existingCliente, clienteDTO);

                return existingCliente;
            })
            .map(clienteRepository::save)
            .map(clienteMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClienteDTO> findAll() {
        log.debug("Request to get all Clientes");
        return clienteRepository.findAll().stream().map(clienteMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public ClienteDTO findOne(Long id) {
        log.debug("Request to get Cliente : {}", id);
        Cliente cliente = clienteRepository.clientePorId(id);
        cliente.setFacturasCliente(facturasPorCliente(id));

        return clienteMapper.toDto(cliente);
    }

    private List<FacturaVenta> facturasPorCliente(Long id) {
        List<FacturaVenta> facturasCliente = clienteRepository.facturasPorCliente(id);

        return facturasCliente;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClienteDTO> clientesFiltro(ClienteDTO cliente) {
        log.debug("Request to get clientes per filter");

        StringBuilder sb = new StringBuilder();
        Map<String, Object> filtros = new HashMap<String, Object>();

        sb.append(Constants.CLIENTE_BASE);

        if (cliente.getNombre() != null && !cliente.getNombre().isEmpty()) {
            sb.append(Constants.CLIENTE_FILTRO_NOMBRE);
            filtros.put("nombre", "%" + cliente.getNombre().toUpperCase() + "%");
        }

        if (cliente.getNumeroCC() != null && !cliente.getNumeroCC().isEmpty()) {
            sb.append(Constants.CLIENTE_FILTRO_NUMCC);
            filtros.put("numeroCC", "%" + cliente.getNumeroCC().toUpperCase() + "%");
        }

        Query q = entityManager.createQuery(sb.toString());
        for (Map.Entry<String, Object> filtro : filtros.entrySet()) {
            q.setParameter(filtro.getKey(), filtro.getValue());
        }

        List<Cliente> clientes = q.getResultList();

        return clientes.stream().map(clienteMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Cliente : {}", id);
        clienteRepository.deleteById(id);
    }
}
