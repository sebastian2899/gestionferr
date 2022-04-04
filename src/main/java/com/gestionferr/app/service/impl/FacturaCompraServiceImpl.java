package com.gestionferr.app.service.impl;

import com.gestionferr.app.config.Constants;
import com.gestionferr.app.domain.FacturaCompra;
import com.gestionferr.app.domain.ItemFacturaCompra;
import com.gestionferr.app.repository.FacturaCompraRepository;
import com.gestionferr.app.repository.ItemFacturaCompraRepository;
import com.gestionferr.app.service.FacturaCompraService;
import com.gestionferr.app.service.dto.FacturaCompraDTO;
import com.gestionferr.app.service.dto.RegistroFacturaCompraDTO;
import com.gestionferr.app.service.mapper.FacturaCompraMapper;
import java.math.BigDecimal;
import java.time.Instant;
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
 * Service Implementation for managing {@link FacturaCompra}.
 */
@Service
@Transactional
public class FacturaCompraServiceImpl implements FacturaCompraService {

    private final Logger log = LoggerFactory.getLogger(FacturaCompraServiceImpl.class);

    private final FacturaCompraRepository facturaCompraRepository;

    private final FacturaCompraMapper facturaCompraMapper;

    private final ItemFacturaCompraRepository itemFacturaCompraRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public FacturaCompraServiceImpl(
        ItemFacturaCompraRepository itemFacturaCompraRepository,
        FacturaCompraRepository facturaCompraRepository,
        FacturaCompraMapper facturaCompraMapper
    ) {
        this.facturaCompraRepository = facturaCompraRepository;
        this.facturaCompraMapper = facturaCompraMapper;
        this.itemFacturaCompraRepository = itemFacturaCompraRepository;
    }

    @Override
    public FacturaCompraDTO save(FacturaCompraDTO facturaCompraDTO) {
        log.debug("Request to save FacturaCompra : {}", facturaCompraDTO);
        FacturaCompra facturaCompra = facturaCompraMapper.toEntity(facturaCompraDTO);

        facturaCompra.setFechaCreacion(Instant.now());

        facturaCompra = facturaCompraRepository.save(facturaCompra);

        if (facturaCompraDTO.getIdProovedor() != null) {
            String nombreProovedor = facturaCompraRepository.nombreProovedroPorId(facturaCompraDTO.getIdProovedor());
            facturaCompra.setInfoCliente(nombreProovedor);
        }

        if (facturaCompra.getValorPagado() == null) {
            facturaCompra.setValorPagado(BigDecimal.ZERO);
        }

        if (facturaCompra.getItemsFacturaCompra() != null && facturaCompra.getItemsFacturaCompra().size() > 0) {
            ItemFacturaCompra itemFacturaCompra = null;

            for (ItemFacturaCompra item : facturaCompra.getItemsFacturaCompra()) {
                itemFacturaCompra = new ItemFacturaCompra();
                itemFacturaCompra.setIdFacturaCompra(facturaCompra.getId());
                itemFacturaCompra.setIdProducto(item.getIdProducto());
                itemFacturaCompra.setCantidad(item.getCantidad());
                itemFacturaCompra.setPrecio(item.getPrecio());

                itemFacturaCompraRepository.save(itemFacturaCompra);

                Query q = entityManager
                    .createQuery(Constants.SUMAR_PRODUCTOS_FACTURA_REVERTIDA)
                    .setParameter("cantidad", BigDecimal.valueOf(item.getCantidad()))
                    .setParameter("id", item.getIdProducto());

                q.executeUpdate();
            }
        }

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

        Query q = entityManager.createQuery(Constants.FACTURA_COMPRA_ORDER_BY);

        List<FacturaCompra> facturasOrdenadas = q.getResultList();

        return facturasOrdenadas.stream().map(facturaCompraMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public FacturaCompraDTO findOne(Long id) {
        log.debug("Request to get FacturaCompra : {}", id);
        FacturaCompra facturaPorId = facturaCompraRepository.facuraPorId(id);
        facturaPorId.setItemsFacturaCompra(itemsFacturaCompraPorFactura(id));

        return facturaCompraMapper.toDto(facturaPorId);
    }

    /*
     * @Scheduled(cron = "* * * * * *") public void cambiarEstadoFacturaCompra() {
     *
     * List<FacturaCompra>facturasCompra = facturaCompraRepository.findAll();
     *
     * for(FacturaCompra factura: facturasCompra) { int valorDeuda =
     * factura.getValorDeuda().intValue(); if(valorDeuda == 0 ) {
     * factura.setEstado("PAGADA"); }else { factura.setEstado("DEUDA"); } }
     *
     * }
     */
    private List<ItemFacturaCompra> itemsFacturaCompraPorFactura(Long id) {
        List<ItemFacturaCompra> itemsFactura = facturaCompraRepository.itemsPorFactura(id);

        for (ItemFacturaCompra item : itemsFactura) {
            String nombreProducto = facturaCompraRepository.nombreProductoPorId(item.getIdProducto());
            item.setNombreProducto(nombreProducto);
        }

        return itemsFactura;
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete FacturaCompra : {}", id);

        List<ItemFacturaCompra> ItemsfacturaCompraId = facturaCompraRepository.itemsPorFactura(id);

        for (ItemFacturaCompra item : ItemsfacturaCompraId) {
            Query q = entityManager
                .createQuery(Constants.RESTAR_PRODUCTOS_SELECCIONADOS)
                .setParameter("cantidad", BigDecimal.valueOf(item.getCantidad()))
                .setParameter("id", item.getIdProducto());

            q.executeUpdate();
        }

        //ELIMINAMOS TODOS LOS ITEMS RELACIONADOS A ESTA FACTURA.
        Query itemQuery = entityManager.createQuery(Constants.ELIMINAR_ITEMS_FACTURA_COMPRA).setParameter("idFactura", id);
        itemQuery.executeUpdate();

        facturaCompraRepository.deleteById(id);
    }

    @Override
    public List<FacturaCompraDTO> facturasCompraFiltro(FacturaCompraDTO facturaCompra) {
        log.debug("Request to get all facturas per filters");

        StringBuilder sb = new StringBuilder();
        Map<String, Object> filtros = new HashMap<String, Object>();

        sb.append(Constants.FACTURA_COMPRA_BASE);

        if (facturaCompra.getNumeroFactura() != null && !facturaCompra.getNumeroFactura().isEmpty()) {
            sb.append(Constants.FACTURA_COMPRA_NUMERO_FACTURA);
            filtros.put("numeroFactura", "%" + facturaCompra.getNumeroFactura() + "%");
        }

        if (facturaCompra.getInfoCliente() != null && !facturaCompra.getInfoCliente().isEmpty()) {
            sb.append(Constants.FACTURA_COMPRA_INFO_PROVEEDOR);
            filtros.put("infoProveedor", "%" + facturaCompra.getInfoCliente().toUpperCase() + "%");
        }

        if (facturaCompra.getEstado() != null && !facturaCompra.getEstado().isEmpty()) {
            sb.append(Constants.FACTURA_COMPRA__ESTADO);
            filtros.put("estado", "%" + facturaCompra.getEstado().toUpperCase() + "%");
        }

        Query q = entityManager.createQuery(sb.toString());
        for (Map.Entry<String, Object> filtro : filtros.entrySet()) {
            q.setParameter(filtro.getKey(), filtro.getValue());
        }

        List<FacturaCompra> facturasCompras = q.getResultList();

        return facturasCompras.stream().map(facturaCompraMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public RegistroFacturaCompraDTO valoresFacturaCompraMes(String fechaInicio, String fechaFin) {
        log.debug("Request to get values of facturaCompra per dates");

        String fechaIni = fechaInicio.substring(0, fechaInicio.indexOf("T"));
        String fechaFn = fechaFin.substring(0, fechaFin.indexOf("T"));

        RegistroFacturaCompraDTO registroHistoricoFactura = new RegistroFacturaCompraDTO();

        List<Object[]> valoresFacturaMes = facturaCompraRepository.valoresFacturaMes(fechaIni, fechaFn);

        BigDecimal valorFactura = BigDecimal.ZERO;
        BigDecimal valorPagado = BigDecimal.ZERO;
        BigDecimal valorDeuda = BigDecimal.ZERO;

        if (valoresFacturaMes != null && valoresFacturaMes.size() > 0) {
            for (Object[] dato : valoresFacturaMes) {
                valorFactura = new BigDecimal(dato[0].toString());
                valorPagado = new BigDecimal(dato[1].toString());
                valorDeuda = new BigDecimal(dato[2].toString());
            }

            registroHistoricoFactura.setValorFactura(valorFactura);
            registroHistoricoFactura.setValorPagado(valorPagado);
            registroHistoricoFactura.setValorDeuda(valorDeuda);
        }

        return registroHistoricoFactura;
    }

    @Override
    public List<FacturaCompraDTO> facturaPorFecha(String fecha) {
        log.debug("Request to get all facturaCompra per date");

        String fechaFormat = fecha.substring(0, fecha.indexOf("T"));

        Query q = entityManager.createQuery(Constants.FACTURA_COMPRA_FECHA).setParameter("fecha", fechaFormat);

        List<FacturaCompra> facturaCompras = q.getResultList();

        return facturaCompras.stream().map(facturaCompraMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public Boolean validarNumeroFacturaSave(String numeroFactura) {
        log.debug("Request to validated save factura compra");

        int resultado = facturaCompraRepository.validarNumeroFactura(numeroFactura);
        Boolean resp = false;

        if (resultado >= 1) {
            resp = true;
        }

        return resp;
    }
}
