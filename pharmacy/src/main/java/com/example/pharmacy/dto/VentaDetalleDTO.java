package com.example.pharmacy.dto;

public class VentaDetalleDTO {

    private Long idVentaDetalle;
    private Long idMedicamento;
    private Integer cantidad;
    private Double precioUnitario;
    private Double totalLinea;

    public Long getIdVentaDetalle() {
        return idVentaDetalle;
    }

    public void setIdVentaDetalle(Long idVentaDetalle) {
        this.idVentaDetalle = idVentaDetalle;
    }

    public Long getIdMedicamento() {
        return idMedicamento;
    }

    public void setIdMedicamento(Long idMedicamento) {
        this.idMedicamento = idMedicamento;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(Double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public Double getTotalLinea() {
        return totalLinea;
    }

    public void setTotalLinea(Double totalLinea) {
        this.totalLinea = totalLinea;
    }


    public boolean isCantidadValida() {
        return cantidad != null && cantidad > 0;
    }

    @Override
    public String toString() {
        return "VentaDetalleDTO{" +
                "idVentaDetalle=" + idVentaDetalle +
                ", idMedicamento=" + idMedicamento +
                ", cantidad=" + cantidad +
                ", precioUnitario=" + precioUnitario +
                ", totalLinea=" + totalLinea +
                '}';
    }
}
