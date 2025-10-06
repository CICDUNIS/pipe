package com.example.pharmacy.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AutorizacionMedicamentoDTOTest {

    private AutorizacionMedicamentoDTO autorizacionDTO;

    @BeforeEach
    void setUp() {
        autorizacionDTO = new AutorizacionMedicamentoDTO();
    }

    @Test
    void testGetAutorizacion() {
        assertNull(autorizacionDTO.getAutorizacion());
        
        autorizacionDTO.setAutorizacion("AUTH-001");
        assertEquals("AUTH-001", autorizacionDTO.getAutorizacion());
    }

    @Test
    void testSetAutorizacion() {
        autorizacionDTO.setAutorizacion("AUTH-ABC123");
        assertEquals("AUTH-ABC123", autorizacionDTO.getAutorizacion());
        
        autorizacionDTO.setAutorizacion("");
        assertEquals("", autorizacionDTO.getAutorizacion());
        
        autorizacionDTO.setAutorizacion(null);
        assertNull(autorizacionDTO.getAutorizacion());
    }

    @Test
    void testGetMontoAutorizado() {
        assertNull(autorizacionDTO.getMontoAutorizado());
        
        autorizacionDTO.setMontoAutorizado(100.50);
        assertEquals(100.50, autorizacionDTO.getMontoAutorizado());
    }

    @Test
    void testSetMontoAutorizado() {
        autorizacionDTO.setMontoAutorizado(200.00);
        assertEquals(200.00, autorizacionDTO.getMontoAutorizado());
        
        autorizacionDTO.setMontoAutorizado(0.0);
        assertEquals(0.0, autorizacionDTO.getMontoAutorizado());
        
        autorizacionDTO.setMontoAutorizado(null);
        assertNull(autorizacionDTO.getMontoAutorizado());
    }

    @Test
    void testGetCopago() {
        assertNull(autorizacionDTO.getCopago());
        
        autorizacionDTO.setCopago(25.00);
        assertEquals(25.00, autorizacionDTO.getCopago());
    }

    @Test
    void testSetCopago() {
        autorizacionDTO.setCopago(30.50);
        assertEquals(30.50, autorizacionDTO.getCopago());
        
        autorizacionDTO.setCopago(0.0);
        assertEquals(0.0, autorizacionDTO.getCopago());
        
        autorizacionDTO.setCopago(null);
        assertNull(autorizacionDTO.getCopago());
    }

    @Test
    void testGetEstado() {
        assertNull(autorizacionDTO.getEstado());
        
        autorizacionDTO.setEstado("APROBADO");
        assertEquals("APROBADO", autorizacionDTO.getEstado());
    }

    @Test
    void testSetEstado() {
        autorizacionDTO.setEstado("PENDIENTE");
        assertEquals("PENDIENTE", autorizacionDTO.getEstado());
        
        autorizacionDTO.setEstado("");
        assertEquals("", autorizacionDTO.getEstado());
        
        autorizacionDTO.setEstado(null);
        assertNull(autorizacionDTO.getEstado());
    }

    @Test
    void testGetMensaje() {
        assertNull(autorizacionDTO.getMensaje());
        
        autorizacionDTO.setMensaje("Autorización aprobada");
        assertEquals("Autorización aprobada", autorizacionDTO.getMensaje());
    }

    @Test
    void testSetMensaje() {
        autorizacionDTO.setMensaje("Requiere documentación adicional");
        assertEquals("Requiere documentación adicional", autorizacionDTO.getMensaje());
        
        autorizacionDTO.setMensaje("");
        assertEquals("", autorizacionDTO.getMensaje());
        
        autorizacionDTO.setMensaje(null);
        assertNull(autorizacionDTO.getMensaje());
    }

    @Test
    void testSetAutorizacion_WithDifferentValues() {
        autorizacionDTO.setAutorizacion("AUTH-123");
        assertEquals("AUTH-123", autorizacionDTO.getAutorizacion());
        
        autorizacionDTO.setAutorizacion("ABC-456");
        assertEquals("ABC-456", autorizacionDTO.getAutorizacion());
        
        autorizacionDTO.setAutorizacion("XYZ789");
        assertEquals("XYZ789", autorizacionDTO.getAutorizacion());
    }

    @Test
    void testSetMontoAutorizado_WithDifferentValues() {
        autorizacionDTO.setMontoAutorizado(50.0);
        assertEquals(50.0, autorizacionDTO.getMontoAutorizado());
        
        autorizacionDTO.setMontoAutorizado(99.99);
        assertEquals(99.99, autorizacionDTO.getMontoAutorizado());
        
        autorizacionDTO.setMontoAutorizado(0.01);
        assertEquals(0.01, autorizacionDTO.getMontoAutorizado());
    }

    @Test
    void testSetCopago_WithDifferentValues() {
        autorizacionDTO.setCopago(10.0);
        assertEquals(10.0, autorizacionDTO.getCopago());
        
        autorizacionDTO.setCopago(25.50);
        assertEquals(25.50, autorizacionDTO.getCopago());
        
        autorizacionDTO.setCopago(0.99);
        assertEquals(0.99, autorizacionDTO.getCopago());
    }

    @Test
    void testSetEstado_WithDifferentValues() {
        autorizacionDTO.setEstado("APROBADO");
        assertEquals("APROBADO", autorizacionDTO.getEstado());
        
        autorizacionDTO.setEstado("RECHAZADO");
        assertEquals("RECHAZADO", autorizacionDTO.getEstado());
        
        autorizacionDTO.setEstado("PENDIENTE");
        assertEquals("PENDIENTE", autorizacionDTO.getEstado());
    }

    @Test
    void testSetMensaje_WithDifferentValues() {
        autorizacionDTO.setMensaje("Mensaje 1");
        assertEquals("Mensaje 1", autorizacionDTO.getMensaje());
        
        autorizacionDTO.setMensaje("Mensaje 2");
        assertEquals("Mensaje 2", autorizacionDTO.getMensaje());
        
        autorizacionDTO.setMensaje("Test");
        assertEquals("Test", autorizacionDTO.getMensaje());
    }

    @Test
    void testSetMontoAutorizado_WithEdgeCases() {
        autorizacionDTO.setMontoAutorizado(Double.MAX_VALUE);
        assertEquals(Double.MAX_VALUE, autorizacionDTO.getMontoAutorizado());
        
        autorizacionDTO.setMontoAutorizado(Double.MIN_VALUE);
        assertEquals(Double.MIN_VALUE, autorizacionDTO.getMontoAutorizado());
        
        autorizacionDTO.setMontoAutorizado(Double.POSITIVE_INFINITY);
        assertEquals(Double.POSITIVE_INFINITY, autorizacionDTO.getMontoAutorizado());
        
        autorizacionDTO.setMontoAutorizado(Double.NEGATIVE_INFINITY);
        assertEquals(Double.NEGATIVE_INFINITY, autorizacionDTO.getMontoAutorizado());
        
        autorizacionDTO.setMontoAutorizado(Double.NaN);
        assertTrue(Double.isNaN(autorizacionDTO.getMontoAutorizado()));
    }

    @Test
    void testSetCopago_WithEdgeCases() {
        autorizacionDTO.setCopago(Double.MAX_VALUE);
        assertEquals(Double.MAX_VALUE, autorizacionDTO.getCopago());
        
        autorizacionDTO.setCopago(Double.MIN_VALUE);
        assertEquals(Double.MIN_VALUE, autorizacionDTO.getCopago());
        
        autorizacionDTO.setCopago(Double.POSITIVE_INFINITY);
        assertEquals(Double.POSITIVE_INFINITY, autorizacionDTO.getCopago());
        
        autorizacionDTO.setCopago(Double.NEGATIVE_INFINITY);
        assertEquals(Double.NEGATIVE_INFINITY, autorizacionDTO.getCopago());
        
        autorizacionDTO.setCopago(Double.NaN);
        assertTrue(Double.isNaN(autorizacionDTO.getCopago()));
    }

    @Test
    void testToString() {
        autorizacionDTO.setAutorizacion("AUTH-001");
        autorizacionDTO.setMontoAutorizado(100.50);
        autorizacionDTO.setCopago(25.00);
        autorizacionDTO.setEstado("APROBADO");
        autorizacionDTO.setMensaje("Autorización aprobada");
        
        String result = autorizacionDTO.toString();
        
        assertTrue(result.contains("autorizacion='AUTH-001'"));
        assertTrue(result.contains("montoAutorizado=100.5"));
        assertTrue(result.contains("copago=25.0"));
        assertTrue(result.contains("estado='APROBADO'"));
        assertTrue(result.contains("mensaje='Autorización aprobada'"));
        assertTrue(result.startsWith("AutorizacionMedicamentoDTO{"));
        assertTrue(result.endsWith("}"));
    }

    @Test
    void testToString_WithNullValues() {
        String result = autorizacionDTO.toString();
        
        assertTrue(result.contains("autorizacion='null'"));
        assertTrue(result.contains("montoAutorizado=null"));
        assertTrue(result.contains("copago=null"));
        assertTrue(result.contains("estado='null'"));
        assertTrue(result.contains("mensaje='null'"));
    }

    @Test
    void testToString_WithAllValues() {
        autorizacionDTO.setAutorizacion("AUTH-ABC123");
        autorizacionDTO.setMontoAutorizado(200.00);
        autorizacionDTO.setCopago(50.00);
        autorizacionDTO.setEstado("PENDIENTE");
        autorizacionDTO.setMensaje("Requiere documentación");
        
        String result = autorizacionDTO.toString();
        
        assertTrue(result.contains("autorizacion='AUTH-ABC123'"));
        assertTrue(result.contains("montoAutorizado=200.0"));
        assertTrue(result.contains("copago=50.0"));
        assertTrue(result.contains("estado='PENDIENTE'"));
        assertTrue(result.contains("mensaje='Requiere documentación'"));
    }

    @Test
    void testMultipleFieldUpdates() {
        autorizacionDTO.setAutorizacion("AUTH-111");
        autorizacionDTO.setMontoAutorizado(100.0);
        autorizacionDTO.setCopago(25.0);
        autorizacionDTO.setEstado("APROBADO");
        autorizacionDTO.setMensaje("Mensaje 1");
        
        assertEquals("AUTH-111", autorizacionDTO.getAutorizacion());
        assertEquals(100.0, autorizacionDTO.getMontoAutorizado());
        assertEquals(25.0, autorizacionDTO.getCopago());
        assertEquals("APROBADO", autorizacionDTO.getEstado());
        assertEquals("Mensaje 1", autorizacionDTO.getMensaje());
        
        // Update all fields again
        autorizacionDTO.setAutorizacion("AUTH-222");
        autorizacionDTO.setMontoAutorizado(200.0);
        autorizacionDTO.setCopago(50.0);
        autorizacionDTO.setEstado("RECHAZADO");
        autorizacionDTO.setMensaje("Mensaje 2");
        
        assertEquals("AUTH-222", autorizacionDTO.getAutorizacion());
        assertEquals(200.0, autorizacionDTO.getMontoAutorizado());
        assertEquals(50.0, autorizacionDTO.getCopago());
        assertEquals("RECHAZADO", autorizacionDTO.getEstado());
        assertEquals("Mensaje 2", autorizacionDTO.getMensaje());
    }

    @Test
    void testNullToValueToNull() {
        // Start with null values
        assertNull(autorizacionDTO.getAutorizacion());
        assertNull(autorizacionDTO.getMontoAutorizado());
        assertNull(autorizacionDTO.getCopago());
        assertNull(autorizacionDTO.getEstado());
        assertNull(autorizacionDTO.getMensaje());
        
        // Set values
        autorizacionDTO.setAutorizacion("AUTH-TEST");
        autorizacionDTO.setMontoAutorizado(100.0);
        autorizacionDTO.setCopago(25.0);
        autorizacionDTO.setEstado("TEST");
        autorizacionDTO.setMensaje("Test Mensaje");
        
        assertEquals("AUTH-TEST", autorizacionDTO.getAutorizacion());
        assertEquals(100.0, autorizacionDTO.getMontoAutorizado());
        assertEquals(25.0, autorizacionDTO.getCopago());
        assertEquals("TEST", autorizacionDTO.getEstado());
        assertEquals("Test Mensaje", autorizacionDTO.getMensaje());
        
        // Set back to null
        autorizacionDTO.setAutorizacion(null);
        autorizacionDTO.setMontoAutorizado(null);
        autorizacionDTO.setCopago(null);
        autorizacionDTO.setEstado(null);
        autorizacionDTO.setMensaje(null);
        
        assertNull(autorizacionDTO.getAutorizacion());
        assertNull(autorizacionDTO.getMontoAutorizado());
        assertNull(autorizacionDTO.getCopago());
        assertNull(autorizacionDTO.getEstado());
        assertNull(autorizacionDTO.getMensaje());
    }

    @Test
    void testMultipleInstances() {
        AutorizacionMedicamentoDTO dto1 = new AutorizacionMedicamentoDTO();
        AutorizacionMedicamentoDTO dto2 = new AutorizacionMedicamentoDTO();
        
        dto1.setAutorizacion("AUTH-111");
        dto1.setMontoAutorizado(100.0);
        dto1.setEstado("APROBADO");
        
        dto2.setAutorizacion("AUTH-222");
        dto2.setMontoAutorizado(200.0);
        dto2.setEstado("RECHAZADO");
        
        assertEquals("AUTH-111", dto1.getAutorizacion());
        assertEquals(100.0, dto1.getMontoAutorizado());
        assertEquals("APROBADO", dto1.getEstado());
        
        assertEquals("AUTH-222", dto2.getAutorizacion());
        assertEquals(200.0, dto2.getMontoAutorizado());
        assertEquals("RECHAZADO", dto2.getEstado());
        
        // Verify they are independent
        dto1.setAutorizacion("AUTH-333");
        dto1.setMontoAutorizado(300.0);
        dto1.setEstado("PENDIENTE");
        
        assertEquals("AUTH-333", dto1.getAutorizacion());
        assertEquals(300.0, dto1.getMontoAutorizado());
        assertEquals("PENDIENTE", dto1.getEstado());
        
        assertEquals("AUTH-222", dto2.getAutorizacion());
        assertEquals(200.0, dto2.getMontoAutorizado());
        assertEquals("RECHAZADO", dto2.getEstado());
    }
}


