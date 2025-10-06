package com.example.pharmacy.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioTest {

    @Test
    void testConstructorVacioYSettersGetters() {
        Usuario usuario = new Usuario();

        usuario.setIdUsuario(10L);
        usuario.setNombre("Sofi");
        usuario.setCorreo("sofi@example.com");
        usuario.setPasswordHash("hashedpassword");
        usuario.setActivo("Y");
        usuario.setFechaCreacion(LocalDateTime.of(2025, 8, 7, 12, 0));
        usuario.setPerfilCompleto("N");
        usuario.setPrimerLogin("Y");

        assertEquals(10L, usuario.getIdUsuario());
        assertEquals("Sofi", usuario.getNombre());
        assertEquals("sofi@example.com", usuario.getCorreo());
        assertEquals("hashedpassword", usuario.getPasswordHash());
        assertEquals("Y", usuario.getActivo());
        assertEquals(LocalDateTime.of(2025, 8, 7, 12, 0), usuario.getFechaCreacion());
        assertEquals("N", usuario.getPerfilCompleto());
        assertEquals("Y", usuario.getPrimerLogin());

        assertTrue(usuario.isActivo());
        assertFalse(usuario.isPerfilCompleto());
        assertTrue(usuario.isPrimerLogin());
    }

    @Test
    void testConstructorConParametros() {
        Usuario usuario = new Usuario(20L, "Ana", "ana@example.com", "pass123", "N");

        assertEquals(20L, usuario.getIdUsuario());
        assertEquals("Ana", usuario.getNombre());
        assertEquals("ana@example.com", usuario.getCorreo());
        assertEquals("pass123", usuario.getPasswordHash());
        assertEquals("N", usuario.getActivo());

        // Fecha creación, perfilCompleto y primerLogin se inicializan en el constructor
        assertNotNull(usuario.getFechaCreacion());
        assertEquals("N", usuario.getPerfilCompleto());
        assertEquals("Y", usuario.getPrimerLogin());

        assertFalse(usuario.isActivo());
        assertFalse(usuario.isPerfilCompleto());
        assertTrue(usuario.isPrimerLogin());
    }

    @Test
    void testToString() {
        Usuario usuario = new Usuario(30L, "Carlos", "carlos@example.com", "hash", "Y");
        usuario.setPerfilCompleto("Y");
        usuario.setPrimerLogin("N");

        String esperado = "Usuario{" +
                "idUsuario=30, nombre='Carlos', correo='carlos@example.com', activo='Y', perfilCompleto='Y', primerLogin='N'" +
                '}';

        assertEquals(esperado, usuario.toString());
    }
}
