package com.gestionferr.app.service.impl;

import com.gestionferr.app.config.Constants;
import com.gestionferr.app.domain.Proveedor;
import com.gestionferr.app.repository.ProveedorRepository;
import com.gestionferr.app.service.ProveedorService;
import com.gestionferr.app.service.dto.ItemPorFacturaCompra;
import com.gestionferr.app.service.dto.ProveedorDTO;
import com.gestionferr.app.service.mapper.ProveedorMapper;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
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
 * Service Implementation for managing {@link Proveedor}.
 */
@Service
@Transactional
public class ProveedorServiceImpl implements ProveedorService {

    private final Logger log = LoggerFactory.getLogger(ProveedorServiceImpl.class);

    private final ProveedorRepository proveedorRepository;

    private final ProveedorMapper proveedorMapper;

    @PersistenceContext
    private EntityManager entityManager;

    public ProveedorServiceImpl(ProveedorRepository proveedorRepository, ProveedorMapper proveedorMapper) {
        this.proveedorRepository = proveedorRepository;
        this.proveedorMapper = proveedorMapper;
    }

    @Override
    public ProveedorDTO save(ProveedorDTO proveedorDTO) {
        log.debug("Request to save Proveedor : {}", proveedorDTO);
        Proveedor proveedor = proveedorMapper.toEntity(proveedorDTO);
        proveedor = proveedorRepository.save(proveedor);
        return proveedorMapper.toDto(proveedor);
    }

    @Override
    public Optional<ProveedorDTO> partialUpdate(ProveedorDTO proveedorDTO) {
        log.debug("Request to partially update Proveedor : {}", proveedorDTO);

        return proveedorRepository
            .findById(proveedorDTO.getId())
            .map(existingProveedor -> {
                proveedorMapper.partialUpdate(existingProveedor, proveedorDTO);

                return existingProveedor;
            })
            .map(proveedorRepository::save)
            .map(proveedorMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProveedorDTO> findAll() {
        log.debug("Request to get all Proveedors");
        return proveedorRepository.findAll().stream().map(proveedorMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public ProveedorDTO findOne(Long id) throws ParseException {
        log.debug("Request to get Proveedor : {}", id);

        Proveedor proveedor = proveedorRepository.proveedorPorId(id);
        proveedor.setFacturasProovedor(facturasProovedor(id));

        return proveedorMapper.toDto(proveedor);
    }

    private List<ItemPorFacturaCompra> facturasProovedor(Long id) throws ParseException {
        List<Object[]> datosFacturaProveedor = proveedorRepository.facturasProveedor(id);
        List<ItemPorFacturaCompra> facturasProveedor = new ArrayList<>();
        ItemPorFacturaCompra facturaCompra = null;

        for (Object[] dato : datosFacturaProveedor) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Instant fecha = format.parse(dato[1].toString().substring(0, dato[1].toString().indexOf("T"))).toInstant();
            Date fechaFormat = Date.from(fecha);
            String fechaFormatString = format.format(fechaFormat);
            facturaCompra = new ItemPorFacturaCompra();
            facturaCompra.setNumeroFactura(dato[0].toString());
            facturaCompra.setFecha(fechaFormatString);
            facturaCompra.setValorFactura(new BigDecimal(dato[2].toString()));
            facturaCompra.setValorDeuda(new BigDecimal(dato[3].toString()));
            facturasProveedor.add(facturaCompra);
        }

        return facturasProveedor;
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Proveedor : {}", id);
        proveedorRepository.deleteById(id);
    }

    @Override
    public List<ProveedorDTO> proveedorFiltros(ProveedorDTO proveedor) {
        log.debug("Request to get proveedores per filters");

        StringBuilder sb = new StringBuilder();
        Map<String, Object> filtros = new HashMap<String, Object>();

        //PROVEEDORES BASE
        sb.append(Constants.PROVEEDOR_BASE);

        if (proveedor.getNombre() != null && !proveedor.getNombre().isEmpty()) {
            sb.append(Constants.PROVEEDOR_NOMBRE);
            filtros.put("nombre", "%" + proveedor.getNombre().toUpperCase() + "%");
        }

        if (proveedor.getNumeroContacto() != null && !proveedor.getNumeroContacto().isEmpty()) {
            sb.append(Constants.PROVEEDOR_NUMERO_CONTACTO);
            filtros.put("numeroContacto", "%" + proveedor.getNumeroContacto().toUpperCase() + "%");
        }

        if (proveedor.getNumeroCC() != null && !proveedor.getNumeroCC().isEmpty()) {
            sb.append(Constants.PROVEEDOR_NUMERO_IDENT);
            filtros.put("numeroIdent", "%" + proveedor.getNumeroCC().toUpperCase() + "%");
        }

        if (proveedor.getTipoProveedor() != null) {
            sb.append(Constants.PROOVEDOR_TIPO_PROVEEDOR);
            filtros.put("tipoProveedor", proveedor.getTipoProveedor());
        }

        Query q = entityManager.createQuery(sb.toString());
        for (Map.Entry<String, Object> filtro : filtros.entrySet()) {
            q.setParameter(filtro.getKey(), filtro.getValue());
        }

        List<Proveedor> proveedores = q.getResultList();

        return proveedores.stream().map(proveedorMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }
}
