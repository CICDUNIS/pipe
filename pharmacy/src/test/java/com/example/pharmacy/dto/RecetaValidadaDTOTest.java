package com.example.pharmacy.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class RecetaValidadaDTOTest {

    private RecetaValidadaDTO recetaValidadaDTO;

    @BeforeEach
    void setUp() {
        recetaValidadaDTO = new RecetaValidadaDTO();
    }

    @Test
    void testGetCodigoReceta() {
        assertNull(recetaValidadaDTO.getCodigoReceta());
        
        recetaValidadaDTO.setCodigoReceta("REC-001");
        assertEquals("REC-001", recetaValidadaDTO.getCodigoReceta());
    }

    @Test
    void testSetCodigoReceta() {
        recetaValidadaDTO.setCodigoReceta("REC-ABC123");
        assertEquals("REC-ABC123", recetaValidadaDTO.getCodigoReceta());
        
        recetaValidadaDTO.setCodigoReceta("");
        assertEquals("", recetaValidadaDTO.getCodigoReceta());
        
        recetaValidadaDTO.setCodigoReceta(null);
        assertNull(recetaValidadaDTO.getCodigoReceta());
    }

    @Test
    void testIsValida() {
        assertFalse(recetaValidadaDTO.isValida());
        
        recetaValidadaDTO.setValida(true);
        assertTrue(recetaValidadaDTO.isValida());
    }

    @Test
    void testSetValida() {
        recetaValidadaDTO.setValida(true);
        assertTrue(recetaValidadaDTO.isValida());
        
        recetaValidadaDTO.setValida(false);
        assertFalse(recetaValidadaDTO.isValida());
    }

    @Test
    void testGetPaciente() {
        assertNull(recetaValidadaDTO.getPaciente());
        
        recetaValidadaDTO.setPaciente("Juan Pérez");
        assertEquals("Juan Pérez", recetaValidadaDTO.getPaciente());
    }

    @Test
    void testSetPaciente() {
        recetaValidadaDTO.setPaciente("María García");
        assertEquals("María García", recetaValidadaDTO.getPaciente());
        
        recetaValidadaDTO.setPaciente("");
        assertEquals("", recetaValidadaDTO.getPaciente());
        
        recetaValidadaDTO.setPaciente(null);
        assertNull(recetaValidadaDTO.getPaciente());
    }

    @Test
    void testGetDoctor() {
        assertNull(recetaValidadaDTO.getDoctor());
        
        recetaValidadaDTO.setDoctor("Dr. Carlos López");
        assertEquals("Dr. Carlos López", recetaValidadaDTO.getDoctor());
    }

    @Test
    void testSetDoctor() {
        recetaValidadaDTO.setDoctor("Dr. Ana Martínez");
        assertEquals("Dr. Ana Martínez", recetaValidadaDTO.getDoctor());
        
        recetaValidadaDTO.setDoctor("");
        assertEquals("", recetaValidadaDTO.getDoctor());
        
        recetaValidadaDTO.setDoctor(null);
        assertNull(recetaValidadaDTO.getDoctor());
    }

    @Test
    void testGetMedicamentos() {
        assertNull(recetaValidadaDTO.getMedicamentos());
        
        List<String> medicamentos = new ArrayList<>();
        medicamentos.add("Paracetamol");
        recetaValidadaDTO.setMedicamentos(medicamentos);
        assertEquals(medicamentos, recetaValidadaDTO.getMedicamentos());
    }

    @Test
    void testSetMedicamentos() {
        List<String> medicamentos = new ArrayList<>();
        medicamentos.add("Ibuprofeno");
        medicamentos.add("Aspirina");
        recetaValidadaDTO.setMedicamentos(medicamentos);
        
        assertEquals(medicamentos, recetaValidadaDTO.getMedicamentos());
        assertEquals(2, recetaValidadaDTO.getMedicamentos().size());
        
        recetaValidadaDTO.setMedicamentos(null);
        assertNull(recetaValidadaDTO.getMedicamentos());
    }

    @Test
    void testGetObservaciones() {
        assertNull(recetaValidadaDTO.getObservaciones());
        
        recetaValidadaDTO.setObservaciones("Tomar con alimentos");
        assertEquals("Tomar con alimentos", recetaValidadaDTO.getObservaciones());
    }

    @Test
    void testSetObservaciones() {
        recetaValidadaDTO.setObservaciones("Evitar alcohol");
        assertEquals("Evitar alcohol", recetaValidadaDTO.getObservaciones());
        
        recetaValidadaDTO.setObservaciones("");
        assertEquals("", recetaValidadaDTO.getObservaciones());
        
        recetaValidadaDTO.setObservaciones(null);
        assertNull(recetaValidadaDTO.getObservaciones());
    }

    @Test
    void testSetCodigoReceta_WithDifferentValues() {
        recetaValidadaDTO.setCodigoReceta("REC-123");
        assertEquals("REC-123", recetaValidadaDTO.getCodigoReceta());
        
        recetaValidadaDTO.setCodigoReceta("ABC-456");
        assertEquals("ABC-456", recetaValidadaDTO.getCodigoReceta());
        
        recetaValidadaDTO.setCodigoReceta("XYZ789");
        assertEquals("XYZ789", recetaValidadaDTO.getCodigoReceta());
    }

    @Test
    void testSetValida_WithDifferentValues() {
        recetaValidadaDTO.setValida(true);
        assertTrue(recetaValidadaDTO.isValida());
        
        recetaValidadaDTO.setValida(false);
        assertFalse(recetaValidadaDTO.isValida());
        
        recetaValidadaDTO.setValida(true);
        assertTrue(recetaValidadaDTO.isValida());
    }

    @Test
    void testSetPaciente_WithDifferentValues() {
        recetaValidadaDTO.setPaciente("Paciente 1");
        assertEquals("Paciente 1", recetaValidadaDTO.getPaciente());
        
        recetaValidadaDTO.setPaciente("Paciente 2");
        assertEquals("Paciente 2", recetaValidadaDTO.getPaciente());
        
        recetaValidadaDTO.setPaciente("A");
        assertEquals("A", recetaValidadaDTO.getPaciente());
    }

    @Test
    void testSetDoctor_WithDifferentValues() {
        recetaValidadaDTO.setDoctor("Doctor 1");
        assertEquals("Doctor 1", recetaValidadaDTO.getDoctor());
        
        recetaValidadaDTO.setDoctor("Doctor 2");
        assertEquals("Doctor 2", recetaValidadaDTO.getDoctor());
        
        recetaValidadaDTO.setDoctor("Dr. X");
        assertEquals("Dr. X", recetaValidadaDTO.getDoctor());
    }

    @Test
    void testSetMedicamentos_WithDifferentValues() {
        List<String> medicamentos1 = Arrays.asList("Medicamento 1");
        recetaValidadaDTO.setMedicamentos(medicamentos1);
        assertEquals(medicamentos1, recetaValidadaDTO.getMedicamentos());
        assertEquals(1, recetaValidadaDTO.getMedicamentos().size());
        
        List<String> medicamentos2 = Arrays.asList("Medicamento 1", "Medicamento 2");
        recetaValidadaDTO.setMedicamentos(medicamentos2);
        assertEquals(medicamentos2, recetaValidadaDTO.getMedicamentos());
        assertEquals(2, recetaValidadaDTO.getMedicamentos().size());
        
        List<String> medicamentos3 = new ArrayList<>();
        recetaValidadaDTO.setMedicamentos(medicamentos3);
        assertEquals(medicamentos3, recetaValidadaDTO.getMedicamentos());
        assertTrue(recetaValidadaDTO.getMedicamentos().isEmpty());
    }

    @Test
    void testSetObservaciones_WithDifferentValues() {
        recetaValidadaDTO.setObservaciones("Observación 1");
        assertEquals("Observación 1", recetaValidadaDTO.getObservaciones());
        
        recetaValidadaDTO.setObservaciones("Observación 2");
        assertEquals("Observación 2", recetaValidadaDTO.getObservaciones());
        
        recetaValidadaDTO.setObservaciones("Test");
        assertEquals("Test", recetaValidadaDTO.getObservaciones());
    }

    @Test
    void testSetMedicamentos_WithModifiableList() {
        List<String> medicamentos = new ArrayList<>();
        medicamentos.add("Paracetamol");
        recetaValidadaDTO.setMedicamentos(medicamentos);
        
        assertEquals(1, recetaValidadaDTO.getMedicamentos().size());
        
        // Modify the original list
        medicamentos.add("Ibuprofeno");
        
        // The DTO should reflect the changes
        assertEquals(2, recetaValidadaDTO.getMedicamentos().size());
    }

    @Test
    void testSetMedicamentos_WithUnmodifiableList() {
        List<String> medicamentos = Arrays.asList("Paracetamol", "Ibuprofeno");
        recetaValidadaDTO.setMedicamentos(medicamentos);
        
        assertEquals(medicamentos, recetaValidadaDTO.getMedicamentos());
        assertEquals(2, recetaValidadaDTO.getMedicamentos().size());
    }

    @Test
    void testSetMedicamentos_WithLargeList() {
        List<String> largeList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            largeList.add("Medicamento " + i);
        }
        
        recetaValidadaDTO.setMedicamentos(largeList);
        
        assertEquals(largeList, recetaValidadaDTO.getMedicamentos());
        assertEquals(100, recetaValidadaDTO.getMedicamentos().size());
    }

    @Test
    void testToString() {
        recetaValidadaDTO.setCodigoReceta("REC-001");
        recetaValidadaDTO.setValida(true);
        recetaValidadaDTO.setPaciente("Juan Pérez");
        recetaValidadaDTO.setDoctor("Dr. Carlos López");
        recetaValidadaDTO.setMedicamentos(Arrays.asList("Paracetamol", "Ibuprofeno"));
        recetaValidadaDTO.setObservaciones("Tomar con alimentos");
        
        String result = recetaValidadaDTO.toString();
        
        assertTrue(result.contains("codigoReceta='REC-001'"));
        assertTrue(result.contains("valida=true"));
        assertTrue(result.contains("paciente='Juan Pérez'"));
        assertTrue(result.contains("doctor='Dr. Carlos López'"));
        assertTrue(result.contains("medicamentos=[Paracetamol, Ibuprofeno]"));
        assertTrue(result.contains("observaciones='Tomar con alimentos'"));
        assertTrue(result.startsWith("RecetaValidadaDTO{"));
        assertTrue(result.endsWith("}"));
    }

    @Test
    void testToString_WithNullValues() {
        String result = recetaValidadaDTO.toString();
        
        assertTrue(result.contains("codigoReceta='null'"));
        assertTrue(result.contains("valida=false"));
        assertTrue(result.contains("paciente='null'"));
        assertTrue(result.contains("doctor='null'"));
        assertTrue(result.contains("medicamentos=null"));
        assertTrue(result.contains("observaciones='null'"));
    }

    @Test
    void testToString_WithAllValues() {
        recetaValidadaDTO.setCodigoReceta("REC-ABC123");
        recetaValidadaDTO.setValida(false);
        recetaValidadaDTO.setPaciente("María García");
        recetaValidadaDTO.setDoctor("Dr. Ana Martínez");
        recetaValidadaDTO.setMedicamentos(Arrays.asList("Aspirina"));
        recetaValidadaDTO.setObservaciones("Evitar alcohol");
        
        String result = recetaValidadaDTO.toString();
        
        assertTrue(result.contains("codigoReceta='REC-ABC123'"));
        assertTrue(result.contains("valida=false"));
        assertTrue(result.contains("paciente='María García'"));
        assertTrue(result.contains("doctor='Dr. Ana Martínez'"));
        assertTrue(result.contains("medicamentos=[Aspirina]"));
        assertTrue(result.contains("observaciones='Evitar alcohol'"));
    }

    @Test
    void testMultipleFieldUpdates() {
        recetaValidadaDTO.setCodigoReceta("REC-111");
        recetaValidadaDTO.setValida(true);
        recetaValidadaDTO.setPaciente("Paciente 1");
        recetaValidadaDTO.setDoctor("Doctor 1");
        recetaValidadaDTO.setMedicamentos(Arrays.asList("Medicamento 1"));
        recetaValidadaDTO.setObservaciones("Observación 1");
        
        assertEquals("REC-111", recetaValidadaDTO.getCodigoReceta());
        assertTrue(recetaValidadaDTO.isValida());
        assertEquals("Paciente 1", recetaValidadaDTO.getPaciente());
        assertEquals("Doctor 1", recetaValidadaDTO.getDoctor());
        assertEquals(1, recetaValidadaDTO.getMedicamentos().size());
        assertEquals("Observación 1", recetaValidadaDTO.getObservaciones());
        
        // Update all fields again
        recetaValidadaDTO.setCodigoReceta("REC-222");
        recetaValidadaDTO.setValida(false);
        recetaValidadaDTO.setPaciente("Paciente 2");
        recetaValidadaDTO.setDoctor("Doctor 2");
        recetaValidadaDTO.setMedicamentos(Arrays.asList("Medicamento 1", "Medicamento 2"));
        recetaValidadaDTO.setObservaciones("Observación 2");
        
        assertEquals("REC-222", recetaValidadaDTO.getCodigoReceta());
        assertFalse(recetaValidadaDTO.isValida());
        assertEquals("Paciente 2", recetaValidadaDTO.getPaciente());
        assertEquals("Doctor 2", recetaValidadaDTO.getDoctor());
        assertEquals(2, recetaValidadaDTO.getMedicamentos().size());
        assertEquals("Observación 2", recetaValidadaDTO.getObservaciones());
    }

    @Test
    void testNullToValueToNull() {
        // Start with null values
        assertNull(recetaValidadaDTO.getCodigoReceta());
        assertNull(recetaValidadaDTO.getPaciente());
        assertNull(recetaValidadaDTO.getDoctor());
        assertNull(recetaValidadaDTO.getMedicamentos());
        assertNull(recetaValidadaDTO.getObservaciones());
        
        // Set values
        recetaValidadaDTO.setCodigoReceta("REC-TEST");
        recetaValidadaDTO.setPaciente("Test Paciente");
        recetaValidadaDTO.setDoctor("Test Doctor");
        recetaValidadaDTO.setMedicamentos(Arrays.asList("Test Medicamento"));
        recetaValidadaDTO.setObservaciones("Test Observación");
        
        assertEquals("REC-TEST", recetaValidadaDTO.getCodigoReceta());
        assertEquals("Test Paciente", recetaValidadaDTO.getPaciente());
        assertEquals("Test Doctor", recetaValidadaDTO.getDoctor());
        assertEquals(1, recetaValidadaDTO.getMedicamentos().size());
        assertEquals("Test Observación", recetaValidadaDTO.getObservaciones());
        
        // Set back to null
        recetaValidadaDTO.setCodigoReceta(null);
        recetaValidadaDTO.setPaciente(null);
        recetaValidadaDTO.setDoctor(null);
        recetaValidadaDTO.setMedicamentos(null);
        recetaValidadaDTO.setObservaciones(null);
        
        assertNull(recetaValidadaDTO.getCodigoReceta());
        assertNull(recetaValidadaDTO.getPaciente());
        assertNull(recetaValidadaDTO.getDoctor());
        assertNull(recetaValidadaDTO.getMedicamentos());
        assertNull(recetaValidadaDTO.getObservaciones());
    }

    @Test
    void testMultipleInstances() {
        RecetaValidadaDTO dto1 = new RecetaValidadaDTO();
        RecetaValidadaDTO dto2 = new RecetaValidadaDTO();
        
        dto1.setCodigoReceta("REC-111");
        dto1.setValida(true);
        dto1.setPaciente("Paciente 1");
        
        dto2.setCodigoReceta("REC-222");
        dto2.setValida(false);
        dto2.setPaciente("Paciente 2");
        
        assertEquals("REC-111", dto1.getCodigoReceta());
        assertTrue(dto1.isValida());
        assertEquals("Paciente 1", dto1.getPaciente());
        
        assertEquals("REC-222", dto2.getCodigoReceta());
        assertFalse(dto2.isValida());
        assertEquals("Paciente 2", dto2.getPaciente());
        
        // Verify they are independent
        dto1.setCodigoReceta("REC-333");
        dto1.setValida(false);
        dto1.setPaciente("Paciente 3");
        
        assertEquals("REC-333", dto1.getCodigoReceta());
        assertFalse(dto1.isValida());
        assertEquals("Paciente 3", dto1.getPaciente());
        
        assertEquals("REC-222", dto2.getCodigoReceta());
        assertFalse(dto2.isValida());
        assertEquals("Paciente 2", dto2.getPaciente());
    }
}


