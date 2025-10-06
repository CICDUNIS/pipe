package com.example.pharmacy.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CheckoutDTO {
    
    private Long idCart;
    private Double descuento;
    private String email;
    
    public Long getIdCart() {
        return idCart;
    }
    
    public void setIdCart(Long idCart) {
        this.idCart = idCart;
    }
    
    public Double getDescuento() {
        return descuento;
    }
    
    public void setDescuento(Double descuento) {
        this.descuento = descuento;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
} 