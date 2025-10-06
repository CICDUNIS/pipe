package com.example.pharmacy.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("RECETA")
public class Receta {

    @Id
    @Column("ID_RECETA")
    private Long idReceta;

    @Column("CODIGO_RECETA")
    private String codigoReceta;

    @Column("FECHA")
    private LocalDateTime fecha;   

    @Column("ID_USUARIO")
    private Long idUsuario;        

    @Column("APROBADO_SEGURO")
    private String aprobadoSeguro; 

    @Column("PDF_URL")
    private String pdfUrl;


    public Long getIdReceta() {
        return idReceta;
    }

    public void setIdReceta(Long idReceta) {
        this.idReceta = idReceta;
    }

    public String getCodigoReceta() {
        return codigoReceta;
    }

    public void setCodigoReceta(String codigoReceta) {
        this.codigoReceta = codigoReceta;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getAprobadoSeguro() {
        return aprobadoSeguro;
    }

    public void setAprobadoSeguro(String aprobadoSeguro) {
        this.aprobadoSeguro = aprobadoSeguro;
    }

    public String getPdfUrl() {
        return pdfUrl;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }
}


