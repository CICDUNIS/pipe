package com.example.pharmacy.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RecetaDetalleTest {

    @Test
    void testGettersAndSetters() {
        RecetaDetalle detalle = new RecetaDetalle();

        Long idDetalle = 1L;
        Long idReceta = 10L;
        Long idMedicamento = 20L;
        String dosis = "1 tableta";
        String frecuencia = "Cada 8 horas";
        String duracion = "5 días";
        Integer cantidad = 15;
        String observaciones = "Tomar con agua";

        detalle.setIdDetalle(idDetalle);
        detalle.setIdReceta(idReceta);
        detalle.setIdMedicamento(idMedicamento);
        detalle.setDosis(dosis);
        detalle.setFrecuencia(frecuencia);
        detalle.setDuracion(duracion);
        detalle.setCantidadRequerida(cantidad);
        detalle.setObservaciones(observaciones);

        assertEquals(idDetalle, detalle.getIdDetalle());
        assertEquals(idReceta, detalle.getIdReceta());
        assertEquals(idMedicamento, detalle.getIdMedicamento());
        assertEquals(dosis, detalle.getDosis());
        assertEquals(frecuencia, detalle.getFrecuencia());
        assertEquals(duracion, detalle.getDuracion());
        assertEquals(cantidad, detalle.getCantidadRequerida());
        assertEquals(observaciones, detalle.getObservaciones());
    }
}
