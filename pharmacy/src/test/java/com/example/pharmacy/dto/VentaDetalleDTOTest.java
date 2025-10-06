package com.example.pharmacy.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class VentaDetalleDTOTest {

    private VentaDetalleDTO ventaDetalleDTO;

    @BeforeEach
    void setUp() {
        ventaDetalleDTO = new VentaDetalleDTO();
    }

    @Test
    void testGetIdVentaDetalle() {
        assertNull(ventaDetalleDTO.getIdVentaDetalle());
        
        ventaDetalleDTO.setIdVentaDetalle(100L);
        assertEquals(100L, ventaDetalleDTO.getIdVentaDetalle());
    }

    @Test
    void testSetIdVentaDetalle() {
        ventaDetalleDTO.setIdVentaDetalle(200L);
        assertEquals(200L, ventaDetalleDTO.getIdVentaDetalle());
        
        ventaDetalleDTO.setIdVentaDetalle(null);
        assertNull(ventaDetalleDTO.getIdVentaDetalle());
    }

    @Test
    void testGetIdMedicamento() {
        assertNull(ventaDetalleDTO.getIdMedicamento());
        
        ventaDetalleDTO.setIdMedicamento(50L);
        assertEquals(50L, ventaDetalleDTO.getIdMedicamento());
    }

    @Test
    void testSetIdMedicamento() {
        ventaDetalleDTO.setIdMedicamento(75L);
        assertEquals(75L, ventaDetalleDTO.getIdMedicamento());
        
        ventaDetalleDTO.setIdMedicamento(null);
        assertNull(ventaDetalleDTO.getIdMedicamento());
    }

    @Test
    void testGetCantidad() {
        assertNull(ventaDetalleDTO.getCantidad());
        
        ventaDetalleDTO.setCantidad(5);
        assertEquals(5, ventaDetalleDTO.getCantidad());
    }

    @Test
    void testSetCantidad() {
        ventaDetalleDTO.setCantidad(10);
        assertEquals(10, ventaDetalleDTO.getCantidad());
        
        ventaDetalleDTO.setCantidad(0);
        assertEquals(0, ventaDetalleDTO.getCantidad());
        
        ventaDetalleDTO.setCantidad(null);
        assertNull(ventaDetalleDTO.getCantidad());
    }

    @Test
    void testGetPrecioUnitario() {
        assertNull(ventaDetalleDTO.getPrecioUnitario());
        
        ventaDetalleDTO.setPrecioUnitario(15.50);
        assertEquals(15.50, ventaDetalleDTO.getPrecioUnitario());
    }

    @Test
    void testSetPrecioUnitario() {
        ventaDetalleDTO.setPrecioUnitario(25.75);
        assertEquals(25.75, ventaDetalleDTO.getPrecioUnitario());
        
        ventaDetalleDTO.setPrecioUnitario(0.0);
        assertEquals(0.0, ventaDetalleDTO.getPrecioUnitario());
        
        ventaDetalleDTO.setPrecioUnitario(null);
        assertNull(ventaDetalleDTO.getPrecioUnitario());
    }

    @Test
    void testGetTotalLinea() {
        assertNull(ventaDetalleDTO.getTotalLinea());
        
        ventaDetalleDTO.setTotalLinea(77.50);
        assertEquals(77.50, ventaDetalleDTO.getTotalLinea());
    }

    @Test
    void testSetTotalLinea() {
        ventaDetalleDTO.setTotalLinea(99.99);
        assertEquals(99.99, ventaDetalleDTO.getTotalLinea());
        
        ventaDetalleDTO.setTotalLinea(0.0);
        assertEquals(0.0, ventaDetalleDTO.getTotalLinea());
        
        ventaDetalleDTO.setTotalLinea(null);
        assertNull(ventaDetalleDTO.getTotalLinea());
    }

    @Test
    void testIsCantidadValida_ValidQuantity() {
        ventaDetalleDTO.setCantidad(1);
        assertTrue(ventaDetalleDTO.isCantidadValida());
        
        ventaDetalleDTO.setCantidad(5);
        assertTrue(ventaDetalleDTO.isCantidadValida());
        
        ventaDetalleDTO.setCantidad(100);
        assertTrue(ventaDetalleDTO.isCantidadValida());
    }

    @Test
    void testIsCantidadValida_InvalidQuantity() {
        // Null cantidad
        ventaDetalleDTO.setCantidad(null);
        assertFalse(ventaDetalleDTO.isCantidadValida());
        
        // Cantidad <= 0
        ventaDetalleDTO.setCantidad(0);
        assertFalse(ventaDetalleDTO.isCantidadValida());
        
        ventaDetalleDTO.setCantidad(-1);
        assertFalse(ventaDetalleDTO.isCantidadValida());
        
        ventaDetalleDTO.setCantidad(-10);
        assertFalse(ventaDetalleDTO.isCantidadValida());
    }

    @Test
    void testIsCantidadValida_EdgeCases() {
        // Cantidad mínima válida
        ventaDetalleDTO.setCantidad(1);
        assertTrue(ventaDetalleDTO.isCantidadValida());
        
        // Cantidad máxima (Integer.MAX_VALUE)
        ventaDetalleDTO.setCantidad(Integer.MAX_VALUE);
        assertTrue(ventaDetalleDTO.isCantidadValida());
    }

    @Test
    void testToString() {
        ventaDetalleDTO.setIdVentaDetalle(100L);
        ventaDetalleDTO.setIdMedicamento(50L);
        ventaDetalleDTO.setCantidad(3);
        ventaDetalleDTO.setPrecioUnitario(12.50);
        ventaDetalleDTO.setTotalLinea(37.50);
        
        String result = ventaDetalleDTO.toString();
        
        assertTrue(result.contains("idVentaDetalle=100"));
        assertTrue(result.contains("idMedicamento=50"));
        assertTrue(result.contains("cantidad=3"));
        assertTrue(result.contains("precioUnitario=12.5"));
        assertTrue(result.contains("totalLinea=37.5"));
        assertTrue(result.startsWith("VentaDetalleDTO{"));
        assertTrue(result.endsWith("}"));
    }

    @Test
    void testToString_WithNullValues() {
        String result = ventaDetalleDTO.toString();
        
        assertTrue(result.contains("idVentaDetalle=null"));
        assertTrue(result.contains("idMedicamento=null"));
        assertTrue(result.contains("cantidad=null"));
        assertTrue(result.contains("precioUnitario=null"));
        assertTrue(result.contains("totalLinea=null"));
    }

    @Test
    void testToString_WithAllValues() {
        ventaDetalleDTO.setIdVentaDetalle(200L);
        ventaDetalleDTO.setIdMedicamento(75L);
        ventaDetalleDTO.setCantidad(2);
        ventaDetalleDTO.setPrecioUnitario(25.00);
        ventaDetalleDTO.setTotalLinea(50.00);
        
        String result = ventaDetalleDTO.toString();
        
        assertTrue(result.contains("idVentaDetalle=200"));
        assertTrue(result.contains("idMedicamento=75"));
        assertTrue(result.contains("cantidad=2"));
        assertTrue(result.contains("precioUnitario=25.0"));
        assertTrue(result.contains("totalLinea=50.0"));
    }

    @Test
    void testToString_WithZeroValues() {
        ventaDetalleDTO.setIdVentaDetalle(0L);
        ventaDetalleDTO.setIdMedicamento(0L);
        ventaDetalleDTO.setCantidad(0);
        ventaDetalleDTO.setPrecioUnitario(0.0);
        ventaDetalleDTO.setTotalLinea(0.0);
        
        String result = ventaDetalleDTO.toString();
        
        assertTrue(result.contains("idVentaDetalle=0"));
        assertTrue(result.contains("idMedicamento=0"));
        assertTrue(result.contains("cantidad=0"));
        assertTrue(result.contains("precioUnitario=0.0"));
        assertTrue(result.contains("totalLinea=0.0"));
    }

    @Test
    void testToString_WithNegativeValues() {
        ventaDetalleDTO.setIdVentaDetalle(-1L);
        ventaDetalleDTO.setIdMedicamento(-1L);
        ventaDetalleDTO.setCantidad(-5);
        ventaDetalleDTO.setPrecioUnitario(-10.50);
        ventaDetalleDTO.setTotalLinea(-52.50);
        
        String result = ventaDetalleDTO.toString();
        
        assertTrue(result.contains("idVentaDetalle=-1"));
        assertTrue(result.contains("idMedicamento=-1"));
        assertTrue(result.contains("cantidad=-5"));
        assertTrue(result.contains("precioUnitario=-10.5"));
        assertTrue(result.contains("totalLinea=-52.5"));
    }

    @Test
    void testToString_WithDecimalValues() {
        ventaDetalleDTO.setIdVentaDetalle(123L);
        ventaDetalleDTO.setIdMedicamento(456L);
        ventaDetalleDTO.setCantidad(7);
        ventaDetalleDTO.setPrecioUnitario(12.345);
        ventaDetalleDTO.setTotalLinea(86.415);
        
        String result = ventaDetalleDTO.toString();
        
        assertTrue(result.contains("idVentaDetalle=123"));
        assertTrue(result.contains("idMedicamento=456"));
        assertTrue(result.contains("cantidad=7"));
        assertTrue(result.contains("precioUnitario=12.345"));
        assertTrue(result.contains("totalLinea=86.415"));
    }
}


