package com.example.pharmacy.service;

import com.example.pharmacy.dto.FacturaDTO;
import com.example.pharmacy.model.Venta;

import java.io.ByteArrayOutputStream;

public interface FacturaService {
    
    FacturaDTO createFactura(Venta venta);
    
    FacturaDTO getFactura(Long idFactura);
    
    FacturaDTO getFacturaByVenta(Long idVenta);
    
    ByteArrayOutputStream generatePdfFactura(Long idFactura);
    
    void sendFacturaEmail(Long idFactura, String email);
} 