package com.gestionferr.app.domain;

import com.gestionferr.app.domain.enumeration.TipoFacturaEnum;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A FacturaVenta.
 */
@Entity
@Table(name = "factura_venta")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class FacturaVenta implements Serializable {

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

    @Column(name = "id_cliente")
    private Long idCliente;

    @Column(name = "estado")
    private String estado;

    @Transient
    private List<ItemFacturaVenta> itemsPorFactura;

    public Long getId() {
        return this.id;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public FacturaVenta id(Long id) {
        this.setId(id);
        return this;
    }

    public List<ItemFacturaVenta> getItemsPorFactura() {
        return itemsPorFactura;
    }

    public void setItemsPorFactura(List<ItemFacturaVenta> itemsPorFactura) {
        this.itemsPorFactura = itemsPorFactura;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroFactura() {
        return this.numeroFactura;
    }

    public FacturaVenta numeroFactura(String numeroFactura) {
        this.setNumeroFactura(numeroFactura);
        return this;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public Instant getFechaCreacion() {
        return this.fechaCreacion;
    }

    public FacturaVenta fechaCreacion(Instant fechaCreacion) {
        this.setFechaCreacion(fechaCreacion);
        return this;
    }

    public void setFechaCreacion(Instant fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getInfoCliente() {
        return this.infoCliente;
    }

    public FacturaVenta infoCliente(String infoCliente) {
        this.setInfoCliente(infoCliente);
        return this;
    }

    public void setInfoCliente(String infoCliente) {
        this.infoCliente = infoCliente;
    }

    public BigDecimal getValorFactura() {
        return this.valorFactura;
    }

    public FacturaVenta valorFactura(BigDecimal valorFactura) {
        this.setValorFactura(valorFactura);
        return this;
    }

    public void setValorFactura(BigDecimal valorFactura) {
        this.valorFactura = valorFactura;
    }

    public BigDecimal getValorPagado() {
        return this.valorPagado;
    }

    public FacturaVenta valorPagado(BigDecimal valorPagado) {
        this.setValorPagado(valorPagado);
        return this;
    }

    public void setValorPagado(BigDecimal valorPagado) {
        this.valorPagado = valorPagado;
    }

    public BigDecimal getValorDeuda() {
        return this.valorDeuda;
    }

    public FacturaVenta valorDeuda(BigDecimal valorDeuda) {
        this.setValorDeuda(valorDeuda);
        return this;
    }

    public void setValorDeuda(BigDecimal valorDeuda) {
        this.valorDeuda = valorDeuda;
    }

    public TipoFacturaEnum getTipoFactura() {
        return this.tipoFactura;
    }

    public FacturaVenta tipoFactura(TipoFacturaEnum tipoFactura) {
        this.setTipoFactura(tipoFactura);
        return this;
    }

    public void setTipoFactura(TipoFacturaEnum tipoFactura) {
        this.tipoFactura = tipoFactura;
    }

    public Long getIdCliente() {
        return this.idCliente;
    }

    public FacturaVenta idCliente(Long idCliente) {
        this.setIdCliente(idCliente);
        return this;
    }

    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FacturaVenta)) {
            return false;
        }
        return id != null && id.equals(((FacturaVenta) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FacturaVenta{" +
            "id=" + getId() +
            ", numeroFactura='" + getNumeroFactura() + "'" +
            ", fechaCreacion='" + getFechaCreacion() + "'" +
            ", infoCliente='" + getInfoCliente() + "'" +
            ", valorFactura=" + getValorFactura() +
            ", valorPagado=" + getValorPagado() +
            ", valorDeuda=" + getValorDeuda() +
            ", tipoFactura='" + getTipoFactura() + "'" +
            ", idCliente=" + getIdCliente() +
            "}";
    }
}
