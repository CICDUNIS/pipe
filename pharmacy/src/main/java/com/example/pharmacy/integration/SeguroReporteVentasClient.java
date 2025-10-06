package com.example.pharmacy.integration;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class SeguroReporteVentasClient {

    /**
     * Simula el envío del reporte de ventas al sistema de seguros.
     *
     * @param payload Mapa que contiene los datos del reporte, por ejemplo:
     *                {
     *                   "codigoReceta": "REC-009",
     *                   "numeroAfiliacion": "AFI-1111",
     *                   "detalleMedicamentos": [
     *                       { "codigoMedicamento": "MED-001", "cantidad": 2, "subtotal": 40.0 },
     *                       ...
     *                   ],
     *                   "montoTotal": 100.0
     *                }
     * @return Un mapa con la respuesta simulada, por ejemplo:
     *         { "recibido": true, "mensaje": "Reporte registrado en Seguro SIM" }
     */
    public Map<String, Object> reporteVentas(Map<String, Object> payload) {
        Map<String, Object> response = new HashMap<>();
        response.put("recibido", true);
        response.put("mensaje", "Reporte registrado en Seguro SIM");
        return response;
    }
}
