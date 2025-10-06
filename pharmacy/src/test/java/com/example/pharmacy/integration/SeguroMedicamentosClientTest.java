package com.example.pharmacy.integration;

import com.example.pharmacy.dto.AutorizacionMedicamentoDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SeguroMedicamentosClientTest {

    private final SeguroMedicamentosClient seguroMedicamentosClient = new SeguroMedicamentosClient();

    @Test
    void autorizarMedicamento_exito() {
        String codigoReceta = "REC-123";
        String numeroAfiliacion = "AFI-9999";
        Double costoTotal = 100.0;

        AutorizacionMedicamentoDTO resultado = seguroMedicamentosClient.autorizarMedicamento(codigoReceta, numeroAfiliacion, costoTotal);

        assertNotNull(resultado);
        assertEquals("AUT-12345", resultado.getAutorizacion());
        assertEquals(costoTotal * 0.8, resultado.getMontoAutorizado());
        assertEquals(costoTotal * 0.2, resultado.getCopago());
        assertEquals("aprobado", resultado.getEstado());
        assertEquals("Autorización exitosa para receta " + codigoReceta, resultado.getMensaje());
    }

    @Test
    void autorizarMedicamento_rechazo() {
        String codigoReceta = "REC-456";
        String numeroAfiliacion = "AFI-0000";
        Double costoTotal = 200.0;

        AutorizacionMedicamentoDTO resultado = seguroMedicamentosClient.autorizarMedicamento(codigoReceta, numeroAfiliacion, costoTotal);

        assertNotNull(resultado);
        assertEquals("AUT-00000", resultado.getAutorizacion());
        // Como no se autorizó, monto autorizado y copago pueden ser null o 0 según implementación.
        // Aquí asumo que son Double (objetos), puedes ajustar según tu DTO.
        assertNull(resultado.getMontoAutorizado());
        assertNull(resultado.getCopago());
        assertEquals("rechazado", resultado.getEstado());
        assertEquals("No existe cobertura para el número de afiliación " + numeroAfiliacion, resultado.getMensaje());
    }
}
