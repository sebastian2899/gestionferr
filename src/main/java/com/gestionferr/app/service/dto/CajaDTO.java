package com.gestionferr.app.service.dto;

import com.gestionferr.app.domain.enumeration.TipoEstadoEnum;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.gestionferr.app.domain.Caja} entity.
 */
public class CajaDTO implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Long id;

    private Instant fechaCreacion;

    private BigDecimal valorVentaDia;

    private BigDecimal valorRegistradoDia;

    private TipoEstadoEnum estado;

    private BigDecimal diferencia;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Instant fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public BigDecimal getValorVentaDia() {
        return valorVentaDia;
    }

    public void setValorVentaDia(BigDecimal valorVentaDia) {
        this.valorVentaDia = valorVentaDia;
    }

    public BigDecimal getValorRegistradoDia() {
        return valorRegistradoDia;
    }

    public void setValorRegistradoDia(BigDecimal valorRegistradoDia) {
        this.valorRegistradoDia = valorRegistradoDia;
    }

    public TipoEstadoEnum getEstado() {
        return estado;
    }

    public void setEstado(TipoEstadoEnum estado) {
        this.estado = estado;
    }

    public BigDecimal getDiferencia() {
        return diferencia;
    }

    public void setDiferencia(BigDecimal diferencia) {
        this.diferencia = diferencia;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CajaDTO)) {
            return false;
        }

        CajaDTO cajaDTO = (CajaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, cajaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CajaDTO{" +
            "id=" + getId() +
            ", fechaCreacion='" + getFechaCreacion() + "'" +
            ", valorVentaDia=" + getValorVentaDia() +
            ", valorRegistradoDia=" + getValorRegistradoDia() +
            ", estado='" + getEstado() + "'" +
            ", diferencia=" + getDiferencia() +
            "}";
    }
}
