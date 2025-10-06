package com.example.pharmacy.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CarritoDetalleTest {

    @Test
    void testGettersAndSetters() {
        CarritoDetalle detalle = new CarritoDetalle();

        Long idCartItem = 10L;
        Long idCart = 5L;
        Long idMedicamento = 42L;
        Integer cantidad = 3;
        Double precioUnitario = 15.99;

        detalle.setIdCartItem(idCartItem);
        detalle.setIdCart(idCart);
        detalle.setIdMedicamento(idMedicamento);
        detalle.setCantidad(cantidad);
        detalle.setPrecioUnitario(precioUnitario);

        assertEquals(idCartItem, detalle.getIdCartItem());
        assertEquals(idCart, detalle.getIdCart());
        assertEquals(idMedicamento, detalle.getIdMedicamento());
        assertEquals(cantidad, detalle.getCantidad());
        assertEquals(precioUnitario, detalle.getPrecioUnitario());
    }

    @Test
    void testValoresNulosIniciales() {
        CarritoDetalle detalle = new CarritoDetalle();

        assertNull(detalle.getIdCartItem());
        assertNull(detalle.getIdCart());
        assertNull(detalle.getIdMedicamento());
        assertNull(detalle.getCantidad());
        assertNull(detalle.getPrecioUnitario());
    }
}
