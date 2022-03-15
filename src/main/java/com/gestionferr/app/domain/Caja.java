package com.gestionferr.app.domain;

import com.gestionferr.app.domain.enumeration.TipoEstadoEnum;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Caja.
 */
@Entity
@Table(name = "caja")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Caja implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "fecha_creacion")
    private Instant fechaCreacion;

    @Column(name = "valor_venta_dia", precision = 21, scale = 2)
    private BigDecimal valorVentaDia;

    @Column(name = "valor_registrado_dia", precision = 21, scale = 2)
    private BigDecimal valorRegistradoDia;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private TipoEstadoEnum estado;

    @Column(name = "diferencia", precision = 21, scale = 2)
    private BigDecimal diferencia;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Caja id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getFechaCreacion() {
        return this.fechaCreacion;
    }

    public Caja fechaCreacion(Instant fechaCreacion) {
        this.setFechaCreacion(fechaCreacion);
        return this;
    }

    public void setFechaCreacion(Instant fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public BigDecimal getValorVentaDia() {
        return this.valorVentaDia;
    }

    public Caja valorVentaDia(BigDecimal valorVentaDia) {
        this.setValorVentaDia(valorVentaDia);
        return this;
    }

    public void setValorVentaDia(BigDecimal valorVentaDia) {
        this.valorVentaDia = valorVentaDia;
    }

    public BigDecimal getValorRegistradoDia() {
        return this.valorRegistradoDia;
    }

    public Caja valorRegistradoDia(BigDecimal valorRegistradoDia) {
        this.setValorRegistradoDia(valorRegistradoDia);
        return this;
    }

    public void setValorRegistradoDia(BigDecimal valorRegistradoDia) {
        this.valorRegistradoDia = valorRegistradoDia;
    }

    public TipoEstadoEnum getEstado() {
        return this.estado;
    }

    public Caja estado(TipoEstadoEnum estado) {
        this.setEstado(estado);
        return this;
    }

    public void setEstado(TipoEstadoEnum estado) {
        this.estado = estado;
    }

    public BigDecimal getDiferencia() {
        return this.diferencia;
    }

    public Caja diferencia(BigDecimal diferencia) {
        this.setDiferencia(diferencia);
        return this;
    }

    public void setDiferencia(BigDecimal diferencia) {
        this.diferencia = diferencia;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Caja)) {
            return false;
        }
        return id != null && id.equals(((Caja) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Caja{" +
            "id=" + getId() +
            ", fechaCreacion='" + getFechaCreacion() + "'" +
            ", valorVentaDia=" + getValorVentaDia() +
            ", valorRegistradoDia=" + getValorRegistradoDia() +
            ", estado='" + getEstado() + "'" +
            ", diferencia=" + getDiferencia() +
            "}";
    }
}
