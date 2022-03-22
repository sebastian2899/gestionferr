package com.gestionferr.app.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

public class FacturaVentasFechaDTO implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private BigDecimal valorTotal;

    private BigDecimal valorPagado;

    private BigDecimal diferencia;

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public BigDecimal getValorPagado() {
        return valorPagado;
    }

    public void setValorPagado(BigDecimal valorPagado) {
        this.valorPagado = valorPagado;
    }

    public BigDecimal getDiferencia() {
        return diferencia;
    }

    public void setDiferencia(BigDecimal diferencia) {
        this.diferencia = diferencia;
    }

    @Override
    public int hashCode() {
        return Objects.hash(diferencia, valorPagado, valorTotal);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof FacturaVentasFechaDTO)) return false;
        FacturaVentasFechaDTO other = (FacturaVentasFechaDTO) obj;
        return (
            Objects.equals(diferencia, other.diferencia) &&
            Objects.equals(valorPagado, other.valorPagado) &&
            Objects.equals(valorTotal, other.valorTotal)
        );
    }
}
