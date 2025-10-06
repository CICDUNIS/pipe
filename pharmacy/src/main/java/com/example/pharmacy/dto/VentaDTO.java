package com.example.pharmacy.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class VentaDTO {

    private Long idVenta;
    private Long idUsuario;
    private Long idReceta;
    private String codigoReceta;
    private String numeroAfiliacion;
    private LocalDateTime fechaVenta;
    private Double total;
    private Double impuesto;
    private Double descuento;
    private Double montoPagado;
    private List<VentaDetalleDTO> detalles;

    public Long getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(Long idVenta) {
        this.idVenta = idVenta;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

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

    public String getNumeroAfiliacion() {
        return numeroAfiliacion;
    }

    public void setNumeroAfiliacion(String numeroAfiliacion) {
        this.numeroAfiliacion = numeroAfiliacion;
    }

    public LocalDateTime getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(LocalDateTime fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Double getImpuesto() {
        return impuesto;
    }

    public void setImpuesto(Double impuesto) {
        this.impuesto = impuesto;
    }

    public Double getDescuento() {
        return descuento;
    }

    public void setDescuento(Double descuento) {
        this.descuento = descuento;
    }

    public Double getMontoPagado() {
        return montoPagado;
    }

    public void setMontoPagado(Double montoPagado) {
        this.montoPagado = montoPagado;
    }

    public List<VentaDetalleDTO> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<VentaDetalleDTO> detalles) {
        this.detalles = detalles;
    }


    public boolean isCodigoRecetaValido() {
        return codigoReceta != null && codigoReceta.matches("^REC-[A-Za-z0-9]+$");
    }

    public boolean isNumeroAfiliacionValido() {
        return numeroAfiliacion != null && numeroAfiliacion.matches("^AFI-[A-Za-z0-9]+$");
    }

    public boolean isDetallesValidos() {
        return detalles != null && !detalles.isEmpty() &&
                detalles.stream().allMatch(d -> d.getCantidad() != null && d.getCantidad() > 0);
    }

    @Override
    public String toString() {
        return "VentaDTO{" +
                "idVenta=" + idVenta +
                ", idUsuario=" + idUsuario +
                ", idReceta=" + idReceta +
                ", codigoReceta='" + codigoReceta + '\'' +
                ", numeroAfiliacion='" + numeroAfiliacion + '\'' +
                ", fechaVenta=" + fechaVenta +
                ", total=" + total +
                ", impuesto=" + impuesto +
                ", descuento=" + descuento +
                ", montoPagado=" + montoPagado +
                ", detalles=" + detalles +
                '}';
    }
}
