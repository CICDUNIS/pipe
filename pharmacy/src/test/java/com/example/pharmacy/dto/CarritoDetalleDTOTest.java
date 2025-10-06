package com.example.pharmacy.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CarritoDetalleDTOTest {

    private CarritoDetalleDTO carritoDetalleDTO;

    @BeforeEach
    void setUp() {
        carritoDetalleDTO = new CarritoDetalleDTO();
    }

    @Test
    void testGetIdCartItem() {
        assertNull(carritoDetalleDTO.getIdCartItem());
        
        carritoDetalleDTO.setIdCartItem(1L);
        assertEquals(1L, carritoDetalleDTO.getIdCartItem());
    }

    @Test
    void testSetIdCartItem() {
        carritoDetalleDTO.setIdCartItem(100L);
        assertEquals(100L, carritoDetalleDTO.getIdCartItem());
        
        carritoDetalleDTO.setIdCartItem(0L);
        assertEquals(0L, carritoDetalleDTO.getIdCartItem());
        
        carritoDetalleDTO.setIdCartItem(null);
        assertNull(carritoDetalleDTO.getIdCartItem());
    }

    @Test
    void testGetIdMedicamento() {
        assertNull(carritoDetalleDTO.getIdMedicamento());
        
        carritoDetalleDTO.setIdMedicamento(50L);
        assertEquals(50L, carritoDetalleDTO.getIdMedicamento());
    }

    @Test
    void testSetIdMedicamento() {
        carritoDetalleDTO.setIdMedicamento(200L);
        assertEquals(200L, carritoDetalleDTO.getIdMedicamento());
        
        carritoDetalleDTO.setIdMedicamento(0L);
        assertEquals(0L, carritoDetalleDTO.getIdMedicamento());
        
        carritoDetalleDTO.setIdMedicamento(null);
        assertNull(carritoDetalleDTO.getIdMedicamento());
    }

    @Test
    void testGetNombreMedicamento() {
        assertNull(carritoDetalleDTO.getNombreMedicamento());
        
        carritoDetalleDTO.setNombreMedicamento("Paracetamol");
        assertEquals("Paracetamol", carritoDetalleDTO.getNombreMedicamento());
    }

    @Test
    void testSetNombreMedicamento() {
        carritoDetalleDTO.setNombreMedicamento("Ibuprofeno");
        assertEquals("Ibuprofeno", carritoDetalleDTO.getNombreMedicamento());
        
        carritoDetalleDTO.setNombreMedicamento("");
        assertEquals("", carritoDetalleDTO.getNombreMedicamento());
        
        carritoDetalleDTO.setNombreMedicamento(null);
        assertNull(carritoDetalleDTO.getNombreMedicamento());
    }

    @Test
    void testGetCantidad() {
        assertNull(carritoDetalleDTO.getCantidad());
        
        carritoDetalleDTO.setCantidad(5);
        assertEquals(5, carritoDetalleDTO.getCantidad());
    }

    @Test
    void testSetCantidad() {
        carritoDetalleDTO.setCantidad(10);
        assertEquals(10, carritoDetalleDTO.getCantidad());
        
        carritoDetalleDTO.setCantidad(0);
        assertEquals(0, carritoDetalleDTO.getCantidad());
        
        carritoDetalleDTO.setCantidad(null);
        assertNull(carritoDetalleDTO.getCantidad());
    }

    @Test
    void testGetPrecioUnitario() {
        assertNull(carritoDetalleDTO.getPrecioUnitario());
        
        carritoDetalleDTO.setPrecioUnitario(15.50);
        assertEquals(15.50, carritoDetalleDTO.getPrecioUnitario());
    }

    @Test
    void testSetPrecioUnitario() {
        carritoDetalleDTO.setPrecioUnitario(25.00);
        assertEquals(25.00, carritoDetalleDTO.getPrecioUnitario());
        
        carritoDetalleDTO.setPrecioUnitario(0.0);
        assertEquals(0.0, carritoDetalleDTO.getPrecioUnitario());
        
        carritoDetalleDTO.setPrecioUnitario(null);
        assertNull(carritoDetalleDTO.getPrecioUnitario());
    }

    @Test
    void testGetTotal() {
        assertNull(carritoDetalleDTO.getTotal());
        
        carritoDetalleDTO.setTotal(77.50);
        assertEquals(77.50, carritoDetalleDTO.getTotal());
    }

    @Test
    void testSetTotal() {
        carritoDetalleDTO.setTotal(100.00);
        assertEquals(100.00, carritoDetalleDTO.getTotal());
        
        carritoDetalleDTO.setTotal(0.0);
        assertEquals(0.0, carritoDetalleDTO.getTotal());
        
        carritoDetalleDTO.setTotal(null);
        assertNull(carritoDetalleDTO.getTotal());
    }

    @Test
    void testGetRequiereReceta() {
        assertNull(carritoDetalleDTO.getRequiereReceta());
        
        carritoDetalleDTO.setRequiereReceta("Sí");
        assertEquals("Sí", carritoDetalleDTO.getRequiereReceta());
    }

    @Test
    void testSetRequiereReceta() {
        carritoDetalleDTO.setRequiereReceta("No");
        assertEquals("No", carritoDetalleDTO.getRequiereReceta());
        
        carritoDetalleDTO.setRequiereReceta("");
        assertEquals("", carritoDetalleDTO.getRequiereReceta());
        
        carritoDetalleDTO.setRequiereReceta(null);
        assertNull(carritoDetalleDTO.getRequiereReceta());
    }

    @Test
    void testSetIdCartItem_WithDifferentValues() {
        carritoDetalleDTO.setIdCartItem(1L);
        assertEquals(1L, carritoDetalleDTO.getIdCartItem());
        
        carritoDetalleDTO.setIdCartItem(999L);
        assertEquals(999L, carritoDetalleDTO.getIdCartItem());
        
        carritoDetalleDTO.setIdCartItem(Long.MAX_VALUE);
        assertEquals(Long.MAX_VALUE, carritoDetalleDTO.getIdCartItem());
    }

    @Test
    void testSetIdMedicamento_WithDifferentValues() {
        carritoDetalleDTO.setIdMedicamento(10L);
        assertEquals(10L, carritoDetalleDTO.getIdMedicamento());
        
        carritoDetalleDTO.setIdMedicamento(500L);
        assertEquals(500L, carritoDetalleDTO.getIdMedicamento());
        
        carritoDetalleDTO.setIdMedicamento(Long.MIN_VALUE);
        assertEquals(Long.MIN_VALUE, carritoDetalleDTO.getIdMedicamento());
    }

    @Test
    void testSetNombreMedicamento_WithDifferentValues() {
        carritoDetalleDTO.setNombreMedicamento("Medicamento 1");
        assertEquals("Medicamento 1", carritoDetalleDTO.getNombreMedicamento());
        
        carritoDetalleDTO.setNombreMedicamento("Medicamento 2");
        assertEquals("Medicamento 2", carritoDetalleDTO.getNombreMedicamento());
        
        carritoDetalleDTO.setNombreMedicamento("A");
        assertEquals("A", carritoDetalleDTO.getNombreMedicamento());
    }

    @Test
    void testSetCantidad_WithDifferentValues() {
        carritoDetalleDTO.setCantidad(1);
        assertEquals(1, carritoDetalleDTO.getCantidad());
        
        carritoDetalleDTO.setCantidad(99);
        assertEquals(99, carritoDetalleDTO.getCantidad());
        
        carritoDetalleDTO.setCantidad(1000);
        assertEquals(1000, carritoDetalleDTO.getCantidad());
    }

    @Test
    void testSetPrecioUnitario_WithDifferentValues() {
        carritoDetalleDTO.setPrecioUnitario(10.0);
        assertEquals(10.0, carritoDetalleDTO.getPrecioUnitario());
        
        carritoDetalleDTO.setPrecioUnitario(99.99);
        assertEquals(99.99, carritoDetalleDTO.getPrecioUnitario());
        
        carritoDetalleDTO.setPrecioUnitario(0.01);
        assertEquals(0.01, carritoDetalleDTO.getPrecioUnitario());
    }

    @Test
    void testSetTotal_WithDifferentValues() {
        carritoDetalleDTO.setTotal(50.0);
        assertEquals(50.0, carritoDetalleDTO.getTotal());
        
        carritoDetalleDTO.setTotal(150.50);
        assertEquals(150.50, carritoDetalleDTO.getTotal());
        
        carritoDetalleDTO.setTotal(0.99);
        assertEquals(0.99, carritoDetalleDTO.getTotal());
    }

    @Test
    void testSetRequiereReceta_WithDifferentValues() {
        carritoDetalleDTO.setRequiereReceta("Sí");
        assertEquals("Sí", carritoDetalleDTO.getRequiereReceta());
        
        carritoDetalleDTO.setRequiereReceta("No");
        assertEquals("No", carritoDetalleDTO.getRequiereReceta());
        
        carritoDetalleDTO.setRequiereReceta("Opcional");
        assertEquals("Opcional", carritoDetalleDTO.getRequiereReceta());
    }

    @Test
    void testSetPrecioUnitario_WithEdgeCases() {
        carritoDetalleDTO.setPrecioUnitario(Double.MAX_VALUE);
        assertEquals(Double.MAX_VALUE, carritoDetalleDTO.getPrecioUnitario());
        
        carritoDetalleDTO.setPrecioUnitario(Double.MIN_VALUE);
        assertEquals(Double.MIN_VALUE, carritoDetalleDTO.getPrecioUnitario());
        
        carritoDetalleDTO.setPrecioUnitario(Double.POSITIVE_INFINITY);
        assertEquals(Double.POSITIVE_INFINITY, carritoDetalleDTO.getPrecioUnitario());
        
        carritoDetalleDTO.setPrecioUnitario(Double.NEGATIVE_INFINITY);
        assertEquals(Double.NEGATIVE_INFINITY, carritoDetalleDTO.getPrecioUnitario());
        
        carritoDetalleDTO.setPrecioUnitario(Double.NaN);
        assertTrue(Double.isNaN(carritoDetalleDTO.getPrecioUnitario()));
    }

    @Test
    void testSetTotal_WithEdgeCases() {
        carritoDetalleDTO.setTotal(Double.MAX_VALUE);
        assertEquals(Double.MAX_VALUE, carritoDetalleDTO.getTotal());
        
        carritoDetalleDTO.setTotal(Double.MIN_VALUE);
        assertEquals(Double.MIN_VALUE, carritoDetalleDTO.getTotal());
        
        carritoDetalleDTO.setTotal(Double.POSITIVE_INFINITY);
        assertEquals(Double.POSITIVE_INFINITY, carritoDetalleDTO.getTotal());
        
        carritoDetalleDTO.setTotal(Double.NEGATIVE_INFINITY);
        assertEquals(Double.NEGATIVE_INFINITY, carritoDetalleDTO.getTotal());
        
        carritoDetalleDTO.setTotal(Double.NaN);
        assertTrue(Double.isNaN(carritoDetalleDTO.getTotal()));
    }

    @Test
    void testSetCantidad_WithEdgeCases() {
        carritoDetalleDTO.setCantidad(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, carritoDetalleDTO.getCantidad());
        
        carritoDetalleDTO.setCantidad(Integer.MIN_VALUE);
        assertEquals(Integer.MIN_VALUE, carritoDetalleDTO.getCantidad());
        
        carritoDetalleDTO.setCantidad(0);
        assertEquals(0, carritoDetalleDTO.getCantidad());
    }

    @Test
    void testMultipleFieldUpdates() {
        carritoDetalleDTO.setIdCartItem(111L);
        carritoDetalleDTO.setIdMedicamento(222L);
        carritoDetalleDTO.setNombreMedicamento("Medicamento 1");
        carritoDetalleDTO.setCantidad(5);
        carritoDetalleDTO.setPrecioUnitario(15.0);
        carritoDetalleDTO.setTotal(75.0);
        carritoDetalleDTO.setRequiereReceta("Sí");
        
        assertEquals(111L, carritoDetalleDTO.getIdCartItem());
        assertEquals(222L, carritoDetalleDTO.getIdMedicamento());
        assertEquals("Medicamento 1", carritoDetalleDTO.getNombreMedicamento());
        assertEquals(5, carritoDetalleDTO.getCantidad());
        assertEquals(15.0, carritoDetalleDTO.getPrecioUnitario());
        assertEquals(75.0, carritoDetalleDTO.getTotal());
        assertEquals("Sí", carritoDetalleDTO.getRequiereReceta());
        
        // Update all fields again
        carritoDetalleDTO.setIdCartItem(333L);
        carritoDetalleDTO.setIdMedicamento(444L);
        carritoDetalleDTO.setNombreMedicamento("Medicamento 2");
        carritoDetalleDTO.setCantidad(10);
        carritoDetalleDTO.setPrecioUnitario(25.0);
        carritoDetalleDTO.setTotal(250.0);
        carritoDetalleDTO.setRequiereReceta("No");
        
        assertEquals(333L, carritoDetalleDTO.getIdCartItem());
        assertEquals(444L, carritoDetalleDTO.getIdMedicamento());
        assertEquals("Medicamento 2", carritoDetalleDTO.getNombreMedicamento());
        assertEquals(10, carritoDetalleDTO.getCantidad());
        assertEquals(25.0, carritoDetalleDTO.getPrecioUnitario());
        assertEquals(250.0, carritoDetalleDTO.getTotal());
        assertEquals("No", carritoDetalleDTO.getRequiereReceta());
    }

    @Test
    void testNullToValueToNull() {
        // Start with null values
        assertNull(carritoDetalleDTO.getIdCartItem());
        assertNull(carritoDetalleDTO.getIdMedicamento());
        assertNull(carritoDetalleDTO.getNombreMedicamento());
        assertNull(carritoDetalleDTO.getCantidad());
        assertNull(carritoDetalleDTO.getPrecioUnitario());
        assertNull(carritoDetalleDTO.getTotal());
        assertNull(carritoDetalleDTO.getRequiereReceta());
        
        // Set values
        carritoDetalleDTO.setIdCartItem(999L);
        carritoDetalleDTO.setIdMedicamento(888L);
        carritoDetalleDTO.setNombreMedicamento("Test Medicamento");
        carritoDetalleDTO.setCantidad(99);
        carritoDetalleDTO.setPrecioUnitario(99.99);
        carritoDetalleDTO.setTotal(9999.01);
        carritoDetalleDTO.setRequiereReceta("Test");
        
        assertEquals(999L, carritoDetalleDTO.getIdCartItem());
        assertEquals(888L, carritoDetalleDTO.getIdMedicamento());
        assertEquals("Test Medicamento", carritoDetalleDTO.getNombreMedicamento());
        assertEquals(99, carritoDetalleDTO.getCantidad());
        assertEquals(99.99, carritoDetalleDTO.getPrecioUnitario());
        assertEquals(9999.01, carritoDetalleDTO.getTotal());
        assertEquals("Test", carritoDetalleDTO.getRequiereReceta());
        
        // Set back to null
        carritoDetalleDTO.setIdCartItem(null);
        carritoDetalleDTO.setIdMedicamento(null);
        carritoDetalleDTO.setNombreMedicamento(null);
        carritoDetalleDTO.setCantidad(null);
        carritoDetalleDTO.setPrecioUnitario(null);
        carritoDetalleDTO.setTotal(null);
        carritoDetalleDTO.setRequiereReceta(null);
        
        assertNull(carritoDetalleDTO.getIdCartItem());
        assertNull(carritoDetalleDTO.getIdMedicamento());
        assertNull(carritoDetalleDTO.getNombreMedicamento());
        assertNull(carritoDetalleDTO.getCantidad());
        assertNull(carritoDetalleDTO.getPrecioUnitario());
        assertNull(carritoDetalleDTO.getTotal());
        assertNull(carritoDetalleDTO.getRequiereReceta());
    }

    @Test
    void testMultipleInstances() {
        CarritoDetalleDTO dto1 = new CarritoDetalleDTO();
        CarritoDetalleDTO dto2 = new CarritoDetalleDTO();
        
        dto1.setIdCartItem(111L);
        dto1.setIdMedicamento(222L);
        dto1.setNombreMedicamento("Medicamento 1");
        
        dto2.setIdCartItem(333L);
        dto2.setIdMedicamento(444L);
        dto2.setNombreMedicamento("Medicamento 2");
        
        assertEquals(111L, dto1.getIdCartItem());
        assertEquals(222L, dto1.getIdMedicamento());
        assertEquals("Medicamento 1", dto1.getNombreMedicamento());
        
        assertEquals(333L, dto2.getIdCartItem());
        assertEquals(444L, dto2.getIdMedicamento());
        assertEquals("Medicamento 2", dto2.getNombreMedicamento());
        
        // Verify they are independent
        dto1.setIdCartItem(555L);
        dto1.setIdMedicamento(666L);
        dto1.setNombreMedicamento("Medicamento 3");
        
        assertEquals(555L, dto1.getIdCartItem());
        assertEquals(666L, dto1.getIdMedicamento());
        assertEquals("Medicamento 3", dto1.getNombreMedicamento());
        
        assertEquals(333L, dto2.getIdCartItem());
        assertEquals(444L, dto2.getIdMedicamento());
        assertEquals("Medicamento 2", dto2.getNombreMedicamento());
    }
}


