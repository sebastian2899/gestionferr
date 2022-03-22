package com.gestionferr.app.service.impl;

import com.gestionferr.app.config.Constants;
import com.gestionferr.app.domain.Abono;
import com.gestionferr.app.domain.FacturaVenta;
import com.gestionferr.app.domain.ItemFacturaVenta;
import com.gestionferr.app.repository.AbonoRepository;
import com.gestionferr.app.repository.FacturaVentaRepository;
import com.gestionferr.app.repository.ItemFacturaVentaRepository;
import com.gestionferr.app.service.FacturaVentaService;
import com.gestionferr.app.service.dto.FacturaVentaDTO;
import com.gestionferr.app.service.dto.FacturaVentasFechaDTO;
import com.gestionferr.app.service.mapper.FacturaVentaMapper;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;
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
import org.apache.commons.io.FileUtils;
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

    private final AbonoRepository abonoRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public FacturaVentaServiceImpl(
        AbonoRepository abonoRepository,
        ItemFacturaVentaRepository itemFacturaVentaRepository,
        FacturaVentaRepository facturaVentaRepository,
        FacturaVentaMapper facturaVentaMapper
    ) {
        this.facturaVentaRepository = facturaVentaRepository;
        this.facturaVentaMapper = facturaVentaMapper;
        this.itemFacturaVentaRepository = itemFacturaVentaRepository;
        this.abonoRepository = abonoRepository;
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
        return facturaVentaRepository
            .facturasOrdenadas()
            .stream()
            .map(facturaVentaMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public FacturaVentaDTO facturaVentaPorId(Long id) {
        log.debug("Request to get FacturaVenta : {}", id);

        FacturaVenta factura = facturaVentaRepository.facturaVentaPorId(id);

        factura.setItemsPorFactura(consultarItemsFacturaVenta(id));

        return facturaVentaMapper.toDto(factura);
    }

    @Override
    @Transactional
    public List<FacturaVentaDTO> facturasPorFiltro(FacturaVentaDTO facturaVentaDTO) {
        log.debug("Request to get facturas per filtro");

        StringBuilder sb = new StringBuilder();
        Map<String, Object> filtros = new HashMap<String, Object>();

        List<FacturaVentaDTO> facturasPorFiltro = null;

        sb.append(Constants.FACTURA_FILTRO_GENERAL);

        if (facturaVentaDTO.getNumeroFactura() != null && !facturaVentaDTO.getNumeroFactura().isEmpty()) {
            sb.append(Constants.FACTURA_FILTRO_NUMERO_FACTURA);
            filtros.put("numeroFactura", "%" + facturaVentaDTO.getNumeroFactura() + "%");
        }

        if (facturaVentaDTO.getInfoCliente() != null && !facturaVentaDTO.getInfoCliente().isEmpty()) {
            sb.append(Constants.FACTURA_FILTRO_NOMBRE_CLIENTE);
            filtros.put("nombreCliente", "%" + facturaVentaDTO.getInfoCliente().toUpperCase() + "%");
        }

        if (facturaVentaDTO.getEstado() != null && !facturaVentaDTO.getEstado().isEmpty()) {
            sb.append(Constants.FACTURA_FILTRO_ESTADO_FACTURA);
            filtros.put("estado", facturaVentaDTO.getEstado().toUpperCase());
        }

        Query q = entityManager.createQuery(sb.toString());

        for (Map.Entry<String, Object> filtro : filtros.entrySet()) {
            q.setParameter(filtro.getKey(), filtro.getValue());
        }

        return facturasPorFiltro = q.getResultList();
    }

    @Override
    public List<FacturaVentaDTO> facturasPorFecha(String fecha) {
        log.debug("Request to get all facturas per date filters");

        String fechaFormat = fecha.substring(0, fecha.indexOf("T"));

        Query q = entityManager.createQuery(Constants.FACTURA_FILTRO_FECHA_FACTURA).setParameter("fecha", fechaFormat);
        log.debug("date: " + fechaFormat);
        List<FacturaVenta> facturasPorFecha = q.getResultList();

        return facturasPorFecha.stream().map(facturaVentaMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
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
    public FacturaVentasFechaDTO valoresFacturaFecha(Instant fechaInicio, Instant fechaFin) {
        log.debug("Request to ger values of dates");

        Query q = entityManager
            .createQuery(Constants.CONSULTAR_VALORES_FACTURA_MENSUAL)
            .setParameter("fechaInicio", fechaInicio)
            .setParameter("fechaFin", fechaFin);

        FacturaVentasFechaDTO valoresFacturaFechas = new FacturaVentasFechaDTO();
        List<Object[]> valoresFacturaFecha = q.getResultList();

        for (Object[] valor : valoresFacturaFecha) {
            valoresFacturaFechas.setValorTotal((BigDecimal) valor[0]);
            valoresFacturaFechas.setValorPagado((BigDecimal) valor[1]);
            valoresFacturaFechas.setDiferencia((BigDecimal) valor[2]);
        }

        return valoresFacturaFechas;
    }

    @Override
    public byte[] generarReporteFacturaVentasMensual() {
        log.debug("Request to generate monthly resport of factura ventas");

        Document document = null;
        //PdfDocument docment = null;
        String nombreDocumento = null;

        Calendar fechaInicio = Calendar.getInstance();
        Calendar fechaFin = Calendar.getInstance();

        fechaInicio.add(Calendar.MONTH, -1);

        Instant fechaIn = fechaInicio.toInstant();
        Instant fechaFn = fechaFin.toInstant();

        Query q = entityManager
            .createQuery(Constants.CONSULTAR_FACUTRAS_POR_FECHAS)
            .setParameter("fechaInicio", fechaIn)
            .setParameter("fechaFin", fechaFn);

        List<FacturaVenta> facturasPorFechas = q.getResultList();

        try {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            document = new Document();
            nombreDocumento = "Reporte Facturas Mensual" + ".pdf";
            FileOutputStream fileOutputStream = new FileOutputStream(nombreDocumento);

            PdfWriter.getInstance(document, fileOutputStream);
            document.open();

            Date fechaI = Date.from(fechaIn);
            Date fechaF = Date.from(fechaFn);

            String fecha1 = format.format(fechaI);
            String fecha2 = format.format(fechaF);

            Paragraph titulo = new Paragraph("Reporte Mensual Factura Ventas");
            titulo.setAlignment(1);
            document.add(titulo);

            document.add(Chunk.NEWLINE);

            Paragraph fIni = new Paragraph(fecha1);
            Paragraph fFn = new Paragraph(fecha2);

            fIni.setAlignment(2);
            fFn.setAlignment(2);

            document.add(fIni);
            document.add(fFn);

            document.add(Chunk.NEWLINE);
            for (FacturaVenta factura : facturasPorFechas) {
                PdfPTable table = new PdfPTable(6);
                table.setWidthPercentage(100);
                Date fechaFactura = Date.from(factura.getFechaCreacion());
                String fecha = format.format(fechaFactura);

                PdfPCell valorFactura = new PdfPCell(new Phrase("Valor Factura"));
                valorFactura.setBackgroundColor(BaseColor.ORANGE);
                PdfPCell valorPagado = new PdfPCell(new Phrase("Valor Pagado"));
                valorPagado.setBackgroundColor(BaseColor.ORANGE);
                PdfPCell valorDeuda = new PdfPCell(new Phrase("Valor Deuda"));
                valorDeuda.setBackgroundColor(BaseColor.ORANGE);
                PdfPCell fechaCreacion = new PdfPCell(new Phrase("Fecha Factura"));
                fechaCreacion.setBackgroundColor(BaseColor.ORANGE);
                PdfPCell infoCliente = new PdfPCell(new Phrase("Cliente"));
                infoCliente.setBackgroundColor(BaseColor.ORANGE);
                PdfPCell estado = new PdfPCell(new Phrase("Estado"));
                estado.setBackgroundColor(BaseColor.ORANGE);

                table.addCell(valorFactura);
                table.addCell(valorPagado);
                table.addCell(valorDeuda);
                table.addCell(fechaCreacion);
                table.addCell(infoCliente);
                table.addCell(estado);

                table.addCell(factura.getValorFactura().toString());
                table.addCell(factura.getValorPagado().toString());
                table.addCell(factura.getValorDeuda().toString());
                table.addCell(fecha);
                table.addCell(factura.getInfoCliente());
                table.addCell(factura.getEstado());

                document.add(table);
                document.add(Chunk.NEWLINE);

                List<Abono> abonosPorFactura = abonoRepository.abonosPorFactura(factura.getId());
                if (abonosPorFactura.size() > 0) {
                    Paragraph abonoTitulo = new Paragraph("ABONOS DE LA FACTURA N:" + factura.getNumeroFactura());
                    abonoTitulo.setAlignment(1);
                    document.add(abonoTitulo);
                    document.add(Chunk.NEWLINE);
                    PdfPTable tableAbono = new PdfPTable(3);
                    PdfPCell fechaAbo = new PdfPCell(new Phrase("Fecha Abono"));
                    fechaAbo.setBackgroundColor(BaseColor.GREEN);
                    PdfPCell valorAbono = new PdfPCell(new Phrase("Valor abono"));
                    valorAbono.setBackgroundColor(BaseColor.GREEN);
                    PdfPCell tipoFactura = new PdfPCell(new Phrase("Tipo Factura"));
                    tipoFactura.setBackgroundColor(BaseColor.GREEN);

                    tableAbono.addCell(fechaAbo);
                    tableAbono.addCell(valorAbono);
                    tableAbono.addCell(tipoFactura);

                    for (Abono abono : abonosPorFactura) {
                        Date fechaAbono = Date.from(abono.getFechaCreacion());
                        String fAbono = format.format(fechaAbono);

                        tableAbono.addCell(fAbono);
                        tableAbono.addCell(abono.getValorAbono().toString());
                        tableAbono.addCell(abono.getTipoFactura());
                    }

                    document.add(tableAbono);
                    document.add(Chunk.NEWLINE);
                }
            }

            document.close();

            return FileUtils.readFileToByteArray(new File(nombreDocumento));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete FacturaVenta : {}", id);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

        //SE VUELVEN A SUMAR LOS PRODUCTOS QUE SE VENDIERON EN LA FACTURA QUE ESTA POR REVERTIRSE
        List<Object[]> datos = itemFacturaVentaRepository.productosRevertir(id);

        for (Object[] dato : datos) {
            Long cantidad = Long.parseLong(dato[1].toString());
            BigDecimal cant = BigDecimal.valueOf(cantidad);
            Query qu = entityManager
                .createQuery(Constants.SUMAR_PRODUCTOS_FACTURA_REVERTIDA)
                .setParameter("cantidad", cant)
                .setParameter("id", Long.parseLong(dato[0].toString()));

            qu.executeUpdate();
        }

        //SE RESTA EL VALOR PAGADO DE LA FACTURA A LA CAJA CREADA EL MISMO DIA DE LA FACTURA
        FacturaVenta facturaVenta = facturaVentaRepository.facturaVentaPorId(id);
        if (facturaVenta.getValorPagado() != null) {
            Date fechaFactura = Date.from(facturaVenta.getFechaCreacion());
            String fechaFormat = format.format(fechaFactura);
            Query queryFactura = entityManager
                .createQuery(Constants.ELIMINAR_VALOR_CAJA_FACTURA_FECHA)
                .setParameter("valorFactura", facturaVenta.getValorPagado())
                .setParameter("fechaCreacion", fechaFormat);

            queryFactura.executeUpdate();

            //SI LA CAJA QUEDA CON UN VALOR DE 0 SE PROCEDE A ELIMINARLA DE LA BD
            Query queryEliminaCaja = entityManager.createQuery(Constants.ELIMINAR_CAJA_VALOR_CERO).setParameter("fecha", fechaFormat);

            queryEliminaCaja.executeUpdate();
        }

        //SE CONSULTAN TODOS LOS ABONOS CON EL ID DE LA FACTURA
        List<Abono> abonosPorFactura = facturaVentaRepository.abonosPorFactura(id);
        if (abonosPorFactura != null) {
            for (Abono abono : abonosPorFactura) {
                Date fecha = Date.from(abono.getFechaCreacion());
                String fechaAbono = format.format(fecha);
                Query caja = entityManager
                    .createQuery(Constants.ELIMINAR_VALOR_CAJA_ABONO_FECHA)
                    .setParameter("valorAbono", abono.getValorAbono())
                    .setParameter(fechaAbono, fecha);

                caja.executeUpdate();
            }
        }

        if (id != null) {
            //SE BORRAN TODOS LOS ABONOS RELACIONADOS CON LA FACTURA QUE SE ELIMINO
            Query q = entityManager.createQuery(Constants.ELIMINAR_ABONOS_FACTURA).setParameter("id", id);
            q.executeUpdate();
        }

        //SE BORRAN TODOS LOS ITEMS FACTURA RELACIONADOS CON ESTA FACTUDA
        eliminarItemsPOrFactura(id);

        facturaVentaRepository.deleteById(id);
    }

    private void eliminarItemsPOrFactura(Long id) {
        Query query = entityManager.createQuery(Constants.ELIMINAR_ITEMS_POR_FACTURA).setParameter("id", id);

        query.executeUpdate();
    }
}
