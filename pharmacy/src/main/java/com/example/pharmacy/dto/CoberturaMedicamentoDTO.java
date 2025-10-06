package com.example.pharmacy.dto;

public class CoberturaMedicamentoDTO {

    private String numeroAfiliacion;
    private String codigoMedicamento;
    private String nombre;
    private boolean cubierto;
    private double montoAutorizado;
    private double copago;
    private String mensaje;

    public CoberturaMedicamentoDTO() {
    }

    public String getNumeroAfiliacion() {
        return numeroAfiliacion;
    }

    public void setNumeroAfiliacion(String numeroAfiliacion) {
        this.numeroAfiliacion = numeroAfiliacion;
    }

    public String getCodigoMedicamento() {
        return codigoMedicamento;
    }

    public void setCodigoMedicamento(String codigoMedicamento) {
        this.codigoMedicamento = codigoMedicamento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isCubierto() {
        return cubierto;
    }

    public void setCubierto(boolean cubierto) {
        this.cubierto = cubierto;
    }

    public double getMontoAutorizado() {
        return montoAutorizado;
    }

    public void setMontoAutorizado(double montoAutorizado) {
        this.montoAutorizado = montoAutorizado;
    }

    public double getCopago() {
        return copago;
    }

    public void setCopago(double copago) {
        this.copago = copago;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    @Override
    public String toString() {
        return "CoberturaMedicamentoDTO{" +
                "numeroAfiliacion='" + numeroAfiliacion + '\'' +
                ", codigoMedicamento='" + codigoMedicamento + '\'' +
                ", nombre='" + nombre + '\'' +
                ", cubierto=" + cubierto +
                ", montoAutorizado=" + montoAutorizado +
                ", copago=" + copago +
                ", mensaje='" + mensaje + '\'' +
                '}';
    }
}



