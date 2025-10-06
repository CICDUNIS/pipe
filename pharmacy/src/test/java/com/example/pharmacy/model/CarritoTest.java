package com.example.pharmacy.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CarritoTest {

    @Test
    void testGettersAndSetters() {
        Carrito carrito = new Carrito();

        Long idCart = 1L;
        Long idUsuario = 99L;
        String status = "ACTIVO";
        LocalDateTime fechaCreacion = LocalDateTime.of(2025, 8, 7, 10, 30);
        LocalDateTime fechaActualizacion = LocalDateTime.of(2025, 8, 7, 12, 0);

        carrito.setIdCart(idCart);
        carrito.setIdUsuario(idUsuario);
        carrito.setStatus(status);
        carrito.setFechaCreacion(fechaCreacion);
        carrito.setFechaActualizacion(fechaActualizacion);

        assertEquals(idCart, carrito.getIdCart());
        assertEquals(idUsuario, carrito.getIdUsuario());
        assertEquals(status, carrito.getStatus());
        assertEquals(fechaCreacion, carrito.getFechaCreacion());
        assertEquals(fechaActualizacion, carrito.getFechaActualizacion());
    }

    @Test
    void testValoresNulosIniciales() {
        Carrito carrito = new Carrito();

        assertNull(carrito.getIdCart());
        assertNull(carrito.getIdUsuario());
        assertNull(carrito.getStatus());
        assertNull(carrito.getFechaCreacion());
        assertNull(carrito.getFechaActualizacion());
    }
}
