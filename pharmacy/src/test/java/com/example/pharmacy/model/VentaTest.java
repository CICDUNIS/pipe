package com.example.pharmacy.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class VentaTest {

    @Test
    void testGettersAndSetters() {
        Venta venta = new Venta();

        Long idVenta = 1L;
        Long idUsuario = 2L;
        Long idReceta = 3L;
        LocalDateTime fechaVenta = LocalDateTime.now();
        Double total = 100.0;
        Double impuesto = 15.0;
        Double descuento = 5.0;
        Double montoPagado = 110.0;

        venta.setIdVenta(idVenta);
        venta.setIdUsuario(idUsuario);
        venta.setIdReceta(idReceta);
        venta.setFechaVenta(fechaVenta);
        venta.setTotal(total);
        venta.setImpuesto(impuesto);
        venta.setDescuento(descuento);
        venta.setMontoPagado(montoPagado);

        assertEquals(idVenta, venta.getIdVenta());
        assertEquals(idUsuario, venta.getIdUsuario());
        assertEquals(idReceta, venta.getIdReceta());
        assertEquals(fechaVenta, venta.getFechaVenta());
        assertEquals(total, venta.getTotal());
        assertEquals(impuesto, venta.getImpuesto());
        assertEquals(descuento, venta.getDescuento());
        assertEquals(montoPagado, venta.getMontoPagado());
    }
}
