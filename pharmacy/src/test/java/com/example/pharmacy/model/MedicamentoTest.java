package com.example.pharmacy.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MedicamentoTest {

    @Test
    void testGettersAndSetters() {
        Medicamento medicamento = new Medicamento();

        medicamento.setIdMedicamento(1L);
        medicamento.setCodigo("MED123");
        medicamento.setNombre("Paracetamol");
        medicamento.setCategoria("Analgésico");
        medicamento.setPrincipioActivo("Paracetamol");
        medicamento.setDescripcion("Alivia el dolor y la fiebre.");
        medicamento.setFotoUrl("http://example.com/foto.jpg");
        medicamento.setConcentracion("500mg");
        medicamento.setPresentacion("Tabletas");
        medicamento.setNumeroUnidades(20);
        medicamento.setMarca("Genérico");
        medicamento.setRequiereReceta("Y");
        medicamento.setStock(100);
        medicamento.setPrecio(10.5);

        assertEquals(1L, medicamento.getIdMedicamento());
        assertEquals("MED123", medicamento.getCodigo());
        assertEquals("Paracetamol", medicamento.getNombre());
        assertEquals("Analgésico", medicamento.getCategoria());
        assertEquals("Paracetamol", medicamento.getPrincipioActivo());
        assertEquals("Alivia el dolor y la fiebre.", medicamento.getDescripcion());
        assertEquals("http://example.com/foto.jpg", medicamento.getFotoUrl());
        assertEquals("500mg", medicamento.getConcentracion());
        assertEquals("Tabletas", medicamento.getPresentacion());
        assertEquals(20, medicamento.getNumeroUnidades());
        assertEquals("Genérico", medicamento.getMarca());
        assertEquals("Y", medicamento.getRequiereReceta());
        assertEquals(100, medicamento.getStock());
        assertEquals(10.5, medicamento.getPrecio());
    }

    @Test
    void testIsRequiereReceta() {
        Medicamento medicamento = new Medicamento();

        medicamento.setRequiereReceta("Y");
        assertTrue(medicamento.isRequiereReceta());

        medicamento.setRequiereReceta("y");
        assertTrue(medicamento.isRequiereReceta());

        medicamento.setRequiereReceta("N");
        assertFalse(medicamento.isRequiereReceta());

        medicamento.setRequiereReceta("n");
        assertFalse(medicamento.isRequiereReceta());

        medicamento.setRequiereReceta(null);
        assertFalse(medicamento.isRequiereReceta());
    }

    @Test
    void testSetRequiereRecetaBoolean() {
        Medicamento medicamento = new Medicamento();

        medicamento.setRequiereRecetaBoolean(true);
        assertEquals("Y", medicamento.getRequiereReceta());
        assertTrue(medicamento.isRequiereReceta());

        medicamento.setRequiereRecetaBoolean(false);
        assertEquals("N", medicamento.getRequiereReceta());
        assertFalse(medicamento.isRequiereReceta());
    }
}
