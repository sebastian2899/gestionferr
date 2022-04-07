package com.gestionferr.app.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.gestionferr.app.domain.Egreso} entity.
 */
public class EgresoDTO implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Long id;

    private Instant fechaCreacion;

    private String descripcion;

    private BigDecimal valorEgreso;

    private String estadoEditar;

    public String getEstadoEditar() {
        return estadoEditar;
    }

    public void setEstadoEditar(String estadoEditar) {
        this.estadoEditar = estadoEditar;
    }

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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getValorEgreso() {
        return valorEgreso;
    }

    public void setValorEgreso(BigDecimal valorEgreso) {
        this.valorEgreso = valorEgreso;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EgresoDTO)) {
            return false;
        }

        EgresoDTO egresoDTO = (EgresoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, egresoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EgresoDTO{" +
            "id=" + getId() +
            ", fechaCreacion='" + getFechaCreacion() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", valorEgreso=" + getValorEgreso() +
            "}";
    }
}
