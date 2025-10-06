package com.example.pharmacy.dto;

import java.time.LocalDateTime;
import java.util.List;

public class RecetaDTO {

    private Long idReceta;
    private String codigoReceta;
    private LocalDateTime fecha;
    private Long idUsuario;
    private String aprobadoSeguro;  
    private String pdfUrl;

    private List<RecetaDetalleDTO> detalles;

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

    public List<RecetaDetalleDTO> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<RecetaDetalleDTO> detalles) {
        this.detalles = detalles;
    }
}


