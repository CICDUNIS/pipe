package com.example.pharmacy.controllers;

import com.example.pharmacy.dto.RecetaValidadaDTO;
import com.example.pharmacy.integration.HospitalEntregaClient;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/farmacia/hospital-sim")
public class HospitalIntegrationController {

    private final HospitalEntregaClient entregaClient;

    public HospitalIntegrationController(HospitalEntregaClient entregaClient) {
        this.entregaClient = entregaClient;
    }

   // @GetMapping("/validar-receta")
   // public RecetaValidadaDTO validarReceta(@RequestParam String codigoReceta) {
   //     return hospitalClient.validarReceta(codigoReceta);
   // }

    @PostMapping("/confirmar-entrega")
    public Map<String, Object> confirmarEntrega(@RequestBody Map<String, String> body) {
        // Esperamos:
        // { "codigoReceta": "REC-999", "paciente": "Juan", "entregadoPor": "Farmacia X", "fechaEntrega": "2025-12-01T14:30:00" }
        String codigoReceta = body.get("codigoReceta");
        String paciente = body.get("paciente");
        String entregadoPor = body.get("entregadoPor");
        LocalDateTime fechaEntrega = LocalDateTime.parse(body.get("fechaEntrega"));

        return entregaClient.confirmarEntrega(codigoReceta, fechaEntrega, paciente, entregadoPor);
    }
}


