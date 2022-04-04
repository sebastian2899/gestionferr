package com.gestionferr.app.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ItemFacturaCompra.
 */
@Entity
@Table(name = "item_factura_compra")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ItemFacturaCompra implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "id_factura_compra")
    private Long idFacturaCompra;

    @Column(name = "id_producto")
    private Long idProducto;

    @Column(name = "cantidad")
    private Long cantidad;

    @Column(name = "precio", precision = 21, scale = 2)
    private BigDecimal precio;

    @Transient
    private String nombreProducto;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public ItemFacturaCompra id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdFacturaCompra() {
        return this.idFacturaCompra;
    }

    public ItemFacturaCompra idFacturaCompra(Long idFacturaCompra) {
        this.setIdFacturaCompra(idFacturaCompra);
        return this;
    }

    public void setIdFacturaCompra(Long idFacturaCompra) {
        this.idFacturaCompra = idFacturaCompra;
    }

    public Long getIdProducto() {
        return this.idProducto;
    }

    public ItemFacturaCompra idProducto(Long idProducto) {
        this.setIdProducto(idProducto);
        return this;
    }

    public void setIdProducto(Long idProducto) {
        this.idProducto = idProducto;
    }

    public Long getCantidad() {
        return this.cantidad;
    }

    public ItemFacturaCompra cantidad(Long cantidad) {
        this.setCantidad(cantidad);
        return this;
    }

    public void setCantidad(Long cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecio() {
        return this.precio;
    }

    public ItemFacturaCompra precio(BigDecimal precio) {
        this.setPrecio(precio);
        return this;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ItemFacturaCompra)) {
            return false;
        }
        return id != null && id.equals(((ItemFacturaCompra) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ItemFacturaCompra{" +
            "id=" + getId() +
            ", idFacturaCompra=" + getIdFacturaCompra() +
            ", idProducto=" + getIdProducto() +
            ", cantidad=" + getCantidad() +
            ", precio=" + getPrecio() +
            "}";
    }
}
