package com.example.pharmacy.integration;

import com.example.pharmacy.dto.CoberturaMedicamentoDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SeguroClientTest {

    private final SeguroClient seguroClient = new SeguroClient();

    @Test
    void validarMedicamento_coberturaValida() {
        String numeroAfiliacion = "AFI-9999";
        String codigoMedicamento = "1";

        CoberturaMedicamentoDTO resultado = seguroClient.validarMedicamento(numeroAfiliacion, codigoMedicamento);

        assertNotNull(resultado);
        assertEquals(numeroAfiliacion, resultado.getNumeroAfiliacion());
        assertEquals(codigoMedicamento, resultado.getCodigoMedicamento());
        assertTrue(resultado.isCubierto());
        assertEquals(100.0, resultado.getMontoAutorizado());
        assertEquals(20.0, resultado.getCopago());
        assertEquals("Cobertura 80% para este medicamento.", resultado.getMensaje());
    }

    @Test
    void validarMedicamento_coberturaInvalida() {
        String numeroAfiliacion = "AFI-0000";
        String codigoMedicamento = "999";

        CoberturaMedicamentoDTO resultado = seguroClient.validarMedicamento(numeroAfiliacion, codigoMedicamento);

        assertNotNull(resultado);
        assertEquals(numeroAfiliacion, resultado.getNumeroAfiliacion());
        assertEquals(codigoMedicamento, resultado.getCodigoMedicamento());
        assertFalse(resultado.isCubierto());
        assertEquals(0.0, resultado.getMontoAutorizado());
        assertEquals(0.0, resultado.getCopago());
        assertEquals("No se encontró cobertura.", resultado.getMensaje());
    }

    @Test
    void validarMedicamento_coberturaValidaCaseInsensitive() {
        String numeroAfiliacion = "afi-9999";
        String codigoMedicamento = "1";

        CoberturaMedicamentoDTO resultado = seguroClient.validarMedicamento(numeroAfiliacion, codigoMedicamento);

        assertNotNull(resultado);
        assertEquals(numeroAfiliacion, resultado.getNumeroAfiliacion());
        assertEquals(codigoMedicamento, resultado.getCodigoMedicamento());
        assertTrue(resultado.isCubierto());
        assertEquals(100.0, resultado.getMontoAutorizado());
        assertEquals(20.0, resultado.getCopago());
        assertEquals("Cobertura 80% para este medicamento.", resultado.getMensaje());
    }

    @Test
    void validarMedicamento_coberturaValidaCaseInsensitiveCodigo() {
        String numeroAfiliacion = "AFI-9999";
        String codigoMedicamento = "1";

        CoberturaMedicamentoDTO resultado = seguroClient.validarMedicamento(numeroAfiliacion, codigoMedicamento);

        assertNotNull(resultado);
        assertEquals(numeroAfiliacion, resultado.getNumeroAfiliacion());
        assertEquals(codigoMedicamento, resultado.getCodigoMedicamento());
        assertTrue(resultado.isCubierto());
        assertEquals(100.0, resultado.getMontoAutorizado());
        assertEquals(20.0, resultado.getCopago());
        assertEquals("Cobertura 80% para este medicamento.", resultado.getMensaje());
    }

    @Test
    void validarMedicamento_conNumeroAfiliacionNulo() {
        String numeroAfiliacion = null;
        String codigoMedicamento = "1";

        CoberturaMedicamentoDTO resultado = seguroClient.validarMedicamento(numeroAfiliacion, codigoMedicamento);

        assertNotNull(resultado);
        assertEquals(numeroAfiliacion, resultado.getNumeroAfiliacion());
        assertEquals(codigoMedicamento, resultado.getCodigoMedicamento());
        assertFalse(resultado.isCubierto());
        assertEquals(0.0, resultado.getMontoAutorizado());
        assertEquals(0.0, resultado.getCopago());
        assertEquals("No se encontró cobertura.", resultado.getMensaje());
    }

    @Test
    void validarMedicamento_conCodigoMedicamentoNulo() {
        String numeroAfiliacion = "AFI-9999";
        String codigoMedicamento = null;

        CoberturaMedicamentoDTO resultado = seguroClient.validarMedicamento(numeroAfiliacion, codigoMedicamento);

        assertNotNull(resultado);
        assertEquals(numeroAfiliacion, resultado.getNumeroAfiliacion());
        assertEquals(codigoMedicamento, resultado.getCodigoMedicamento());
        assertFalse(resultado.isCubierto());
        assertEquals(0.0, resultado.getMontoAutorizado());
        assertEquals(0.0, resultado.getCopago());
        assertEquals("No se encontró cobertura.", resultado.getMensaje());
    }

    @Test
    void validarMedicamento_conAmbosParametrosNulos() {
        String numeroAfiliacion = null;
        String codigoMedicamento = null;

        CoberturaMedicamentoDTO resultado = seguroClient.validarMedicamento(numeroAfiliacion, codigoMedicamento);

        assertNotNull(resultado);
        assertEquals(numeroAfiliacion, resultado.getNumeroAfiliacion());
        assertEquals(codigoMedicamento, resultado.getCodigoMedicamento());
        assertFalse(resultado.isCubierto());
        assertEquals(0.0, resultado.getMontoAutorizado());
        assertEquals(0.0, resultado.getCopago());
        assertEquals("No se encontró cobertura.", resultado.getMensaje());
    }

    @Test
    void validarMedicamento_conNumeroAfiliacionVacio() {
        String numeroAfiliacion = "";
        String codigoMedicamento = "1";

        CoberturaMedicamentoDTO resultado = seguroClient.validarMedicamento(numeroAfiliacion, codigoMedicamento);

        assertNotNull(resultado);
        assertEquals(numeroAfiliacion, resultado.getNumeroAfiliacion());
        assertEquals(codigoMedicamento, resultado.getCodigoMedicamento());
        assertFalse(resultado.isCubierto());
        assertEquals(0.0, resultado.getMontoAutorizado());
        assertEquals(0.0, resultado.getCopago());
        assertEquals("No se encontró cobertura.", resultado.getMensaje());
    }

    @Test
    void validarMedicamento_conCodigoMedicamentoVacio() {
        String numeroAfiliacion = "AFI-9999";
        String codigoMedicamento = "";

        CoberturaMedicamentoDTO resultado = seguroClient.validarMedicamento(numeroAfiliacion, codigoMedicamento);

        assertNotNull(resultado);
        assertEquals(numeroAfiliacion, resultado.getNumeroAfiliacion());
        assertEquals(codigoMedicamento, resultado.getCodigoMedicamento());
        assertFalse(resultado.isCubierto());
        assertEquals(0.0, resultado.getMontoAutorizado());
        assertEquals(0.0, resultado.getCopago());
        assertEquals("No se encontró cobertura.", resultado.getMensaje());
    }
}
