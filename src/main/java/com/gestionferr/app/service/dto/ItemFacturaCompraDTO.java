package com.gestionferr.app.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.gestionferr.app.domain.ItemFacturaCompra} entity.
 */
public class ItemFacturaCompraDTO implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long idFacturaCompra;

    private Long idProducto;

    private Long cantidad;

    private BigDecimal precio;

    private String nombreProducto;

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdFacturaCompra() {
        return idFacturaCompra;
    }

    public void setIdFacturaCompra(Long idFacturaCompra) {
        this.idFacturaCompra = idFacturaCompra;
    }

    public Long getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Long idProducto) {
        this.idProducto = idProducto;
    }

    public Long getCantidad() {
        return cantidad;
    }

    public void setCantidad(Long cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ItemFacturaCompraDTO)) {
            return false;
        }

        ItemFacturaCompraDTO itemFacturaCompraDTO = (ItemFacturaCompraDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, itemFacturaCompraDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ItemFacturaCompraDTO{" +
            "id=" + getId() +
            ", idFacturaCompra=" + getIdFacturaCompra() +
            ", idProducto=" + getIdProducto() +
            ", cantidad=" + getCantidad() +
            ", precio=" + getPrecio() +
            "}";
    }
}
