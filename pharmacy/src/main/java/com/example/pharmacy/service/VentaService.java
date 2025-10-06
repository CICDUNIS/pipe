package com.example.pharmacy.service;

import com.example.pharmacy.dto.VentaDTO;

public interface VentaService {

    VentaDTO createVenta(VentaDTO dto);

    VentaDTO getVentaWithDetails(Long idVenta);

    VentaDTO updateVenta(Long idVenta, VentaDTO dto);

    void deleteVenta(Long idVenta);
}



