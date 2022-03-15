package com.gestionferr.app.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Abono.
 */
@Entity
@Table(name = "abono")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Abono implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "id_factura")
    private Long idFactura;

    @Column(name = "fecha_creacion")
    private Instant fechaCreacion;

    @Column(name = "valor_abono", precision = 21, scale = 2)
    private BigDecimal valorAbono;

    @Column(name = "tipo_factura")
    private String tipoFactura;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Abono id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdFactura() {
        return this.idFactura;
    }

    public Abono idFactura(Long idFactura) {
        this.setIdFactura(idFactura);
        return this;
    }

    public void setIdFactura(Long idFactura) {
        this.idFactura = idFactura;
    }

    public Instant getFechaCreacion() {
        return this.fechaCreacion;
    }

    public Abono fechaCreacion(Instant fechaCreacion) {
        this.setFechaCreacion(fechaCreacion);
        return this;
    }

    public void setFechaCreacion(Instant fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public BigDecimal getValorAbono() {
        return this.valorAbono;
    }

    public Abono valorAbono(BigDecimal valorAbono) {
        this.setValorAbono(valorAbono);
        return this;
    }

    public void setValorAbono(BigDecimal valorAbono) {
        this.valorAbono = valorAbono;
    }

    public String getTipoFactura() {
        return this.tipoFactura;
    }

    public Abono tipoFactura(String tipoFactura) {
        this.setTipoFactura(tipoFactura);
        return this;
    }

    public void setTipoFactura(String tipoFactura) {
        this.tipoFactura = tipoFactura;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Abono)) {
            return false;
        }
        return id != null && id.equals(((Abono) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Abono{" +
            "id=" + getId() +
            ", idFactura=" + getIdFactura() +
            ", fechaCreacion='" + getFechaCreacion() + "'" +
            ", valorAbono=" + getValorAbono() +
            ", tipoFactura='" + getTipoFactura() + "'" +
            "}";
    }
}
