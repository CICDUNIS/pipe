package com.example.pharmacy.dto;

public class AutorizacionMedicamentoDTO {

    private String autorizacion;
    private Double montoAutorizado;
    private Double copago;
    private String estado;
    private String mensaje;

    public AutorizacionMedicamentoDTO() {
    }

    public String getAutorizacion() {
        return autorizacion;
    }

    public void setAutorizacion(String autorizacion) {
        this.autorizacion = autorizacion;
    }

    public Double getMontoAutorizado() {
        return montoAutorizado;
    }

    public void setMontoAutorizado(Double montoAutorizado) {
        this.montoAutorizado = montoAutorizado;
    }

    public Double getCopago() {
        return copago;
    }

    public void setCopago(Double copago) {
        this.copago = copago;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    @Override
    public String toString() {
        return "AutorizacionMedicamentoDTO{" +
                "autorizacion='" + autorizacion + '\'' +
                ", montoAutorizado=" + montoAutorizado +
                ", copago=" + copago +
                ", estado='" + estado + '\'' +
                ", mensaje='" + mensaje + '\'' +
                '}';
    }
}
