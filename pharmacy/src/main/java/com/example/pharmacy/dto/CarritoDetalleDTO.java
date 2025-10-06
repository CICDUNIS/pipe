package com.example.pharmacy.dto;

public class CarritoDetalleDTO {
    private Long idCartItem;
    private Long idMedicamento;
    private String nombreMedicamento;
    private Integer cantidad;
    private Double precioUnitario;
    private Double total;
    private String requiereReceta;

    public Long getIdCartItem() {
        return idCartItem;
    }

    public void setIdCartItem(Long idCartItem) {
        this.idCartItem = idCartItem;
    }

    public Long getIdMedicamento() {
        return idMedicamento;
    }

    public void setIdMedicamento(Long idMedicamento) {
        this.idMedicamento = idMedicamento;
    }

    public String getNombreMedicamento() {
        return nombreMedicamento;
    }

    public void setNombreMedicamento(String nombreMedicamento) {
        this.nombreMedicamento = nombreMedicamento;
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

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getRequiereReceta() {
        return requiereReceta;
    }

    public void setRequiereReceta(String requiereReceta) {
        this.requiereReceta = requiereReceta;
    }
}
