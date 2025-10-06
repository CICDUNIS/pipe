package com.example.pharmacy.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import java.io.Serializable;

@Table("USUARIOROL")
public class UsuarioRol implements Serializable {

    @Id
    @Column("ID_USUARIO")
    private Long idUsuario;

    @Column("ID_ROL")
    private Long idRol;

    public UsuarioRol() {
    }

    public UsuarioRol(Long idUsuario, Long idRol) {
        this.idUsuario = idUsuario;
        this.idRol = idRol;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Long getIdRol() {
        return idRol;
    }

    public void setIdRol(Long idRol) {
        this.idRol = idRol;
    }

    @Override
    public String toString() {
        return "UsuarioRol{" +
                "idUsuario=" + idUsuario +
                ", idRol=" + idRol +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        UsuarioRol that = (UsuarioRol) o;
        
        if (idUsuario != null ? !idUsuario.equals(that.idUsuario) : that.idUsuario != null) return false;
        return idRol != null ? idRol.equals(that.idRol) : that.idRol == null;
    }
    
    @Override
    public int hashCode() {
        int result = idUsuario != null ? idUsuario.hashCode() : 0;
        result = 31 * result + (idRol != null ? idRol.hashCode() : 0);
        return result;
    }
} 