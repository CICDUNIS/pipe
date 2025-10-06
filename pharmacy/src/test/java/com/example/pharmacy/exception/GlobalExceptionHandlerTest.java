package com.example.pharmacy.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleException_deberiaRetornarResponseEntityConErrorInterno() {
        String mensajeError = "Error de prueba";
        Exception exception = new Exception(mensajeError);

        ResponseEntity<Map<String, Object>> response = handler.handleException(exception);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

        Map<String, Object> body = response.getBody();
        assertNotNull(body);

        assertEquals(500, body.get("status"));
        assertEquals("Internal Server Error", body.get("error"));
        assertEquals(mensajeError, body.get("message"));

        // El trace debe contener el primer elemento del stacktrace como String
        assertTrue(body.get("trace") instanceof String);
        String traceString = (String) body.get("trace");
        assertTrue(traceString.contains("GlobalExceptionHandlerTest")); // debería apuntar a la clase del test
    }
}
