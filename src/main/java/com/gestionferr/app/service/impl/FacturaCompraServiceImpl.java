package com.gestionferr.app.service.impl;

import com.gestionferr.app.config.Constants;
import com.gestionferr.app.domain.Abono;
import com.gestionferr.app.domain.FacturaCompra;
import com.gestionferr.app.domain.ItemFacturaCompra;
import com.gestionferr.app.domain.Proveedor;
import com.gestionferr.app.repository.FacturaCompraRepository;
import com.gestionferr.app.repository.ItemFacturaCompraRepository;
import com.gestionferr.app.service.FacturaCompraService;
import com.gestionferr.app.service.dto.FacturaCompraDTO;
import com.gestionferr.app.service.dto.RegistroFacturaCompraDTO;
import com.gestionferr.app.service.mapper.FacturaCompraMapper;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
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

    @Override
    public byte[] facturaCompraReport() {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Document document = null;
        String nombreDocumento = null;

        Calendar fIni = Calendar.getInstance();
        fIni.add(Calendar.MONTH, -1);

        Instant fechaIni = fIni.toInstant();
        Instant fechaFin = Instant.now();

        List<FacturaCompra> facturaCompraFechas = facturaCompraRepository.facturaCompraReport(fechaIni, fechaFin);

        try {
            document = new Document();
            nombreDocumento = "Reporte PDF FacturaCompra" + ".pdf";
            FileOutputStream fileOutputStream = new FileOutputStream(nombreDocumento);
            PdfWriter.getInstance(document, fileOutputStream);
            document.open();

            Date fi = Date.from(fechaIni);
            Date fn = Date.from(fechaFin);

            String fIn = format.format(fi);
            String ffn = format.format(fn);

            Paragraph titulo = new Paragraph("Reporte Factura Compra PDF Mensual");
            titulo.setAlignment(1);

            document.add(titulo);
            document.add(Chunk.NEWLINE);

            Paragraph f1 = new Paragraph("Desde la fecha: " + fIn);
            Paragraph f2 = new Paragraph("Hasta la fecha: " + ffn);
            f1.setAlignment(2);
            f2.setAlignment(2);
            document.add(f1);
            document.add(f2);
            document.add(Chunk.NEWLINE);

            Proveedor proveedor = null;
            for (FacturaCompra factura : facturaCompraFechas) {
                if (factura.getIdProovedor() != null) {
                    proveedor = new Proveedor();
                    List<String[]> infoProveedor = facturaCompraRepository.proveedorFactura(factura.getIdProovedor());
                    for (String[] dato : infoProveedor) {
                        proveedor.setNombre(dato[0]);
                        proveedor.setNumeroContacto(dato[1]);
                        proveedor.setNumeroCC(dato[2]);
                    }

                    PdfPTable tableProv = new PdfPTable(3);
                    tableProv.setWidthPercentage(100);
                    PdfPCell nombre = new PdfPCell(new Phrase("Nombre"));
                    nombre.setBackgroundColor(BaseColor.ORANGE);
                    PdfPCell numeroContact = new PdfPCell(new Phrase("Numero Contacto"));
                    numeroContact.setBackgroundColor(BaseColor.ORANGE);
                    PdfPCell NumeroCC = new PdfPCell(new Phrase("Numero Documento"));
                    NumeroCC.setBackgroundColor(BaseColor.ORANGE);

                    tableProv.addCell(nombre);
                    tableProv.addCell(numeroContact);
                    tableProv.addCell(NumeroCC);

                    PdfPCell nombreProv = new PdfPCell(new Phrase(proveedor.getNombre()));
                    PdfPCell numeroContactProv = new PdfPCell(new Phrase(proveedor.getNumeroContacto()));
                    PdfPCell NumeroCCProv = new PdfPCell(new Phrase(proveedor.getNumeroCC()));

                    tableProv.addCell(nombreProv);
                    tableProv.addCell(numeroContactProv);
                    tableProv.addCell(NumeroCCProv);

                    document.add(tableProv);
                }

                PdfPTable tableFact = new PdfPTable(6);
                tableFact.setWidthPercentage(100);

                PdfPCell numeroFact = new PdfPCell(new Phrase("Numero Factura"));
                numeroFact.setBackgroundColor(BaseColor.ORANGE);
                PdfPCell fechaCreacion = new PdfPCell(new Phrase("Fecha Creacion"));
                fechaCreacion.setBackgroundColor(BaseColor.ORANGE);
                PdfPCell totalFactura = new PdfPCell(new Phrase("Total Factura"));
                totalFactura.setBackgroundColor(BaseColor.ORANGE);
                PdfPCell totalPagado = new PdfPCell(new Phrase("Total Pagado"));
                totalPagado.setBackgroundColor(BaseColor.ORANGE);
                PdfPCell deuda = new PdfPCell(new Phrase("Deuda"));
                deuda.setBackgroundColor(BaseColor.ORANGE);
                PdfPCell estado = new PdfPCell(new Phrase("Estado"));
                estado.setBackgroundColor(BaseColor.ORANGE);

                tableFact.addCell(numeroFact);
                tableFact.addCell(fechaCreacion);
                tableFact.addCell(totalFactura);
                tableFact.addCell(totalPagado);
                tableFact.addCell(deuda);
                tableFact.addCell(estado);

                PdfPCell numeroFactura = new PdfPCell(new Phrase(factura.getNumeroFactura()));
                Date fe = Date.from(factura.getFechaCreacion());
                String fecha = format.format(fe);
                PdfPCell fechaCrea = new PdfPCell(new Phrase(fecha));
                PdfPCell totalFact = new PdfPCell(new Phrase(factura.getValorFactura().toString()));
                PdfPCell totalPag = new PdfPCell(new Phrase(factura.getValorPagado().toString()));
                PdfPCell deud = new PdfPCell(new Phrase(factura.getValorDeuda().toString()));
                PdfPCell estad = new PdfPCell(new Phrase(factura.getEstado()));

                tableFact.addCell(numeroFactura);
                tableFact.addCell(fechaCrea);
                tableFact.addCell(totalFact);
                tableFact.addCell(totalPag);
                tableFact.addCell(deud);
                tableFact.addCell(estad);

                document.add(tableFact);

                List<Abono> abonosPorFactura = facturaCompraRepository.abonoFacturaCompra(factura.getId());

                if (abonosPorFactura != null && abonosPorFactura.size() > 0) {
                    PdfPTable tablaAbono = new PdfPTable(2);
                    tablaAbono.setWidthPercentage(100);

                    Font white = new Font(FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.WHITE);
                    Chunk fechaAbn = new Chunk("Fecha del abono", white);
                    Chunk valorAbn = new Chunk("Valor abonado", white);

                    PdfPCell fechaAbono = new PdfPCell(new Paragraph(fechaAbn));
                    fechaAbono.setBackgroundColor(BaseColor.BLUE);
                    PdfPCell valorAbono = new PdfPCell(new Phrase(valorAbn));
                    valorAbono.setBackgroundColor(BaseColor.BLUE);

                    tablaAbono.addCell(fechaAbono);
                    tablaAbono.addCell(valorAbono);

                    for (Abono abono : abonosPorFactura) {
                        Date fechaAbonoDate = Date.from(abono.getFechaCreacion());
                        String fechaAbonoFormat = format.format(fechaAbonoDate);

                        PdfPCell fechaA = new PdfPCell(new Phrase(fechaAbonoFormat));
                        PdfPCell ValorA = new PdfPCell(new Phrase(abono.getValorAbono().toString()));

                        tablaAbono.addCell(fechaA);
                        tablaAbono.addCell(ValorA);
                    }

                    document.add(tablaAbono);
                }

                document.add(Chunk.NEWLINE);
            }

            document.close();
            return FileUtils.readFileToByteArray(new File(nombreDocumento));
        } catch (Exception e) {
            e.printStackTrace();
            e.getMessage();
            return null;
        }
    }
}
