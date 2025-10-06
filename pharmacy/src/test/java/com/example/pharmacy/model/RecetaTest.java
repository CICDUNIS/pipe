package com.example.pharmacy.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class RecetaTest {

    @Test
    void testGettersAndSetters() {
        Receta receta = new Receta();
        Long id = 1L;
        String codigo = "RX12345";
        LocalDateTime fecha = LocalDateTime.now();
        Long idUsuario = 100L;
        String aprobado = "SÍ";
        String pdfUrl = "http://example.com/receta.pdf";

        receta.setIdReceta(id);
        receta.setCodigoReceta(codigo);
        receta.setFecha(fecha);
        receta.setIdUsuario(idUsuario);
        receta.setAprobadoSeguro(aprobado);
        receta.setPdfUrl(pdfUrl);

        assertEquals(id, receta.getIdReceta());
        assertEquals(codigo, receta.getCodigoReceta());
        assertEquals(fecha, receta.getFecha());
        assertEquals(idUsuario, receta.getIdUsuario());
        assertEquals(aprobado, receta.getAprobadoSeguro());
        assertEquals(pdfUrl, receta.getPdfUrl());
    }
}
