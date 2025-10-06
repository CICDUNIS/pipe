package com.example.pharmacy.util;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.*;

class UserDetailsTest {

    private final UserDetails userDetails = new UserDetails();

    @AfterEach
    void cleanUp() {
        // Limpia el contexto después de cada test
        SecurityContextHolder.clearContext();
    }

    @Test
    void getUsuarioActual_deberiaRetornarNombreUsuarioCuandoAutenticado() {
        // Simula autenticación
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken("sofiUsuario", null, null);
        SecurityContextHolder.getContext().setAuthentication(auth);

        String usuario = userDetails.getUsuarioActual();
        assertEquals("sofiUsuario", usuario);
    }

    @Test
    void getUsuarioActual_deberiaRetornarAnonymousCuandoNoHayAutenticacion() {
        // No se setea nada en el contexto (queda null)
        String usuario = userDetails.getUsuarioActual();
        assertEquals("anonymous", usuario);
    }
}
