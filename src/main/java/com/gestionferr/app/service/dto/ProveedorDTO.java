package com.gestionferr.app.service.dto;

import com.gestionferr.app.domain.enumeration.TipoIdentificacionEnum;
import com.gestionferr.app.domain.enumeration.TipoProveedorEnum;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.gestionferr.app.domain.Proveedor} entity.
 */
public class ProveedorDTO implements Serializable {

    private Long id;

    private String nombre;

    private String numeroContacto;

    private String email;

    private TipoIdentificacionEnum tipoIdentificacion;

    private String numeroCC;

    private TipoProveedorEnum tipoProveedor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNumeroContacto() {
        return numeroContacto;
    }

    public void setNumeroContacto(String numeroContacto) {
        this.numeroContacto = numeroContacto;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public TipoIdentificacionEnum getTipoIdentificacion() {
        return tipoIdentificacion;
    }

    public void setTipoIdentificacion(TipoIdentificacionEnum tipoIdentificacion) {
        this.tipoIdentificacion = tipoIdentificacion;
    }

    public String getNumeroCC() {
        return numeroCC;
    }

    public void setNumeroCC(String numeroCC) {
        this.numeroCC = numeroCC;
    }

    public TipoProveedorEnum getTipoProveedor() {
        return tipoProveedor;
    }

    public void setTipoProveedor(TipoProveedorEnum tipoProveedor) {
        this.tipoProveedor = tipoProveedor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProveedorDTO)) {
            return false;
        }

        ProveedorDTO proveedorDTO = (ProveedorDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, proveedorDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProveedorDTO{" +
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
