package com.example.pharmacy.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("COMENTARIOMEDICAMENTO")
public class ComentarioMedicamento {
    
    @Id
    @Column("ID_COMENTARIO")
    private Long idComentario;
    
    @Column("ID_MEDICAMENTO")
    private Long idMedicamento;
    
    @Column("ID_USUARIO")
    private Long idUsuario;
    
    @Column("TEXTO")
    private String texto;
    
    @Column("FECHA")
    private LocalDateTime fecha;
    
    @Column("PARENT_ID")
    private Long parentId;
    
    private String nombreUsuario;
    
    private List<ComentarioMedicamento> respuestas = new ArrayList<>();

    public Long getIdComentario() {
        return idComentario;
    }

    public void setIdComentario(Long idComentario) {
        this.idComentario = idComentario;
    }

    public Long getIdMedicamento() {
        return idMedicamento;
    }

    public void setIdMedicamento(Long idMedicamento) {
        this.idMedicamento = idMedicamento;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public List<ComentarioMedicamento> getRespuestas() {
        return respuestas;
    }

    public void setRespuestas(List<ComentarioMedicamento> respuestas) {
        this.respuestas = respuestas;
    }
} 