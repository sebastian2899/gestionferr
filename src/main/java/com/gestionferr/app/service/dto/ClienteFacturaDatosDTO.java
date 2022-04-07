package com.gestionferr.app.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

public class ClienteFacturaDatosDTO implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Long idFacturaVenta;

    private String fechaCreacion;

    private String numeroFactura;

    private BigDecimal valorFactura;

    private BigDecimal valorPagado;

    private BigDecimal valorDeuda;

    public Long getIdFacturaVenta() {
        return idFacturaVenta;
    }

    public void setIdFacturaVenta(Long idFacturaVenta) {
        this.idFacturaVenta = idFacturaVenta;
    }

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
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

    @Override
    public int hashCode() {
        return Objects.hash(fechaCreacion, idFacturaVenta, numeroFactura, valorDeuda, valorFactura, valorPagado);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ClienteFacturaDatosDTO)) return false;
        ClienteFacturaDatosDTO other = (ClienteFacturaDatosDTO) obj;
        return (
            Objects.equals(fechaCreacion, other.fechaCreacion) &&
            Objects.equals(idFacturaVenta, other.idFacturaVenta) &&
            Objects.equals(numeroFactura, other.numeroFactura) &&
            Objects.equals(valorDeuda, other.valorDeuda) &&
            Objects.equals(valorFactura, other.valorFactura) &&
            Objects.equals(valorPagado, other.valorPagado)
        );
    }
}
