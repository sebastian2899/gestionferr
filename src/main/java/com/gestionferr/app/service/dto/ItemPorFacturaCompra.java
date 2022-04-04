package com.gestionferr.app.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

public class ItemPorFacturaCompra implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String numeroFactura;

    private String fecha;

    private BigDecimal valorFactura;

    private BigDecimal valorDeuda;

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public BigDecimal getValorFactura() {
        return valorFactura;
    }

    public void setValorFactura(BigDecimal valorFactura) {
        this.valorFactura = valorFactura;
    }

    public BigDecimal getValorDeuda() {
        return valorDeuda;
    }

    public void setValorDeuda(BigDecimal valorDeuda) {
        this.valorDeuda = valorDeuda;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fecha, numeroFactura, valorDeuda, valorFactura);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ItemPorFacturaCompra)) return false;
        ItemPorFacturaCompra other = (ItemPorFacturaCompra) obj;
        return (
            Objects.equals(fecha, other.fecha) &&
            Objects.equals(numeroFactura, other.numeroFactura) &&
            Objects.equals(valorDeuda, other.valorDeuda) &&
            Objects.equals(valorFactura, other.valorFactura)
        );
    }
}
