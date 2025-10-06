package com.example.pharmacy.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class AuditoriaFarmaciaTest {

    @Test
    void testGettersSetters() {
        AuditoriaFarmacia auditoria = new AuditoriaFarmacia();

        // Valores de prueba
        Long id = 123L;
        String tabla = "TABLA_TEST";
        String tipoCambio = "INSERT";
        String descripcion = "Descripcion de prueba";
        String usuario = "usuarioTest";
        LocalDateTime fecha = LocalDateTime.now();

        // Setters
        auditoria.setIdAuditoria(id);
        auditoria.setTablaAfectada(tabla);
        auditoria.setTipoCambio(tipoCambio);
        auditoria.setDescripcion(descripcion);
        auditoria.setUsuario(usuario);
        auditoria.setFecha(fecha);

        // Getters y asserts
        assertEquals(id, auditoria.getIdAuditoria());
        assertEquals(tabla, auditoria.getTablaAfectada());
        assertEquals(tipoCambio, auditoria.getTipoCambio());
        assertEquals(descripcion, auditoria.getDescripcion());
        assertEquals(usuario, auditoria.getUsuario());
        assertEquals(fecha, auditoria.getFecha());
    }

    @Test
    void testValoresNulosPorDefecto() {
        AuditoriaFarmacia auditoria = new AuditoriaFarmacia();

        // Por defecto los valores deben ser null
        assertNull(auditoria.getIdAuditoria());
        assertNull(auditoria.getTablaAfectada());
        assertNull(auditoria.getTipoCambio());
        assertNull(auditoria.getDescripcion());
        assertNull(auditoria.getUsuario());
        assertNull(auditoria.getFecha());
    }
}
