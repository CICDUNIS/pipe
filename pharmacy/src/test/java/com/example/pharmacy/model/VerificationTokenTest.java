package com.example.pharmacy.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class VerificationTokenTest {

    @Test
    void testGettersAndSetters() {
        VerificationToken token = new VerificationToken();

        Long idToken = 1L;
        String tokenStr = "abc123";
        Long idUsuario = 42L;
        LocalDateTime fechaCreacion = LocalDateTime.of(2025, 8, 7, 10, 0);
        LocalDateTime fechaExpiracion = LocalDateTime.of(2025, 8, 8, 10, 0);
        LocalDateTime fechaVerificacion = LocalDateTime.of(2025, 8, 7, 15, 0);

        token.setIdToken(idToken);
        token.setToken(tokenStr);
        token.setIdUsuario(idUsuario);
        token.setFechaCreacion(fechaCreacion);
        token.setFechaExpiracion(fechaExpiracion);
        token.setFechaVerificacion(fechaVerificacion);

        assertEquals(idToken, token.getIdToken());
        assertEquals(tokenStr, token.getToken());
        assertEquals(idUsuario, token.getIdUsuario());
        assertEquals(fechaCreacion, token.getFechaCreacion());
        assertEquals(fechaExpiracion, token.getFechaExpiracion());
        assertEquals(fechaVerificacion, token.getFechaVerificacion());
    }

    @Test
    void testIsExpired() {
        VerificationToken token = new VerificationToken();
        // fechaExpiracion en el pasado -> token expirado
        token.setFechaExpiracion(LocalDateTime.now().minusDays(1));
        assertTrue(token.isExpired());

        // fechaExpiracion en el futuro -> token no expirado
        token.setFechaExpiracion(LocalDateTime.now().plusDays(1));
        assertFalse(token.isExpired());
    }

    @Test
    void testIsVerified() {
        VerificationToken token = new VerificationToken();

        // fechaVerificacion null -> no verificado
        token.setFechaVerificacion(null);
        assertFalse(token.isVerified());

        // fechaVerificacion no null -> verificado
        token.setFechaVerificacion(LocalDateTime.now());
        assertTrue(token.isVerified());
    }

    @Test
    void testConstructorWithArgs() {
        String tokenStr = "token123";
        Long idUsuario = 99L;
        LocalDateTime fechaCreacion = LocalDateTime.of(2025, 8, 7, 9, 0);
        LocalDateTime fechaExpiracion = LocalDateTime.of(2025, 8, 10, 9, 0);

        VerificationToken token = new VerificationToken(tokenStr, idUsuario, fechaCreacion, fechaExpiracion);

        assertEquals(tokenStr, token.getToken());
        assertEquals(idUsuario, token.getIdUsuario());
        assertEquals(fechaCreacion, token.getFechaCreacion());
        assertEquals(fechaExpiracion, token.getFechaExpiracion());

        // idToken and fechaVerificacion should be null by default
        assertNull(token.getIdToken());
        assertNull(token.getFechaVerificacion());
    }
}
