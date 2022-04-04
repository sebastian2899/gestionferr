package com.gestionferr.app.domain;

import com.gestionferr.app.domain.enumeration.TipoIdentificacionEnum;
import com.gestionferr.app.domain.enumeration.TipoProveedorEnum;
import com.gestionferr.app.service.dto.ItemPorFacturaCompra;
import java.io.Serializable;
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
 * A Proveedor.
 */
@Entity
@Table(name = "proveedor")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Proveedor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "numero_contacto")
    private String numeroContacto;

    @Column(name = "email")
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_identificacion")
    private TipoIdentificacionEnum tipoIdentificacion;

    @Column(name = "numero_cc")
    private String numeroCC;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_proveedor")
    private TipoProveedorEnum tipoProveedor;

    @Transient
    private List<ItemPorFacturaCompra> facturasProovedor;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public List<ItemPorFacturaCompra> getFacturasProovedor() {
        return facturasProovedor;
    }

    public void setFacturasProovedor(List<ItemPorFacturaCompra> facturasProovedor) {
        this.facturasProovedor = facturasProovedor;
    }

    public Proveedor id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Proveedor nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNumeroContacto() {
        return this.numeroContacto;
    }

    public Proveedor numeroContacto(String numeroContacto) {
        this.setNumeroContacto(numeroContacto);
        return this;
    }

    public void setNumeroContacto(String numeroContacto) {
        this.numeroContacto = numeroContacto;
    }

    public String getEmail() {
        return this.email;
    }

    public Proveedor email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public TipoIdentificacionEnum getTipoIdentificacion() {
        return this.tipoIdentificacion;
    }

    public Proveedor tipoIdentificacion(TipoIdentificacionEnum tipoIdentificacion) {
        this.setTipoIdentificacion(tipoIdentificacion);
        return this;
    }

    public void setTipoIdentificacion(TipoIdentificacionEnum tipoIdentificacion) {
        this.tipoIdentificacion = tipoIdentificacion;
    }

    public String getNumeroCC() {
        return this.numeroCC;
    }

    public Proveedor numeroCC(String numeroCC) {
        this.setNumeroCC(numeroCC);
        return this;
    }

    public void setNumeroCC(String numeroCC) {
        this.numeroCC = numeroCC;
    }

    public TipoProveedorEnum getTipoProveedor() {
        return this.tipoProveedor;
    }

    public Proveedor tipoProveedor(TipoProveedorEnum tipoProveedor) {
        this.setTipoProveedor(tipoProveedor);
        return this;
    }

    public void setTipoProveedor(TipoProveedorEnum tipoProveedor) {
        this.tipoProveedor = tipoProveedor;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Proveedor)) {
            return false;
        }
        return id != null && id.equals(((Proveedor) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Proveedor{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", numeroContacto='" + getNumeroContacto() + "'" +
            ", email='" + getEmail() + "'" +
            ", tipoIdentificacion='" + getTipoIdentificacion() + "'" +
            ", numeroCC='" + getNumeroCC() + "'" +
            ", tipoProveedor='" + getTipoProveedor() + "'" +
            "}";
    }
}
