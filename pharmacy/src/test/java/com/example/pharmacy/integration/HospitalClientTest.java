package com.example.pharmacy.integration;

import com.example.pharmacy.dto.RecetaValidadaDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HospitalClientTest {

    private final HospitalClient hospitalClient = new HospitalClient();

    @Test
    void validarReceta_deberiaRetornarValidaCuandoCodigoEsCorrecto() {
        String codigoValido = "00256-23423-1000347";

        RecetaValidadaDTO resultado = hospitalClient.validarReceta(codigoValido);

        assertNotNull(resultado);
        assertEquals(codigoValido, resultado.getCodigoReceta());
        assertTrue(resultado.isValida());
        assertEquals("Juan Pérez", resultado.getPaciente());
        assertEquals("Dra. García", resultado.getDoctor());
        assertNotNull(resultado.getMedicamentos());
        assertEquals(2, resultado.getMedicamentos().size());
        assertEquals("Tomar con comida, control en 1 semana.", resultado.getObservaciones());
    }

    @Test
    void validarReceta_deberiaRetornarNoValidaCuandoCodigoEsIncorrecto() {
        String codigoInvalido = "codigo-invalido";

        RecetaValidadaDTO resultado = hospitalClient.validarReceta(codigoInvalido);

        assertNotNull(resultado);
        assertEquals(codigoInvalido, resultado.getCodigoReceta());
        assertFalse(resultado.isValida());
        assertNull(resultado.getPaciente());
        assertNull(resultado.getDoctor());
        assertNull(resultado.getMedicamentos());
        assertEquals("Receta no encontrada en Hospital SIM.", resultado.getObservaciones());
    }
}
