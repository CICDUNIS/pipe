package com.example.pharmacy.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RolTest {

    @Test
    void testConstructoresGettersSetters() {
        // Constructor vacío y setters
        Rol rol1 = new Rol();
        rol1.setIdRol(1L);
        rol1.setNombreRol("ADMIN");

        assertEquals(1L, rol1.getIdRol());
        assertEquals("ADMIN", rol1.getNombreRol());

        // Constructor con parámetros
        Rol rol2 = new Rol(2L, "USER");

        assertEquals(2L, rol2.getIdRol());
        assertEquals("USER", rol2.getNombreRol());
    }

    @Test
    void testToString() {
        Rol rol = new Rol(3L, "MANAGER");
        String esperado = "Rol{idRol=3, nombreRol='MANAGER'}";
        assertEquals(esperado, rol.toString());
    }
}
