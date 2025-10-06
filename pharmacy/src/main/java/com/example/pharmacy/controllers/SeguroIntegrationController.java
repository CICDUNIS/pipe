package com.example.pharmacy.controllers;

import com.example.pharmacy.dto.CoberturaMedicamentoDTO;
import com.example.pharmacy.dto.AutorizacionMedicamentoDTO;
import com.example.pharmacy.integration.SeguroMedicamentosClient;
import com.example.pharmacy.integration.SeguroReporteVentasClient;
import com.example.pharmacy.integration.SeguroClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/farmacia/seguro-sim")
public class SeguroIntegrationController {

    private final SeguroMedicamentosClient medicamentosClient;
    private final SeguroReporteVentasClient reporteVentasClient;
    private final SeguroClient seguroClient;

    public SeguroIntegrationController(SeguroMedicamentosClient medicamentosClient,
                                       SeguroReporteVentasClient reporteVentasClient, SeguroClient seguroClient) {
        this.medicamentosClient = medicamentosClient;
        this.reporteVentasClient = reporteVentasClient;
	this.seguroClient = seguroClient;
    }

    @PostMapping("/autorizar-medicamento")
    public AutorizacionMedicamentoDTO autorizarMedicamento(@RequestBody Map<String, Object> payload) {
        String codigoReceta = (String) payload.get("codigoReceta");
        String numeroAfiliacion = (String) payload.get("numeroAfiliacion");
        Double costoTotal = null;
        Object costoTotalObj = payload.get("costoTotal");
        if (costoTotalObj instanceof Number) {
            costoTotal = ((Number) costoTotalObj).doubleValue();
        } else if (costoTotalObj instanceof String) {
            costoTotal = Double.parseDouble((String) costoTotalObj);
        }
        return medicamentosClient.autorizarMedicamento(codigoReceta, numeroAfiliacion, costoTotal);
    }

    /**
     * Endpoint para reportar ventas al sistema de seguros.
     * Ejemplo: POST /api/farmacia/seguro-sim/reporte-ventas
     * Body:
     * {
     *   "codigoReceta": "REC-009",
     *   "numeroAfiliacion": "AFI-1111",
     *   "detalleMedicamentos": [
     *       { "codigoMedicamento": "MED-001", "cantidad": 2, "subtotal": 40.0 },
     *       ...
     *   ],
     *   "montoTotal": 100.0
     * }
     *
     * @param payload Mapa con los datos del reporte.
     * @return Un mapa con la respuesta simulada.
     */
    @PostMapping("/reporte-ventas")
    public Map<String, Object> reporteVentas(@RequestBody Map<String, Object> payload) {
        return reporteVentasClient.reporteVentas(payload);
    }

    @GetMapping("/validar-medicamento")
    public CoberturaMedicamentoDTO validarMedicamento(@RequestParam String numeroAfiliacion,
		    @RequestParam String codigoMedicamento) {
		    return seguroClient.validarMedicamento(numeroAfiliacion, codigoMedicamento);
		    }
}
