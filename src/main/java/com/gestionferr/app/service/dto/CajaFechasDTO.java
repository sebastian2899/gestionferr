package com.gestionferr.app.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

public class CajaFechasDTO implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private BigDecimal valorTotalDia;

    private BigDecimal valorRegistradoDia;

    private BigDecimal diferencia;

    public BigDecimal getValorTotalDia() {
        return valorTotalDia;
    }

    public void setValorTotalDia(BigDecimal valorTotalDia) {
        this.valorTotalDia = valorTotalDia;
    }

    public BigDecimal getValorRegistradoDia() {
        return valorRegistradoDia;
    }

    public void setValorRegistradoDia(BigDecimal valorRegistradoDia) {
        this.valorRegistradoDia = valorRegistradoDia;
    }

    public BigDecimal getDiferencia() {
        return diferencia;
    }

    public void setDiferencia(BigDecimal diferencia) {
        this.diferencia = diferencia;
    }

    @Override
    public int hashCode() {
        return Objects.hash(diferencia, valorRegistradoDia, valorTotalDia);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof CajaFechasDTO)) return false;
        CajaFechasDTO other = (CajaFechasDTO) obj;
        return (
            Objects.equals(diferencia, other.diferencia) &&
            Objects.equals(valorRegistradoDia, other.valorRegistradoDia) &&
            Objects.equals(valorTotalDia, other.valorTotalDia)
        );
    }
}
