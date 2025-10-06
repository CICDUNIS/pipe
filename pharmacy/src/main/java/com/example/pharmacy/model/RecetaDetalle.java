package com.example.pharmacy.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("RECETADETALLE")
public class RecetaDetalle {

    @Id
    @Column("ID_DETALLE")
    private Long idDetalle;

    @Column("ID_RECETA")
    private Long idReceta;

    @Column("ID_MEDICAMENTO")
    private Long idMedicamento;

    @Column("DOSIS")
    private String dosis;

    @Column("FRECUENCIA")
    private String frecuencia;

    @Column("DURACION")
    private String duracion;

    @Column("CANTIDAD_REQUERIDA")
    private Integer cantidadRequerida;

    @Column("OBSERVACIONES")
    private String observaciones;

    public Long getIdDetalle() {
        return idDetalle;
    }
    public void setIdDetalle(Long idDetalle) {
        this.idDetalle = idDetalle;
    }

    public Long getIdReceta() {
        return idReceta;
    }
    public void setIdReceta(Long idReceta) {
        this.idReceta = idReceta;
    }

    public Long getIdMedicamento() {
        return idMedicamento;
    }
    public void setIdMedicamento(Long idMedicamento) {
        this.idMedicamento = idMedicamento;
    }

    public String getDosis() {
        return dosis;
    }
    public void setDosis(String dosis) {
        this.dosis = dosis;
    }

    public String getFrecuencia() {
        return frecuencia;
    }
    public void setFrecuencia(String frecuencia) {
        this.frecuencia = frecuencia;
    }

    public String getDuracion() {
        return duracion;
    }
    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    public Integer getCantidadRequerida() {
        return cantidadRequerida;
    }
    public void setCantidadRequerida(Integer cantidadRequerida) {
        this.cantidadRequerida = cantidadRequerida;
    }

    public String getObservaciones() {
        return observaciones;
    }
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
