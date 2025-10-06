package com.example.pharmacy.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("VERIFICATION_TOKEN")
public class VerificationToken {

    @Id
    @Column("ID_TOKEN")
    private Long idToken;

    @Column("TOKEN")
    private String token;

    @Column("ID_USUARIO")
    private Long idUsuario;

    @Column("FECHA_CREACION")
    private LocalDateTime fechaCreacion;

    @Column("FECHA_EXPIRACION")
    private LocalDateTime fechaExpiracion;

    @Column("FECHA_VERIFICACION")
    private LocalDateTime fechaVerificacion;

    public VerificationToken() {
    }

    public VerificationToken(String token, Long idUsuario, LocalDateTime fechaCreacion, LocalDateTime fechaExpiracion) {
        this.token = token;
        this.idUsuario = idUsuario;
        this.fechaCreacion = fechaCreacion;
        this.fechaExpiracion = fechaExpiracion;
    }

    public Long getIdToken() {
        return idToken;
    }

    public void setIdToken(Long idToken) {
        this.idToken = idToken;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaExpiracion() {
        return fechaExpiracion;
    }

    public void setFechaExpiracion(LocalDateTime fechaExpiracion) {
        this.fechaExpiracion = fechaExpiracion;
    }

    public LocalDateTime getFechaVerificacion() {
        return fechaVerificacion;
    }

    public void setFechaVerificacion(LocalDateTime fechaVerificacion) {
        this.fechaVerificacion = fechaVerificacion;
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(fechaExpiracion);
    }

    public boolean isVerified() {
        return fechaVerificacion != null;
    }
} 