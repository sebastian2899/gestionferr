package com.gestionferr.app.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ItemFacturaVenta.
 */
@Entity
@Table(name = "item_factura_venta")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ItemFacturaVenta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "id_factura_venta")
    private Long idFacturaVenta;

    @Column(name = "id_producto")
    private Long idProducto;

    @Column(name = "cantidad")
    private Long cantidad;

    @Column(name = "precio", precision = 21, scale = 2)
    private BigDecimal precio;

    @Transient
    private String nombreProducto;

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ItemFacturaVenta id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdFacturaVenta() {
        return this.idFacturaVenta;
    }

    public ItemFacturaVenta idFacturaVenta(Long idFacturaVenta) {
        this.setIdFacturaVenta(idFacturaVenta);
        return this;
    }

    public void setIdFacturaVenta(Long idFacturaVenta) {
        this.idFacturaVenta = idFacturaVenta;
    }

    public Long getIdProducto() {
        return this.idProducto;
    }

    public ItemFacturaVenta idProducto(Long idProducto) {
        this.setIdProducto(idProducto);
        return this;
    }

    public void setIdProducto(Long idProducto) {
        this.idProducto = idProducto;
    }

    public Long getCantidad() {
        return this.cantidad;
    }

    public ItemFacturaVenta cantidad(Long cantidad) {
        this.setCantidad(cantidad);
        return this;
    }

    public void setCantidad(Long cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecio() {
        return this.precio;
    }

    public ItemFacturaVenta precio(BigDecimal precio) {
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
        if (!(o instanceof ItemFacturaVenta)) {
            return false;
        }
        return id != null && id.equals(((ItemFacturaVenta) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ItemFacturaVenta{" +
            "id=" + getId() +
            ", idFacturaVenta=" + getIdFacturaVenta() +
            ", idProducto=" + getIdProducto() +
            ", cantidad=" + getCantidad() +
            ", precio=" + getPrecio() +
            "}";
    }
}
