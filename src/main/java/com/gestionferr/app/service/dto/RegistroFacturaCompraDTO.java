package com.gestionferr.app.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

public class RegistroFacturaCompraDTO implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private BigDecimal valorFactura;

    private BigDecimal valorPagado;

    private BigDecimal valorDeuda;

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
        return Objects.hash(valorDeuda, valorFactura, valorPagado);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof RegistroFacturaCompraDTO)) return false;
        RegistroFacturaCompraDTO other = (RegistroFacturaCompraDTO) obj;
        return (
            Objects.equals(valorDeuda, other.valorDeuda) &&
            Objects.equals(valorFactura, other.valorFactura) &&
            Objects.equals(valorPagado, other.valorPagado)
        );
    }
}
