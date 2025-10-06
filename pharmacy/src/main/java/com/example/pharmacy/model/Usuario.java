package com.example.pharmacy.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("USUARIO")
public class Usuario {

    @Id
    @Column("ID_USUARIO")
    private Long idUsuario;

    @Column("NOMBRE")
    private String nombre;

    @Column("CORREO")
    private String correo;

    @Column("PASSWORD_HASH")
    private String passwordHash;

    @Column("ACTIVO")
    private String activo; 
    
    @Column("FECHA_CREACION")
    private LocalDateTime fechaCreacion;
    
    @Column("PERFIL_COMPLETO")
    private String perfilCompleto;
    
    @Column("PRIMER_LOGIN")
    private String primerLogin;

    public Usuario() {} 

    public Usuario(Long idUsuario, String nombre, String correo, String passwordHash, String activo) { 
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.correo = correo;
        this.passwordHash = passwordHash; 
        this.activo = activo; 
        this.fechaCreacion = LocalDateTime.now();
        this.perfilCompleto = "N";
        this.primerLogin = "Y";
    } 

    public Long getIdUsuario() {
        return idUsuario;
    } 

    public void setIdUsuario(Long idUsuario) { 
        this.idUsuario = idUsuario;
    } 

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) { 
        this.nombre = nombre;
    } 

    public String getCorreo() {
        return correo;
    } 

    public void setCorreo(String correo) {
        this.correo = correo;
    } 

    public String getPasswordHash() {
        return passwordHash;
    } 

    public void setPasswordHash(String passwordHash) { 
        this.passwordHash = passwordHash;
    } 

    public String getActivo() { 
        return activo;
    }

    public void setActivo(String activo) {
        this.activo = activo;
    }

    public boolean isActivo() {
        return "Y".equalsIgnoreCase(activo);
    }
    
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
    
    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    
    public String getPerfilCompleto() {
        return perfilCompleto;
    }
    
    public void setPerfilCompleto(String perfilCompleto) {
        this.perfilCompleto = perfilCompleto;
    }
    
    public boolean isPerfilCompleto() {
        return "Y".equalsIgnoreCase(perfilCompleto);
    }
    
    public String getPrimerLogin() {
        return primerLogin;
    }
    
    public void setPrimerLogin(String primerLogin) {
        this.primerLogin = primerLogin;
    }
    
    public boolean isPrimerLogin() {
        return "Y".equalsIgnoreCase(primerLogin);
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "idUsuario=" + idUsuario +
                ", nombre='" + nombre + '\'' + 
                ", correo='" + correo + '\'' + 
                ", activo='" + activo + '\'' + 
                ", perfilCompleto='" + perfilCompleto + '\'' +
                ", primerLogin='" + primerLogin + '\'' +
                '}';
    }
}



