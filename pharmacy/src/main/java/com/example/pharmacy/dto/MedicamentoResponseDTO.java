// MedicamentoResponseDTO.java
package com.example.pharmacy.dto;

import com.example.pharmacy.model.Medicamento;
import lombok.Data;

@Data
public class MedicamentoResponseDTO {
    private Medicamento medicamento;
    private Double precio;
    private String codigoSeguro;
}
