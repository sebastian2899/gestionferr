package com.gestionferr.app.service.dto;

import com.gestionferr.app.domain.enumeration.TipoFacturaEnum;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.gestionferr.app.domain.FacturaCompra} entity.
 */
public class FacturaCompraDTO implements Serializable {

    private Long id;

    private String numeroFactura;

    private Instant fechaCreacion;

    private String infoCliente;

    private BigDecimal valorFactura;

    private BigDecimal valorPagado;

    private BigDecimal valorDeuda;

    private TipoFacturaEnum tipoFactura;

    private Long idProovedor;

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

    public Long getIdProovedor() {
        return idProovedor;
    }

    public void setIdProovedor(Long idProovedor) {
        this.idProovedor = idProovedor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FacturaCompraDTO)) {
            return false;
        }

        FacturaCompraDTO facturaCompraDTO = (FacturaCompraDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, facturaCompraDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FacturaCompraDTO{" +
            "id=" + getId() +
            ", numeroFactura='" + getNumeroFactura() + "'" +
            ", fechaCreacion='" + getFechaCreacion() + "'" +
            ", infoCliente='" + getInfoCliente() + "'" +
            ", valorFactura=" + getValorFactura() +
            ", valorPagado=" + getValorPagado() +
            ", valorDeuda=" + getValorDeuda() +
            ", tipoFactura='" + getTipoFactura() + "'" +
            ", idProovedor=" + getIdProovedor() +
            "}";
    }
}
