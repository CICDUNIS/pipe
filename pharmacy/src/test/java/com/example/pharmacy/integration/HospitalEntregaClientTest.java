package com.example.pharmacy.integration;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class HospitalEntregaClientTest {

    private final HospitalEntregaClient hospitalEntregaClient = new HospitalEntregaClient();

    @Test
    void confirmarEntrega_deberiaRetornarRespuestaCorrecta() {
        String codigoReceta = "12345-67890";
        LocalDateTime fechaEntrega = LocalDateTime.now();
        String paciente = "Juan Pérez";
        String entregadoPor = "Empleado X";

        Map<String, Object> resultado = hospitalEntregaClient.confirmarEntrega(codigoReceta, fechaEntrega, paciente, entregadoPor);

        assertNotNull(resultado);
        assertTrue(resultado.containsKey("recibido"));
        assertTrue(resultado.containsKey("mensaje"));
        assertEquals(true, resultado.get("recibido"));
        assertEquals("Entrega confirmada en Hospital SIM para receta " + codigoReceta, resultado.get("mensaje"));
    }
}
