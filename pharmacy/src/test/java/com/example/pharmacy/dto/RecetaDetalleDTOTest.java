package com.example.pharmacy.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RecetaDetalleDTOTest {

    private RecetaDetalleDTO recetaDetalleDTO;

    @BeforeEach
    void setUp() {
        recetaDetalleDTO = new RecetaDetalleDTO();
    }

    @Test
    void testGetIdDetalle() {
        assertNull(recetaDetalleDTO.getIdDetalle());
        
        recetaDetalleDTO.setIdDetalle(1L);
        assertEquals(1L, recetaDetalleDTO.getIdDetalle());
    }

    @Test
    void testSetIdDetalle() {
        recetaDetalleDTO.setIdDetalle(100L);
        assertEquals(100L, recetaDetalleDTO.getIdDetalle());
        
        recetaDetalleDTO.setIdDetalle(0L);
        assertEquals(0L, recetaDetalleDTO.getIdDetalle());
        
        recetaDetalleDTO.setIdDetalle(null);
        assertNull(recetaDetalleDTO.getIdDetalle());
    }

    @Test
    void testGetIdMedicamento() {
        assertNull(recetaDetalleDTO.getIdMedicamento());
        
        recetaDetalleDTO.setIdMedicamento(50L);
        assertEquals(50L, recetaDetalleDTO.getIdMedicamento());
    }

    @Test
    void testSetIdMedicamento() {
        recetaDetalleDTO.setIdMedicamento(200L);
        assertEquals(200L, recetaDetalleDTO.getIdMedicamento());
        
        recetaDetalleDTO.setIdMedicamento(0L);
        assertEquals(0L, recetaDetalleDTO.getIdMedicamento());
        
        recetaDetalleDTO.setIdMedicamento(null);
        assertNull(recetaDetalleDTO.getIdMedicamento());
    }

    @Test
    void testGetDosis() {
        assertNull(recetaDetalleDTO.getDosis());
        
        recetaDetalleDTO.setDosis("1 tableta");
        assertEquals("1 tableta", recetaDetalleDTO.getDosis());
    }

    @Test
    void testSetDosis() {
        recetaDetalleDTO.setDosis("2 tabletas");
        assertEquals("2 tabletas", recetaDetalleDTO.getDosis());
        
        recetaDetalleDTO.setDosis("");
        assertEquals("", recetaDetalleDTO.getDosis());
        
        recetaDetalleDTO.setDosis(null);
        assertNull(recetaDetalleDTO.getDosis());
    }

    @Test
    void testGetFrecuencia() {
        assertNull(recetaDetalleDTO.getFrecuencia());
        
        recetaDetalleDTO.setFrecuencia("Cada 8 horas");
        assertEquals("Cada 8 horas", recetaDetalleDTO.getFrecuencia());
    }

    @Test
    void testSetFrecuencia() {
        recetaDetalleDTO.setFrecuencia("Cada 12 horas");
        assertEquals("Cada 12 horas", recetaDetalleDTO.getFrecuencia());
        
        recetaDetalleDTO.setFrecuencia("");
        assertEquals("", recetaDetalleDTO.getFrecuencia());
        
        recetaDetalleDTO.setFrecuencia(null);
        assertNull(recetaDetalleDTO.getFrecuencia());
    }

    @Test
    void testGetDuracion() {
        assertNull(recetaDetalleDTO.getDuracion());
        
        recetaDetalleDTO.setDuracion("7 días");
        assertEquals("7 días", recetaDetalleDTO.getDuracion());
    }

    @Test
    void testSetDuracion() {
        recetaDetalleDTO.setDuracion("10 días");
        assertEquals("10 días", recetaDetalleDTO.getDuracion());
        
        recetaDetalleDTO.setDuracion("");
        assertEquals("", recetaDetalleDTO.getDuracion());
        
        recetaDetalleDTO.setDuracion(null);
        assertNull(recetaDetalleDTO.getDuracion());
    }

    @Test
    void testGetCantidadRequerida() {
        assertNull(recetaDetalleDTO.getCantidadRequerida());
        
        recetaDetalleDTO.setCantidadRequerida(30);
        assertEquals(30, recetaDetalleDTO.getCantidadRequerida());
    }

    @Test
    void testSetCantidadRequerida() {
        recetaDetalleDTO.setCantidadRequerida(60);
        assertEquals(60, recetaDetalleDTO.getCantidadRequerida());
        
        recetaDetalleDTO.setCantidadRequerida(0);
        assertEquals(0, recetaDetalleDTO.getCantidadRequerida());
        
        recetaDetalleDTO.setCantidadRequerida(null);
        assertNull(recetaDetalleDTO.getCantidadRequerida());
    }

    @Test
    void testGetObservaciones() {
        assertNull(recetaDetalleDTO.getObservaciones());
        
        recetaDetalleDTO.setObservaciones("Tomar con alimentos");
        assertEquals("Tomar con alimentos", recetaDetalleDTO.getObservaciones());
    }

    @Test
    void testSetObservaciones() {
        recetaDetalleDTO.setObservaciones("Evitar alcohol");
        assertEquals("Evitar alcohol", recetaDetalleDTO.getObservaciones());
        
        recetaDetalleDTO.setObservaciones("");
        assertEquals("", recetaDetalleDTO.getObservaciones());
        
        recetaDetalleDTO.setObservaciones(null);
        assertNull(recetaDetalleDTO.getObservaciones());
    }

    @Test
    void testSetIdDetalle_WithDifferentValues() {
        recetaDetalleDTO.setIdDetalle(1L);
        assertEquals(1L, recetaDetalleDTO.getIdDetalle());
        
        recetaDetalleDTO.setIdDetalle(999L);
        assertEquals(999L, recetaDetalleDTO.getIdDetalle());
        
        recetaDetalleDTO.setIdDetalle(Long.MAX_VALUE);
        assertEquals(Long.MAX_VALUE, recetaDetalleDTO.getIdDetalle());
    }

    @Test
    void testSetIdMedicamento_WithDifferentValues() {
        recetaDetalleDTO.setIdMedicamento(10L);
        assertEquals(10L, recetaDetalleDTO.getIdMedicamento());
        
        recetaDetalleDTO.setIdMedicamento(500L);
        assertEquals(500L, recetaDetalleDTO.getIdMedicamento());
        
        recetaDetalleDTO.setIdMedicamento(Long.MIN_VALUE);
        assertEquals(Long.MIN_VALUE, recetaDetalleDTO.getIdMedicamento());
    }

    @Test
    void testSetDosis_WithDifferentValues() {
        recetaDetalleDTO.setDosis("Dosis 1");
        assertEquals("Dosis 1", recetaDetalleDTO.getDosis());
        
        recetaDetalleDTO.setDosis("Dosis 2");
        assertEquals("Dosis 2", recetaDetalleDTO.getDosis());
        
        recetaDetalleDTO.setDosis("A");
        assertEquals("A", recetaDetalleDTO.getDosis());
    }

    @Test
    void testSetFrecuencia_WithDifferentValues() {
        recetaDetalleDTO.setFrecuencia("Frecuencia 1");
        assertEquals("Frecuencia 1", recetaDetalleDTO.getFrecuencia());
        
        recetaDetalleDTO.setFrecuencia("Frecuencia 2");
        assertEquals("Frecuencia 2", recetaDetalleDTO.getFrecuencia());
        
        recetaDetalleDTO.setFrecuencia("F");
        assertEquals("F", recetaDetalleDTO.getFrecuencia());
    }

    @Test
    void testSetDuracion_WithDifferentValues() {
        recetaDetalleDTO.setDuracion("Duración 1");
        assertEquals("Duración 1", recetaDetalleDTO.getDuracion());
        
        recetaDetalleDTO.setDuracion("Duración 2");
        assertEquals("Duración 2", recetaDetalleDTO.getDuracion());
        
        recetaDetalleDTO.setDuracion("D");
        assertEquals("D", recetaDetalleDTO.getDuracion());
    }

    @Test
    void testSetCantidadRequerida_WithDifferentValues() {
        recetaDetalleDTO.setCantidadRequerida(10);
        assertEquals(10, recetaDetalleDTO.getCantidadRequerida());
        
        recetaDetalleDTO.setCantidadRequerida(99);
        assertEquals(99, recetaDetalleDTO.getCantidadRequerida());
        
        recetaDetalleDTO.setCantidadRequerida(1);
        assertEquals(1, recetaDetalleDTO.getCantidadRequerida());
    }

    @Test
    void testSetObservaciones_WithDifferentValues() {
        recetaDetalleDTO.setObservaciones("Observación 1");
        assertEquals("Observación 1", recetaDetalleDTO.getObservaciones());
        
        recetaDetalleDTO.setObservaciones("Observación 2");
        assertEquals("Observación 2", recetaDetalleDTO.getObservaciones());
        
        recetaDetalleDTO.setObservaciones("O");
        assertEquals("O", recetaDetalleDTO.getObservaciones());
    }

    @Test
    void testSetCantidadRequerida_WithEdgeCases() {
        recetaDetalleDTO.setCantidadRequerida(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, recetaDetalleDTO.getCantidadRequerida());
        
        recetaDetalleDTO.setCantidadRequerida(Integer.MIN_VALUE);
        assertEquals(Integer.MIN_VALUE, recetaDetalleDTO.getCantidadRequerida());
        
        recetaDetalleDTO.setCantidadRequerida(0);
        assertEquals(0, recetaDetalleDTO.getCantidadRequerida());
    }

    @Test
    void testMultipleFieldUpdates() {
        recetaDetalleDTO.setIdDetalle(111L);
        recetaDetalleDTO.setIdMedicamento(222L);
        recetaDetalleDTO.setDosis("Dosis 1");
        recetaDetalleDTO.setFrecuencia("Frecuencia 1");
        recetaDetalleDTO.setDuracion("Duración 1");
        recetaDetalleDTO.setCantidadRequerida(10);
        recetaDetalleDTO.setObservaciones("Observación 1");
        
        assertEquals(111L, recetaDetalleDTO.getIdDetalle());
        assertEquals(222L, recetaDetalleDTO.getIdMedicamento());
        assertEquals("Dosis 1", recetaDetalleDTO.getDosis());
        assertEquals("Frecuencia 1", recetaDetalleDTO.getFrecuencia());
        assertEquals("Duración 1", recetaDetalleDTO.getDuracion());
        assertEquals(10, recetaDetalleDTO.getCantidadRequerida());
        assertEquals("Observación 1", recetaDetalleDTO.getObservaciones());
        
        // Update all fields again
        recetaDetalleDTO.setIdDetalle(333L);
        recetaDetalleDTO.setIdMedicamento(444L);
        recetaDetalleDTO.setDosis("Dosis 2");
        recetaDetalleDTO.setFrecuencia("Frecuencia 2");
        recetaDetalleDTO.setDuracion("Duración 2");
        recetaDetalleDTO.setCantidadRequerida(20);
        recetaDetalleDTO.setObservaciones("Observación 2");
        
        assertEquals(333L, recetaDetalleDTO.getIdDetalle());
        assertEquals(444L, recetaDetalleDTO.getIdMedicamento());
        assertEquals("Dosis 2", recetaDetalleDTO.getDosis());
        assertEquals("Frecuencia 2", recetaDetalleDTO.getFrecuencia());
        assertEquals("Duración 2", recetaDetalleDTO.getDuracion());
        assertEquals(20, recetaDetalleDTO.getCantidadRequerida());
        assertEquals("Observación 2", recetaDetalleDTO.getObservaciones());
    }

    @Test
    void testNullToValueToNull() {
        // Start with null values
        assertNull(recetaDetalleDTO.getIdDetalle());
        assertNull(recetaDetalleDTO.getIdMedicamento());
        assertNull(recetaDetalleDTO.getDosis());
        assertNull(recetaDetalleDTO.getFrecuencia());
        assertNull(recetaDetalleDTO.getDuracion());
        assertNull(recetaDetalleDTO.getCantidadRequerida());
        assertNull(recetaDetalleDTO.getObservaciones());
        
        // Set values
        recetaDetalleDTO.setIdDetalle(999L);
        recetaDetalleDTO.setIdMedicamento(888L);
        recetaDetalleDTO.setDosis("Test Dosis");
        recetaDetalleDTO.setFrecuencia("Test Frecuencia");
        recetaDetalleDTO.setDuracion("Test Duración");
        recetaDetalleDTO.setCantidadRequerida(99);
        recetaDetalleDTO.setObservaciones("Test Observación");
        
        assertEquals(999L, recetaDetalleDTO.getIdDetalle());
        assertEquals(888L, recetaDetalleDTO.getIdMedicamento());
        assertEquals("Test Dosis", recetaDetalleDTO.getDosis());
        assertEquals("Test Frecuencia", recetaDetalleDTO.getFrecuencia());
        assertEquals("Test Duración", recetaDetalleDTO.getDuracion());
        assertEquals(99, recetaDetalleDTO.getCantidadRequerida());
        assertEquals("Test Observación", recetaDetalleDTO.getObservaciones());
        
        // Set back to null
        recetaDetalleDTO.setIdDetalle(null);
        recetaDetalleDTO.setIdMedicamento(null);
        recetaDetalleDTO.setDosis(null);
        recetaDetalleDTO.setFrecuencia(null);
        recetaDetalleDTO.setDuracion(null);
        recetaDetalleDTO.setCantidadRequerida(null);
        recetaDetalleDTO.setObservaciones(null);
        
        assertNull(recetaDetalleDTO.getIdDetalle());
        assertNull(recetaDetalleDTO.getIdMedicamento());
        assertNull(recetaDetalleDTO.getDosis());
        assertNull(recetaDetalleDTO.getFrecuencia());
        assertNull(recetaDetalleDTO.getDuracion());
        assertNull(recetaDetalleDTO.getCantidadRequerida());
        assertNull(recetaDetalleDTO.getObservaciones());
    }

    @Test
    void testMultipleInstances() {
        RecetaDetalleDTO dto1 = new RecetaDetalleDTO();
        RecetaDetalleDTO dto2 = new RecetaDetalleDTO();
        
        dto1.setIdDetalle(111L);
        dto1.setIdMedicamento(222L);
        dto1.setDosis("Dosis 1");
        
        dto2.setIdDetalle(333L);
        dto2.setIdMedicamento(444L);
        dto2.setDosis("Dosis 2");
        
        assertEquals(111L, dto1.getIdDetalle());
        assertEquals(222L, dto1.getIdMedicamento());
        assertEquals("Dosis 1", dto1.getDosis());
        
        assertEquals(333L, dto2.getIdDetalle());
        assertEquals(444L, dto2.getIdMedicamento());
        assertEquals("Dosis 2", dto2.getDosis());
        
        // Verify they are independent
        dto1.setIdDetalle(555L);
        dto1.setIdMedicamento(666L);
        dto1.setDosis("Dosis 3");
        
        assertEquals(555L, dto1.getIdDetalle());
        assertEquals(666L, dto1.getIdMedicamento());
        assertEquals("Dosis 3", dto1.getDosis());
        
        assertEquals(333L, dto2.getIdDetalle());
        assertEquals(444L, dto2.getIdMedicamento());
        assertEquals("Dosis 2", dto2.getDosis());
    }
}


