package com.gestionferr.app.domain;

import com.gestionferr.app.domain.enumeration.TipoFacturaEnum;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A FacturaCompra.
 */
@Entity
@Table(name = "factura_compra")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class FacturaCompra implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "numero_factura")
    private String numeroFactura;

    @Column(name = "fecha_creacion")
    private Instant fechaCreacion;

    @Column(name = "info_cliente")
    private String infoCliente;

    @Column(name = "valor_factura", precision = 21, scale = 2)
    private BigDecimal valorFactura;

    @Column(name = "valor_pagado", precision = 21, scale = 2)
    private BigDecimal valorPagado;

    @Column(name = "valor_deuda", precision = 21, scale = 2)
    private BigDecimal valorDeuda;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_factura")
    private TipoFacturaEnum tipoFactura;

    @Column(name = "id_proovedor")
    private Long idProovedor;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FacturaCompra id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroFactura() {
        return this.numeroFactura;
    }

    public FacturaCompra numeroFactura(String numeroFactura) {
        this.setNumeroFactura(numeroFactura);
        return this;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public Instant getFechaCreacion() {
        return this.fechaCreacion;
    }

    public FacturaCompra fechaCreacion(Instant fechaCreacion) {
        this.setFechaCreacion(fechaCreacion);
        return this;
    }

    public void setFechaCreacion(Instant fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getInfoCliente() {
        return this.infoCliente;
    }

    public FacturaCompra infoCliente(String infoCliente) {
        this.setInfoCliente(infoCliente);
        return this;
    }

    public void setInfoCliente(String infoCliente) {
        this.infoCliente = infoCliente;
    }

    public BigDecimal getValorFactura() {
        return this.valorFactura;
    }

    public FacturaCompra valorFactura(BigDecimal valorFactura) {
        this.setValorFactura(valorFactura);
        return this;
    }

    public void setValorFactura(BigDecimal valorFactura) {
        this.valorFactura = valorFactura;
    }

    public BigDecimal getValorPagado() {
        return this.valorPagado;
    }

    public FacturaCompra valorPagado(BigDecimal valorPagado) {
        this.setValorPagado(valorPagado);
        return this;
    }

    public void setValorPagado(BigDecimal valorPagado) {
        this.valorPagado = valorPagado;
    }

    public BigDecimal getValorDeuda() {
        return this.valorDeuda;
    }

    public FacturaCompra valorDeuda(BigDecimal valorDeuda) {
        this.setValorDeuda(valorDeuda);
        return this;
    }

    public void setValorDeuda(BigDecimal valorDeuda) {
        this.valorDeuda = valorDeuda;
    }

    public TipoFacturaEnum getTipoFactura() {
        return this.tipoFactura;
    }

    public FacturaCompra tipoFactura(TipoFacturaEnum tipoFactura) {
        this.setTipoFactura(tipoFactura);
        return this;
    }

    public void setTipoFactura(TipoFacturaEnum tipoFactura) {
        this.tipoFactura = tipoFactura;
    }

    public Long getIdProovedor() {
        return this.idProovedor;
    }

    public FacturaCompra idProovedor(Long idProovedor) {
        this.setIdProovedor(idProovedor);
        return this;
    }

    public void setIdProovedor(Long idProovedor) {
        this.idProovedor = idProovedor;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FacturaCompra)) {
            return false;
        }
        return id != null && id.equals(((FacturaCompra) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FacturaCompra{" +
            "id=" + getId() +
            ", numeroFactura='" + getNumeroFactura() + "'" +
            ", fechaCreacion='" + getFechaCreacion() + "'" +
            ", infoCliente='" + getInfoCliente() + "'" +
            ", valorFactura=" + getValorFactura() +
            ", valorPagado=" + getValorPagado() +
            ", valorDeuda=" + getValorDeuda() +
            ", tipoFactura='" + getTipoFactura() + "'" +
            ", idProovedor=" + getIdProovedor() +
            "}";
    }
}
