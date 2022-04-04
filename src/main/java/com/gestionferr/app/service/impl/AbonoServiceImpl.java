package com.gestionferr.app.service.impl;

import com.gestionferr.app.config.Constants;
import com.gestionferr.app.domain.Abono;
import com.gestionferr.app.domain.FacturaCompra;
import com.gestionferr.app.domain.FacturaVenta;
import com.gestionferr.app.repository.AbonoRepository;
import com.gestionferr.app.repository.FacturaCompraRepository;
import com.gestionferr.app.repository.FacturaVentaRepository;
import com.gestionferr.app.service.AbonoService;
import com.gestionferr.app.service.dto.AbonoDTO;
import com.gestionferr.app.service.mapper.AbonoMapper;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
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
 * Service Implementation for managing {@link Abono}.
 */
@Service
@Transactional
public class AbonoServiceImpl implements AbonoService {

    private final Logger log = LoggerFactory.getLogger(AbonoServiceImpl.class);

    private final AbonoRepository abonoRepository;

    private final AbonoMapper abonoMapper;

    private final FacturaVentaRepository facturaVentaRepository;

    private final FacturaCompraRepository facturaCompraRepositoty;

    @PersistenceContext
    private EntityManager entityManager;

    public AbonoServiceImpl(
        FacturaVentaRepository facturaVentaRepository,
        AbonoRepository abonoRepository,
        AbonoMapper abonoMapper,
        FacturaCompraRepository facturaCompraRepositoty
    ) {
        this.abonoRepository = abonoRepository;
        this.abonoMapper = abonoMapper;
        this.facturaVentaRepository = facturaVentaRepository;
        this.facturaCompraRepositoty = facturaCompraRepositoty;
    }

    @Override
    public AbonoDTO save(AbonoDTO abonoDTO) {
        log.debug("Request to save Abono : {}", abonoDTO);
        Abono abono = abonoMapper.toEntity(abonoDTO);
        abono.setFechaCreacion(Instant.now());

        if (abono.getTipoFactura().equalsIgnoreCase("Factura Venta")) {
            FacturaVenta facturaVenta = facturaVentaRepository.facturaVentaPorId(abono.getIdFactura());
            facturaVenta.setValorDeuda(facturaVenta.getValorDeuda().subtract(abono.getValorAbono()));
            int deuda = facturaVenta.getValorDeuda().intValue();
            if (deuda == 0) {
                facturaVenta.setEstado("PAGADA");
            } else {
                facturaVenta.setEstado("DEUDA");
            }
        }

        if (abono.getTipoFactura().equalsIgnoreCase("Factura Compra")) {
            FacturaCompra facturaCompra = facturaCompraRepositoty.facuraPorId(abono.getIdFactura());
            facturaCompra.setValorDeuda(facturaCompra.getValorDeuda().subtract(abono.getValorAbono()));
            int valorDeuda = facturaCompra.getValorDeuda().intValue();

            if (valorDeuda == 0) {
                facturaCompra.setEstado("PAGADA");
            } else {
                facturaCompra.setEstado("DEUDA");
            }
        }

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
    public List<AbonoDTO> abonosPorFactura(Long id, Long codigo) {
        log.debug("Request to get all abonos per facturaVenta");

        List<Abono> abonosPorFactura = null;

        if (codigo == 1) {
            Query q = entityManager.createQuery(Constants.CONSULTAR_ABONOS_POR_FACTURAVENTA).setParameter("idFactura", id);

            abonosPorFactura = q.getResultList();
        }

        if (codigo == 2) {
            Query q = entityManager.createQuery(Constants.CONSULTAR_ABONO_POR_FACTURA_COMPRA).setParameter("idFactura", id);

            abonosPorFactura = q.getResultList();
        }

        return abonoMapper.toDto(abonosPorFactura);
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
