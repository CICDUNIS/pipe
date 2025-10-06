package com.example.pharmacy.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilsTest {

    private JwtUtils jwtUtils;
    private String token;
    private final String correo = "usuario@correo.com";
    private final String rol = "ADMIN";
    private final List<String> roles = Arrays.asList("ADMIN", "USER");

    @BeforeEach
    void setUp() {
        jwtUtils = new JwtUtils();
        token = jwtUtils.generateToken(correo, rol, roles);
    }

    @Test
    void generateToken_deberiaGenerarTokenNoVacio() {
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void validateToken_deberiaRetornarTrueParaTokenValido() {
        assertTrue(jwtUtils.validateToken(token));
    }

    @Test
    void getCorreoFromToken_deberiaExtraerCorreoCorrecto() {
        String extraido = jwtUtils.getCorreoFromToken(token);
        assertEquals(correo, extraido);
    }

    @Test
    void getRolFromToken_deberiaExtraerRolCorrecto() {
        String extraido = jwtUtils.getRolFromToken(token);
        assertEquals(rol, extraido);
    }

    @Test
    void getRolesFromToken_deberiaExtraerRolesCorrectos() {
        List<String> extraidos = jwtUtils.getRolesFromToken(token);
        assertEquals(roles, extraidos);
    }

    @Test
    void validateToken_deberiaRetornarFalseParaTokenMalformado() {
        String tokenInvalido = token + "error";
        assertFalse(jwtUtils.validateToken(tokenInvalido));
    }

    @Test
    void getCorreoFromToken_conTokenInvalido_deberiaLanzarException() {
        String tokenInvalido = token + "error";
        assertThrows(Exception.class, () -> jwtUtils.getCorreoFromToken(tokenInvalido));
    }
}
