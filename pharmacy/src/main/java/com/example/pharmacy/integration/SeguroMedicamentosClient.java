package com.example.pharmacy.integration;

import com.example.pharmacy.dto.AutorizacionMedicamentoDTO;
import org.springframework.stereotype.Component;

@Component
public class SeguroMedicamentosClient {

    /**
     * Este simula la autorización de medicamentos en el sistema Seguro.
     *
     * @param codigoReceta     Código de la receta.
     * @param numeroAfiliacion Número de afiliación del cliente.
     * @param costoTotal       Costo total de los medicamentos.
     * @return AutorizacionMedicamentoDTO con los datos simulados de la autorización.
     */
    public AutorizacionMedicamentoDTO autorizarMedicamento(String codigoReceta, String numeroAfiliacion, Double costoTotal) {
        AutorizacionMedicamentoDTO dto = new AutorizacionMedicamentoDTO();
        if ("AFI-9999".equalsIgnoreCase(numeroAfiliacion)) {
            dto.setAutorizacion("AUT-12345");
            dto.setMontoAutorizado(costoTotal * 0.8);
            dto.setCopago(costoTotal * 0.2);
            dto.setEstado("aprobado");
            dto.setMensaje("Autorización exitosa para receta " + codigoReceta);
        } else {
            dto.setAutorizacion("AUT-00000");
            dto.setEstado("rechazado");
            dto.setMensaje("No existe cobertura para el número de afiliación " + numeroAfiliacion);
        }
        return dto;
    }
}
