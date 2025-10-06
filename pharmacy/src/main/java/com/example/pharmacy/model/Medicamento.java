package com.example.pharmacy.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("MEDICAMENTO")
public class Medicamento {

    @Id
    @Column("ID_MEDICAMENTO")
    private Long idMedicamento;

    @Column("CODIGO")
    private String codigo;

    @Column("NOMBRE")
    private String nombre;

    @Column("CATEGORIA")
    private String categoria;

    @Column("PRINCIPIO_ACTIVO")
    private String principioActivo;

    @Column("DESCRIPCION")
    private String descripcion;

    @Column("FOTO_URL")
    private String fotoUrl;

    @Column("CONCENTRACION")
    private String concentracion;

    @Column("PRESENTACION")
    private String presentacion;

    @Column("NUMERO_UNIDADES")
    private Integer numeroUnidades;

    @Column("MARCA")
    private String marca;

    @Column("REQUIERE_RECETA")
    private String requiereReceta;

    @Column("STOCK")
    private Integer stock;

    @Column("PRECIO")
    private Double precio;

    public Long getIdMedicamento() {
        return idMedicamento;
    }

    public void setIdMedicamento(Long idMedicamento) {
        this.idMedicamento = idMedicamento;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getPrincipioActivo() {
        return principioActivo;
    }

    public void setPrincipioActivo(String principioActivo) {
        this.principioActivo = principioActivo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFotoUrl() {
        return fotoUrl;
    }

    public void setFotoUrl(String fotoUrl) {
        this.fotoUrl = fotoUrl;
    }

    public String getConcentracion() {
        return concentracion;
    }

    public void setConcentracion(String concentracion) {
        this.concentracion = concentracion;
    }

    public String getPresentacion() {
        return presentacion;
    }

    public void setPresentacion(String presentacion) {
        this.presentacion = presentacion;
    }

    public Integer getNumeroUnidades() {
        return numeroUnidades;
    }

    public void setNumeroUnidades(Integer numeroUnidades) {
        this.numeroUnidades = numeroUnidades;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getRequiereReceta() {
        return requiereReceta;
    }

    public void setRequiereReceta(String requiereReceta) {
        this.requiereReceta = requiereReceta;
    }

    public boolean isRequiereReceta() {
        return "Y".equalsIgnoreCase(requiereReceta);
    }

    public void setRequiereRecetaBoolean(boolean requiereReceta) {
        this.requiereReceta = requiereReceta ? "Y" : "N";
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }
}
