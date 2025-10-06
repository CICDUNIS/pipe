package com.example.pharmacy.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioRolTest {

    @Test
    void testConstructorVacioYSettersGetters() {
        UsuarioRol usuarioRol = new UsuarioRol();

        usuarioRol.setIdUsuario(1L);
        usuarioRol.setIdRol(2L);

        assertEquals(1L, usuarioRol.getIdUsuario());
        assertEquals(2L, usuarioRol.getIdRol());
    }

    @Test
    void testConstructorConParametros() {
        UsuarioRol usuarioRol = new UsuarioRol(10L, 20L);

        assertEquals(10L, usuarioRol.getIdUsuario());
        assertEquals(20L, usuarioRol.getIdRol());
    }

    @Test
    void testToString() {
        UsuarioRol usuarioRol = new UsuarioRol(5L, 6L);
        String esperado = "UsuarioRol{idUsuario=5, idRol=6}";
        assertEquals(esperado, usuarioRol.toString());
    }

    @Test
    void testEqualsYHashCode() {
        UsuarioRol ur1 = new UsuarioRol(1L, 2L);
        UsuarioRol ur2 = new UsuarioRol(1L, 2L);
        UsuarioRol ur3 = new UsuarioRol(1L, 3L);
        UsuarioRol ur4 = new UsuarioRol(3L, 2L);
        UsuarioRol urNull = new UsuarioRol(null, null);

        // Igualdad reflexiva
        assertEquals(ur1, ur1);
        // Igualdad con otro objeto igual
        assertEquals(ur1, ur2);
        assertEquals(ur1.hashCode(), ur2.hashCode());

        // Diferente idRol
        assertNotEquals(ur1, ur3);
        // Diferente idUsuario
        assertNotEquals(ur1, ur4);
        // Null
        assertNotEquals(ur1, null);
        // Otro tipo
        assertNotEquals(ur1, "string");

        // Igualdad con null fields
        UsuarioRol urNull2 = new UsuarioRol(null, null);
        assertEquals(urNull, urNull2);
        assertEquals(urNull.hashCode(), urNull2.hashCode());
    }
}
