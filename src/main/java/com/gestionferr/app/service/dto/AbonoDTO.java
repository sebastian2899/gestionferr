package com.gestionferr.app.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.gestionferr.app.domain.Abono} entity.
 */
public class AbonoDTO implements Serializable {

    private Long id;

    private Long idFactura;

    private Instant fechaCreacion;

    private BigDecimal valorAbono;

    private String tipoFactura;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(Long idFactura) {
        this.idFactura = idFactura;
    }

    public Instant getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Instant fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public BigDecimal getValorAbono() {
        return valorAbono;
    }

    public void setValorAbono(BigDecimal valorAbono) {
        this.valorAbono = valorAbono;
    }

    public String getTipoFactura() {
        return tipoFactura;
    }

    public void setTipoFactura(String tipoFactura) {
        this.tipoFactura = tipoFactura;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AbonoDTO)) {
            return false;
        }

        AbonoDTO abonoDTO = (AbonoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, abonoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AbonoDTO{" +
            "id=" + getId() +
            ", idFactura=" + getIdFactura() +
            ", fechaCreacion='" + getFechaCreacion() + "'" +
            ", valorAbono=" + getValorAbono() +
            ", tipoFactura='" + getTipoFactura() + "'" +
            "}";
    }
}
