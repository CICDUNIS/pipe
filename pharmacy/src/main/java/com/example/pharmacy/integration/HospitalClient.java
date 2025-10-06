package com.example.pharmacy.integration;

import com.example.pharmacy.dto.RecetaValidadaDTO;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class HospitalClient {

    public RecetaValidadaDTO validarReceta(String codigoReceta) {
	    System.out.println(codigoReceta);
        if ("00256-23423-1000347".equalsIgnoreCase(codigoReceta)) {
            RecetaValidadaDTO dto = new RecetaValidadaDTO();
            dto.setCodigoReceta(codigoReceta);
            dto.setValida(true);
            dto.setPaciente("Juan Pérez");
            dto.setDoctor("Dra. García");
            dto.setMedicamentos(Arrays.asList("1", "2"));
            dto.setObservaciones("Tomar con comida, control en 1 semana.");
            return dto;
        } else {
            RecetaValidadaDTO dto = new RecetaValidadaDTO();
            dto.setCodigoReceta(codigoReceta);
            dto.setValida(false);
            dto.setObservaciones("Receta no encontrada en Hospital SIM.");
            return dto;
        }
    }
}

