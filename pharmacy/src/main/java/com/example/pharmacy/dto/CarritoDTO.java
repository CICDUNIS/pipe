package com.example.pharmacy.dto;

import java.time.LocalDateTime;
import java.util.List;

public class CarritoDTO {
    private Long idCart;
    private Long idUsuario;
    private String status;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    private List<CarritoDetalleDTO> items;
    private Double total;

    public Long getIdCart() {
        return idCart;
    }

    public void setIdCart(Long idCart) {
        this.idCart = idCart;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public List<CarritoDetalleDTO> getItems() {
        return items;
    }

    public void setItems(List<CarritoDetalleDTO> items) {
        this.items = items;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
}
