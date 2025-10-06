package com.example.pharmacy.service;

import com.example.pharmacy.dto.CarritoDTO;
import com.example.pharmacy.dto.FacturaDTO;

public interface CarritoService {
    CarritoDTO getActiveCart(Long idUsuario);
    CarritoDTO addItem(Long idUsuario, Long idMedicamento, Integer cantidad);
    CarritoDTO updateItemQuantity(Long idUsuario, Long idMedicamento, Integer cantidad);
    CarritoDTO removeItem(Long idUsuario, Long idMedicamento);
    CarritoDTO checkout(Long idUsuario);
    FacturaDTO checkoutWithDiscount(Long idUsuario, Double descuento);
    void clearCart(Long idUsuario);
} 