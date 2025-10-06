package com.example.pharmacy.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CoberturaMedicamentoDTOTest {

    private CoberturaMedicamentoDTO coberturaDTO;

    @BeforeEach
    void setUp() {
        coberturaDTO = new CoberturaMedicamentoDTO();
    }

    @Test
    void testGetNumeroAfiliacion() {
        assertNull(coberturaDTO.getNumeroAfiliacion());
        
        coberturaDTO.setNumeroAfiliacion("AFI-12345");
        assertEquals("AFI-12345", coberturaDTO.getNumeroAfiliacion());
    }

    @Test
    void testSetNumeroAfiliacion() {
        coberturaDTO.setNumeroAfiliacion("AFI-ABC123");
        assertEquals("AFI-ABC123", coberturaDTO.getNumeroAfiliacion());
        
        coberturaDTO.setNumeroAfiliacion("");
        assertEquals("", coberturaDTO.getNumeroAfiliacion());
        
        coberturaDTO.setNumeroAfiliacion(null);
        assertNull(coberturaDTO.getNumeroAfiliacion());
    }

    @Test
    void testGetCodigoMedicamento() {
        assertNull(coberturaDTO.getCodigoMedicamento());
        
        coberturaDTO.setCodigoMedicamento("MED-001");
        assertEquals("MED-001", coberturaDTO.getCodigoMedicamento());
    }

    @Test
    void testSetCodigoMedicamento() {
        coberturaDTO.setCodigoMedicamento("MED-ABC123");
        assertEquals("MED-ABC123", coberturaDTO.getCodigoMedicamento());
        
        coberturaDTO.setCodigoMedicamento("");
        assertEquals("", coberturaDTO.getCodigoMedicamento());
        
        coberturaDTO.setCodigoMedicamento(null);
        assertNull(coberturaDTO.getCodigoMedicamento());
    }

    @Test
    void testGetNombre() {
        assertNull(coberturaDTO.getNombre());
        
        coberturaDTO.setNombre("Paracetamol 500mg");
        assertEquals("Paracetamol 500mg", coberturaDTO.getNombre());
    }

    @Test
    void testSetNombre() {
        coberturaDTO.setNombre("Ibuprofeno 400mg");
        assertEquals("Ibuprofeno 400mg", coberturaDTO.getNombre());
        
        coberturaDTO.setNombre("");
        assertEquals("", coberturaDTO.getNombre());
        
        coberturaDTO.setNombre(null);
        assertNull(coberturaDTO.getNombre());
    }

    @Test
    void testIsCubierto() {
        assertFalse(coberturaDTO.isCubierto());
        
        coberturaDTO.setCubierto(true);
        assertTrue(coberturaDTO.isCubierto());
    }

    @Test
    void testSetCubierto() {
        coberturaDTO.setCubierto(true);
        assertTrue(coberturaDTO.isCubierto());
        
        coberturaDTO.setCubierto(false);
        assertFalse(coberturaDTO.isCubierto());
    }

    @Test
    void testGetMontoAutorizado() {
        assertEquals(0.0, coberturaDTO.getMontoAutorizado());
        
        coberturaDTO.setMontoAutorizado(25.50);
        assertEquals(25.50, coberturaDTO.getMontoAutorizado());
    }

    @Test
    void testSetMontoAutorizado() {
        coberturaDTO.setMontoAutorizado(100.00);
        assertEquals(100.00, coberturaDTO.getMontoAutorizado());
        
        coberturaDTO.setMontoAutorizado(0.0);
        assertEquals(0.0, coberturaDTO.getMontoAutorizado());
        
        coberturaDTO.setMontoAutorizado(-10.0);
        assertEquals(-10.0, coberturaDTO.getMontoAutorizado());
    }

    @Test
    void testGetCopago() {
        assertEquals(0.0, coberturaDTO.getCopago());
        
        coberturaDTO.setCopago(5.00);
        assertEquals(5.00, coberturaDTO.getCopago());
    }

    @Test
    void testSetCopago() {
        coberturaDTO.setCopago(15.75);
        assertEquals(15.75, coberturaDTO.getCopago());
        
        coberturaDTO.setCopago(0.0);
        assertEquals(0.0, coberturaDTO.getCopago());
        
        coberturaDTO.setCopago(-2.50);
        assertEquals(-2.50, coberturaDTO.getCopago());
    }

    @Test
    void testGetMensaje() {
        assertNull(coberturaDTO.getMensaje());
        
        coberturaDTO.setMensaje("Medicamento cubierto al 80%");
        assertEquals("Medicamento cubierto al 80%", coberturaDTO.getMensaje());
    }

    @Test
    void testSetMensaje() {
        coberturaDTO.setMensaje("Requiere autorización previa");
        assertEquals("Requiere autorización previa", coberturaDTO.getMensaje());
        
        coberturaDTO.setMensaje("");
        assertEquals("", coberturaDTO.getMensaje());
        
        coberturaDTO.setMensaje(null);
        assertNull(coberturaDTO.getMensaje());
    }

    @Test
    void testSetNumeroAfiliacion_WithDifferentValues() {
        coberturaDTO.setNumeroAfiliacion("AFI-98765");
        assertEquals("AFI-98765", coberturaDTO.getNumeroAfiliacion());
        
        coberturaDTO.setNumeroAfiliacion("AFI-XYZ789");
        assertEquals("AFI-XYZ789", coberturaDTO.getNumeroAfiliacion());
        
        coberturaDTO.setNumeroAfiliacion("12345");
        assertEquals("12345", coberturaDTO.getNumeroAfiliacion());
    }

    @Test
    void testSetCodigoMedicamento_WithDifferentValues() {
        coberturaDTO.setCodigoMedicamento("MED-123");
        assertEquals("MED-123", coberturaDTO.getCodigoMedicamento());
        
        coberturaDTO.setCodigoMedicamento("ABC-456");
        assertEquals("ABC-456", coberturaDTO.getCodigoMedicamento());
        
        coberturaDTO.setCodigoMedicamento("XYZ789");
        assertEquals("XYZ789", coberturaDTO.getCodigoMedicamento());
    }

    @Test
    void testSetNombre_WithDifferentValues() {
        coberturaDTO.setNombre("Aspirina 100mg");
        assertEquals("Aspirina 100mg", coberturaDTO.getNombre());
        
        coberturaDTO.setNombre("Vitamina C 1000mg");
        assertEquals("Vitamina C 1000mg", coberturaDTO.getNombre());
        
        coberturaDTO.setNombre("A");
        assertEquals("A", coberturaDTO.getNombre());
    }

    @Test
    void testSetCubierto_WithDifferentValues() {
        coberturaDTO.setCubierto(true);
        assertTrue(coberturaDTO.isCubierto());
        
        coberturaDTO.setCubierto(false);
        assertFalse(coberturaDTO.isCubierto());
        
        coberturaDTO.setCubierto(true);
        assertTrue(coberturaDTO.isCubierto());
    }

    @Test
    void testSetMontoAutorizado_WithDifferentValues() {
        coberturaDTO.setMontoAutorizado(50.0);
        assertEquals(50.0, coberturaDTO.getMontoAutorizado());
        
        coberturaDTO.setMontoAutorizado(99.99);
        assertEquals(99.99, coberturaDTO.getMontoAutorizado());
        
        coberturaDTO.setMontoAutorizado(0.01);
        assertEquals(0.01, coberturaDTO.getMontoAutorizado());
    }

    @Test
    void testSetCopago_WithDifferentValues() {
        coberturaDTO.setCopago(10.0);
        assertEquals(10.0, coberturaDTO.getCopago());
        
        coberturaDTO.setCopago(25.50);
        assertEquals(25.50, coberturaDTO.getCopago());
        
        coberturaDTO.setCopago(0.99);
        assertEquals(0.99, coberturaDTO.getCopago());
    }

    @Test
    void testSetMensaje_WithDifferentValues() {
        coberturaDTO.setMensaje("Cobertura completa");
        assertEquals("Cobertura completa", coberturaDTO.getMensaje());
        
        coberturaDTO.setMensaje("No cubierto");
        assertEquals("No cubierto", coberturaDTO.getMensaje());
        
        coberturaDTO.setMensaje("Requiere receta médica");
        assertEquals("Requiere receta médica", coberturaDTO.getMensaje());
    }

    @Test
    void testSetMontoAutorizado_WithEdgeCases() {
        coberturaDTO.setMontoAutorizado(Double.MAX_VALUE);
        assertEquals(Double.MAX_VALUE, coberturaDTO.getMontoAutorizado());
        
        coberturaDTO.setMontoAutorizado(Double.MIN_VALUE);
        assertEquals(Double.MIN_VALUE, coberturaDTO.getMontoAutorizado());
        
        coberturaDTO.setMontoAutorizado(Double.POSITIVE_INFINITY);
        assertEquals(Double.POSITIVE_INFINITY, coberturaDTO.getMontoAutorizado());
        
        coberturaDTO.setMontoAutorizado(Double.NEGATIVE_INFINITY);
        assertEquals(Double.NEGATIVE_INFINITY, coberturaDTO.getMontoAutorizado());
        
        coberturaDTO.setMontoAutorizado(Double.NaN);
        assertTrue(Double.isNaN(coberturaDTO.getMontoAutorizado()));
    }

    @Test
    void testSetCopago_WithEdgeCases() {
        coberturaDTO.setCopago(Double.MAX_VALUE);
        assertEquals(Double.MAX_VALUE, coberturaDTO.getCopago());
        
        coberturaDTO.setCopago(Double.MIN_VALUE);
        assertEquals(Double.MIN_VALUE, coberturaDTO.getCopago());
        
        coberturaDTO.setCopago(Double.POSITIVE_INFINITY);
        assertEquals(Double.POSITIVE_INFINITY, coberturaDTO.getCopago());
        
        coberturaDTO.setCopago(Double.NEGATIVE_INFINITY);
        assertEquals(Double.NEGATIVE_INFINITY, coberturaDTO.getCopago());
        
        coberturaDTO.setCopago(Double.NaN);
        assertTrue(Double.isNaN(coberturaDTO.getCopago()));
    }

    @Test
    void testToString() {
        coberturaDTO.setNumeroAfiliacion("AFI-12345");
        coberturaDTO.setCodigoMedicamento("MED-001");
        coberturaDTO.setNombre("Paracetamol");
        coberturaDTO.setCubierto(true);
        coberturaDTO.setMontoAutorizado(25.50);
        coberturaDTO.setCopago(5.00);
        coberturaDTO.setMensaje("Cubierto al 80%");
        
        String result = coberturaDTO.toString();
        
        assertTrue(result.contains("numeroAfiliacion='AFI-12345'"));
        assertTrue(result.contains("codigoMedicamento='MED-001'"));
        assertTrue(result.contains("nombre='Paracetamol'"));
        assertTrue(result.contains("cubierto=true"));
        assertTrue(result.contains("montoAutorizado=25.5"));
        assertTrue(result.contains("copago=5.0"));
        assertTrue(result.contains("mensaje='Cubierto al 80%'"));
        assertTrue(result.startsWith("CoberturaMedicamentoDTO{"));
        assertTrue(result.endsWith("}"));
    }

    @Test
    void testToString_WithNullValues() {
        String result = coberturaDTO.toString();
        
        assertTrue(result.contains("numeroAfiliacion='null'"));
        assertTrue(result.contains("codigoMedicamento='null'"));
        assertTrue(result.contains("nombre='null'"));
        assertTrue(result.contains("cubierto=false"));
        assertTrue(result.contains("montoAutorizado=0.0"));
        assertTrue(result.contains("copago=0.0"));
        assertTrue(result.contains("mensaje='null'"));
    }

    @Test
    void testToString_WithAllValues() {
        coberturaDTO.setNumeroAfiliacion("AFI-ABC123");
        coberturaDTO.setCodigoMedicamento("MED-XYZ789");
        coberturaDTO.setNombre("Ibuprofeno 400mg");
        coberturaDTO.setCubierto(false);
        coberturaDTO.setMontoAutorizado(0.0);
        coberturaDTO.setCopago(0.0);
        coberturaDTO.setMensaje("No cubierto");
        
        String result = coberturaDTO.toString();
        
        assertTrue(result.contains("numeroAfiliacion='AFI-ABC123'"));
        assertTrue(result.contains("codigoMedicamento='MED-XYZ789'"));
        assertTrue(result.contains("nombre='Ibuprofeno 400mg'"));
        assertTrue(result.contains("cubierto=false"));
        assertTrue(result.contains("montoAutorizado=0.0"));
        assertTrue(result.contains("copago=0.0"));
        assertTrue(result.contains("mensaje='No cubierto'"));
    }

    @Test
    void testMultipleFieldUpdates() {
        coberturaDTO.setNumeroAfiliacion("AFI-111");
        coberturaDTO.setCodigoMedicamento("MED-111");
        coberturaDTO.setNombre("Medicamento 1");
        coberturaDTO.setCubierto(true);
        coberturaDTO.setMontoAutorizado(100.0);
        coberturaDTO.setCopago(20.0);
        coberturaDTO.setMensaje("Mensaje 1");
        
        assertEquals("AFI-111", coberturaDTO.getNumeroAfiliacion());
        assertEquals("MED-111", coberturaDTO.getCodigoMedicamento());
        assertEquals("Medicamento 1", coberturaDTO.getNombre());
        assertTrue(coberturaDTO.isCubierto());
        assertEquals(100.0, coberturaDTO.getMontoAutorizado());
        assertEquals(20.0, coberturaDTO.getCopago());
        assertEquals("Mensaje 1", coberturaDTO.getMensaje());
        
        // Update all fields again
        coberturaDTO.setNumeroAfiliacion("AFI-222");
        coberturaDTO.setCodigoMedicamento("MED-222");
        coberturaDTO.setNombre("Medicamento 2");
        coberturaDTO.setCubierto(false);
        coberturaDTO.setMontoAutorizado(200.0);
        coberturaDTO.setCopago(40.0);
        coberturaDTO.setMensaje("Mensaje 2");
        
        assertEquals("AFI-222", coberturaDTO.getNumeroAfiliacion());
        assertEquals("MED-222", coberturaDTO.getCodigoMedicamento());
        assertEquals("Medicamento 2", coberturaDTO.getNombre());
        assertFalse(coberturaDTO.isCubierto());
        assertEquals(200.0, coberturaDTO.getMontoAutorizado());
        assertEquals(40.0, coberturaDTO.getCopago());
        assertEquals("Mensaje 2", coberturaDTO.getMensaje());
    }

    @Test
    void testNullToValueToNull() {
        // Start with null values
        assertNull(coberturaDTO.getNumeroAfiliacion());
        assertNull(coberturaDTO.getCodigoMedicamento());
        assertNull(coberturaDTO.getNombre());
        assertNull(coberturaDTO.getMensaje());
        
        // Set values
        coberturaDTO.setNumeroAfiliacion("AFI-TEST");
        coberturaDTO.setCodigoMedicamento("MED-TEST");
        coberturaDTO.setNombre("Test Medicamento");
        coberturaDTO.setMensaje("Test Mensaje");
        
        assertEquals("AFI-TEST", coberturaDTO.getNumeroAfiliacion());
        assertEquals("MED-TEST", coberturaDTO.getCodigoMedicamento());
        assertEquals("Test Medicamento", coberturaDTO.getNombre());
        assertEquals("Test Mensaje", coberturaDTO.getMensaje());
        
        // Set back to null
        coberturaDTO.setNumeroAfiliacion(null);
        coberturaDTO.setCodigoMedicamento(null);
        coberturaDTO.setNombre(null);
        coberturaDTO.setMensaje(null);
        
        assertNull(coberturaDTO.getNumeroAfiliacion());
        assertNull(coberturaDTO.getCodigoMedicamento());
        assertNull(coberturaDTO.getNombre());
        assertNull(coberturaDTO.getMensaje());
    }
}


