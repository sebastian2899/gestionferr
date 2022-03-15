package com.gestionferr.app.service.dto;

import com.gestionferr.app.domain.ItemFacturaVenta;
import com.gestionferr.app.domain.enumeration.TipoFacturaEnum;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

/**
 * A DTO for the {@link com.gestionferr.app.domain.FacturaVenta} entity.
 */
public class FacturaVentaDTO implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Long id;

    private String numeroFactura;

    private Instant fechaCreacion;

    private String infoCliente;

    private BigDecimal valorFactura;

    private BigDecimal valorPagado;

    private BigDecimal valorDeuda;

    private List<ItemFacturaVentaDTO> itemsPorFactura;

    private TipoFacturaEnum tipoFactura;

    private Long idCliente;

    private String estado;

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<ItemFacturaVentaDTO> getItemsPorFactura() {
        return itemsPorFactura;
    }

    public void setItemsPorFactura(List<ItemFacturaVentaDTO> itemsPorFactura) {
        this.itemsPorFactura = itemsPorFactura;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public Instant getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Instant fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getInfoCliente() {
        return infoCliente;
    }

    public void setInfoCliente(String infoCliente) {
        this.infoCliente = infoCliente;
    }

    public BigDecimal getValorFactura() {
        return valorFactura;
    }

    public void setValorFactura(BigDecimal valorFactura) {
        this.valorFactura = valorFactura;
    }

    public BigDecimal getValorPagado() {
        return valorPagado;
    }

    public void setValorPagado(BigDecimal valorPagado) {
        this.valorPagado = valorPagado;
    }

    public BigDecimal getValorDeuda() {
        return valorDeuda;
    }

    public void setValorDeuda(BigDecimal valorDeuda) {
        this.valorDeuda = valorDeuda;
    }

    public TipoFacturaEnum getTipoFactura() {
        return tipoFactura;
    }

    public void setTipoFactura(TipoFacturaEnum tipoFactura) {
        this.tipoFactura = tipoFactura;
    }

    public Long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FacturaVentaDTO)) {
            return false;
        }

        FacturaVentaDTO facturaVentaDTO = (FacturaVentaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, facturaVentaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FacturaVentaDTO{" +
            "id=" + getId() +
            ", numeroFactura='" + getNumeroFactura() + "'" +
            ", fechaCreacion='" + getFechaCreacion() + "'" +
            ", infoCliente='" + getInfoCliente() + "'" +
            ", valorFactura=" + getValorFactura() +
            ", valorPagado=" + getValorPagado() +
            ", valorDeuda=" + getValorDeuda() +
            ", tipoFactura='" + getTipoFactura() + "'" +
            ", idCliente=" + getIdCliente() +
            "}";
    }
}
