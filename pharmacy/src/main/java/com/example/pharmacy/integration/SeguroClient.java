package com.example.pharmacy.integration;

import com.example.pharmacy.dto.CoberturaMedicamentoDTO;
import org.springframework.stereotype.Component;

@Component
public class SeguroClient {

    public CoberturaMedicamentoDTO validarMedicamento(String numeroAfiliacion, String codigoMedicamento) {
        if ("AFI-9999".equalsIgnoreCase(numeroAfiliacion) && "1".equalsIgnoreCase(codigoMedicamento)) {
            CoberturaMedicamentoDTO dto = new CoberturaMedicamentoDTO();
            dto.setNumeroAfiliacion(numeroAfiliacion);
            dto.setCodigoMedicamento(codigoMedicamento);
            dto.setCubierto(true);
            dto.setMontoAutorizado(100.0);
            dto.setCopago(20.0);
            dto.setMensaje("Cobertura 80% para este medicamento.");
            return dto;
        } else {
            CoberturaMedicamentoDTO dto = new CoberturaMedicamentoDTO();
            dto.setNumeroAfiliacion(numeroAfiliacion);
            dto.setCodigoMedicamento(codigoMedicamento);
            dto.setCubierto(false);
            dto.setMensaje("No se encontró cobertura.");
            return dto;
        }
    }
}




