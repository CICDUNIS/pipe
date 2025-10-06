package com.example.pharmacy.dto;

import java.util.List;

public class RecetaValidadaDTO {

    private String codigoReceta;
    private boolean valida;
    private String paciente;      
    private String doctor;        
    private List<String> medicamentos; 
    private String observaciones;

    public RecetaValidadaDTO() { }

    public String getCodigoReceta() {
        return codigoReceta;
    }

    public void setCodigoReceta(String codigoReceta) {
        this.codigoReceta = codigoReceta;
    }

    public boolean isValida() {
        return valida;
    }

    public void setValida(boolean valida) {
        this.valida = valida;
    }

    public String getPaciente() {
        return paciente;
    }

    public void setPaciente(String paciente) {
        this.paciente = paciente;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public List<String> getMedicamentos() {
        return medicamentos;
    }

    public void setMedicamentos(List<String> medicamentos) {
        this.medicamentos = medicamentos;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    @Override
    public String toString() {
        return "RecetaValidadaDTO{" +
                "codigoReceta='" + codigoReceta + '\'' +
                ", valida=" + valida +
                ", paciente='" + paciente + '\'' +
                ", doctor='" + doctor + '\'' +
                ", medicamentos=" + medicamentos +
                ", observaciones='" + observaciones + '\'' +
                '}';
    }
}



