package com.example.pharmacy.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ExportMedicamentoDTOTest {

    @Test
    void testDefaultConstructor() {
        ExportMedicamentoDTO dto = new ExportMedicamentoDTO();
        
        assertNull(dto.getIdMedicamento());
        assertNull(dto.getCantidad());
    }

    @Test
    void testParameterizedConstructor() {
        ExportMedicamentoDTO dto = new ExportMedicamentoDTO(100L, 5);
        
        assertEquals(100L, dto.getIdMedicamento());
        assertEquals(5, dto.getCantidad());
    }

    @Test
    void testGetIdMedicamento() {
        ExportMedicamentoDTO dto = new ExportMedicamentoDTO();
        assertNull(dto.getIdMedicamento());
        
        dto.setIdMedicamento(200L);
        assertEquals(200L, dto.getIdMedicamento());
    }

    @Test
    void testSetIdMedicamento() {
        ExportMedicamentoDTO dto = new ExportMedicamentoDTO();
        
        dto.setIdMedicamento(300L);
        assertEquals(300L, dto.getIdMedicamento());
        
        dto.setIdMedicamento(null);
        assertNull(dto.getIdMedicamento());
    }

    @Test
    void testGetCantidad() {
        ExportMedicamentoDTO dto = new ExportMedicamentoDTO();
        assertNull(dto.getCantidad());
        
        dto.setCantidad(10);
        assertEquals(10, dto.getCantidad());
    }

    @Test
    void testSetCantidad() {
        ExportMedicamentoDTO dto = new ExportMedicamentoDTO();
        
        dto.setCantidad(15);
        assertEquals(15, dto.getCantidad());
        
        dto.setCantidad(null);
        assertNull(dto.getCantidad());
    }

    @Test
    void testSetIdMedicamento_WithDifferentValues() {
        ExportMedicamentoDTO dto = new ExportMedicamentoDTO();
        
        dto.setIdMedicamento(1L);
        assertEquals(1L, dto.getIdMedicamento());
        
        dto.setIdMedicamento(999L);
        assertEquals(999L, dto.getIdMedicamento());
        
        dto.setIdMedicamento(0L);
        assertEquals(0L, dto.getIdMedicamento());
        
        dto.setIdMedicamento(-1L);
        assertEquals(-1L, dto.getIdMedicamento());
    }

    @Test
    void testSetCantidad_WithDifferentValues() {
        ExportMedicamentoDTO dto = new ExportMedicamentoDTO();
        
        dto.setCantidad(1);
        assertEquals(1, dto.getCantidad());
        
        dto.setCantidad(100);
        assertEquals(100, dto.getCantidad());
        
        dto.setCantidad(0);
        assertEquals(0, dto.getCantidad());
        
        dto.setCantidad(-5);
        assertEquals(-5, dto.getCantidad());
    }

    @Test
    void testSetIdMedicamento_WithEdgeCases() {
        ExportMedicamentoDTO dto = new ExportMedicamentoDTO();
        
        dto.setIdMedicamento(Long.MAX_VALUE);
        assertEquals(Long.MAX_VALUE, dto.getIdMedicamento());
        
        dto.setIdMedicamento(Long.MIN_VALUE);
        assertEquals(Long.MIN_VALUE, dto.getIdMedicamento());
    }

    @Test
    void testSetCantidad_WithEdgeCases() {
        ExportMedicamentoDTO dto = new ExportMedicamentoDTO();
        
        dto.setCantidad(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, dto.getCantidad());
        
        dto.setCantidad(Integer.MIN_VALUE);
        assertEquals(Integer.MIN_VALUE, dto.getCantidad());
    }

    @Test
    void testToString() {
        ExportMedicamentoDTO dto = new ExportMedicamentoDTO(100L, 5);
        
        String result = dto.toString();
        
        assertTrue(result.contains("idMedicamento=100"));
        assertTrue(result.contains("cantidad=5"));
        assertTrue(result.startsWith("ExportMedicamentoDTO{"));
        assertTrue(result.endsWith("}"));
    }

    @Test
    void testToString_WithNullValues() {
        ExportMedicamentoDTO dto = new ExportMedicamentoDTO();
        
        String result = dto.toString();
        
        assertTrue(result.contains("idMedicamento=null"));
        assertTrue(result.contains("cantidad=null"));
    }

    @Test
    void testToString_WithAllValues() {
        ExportMedicamentoDTO dto = new ExportMedicamentoDTO(200L, 10);
        
        String result = dto.toString();
        
        assertTrue(result.contains("idMedicamento=200"));
        assertTrue(result.contains("cantidad=10"));
    }

    @Test
    void testToString_WithZeroValues() {
        ExportMedicamentoDTO dto = new ExportMedicamentoDTO(0L, 0);
        
        String result = dto.toString();
        
        assertTrue(result.contains("idMedicamento=0"));
        assertTrue(result.contains("cantidad=0"));
    }

    @Test
    void testToString_WithNegativeValues() {
        ExportMedicamentoDTO dto = new ExportMedicamentoDTO(-1L, -5);
        
        String result = dto.toString();
        
        assertTrue(result.contains("idMedicamento=-1"));
        assertTrue(result.contains("cantidad=-5"));
    }

    @Test
    void testMultipleFieldUpdates() {
        ExportMedicamentoDTO dto = new ExportMedicamentoDTO();
        
        dto.setIdMedicamento(100L);
        dto.setCantidad(5);
        
        assertEquals(100L, dto.getIdMedicamento());
        assertEquals(5, dto.getCantidad());
        
        // Update fields again
        dto.setIdMedicamento(200L);
        dto.setCantidad(10);
        
        assertEquals(200L, dto.getIdMedicamento());
        assertEquals(10, dto.getCantidad());
    }

    @Test
    void testNullToValueToNull() {
        ExportMedicamentoDTO dto = new ExportMedicamentoDTO();
        
        // Start with null values
        assertNull(dto.getIdMedicamento());
        assertNull(dto.getCantidad());
        
        // Set values
        dto.setIdMedicamento(100L);
        dto.setCantidad(5);
        
        assertEquals(100L, dto.getIdMedicamento());
        assertEquals(5, dto.getCantidad());
        
        // Set back to null
        dto.setIdMedicamento(null);
        dto.setCantidad(null);
        
        assertNull(dto.getIdMedicamento());
        assertNull(dto.getCantidad());
    }

    @Test
    void testConstructorWithNullValues() {
        ExportMedicamentoDTO dto = new ExportMedicamentoDTO(null, null);
        
        assertNull(dto.getIdMedicamento());
        assertNull(dto.getCantidad());
    }

    @Test
    void testConstructorWithZeroValues() {
        ExportMedicamentoDTO dto = new ExportMedicamentoDTO(0L, 0);
        
        assertEquals(0L, dto.getIdMedicamento());
        assertEquals(0, dto.getCantidad());
    }

    @Test
    void testConstructorWithNegativeValues() {
        ExportMedicamentoDTO dto = new ExportMedicamentoDTO(-1L, -5);
        
        assertEquals(-1L, dto.getIdMedicamento());
        assertEquals(-5, dto.getCantidad());
    }

    @Test
    void testConstructorWithLargeValues() {
        ExportMedicamentoDTO dto = new ExportMedicamentoDTO(Long.MAX_VALUE, Integer.MAX_VALUE);
        
        assertEquals(Long.MAX_VALUE, dto.getIdMedicamento());
        assertEquals(Integer.MAX_VALUE, dto.getCantidad());
    }

    @Test
    void testMultipleInstances() {
        ExportMedicamentoDTO dto1 = new ExportMedicamentoDTO(100L, 5);
        ExportMedicamentoDTO dto2 = new ExportMedicamentoDTO(200L, 10);
        
        assertEquals(100L, dto1.getIdMedicamento());
        assertEquals(5, dto1.getCantidad());
        assertEquals(200L, dto2.getIdMedicamento());
        assertEquals(10, dto2.getCantidad());
        
        // Verify they are independent
        dto1.setIdMedicamento(300L);
        dto1.setCantidad(15);
        
        assertEquals(300L, dto1.getIdMedicamento());
        assertEquals(15, dto1.getCantidad());
        assertEquals(200L, dto2.getIdMedicamento());
        assertEquals(10, dto2.getCantidad());
    }
}


