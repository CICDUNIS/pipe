package com.example.pharmacy.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VentaDTOTest {

    private VentaDTO ventaDTO;
    private VentaDetalleDTO detalle1;
    private VentaDetalleDTO detalle2;

    @BeforeEach
    void setUp() {
        ventaDTO = new VentaDTO();
        
        detalle1 = new VentaDetalleDTO();
        detalle1.setIdVentaDetalle(1L);
        detalle1.setCantidad(2);
        detalle1.setPrecioUnitario(10.50);
        
        detalle2 = new VentaDetalleDTO();
        detalle2.setIdVentaDetalle(2L);
        detalle2.setCantidad(1);
        detalle2.setPrecioUnitario(25.00);
    }

    @Test
    void testGetIdVenta() {
        assertNull(ventaDTO.getIdVenta());
        
        ventaDTO.setIdVenta(100L);
        assertEquals(100L, ventaDTO.getIdVenta());
    }

    @Test
    void testSetIdVenta() {
        ventaDTO.setIdVenta(200L);
        assertEquals(200L, ventaDTO.getIdVenta());
        
        ventaDTO.setIdVenta(null);
        assertNull(ventaDTO.getIdVenta());
    }

    @Test
    void testGetIdUsuario() {
        assertNull(ventaDTO.getIdUsuario());
        
        ventaDTO.setIdUsuario(50L);
        assertEquals(50L, ventaDTO.getIdUsuario());
    }

    @Test
    void testSetIdUsuario() {
        ventaDTO.setIdUsuario(75L);
        assertEquals(75L, ventaDTO.getIdUsuario());
        
        ventaDTO.setIdUsuario(null);
        assertNull(ventaDTO.getIdUsuario());
    }

    @Test
    void testGetIdReceta() {
        assertNull(ventaDTO.getIdReceta());
        
        ventaDTO.setIdReceta(25L);
        assertEquals(25L, ventaDTO.getIdReceta());
    }

    @Test
    void testSetIdReceta() {
        ventaDTO.setIdReceta(30L);
        assertEquals(30L, ventaDTO.getIdReceta());
        
        ventaDTO.setIdReceta(null);
        assertNull(ventaDTO.getIdReceta());
    }

    @Test
    void testGetCodigoReceta() {
        assertNull(ventaDTO.getCodigoReceta());
        
        ventaDTO.setCodigoReceta("REC-12345");
        assertEquals("REC-12345", ventaDTO.getCodigoReceta());
    }

    @Test
    void testSetCodigoReceta() {
        ventaDTO.setCodigoReceta("REC-ABC123");
        assertEquals("REC-ABC123", ventaDTO.getCodigoReceta());
        
        ventaDTO.setCodigoReceta("");
        assertEquals("", ventaDTO.getCodigoReceta());
        
        ventaDTO.setCodigoReceta(null);
        assertNull(ventaDTO.getCodigoReceta());
    }

    @Test
    void testGetNumeroAfiliacion() {
        assertNull(ventaDTO.getNumeroAfiliacion());
        
        ventaDTO.setNumeroAfiliacion("AFI-98765");
        assertEquals("AFI-98765", ventaDTO.getNumeroAfiliacion());
    }

    @Test
    void testSetNumeroAfiliacion() {
        ventaDTO.setNumeroAfiliacion("AFI-XYZ789");
        assertEquals("AFI-XYZ789", ventaDTO.getNumeroAfiliacion());
        
        ventaDTO.setNumeroAfiliacion("");
        assertEquals("", ventaDTO.getNumeroAfiliacion());
        
        ventaDTO.setNumeroAfiliacion(null);
        assertNull(ventaDTO.getNumeroAfiliacion());
    }

    @Test
    void testGetFechaVenta() {
        assertNull(ventaDTO.getFechaVenta());
        
        LocalDateTime fecha = LocalDateTime.now();
        ventaDTO.setFechaVenta(fecha);
        assertEquals(fecha, ventaDTO.getFechaVenta());
    }

    @Test
    void testSetFechaVenta() {
        LocalDateTime fecha1 = LocalDateTime.of(2024, 1, 15, 10, 30);
        ventaDTO.setFechaVenta(fecha1);
        assertEquals(fecha1, ventaDTO.getFechaVenta());
        
        LocalDateTime fecha2 = LocalDateTime.of(2024, 12, 31, 23, 59);
        ventaDTO.setFechaVenta(fecha2);
        assertEquals(fecha2, ventaDTO.getFechaVenta());
        
        ventaDTO.setFechaVenta(null);
        assertNull(ventaDTO.getFechaVenta());
    }

    @Test
    void testGetTotal() {
        assertNull(ventaDTO.getTotal());
        
        ventaDTO.setTotal(150.75);
        assertEquals(150.75, ventaDTO.getTotal());
    }

    @Test
    void testSetTotal() {
        ventaDTO.setTotal(99.99);
        assertEquals(99.99, ventaDTO.getTotal());
        
        ventaDTO.setTotal(0.0);
        assertEquals(0.0, ventaDTO.getTotal());
        
        ventaDTO.setTotal(null);
        assertNull(ventaDTO.getTotal());
    }

    @Test
    void testGetImpuesto() {
        assertNull(ventaDTO.getImpuesto());
        
        ventaDTO.setImpuesto(15.50);
        assertEquals(15.50, ventaDTO.getImpuesto());
    }

    @Test
    void testSetImpuesto() {
        ventaDTO.setImpuesto(12.00);
        assertEquals(12.00, ventaDTO.getImpuesto());
        
        ventaDTO.setImpuesto(0.0);
        assertEquals(0.0, ventaDTO.getImpuesto());
        
        ventaDTO.setImpuesto(null);
        assertNull(ventaDTO.getImpuesto());
    }

    @Test
    void testGetDescuento() {
        assertNull(ventaDTO.getDescuento());
        
        ventaDTO.setDescuento(25.00);
        assertEquals(25.00, ventaDTO.getDescuento());
    }

    @Test
    void testSetDescuento() {
        ventaDTO.setDescuento(10.50);
        assertEquals(10.50, ventaDTO.getDescuento());
        
        ventaDTO.setDescuento(0.0);
        assertEquals(0.0, ventaDTO.getDescuento());
        
        ventaDTO.setDescuento(null);
        assertNull(ventaDTO.getDescuento());
    }

    @Test
    void testGetMontoPagado() {
        assertNull(ventaDTO.getMontoPagado());
        
        ventaDTO.setMontoPagado(200.00);
        assertEquals(200.00, ventaDTO.getMontoPagado());
    }

    @Test
    void testSetMontoPagado() {
        ventaDTO.setMontoPagado(175.25);
        assertEquals(175.25, ventaDTO.getMontoPagado());
        
        ventaDTO.setMontoPagado(0.0);
        assertEquals(0.0, ventaDTO.getMontoPagado());
        
        ventaDTO.setMontoPagado(null);
        assertNull(ventaDTO.getMontoPagado());
    }

    @Test
    void testGetDetalles() {
        assertNull(ventaDTO.getDetalles());
        
        List<VentaDetalleDTO> detalles = Arrays.asList(detalle1, detalle2);
        ventaDTO.setDetalles(detalles);
        assertEquals(detalles, ventaDTO.getDetalles());
    }

    @Test
    void testSetDetalles() {
        List<VentaDetalleDTO> detalles = new ArrayList<>();
        detalles.add(detalle1);
        ventaDTO.setDetalles(detalles);
        assertEquals(1, ventaDTO.getDetalles().size());
        assertEquals(detalle1, ventaDTO.getDetalles().get(0));
        
        ventaDTO.setDetalles(null);
        assertNull(ventaDTO.getDetalles());
    }

    @Test
    void testIsCodigoRecetaValido_ValidCode() {
        ventaDTO.setCodigoReceta("REC-12345");
        assertTrue(ventaDTO.isCodigoRecetaValido());
        
        ventaDTO.setCodigoReceta("REC-ABC123");
        assertTrue(ventaDTO.isCodigoRecetaValido());
        
        ventaDTO.setCodigoReceta("REC-123ABC");
        assertTrue(ventaDTO.isCodigoRecetaValido());
    }

    @Test
    void testIsCodigoRecetaValido_InvalidCode() {
        ventaDTO.setCodigoReceta("12345");
        assertFalse(ventaDTO.isCodigoRecetaValido());
        
        ventaDTO.setCodigoReceta("REC");
        assertFalse(ventaDTO.isCodigoRecetaValido());
        
        ventaDTO.setCodigoReceta("rec-12345");
        assertFalse(ventaDTO.isCodigoRecetaValido());
        
        ventaDTO.setCodigoReceta("REC-123-45");
        assertFalse(ventaDTO.isCodigoRecetaValido());
        
        ventaDTO.setCodigoReceta(null);
        assertFalse(ventaDTO.isCodigoRecetaValido());
        
        ventaDTO.setCodigoReceta("");
        assertFalse(ventaDTO.isCodigoRecetaValido());
    }

    @Test
    void testIsNumeroAfiliacionValido_ValidNumber() {
        ventaDTO.setNumeroAfiliacion("AFI-98765");
        assertTrue(ventaDTO.isNumeroAfiliacionValido());
        
        ventaDTO.setNumeroAfiliacion("AFI-XYZ789");
        assertTrue(ventaDTO.isNumeroAfiliacionValido());
        
        ventaDTO.setNumeroAfiliacion("AFI-123ABC");
        assertTrue(ventaDTO.isNumeroAfiliacionValido());
    }

    @Test
    void testIsNumeroAfiliacionValido_InvalidNumber() {
        ventaDTO.setNumeroAfiliacion("98765");
        assertFalse(ventaDTO.isNumeroAfiliacionValido());
        
        ventaDTO.setNumeroAfiliacion("AFI");
        assertFalse(ventaDTO.isNumeroAfiliacionValido());
        
        ventaDTO.setNumeroAfiliacion("afi-98765");
        assertFalse(ventaDTO.isNumeroAfiliacionValido());
        
        ventaDTO.setNumeroAfiliacion("AFI-987-65");
        assertFalse(ventaDTO.isNumeroAfiliacionValido());
        
        ventaDTO.setNumeroAfiliacion(null);
        assertFalse(ventaDTO.isNumeroAfiliacionValido());
        
        ventaDTO.setNumeroAfiliacion("");
        assertFalse(ventaDTO.isNumeroAfiliacionValido());
    }

    @Test
    void testIsDetallesValidos_ValidDetails() {
        List<VentaDetalleDTO> detalles = Arrays.asList(detalle1, detalle2);
        ventaDTO.setDetalles(detalles);
        assertTrue(ventaDTO.isDetallesValidos());
    }

    @Test
    void testIsDetallesValidos_InvalidDetails() {
        // Null detalles
        ventaDTO.setDetalles(null);
        assertFalse(ventaDTO.isDetallesValidos());
        
        // Empty detalles
        ventaDTO.setDetalles(new ArrayList<>());
        assertFalse(ventaDTO.isDetallesValidos());
        
        // Detalle with null cantidad
        VentaDetalleDTO detalleInvalido = new VentaDetalleDTO();
        detalleInvalido.setCantidad(null);
        List<VentaDetalleDTO> detallesInvalidos = Arrays.asList(detalleInvalido);
        ventaDTO.setDetalles(detallesInvalidos);
        assertFalse(ventaDTO.isDetallesValidos());
        
        // Detalle with cantidad <= 0
        VentaDetalleDTO detalleCero = new VentaDetalleDTO();
        detalleCero.setCantidad(0);
        List<VentaDetalleDTO> detallesCero = Arrays.asList(detalleCero);
        ventaDTO.setDetalles(detallesCero);
        assertFalse(ventaDTO.isDetallesValidos());
        
        VentaDetalleDTO detalleNegativo = new VentaDetalleDTO();
        detalleNegativo.setCantidad(-1);
        List<VentaDetalleDTO> detallesNegativos = Arrays.asList(detalleNegativo);
        ventaDTO.setDetalles(detallesNegativos);
        assertFalse(ventaDTO.isDetallesValidos());
    }

    @Test
    void testIsDetallesValidos_MixedDetails() {
        // One valid, one invalid
        VentaDetalleDTO detalleInvalido = new VentaDetalleDTO();
        detalleInvalido.setCantidad(0);
        
        List<VentaDetalleDTO> detallesMixtos = Arrays.asList(detalle1, detalleInvalido);
        ventaDTO.setDetalles(detallesMixtos);
        assertFalse(ventaDTO.isDetallesValidos());
    }

    @Test
    void testToString() {
        ventaDTO.setIdVenta(100L);
        ventaDTO.setIdUsuario(50L);
        ventaDTO.setCodigoReceta("REC-12345");
        ventaDTO.setTotal(150.75);
        
        String result = ventaDTO.toString();
        
        assertTrue(result.contains("idVenta=100"));
        assertTrue(result.contains("idUsuario=50"));
        assertTrue(result.contains("codigoReceta='REC-12345'"));
        assertTrue(result.contains("total=150.75"));
        assertTrue(result.startsWith("VentaDTO{"));
        assertTrue(result.endsWith("}"));
    }

    @Test
    void testToString_WithNullValues() {
        String result = ventaDTO.toString();
        
        assertTrue(result.contains("idVenta=null"));
        assertTrue(result.contains("idUsuario=null"));
        assertTrue(result.contains("codigoReceta='null'"));
        assertTrue(result.contains("total=null"));
    }

    @Test
    void testToString_WithAllValues() {
        LocalDateTime fecha = LocalDateTime.of(2024, 1, 15, 10, 30);
        List<VentaDetalleDTO> detalles = Arrays.asList(detalle1);
        
        ventaDTO.setIdVenta(200L);
        ventaDTO.setIdUsuario(75L);
        ventaDTO.setIdReceta(25L);
        ventaDTO.setCodigoReceta("REC-ABC123");
        ventaDTO.setNumeroAfiliacion("AFI-XYZ789");
        ventaDTO.setFechaVenta(fecha);
        ventaDTO.setTotal(200.00);
        ventaDTO.setImpuesto(20.00);
        ventaDTO.setDescuento(10.00);
        ventaDTO.setMontoPagado(210.00);
        ventaDTO.setDetalles(detalles);
        
        String result = ventaDTO.toString();
        
        assertTrue(result.contains("idVenta=200"));
        assertTrue(result.contains("idUsuario=75"));
        assertTrue(result.contains("idReceta=25"));
        assertTrue(result.contains("codigoReceta='REC-ABC123'"));
        assertTrue(result.contains("numeroAfiliacion='AFI-XYZ789'"));
        assertTrue(result.contains("fechaVenta=" + fecha));
        assertTrue(result.contains("total=200.0"));
        assertTrue(result.contains("impuesto=20.0"));
        assertTrue(result.contains("descuento=10.0"));
        assertTrue(result.contains("montoPagado=210.0"));
        assertTrue(result.contains("detalles=" + detalles));
    }
}


