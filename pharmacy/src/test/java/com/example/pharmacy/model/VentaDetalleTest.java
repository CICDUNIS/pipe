package com.example.pharmacy.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VentaDetalleTest {

    @Test
    void testGettersAndSetters() {
        VentaDetalle detalle = new VentaDetalle();

        Long idVentaDetalle = 10L;
        Long idVenta = 20L;
        Long idMedicamento = 30L;
        Integer cantidad = 5;
        Double precioUnitario = 15.75;
        Double totalLinea = 78.75;

        detalle.setIdVentaDetalle(idVentaDetalle);
        detalle.setIdVenta(idVenta);
        detalle.setIdMedicamento(idMedicamento);
        detalle.setCantidad(cantidad);
        detalle.setPrecioUnitario(precioUnitario);
        detalle.setTotalLinea(totalLinea);

        assertEquals(idVentaDetalle, detalle.getIdVentaDetalle());
        assertEquals(idVenta, detalle.getIdVenta());
        assertEquals(idMedicamento, detalle.getIdMedicamento());
        assertEquals(cantidad, detalle.getCantidad());
        assertEquals(precioUnitario, detalle.getPrecioUnitario());
        assertEquals(totalLinea, detalle.getTotalLinea());
    }
}
