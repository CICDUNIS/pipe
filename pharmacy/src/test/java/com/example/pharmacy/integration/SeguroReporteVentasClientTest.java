package com.example.pharmacy.integration;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SeguroReporteVentasClientTest {

    private final SeguroReporteVentasClient client = new SeguroReporteVentasClient();

    @Test
    void reporteVentas_respuestaCorrecta() {
        // Preparar payload simulado
        Map<String, Object> detalleMedicamento1 = new HashMap<>();
        detalleMedicamento1.put("codigoMedicamento", "MED-001");
        detalleMedicamento1.put("cantidad", 2);
        detalleMedicamento1.put("subtotal", 40.0);

        Map<String, Object> detalleMedicamento2 = new HashMap<>();
        detalleMedicamento2.put("codigoMedicamento", "MED-002");
        detalleMedicamento2.put("cantidad", 1);
        detalleMedicamento2.put("subtotal", 60.0);

        Map<String, Object> payload = new HashMap<>();
        payload.put("codigoReceta", "REC-009");
        payload.put("numeroAfiliacion", "AFI-1111");
        payload.put("detalleMedicamentos", new Map[]{detalleMedicamento1, detalleMedicamento2});
        payload.put("montoTotal", 100.0);

        // Ejecutar método a testear
        Map<String, Object> response = client.reporteVentas(payload);

        // Verificar resultado
        assertNotNull(response);
        assertTrue(response.containsKey("recibido"));
        assertTrue(response.containsKey("mensaje"));

        assertEquals(true, response.get("recibido"));
        assertEquals("Reporte registrado en Seguro SIM", response.get("mensaje"));
    }
}
