package com.example.pharmacy.dto;

public class ExportMedicamentoDTO {
    private Long idMedicamento;
    private Integer cantidad;

    public ExportMedicamentoDTO() {
    }

    public ExportMedicamentoDTO(Long idMedicamento, Integer cantidad) {
        this.idMedicamento = idMedicamento;
        this.cantidad = cantidad;
    }

    public Long getIdMedicamento() {
        return idMedicamento;
    }

    public void setIdMedicamento(Long idMedicamento) {
        this.idMedicamento = idMedicamento;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    @Override
    public String toString() {
        return "ExportMedicamentoDTO{" +
                "idMedicamento=" + idMedicamento +
                ", cantidad=" + cantidad +
                '}';
    }
}
