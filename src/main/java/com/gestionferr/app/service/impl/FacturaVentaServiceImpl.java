package com.gestionferr.app.service.impl;

import com.gestionferr.app.config.Constants;
import com.gestionferr.app.domain.FacturaVenta;
import com.gestionferr.app.domain.ItemFacturaVenta;
import com.gestionferr.app.repository.FacturaVentaRepository;
import com.gestionferr.app.repository.ItemFacturaVentaRepository;
import com.gestionferr.app.service.FacturaVentaService;
import com.gestionferr.app.service.dto.FacturaVentaDTO;
import com.gestionferr.app.service.mapper.FacturaVentaMapper;
import java.math.BigDecimal;
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
 * Service Implementation for managing {@link FacturaVenta}.
 */
@Service
@Transactional
public class FacturaVentaServiceImpl implements FacturaVentaService {

    private final Logger log = LoggerFactory.getLogger(FacturaVentaServiceImpl.class);

    private final FacturaVentaRepository facturaVentaRepository;

    private final FacturaVentaMapper facturaVentaMapper;

    private final ItemFacturaVentaRepository itemFacturaVentaRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public FacturaVentaServiceImpl(
        ItemFacturaVentaRepository itemFacturaVentaRepository,
        FacturaVentaRepository facturaVentaRepository,
        FacturaVentaMapper facturaVentaMapper
    ) {
        this.facturaVentaRepository = facturaVentaRepository;
        this.facturaVentaMapper = facturaVentaMapper;
        this.itemFacturaVentaRepository = itemFacturaVentaRepository;
    }

    @Override
    public FacturaVentaDTO save(FacturaVentaDTO facturaVentaDTO) {
        log.debug("Request to save FacturaVenta : {}", facturaVentaDTO);
        FacturaVenta facturaVenta = facturaVentaMapper.toEntity(facturaVentaDTO);
        facturaVenta.setFechaCreacion(Instant.now());

        ItemFacturaVenta itemFacturaVenta = null;

        if (facturaVentaDTO.getIdCliente() != null) {
            facturaVenta.setInfoCliente(facturaVentaRepository.nombreCliente(facturaVenta.getIdCliente()));
        }

        facturaVenta = facturaVentaRepository.save(facturaVenta);

        if (facturaVenta.getItemsPorFactura() != null) {
            for (ItemFacturaVenta item : facturaVenta.getItemsPorFactura()) {
                itemFacturaVenta = new ItemFacturaVenta();
                itemFacturaVenta.setCantidad(item.getCantidad());
                itemFacturaVenta.setPrecio(item.getPrecio());
                itemFacturaVenta.setIdProducto(item.getIdProducto());
                itemFacturaVenta.setIdFacturaVenta(facturaVenta.getId());
                itemFacturaVentaRepository.save(itemFacturaVenta);
                BigDecimal cantidad = new BigDecimal(item.getCantidad());

                Query q = entityManager
                    .createQuery(Constants.RESTAR_PRODUCTOS_SELECCIONADOS)
                    .setParameter("cantidad", cantidad)
                    .setParameter("id", item.getIdProducto());
                q.executeUpdate();
            }
        }

        return facturaVentaMapper.toDto(facturaVenta);
    }

    @Override
    public Optional<FacturaVentaDTO> partialUpdate(FacturaVentaDTO facturaVentaDTO) {
        log.debug("Request to partially update FacturaVenta : {}", facturaVentaDTO);

        return facturaVentaRepository
            .findById(facturaVentaDTO.getId())
            .map(existingFacturaVenta -> {
                facturaVentaMapper.partialUpdate(existingFacturaVenta, facturaVentaDTO);

                return existingFacturaVenta;
            })
            .map(facturaVentaRepository::save)
            .map(facturaVentaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FacturaVentaDTO> findAll() {
        log.debug("Request to get all FacturaVentas");
        return facturaVentaRepository.findAll().stream().map(facturaVentaMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public FacturaVentaDTO facturaVentaPorId(Long id) {
        log.debug("Request to get FacturaVenta : {}", id);

        FacturaVenta factura = facturaVentaRepository.facturaVentaPorId(id);

        factura.setItemsPorFactura(consultarItemsFacturaVenta(id));

        return facturaVentaMapper.toDto(factura);
    }

    private List<ItemFacturaVenta> consultarItemsFacturaVenta(Long id) {
        List<ItemFacturaVenta> itemsFacturaVenta = facturaVentaRepository.allItemsFacturaVenta(id);

        /* LOGICA PARA CONSULTAR E INSERTAR LOS DATOS DE LOS ITEMS DE LA FACTURA MEDIANTE UNA LISTA OBJECT
         * List<Object[]>itemsFacturaTemp = facturaVentaRepository.itemsFacturaVenta(id);
         * Producto producto  = null;
         * ItemFacturaVenta itemVenta = null; for(Object[] item: itemsFacturaTemp) {
         * itemVenta = new ItemFacturaVenta(); producto =
         * facturaVentaRepository.productoPorId(Long.parseLong(item[0].toString()));
         * itemVenta.setNombreProducto(producto.getNombre());
         * itemVenta.setCantidad(Long.parseLong(item[1].toString()));
         * itemVenta.setPrecio(producto.getPrecio().multiply(new
         * BigDecimal(Long.parseLong(item[1].toString()))));
         * itemsFacturaVenta.add(itemVenta); }
         */

        for (ItemFacturaVenta item : itemsFacturaVenta) {
            item.setNombreProducto(consultarNombrePrducto(item.getIdProducto()));
        }

        return itemsFacturaVenta;
    }

    private String consultarNombrePrducto(Long id) {
        return facturaVentaRepository.nombreProducto(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete FacturaVenta : {}", id);
        facturaVentaRepository.deleteById(id);
    }
}
