// PrecioMedicamentoCompania.java
package com.example.pharmacy.dto;

import lombok.Data;

@Data
public class PrecioMedicamentoCompania {
    private String codigoMedicamento;
    private String codigoFarmacia;
    private String nombreMedicamento;
    private Double precio;
    private String codigoSeguro;
}
