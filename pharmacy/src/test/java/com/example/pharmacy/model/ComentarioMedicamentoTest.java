package com.example.pharmacy.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ComentarioMedicamentoTest {

    @Test
    void testGettersAndSetters() {
        ComentarioMedicamento comentario = new ComentarioMedicamento();

        Long idComentario = 1L;
        Long idMedicamento = 2L;
        Long idUsuario = 3L;
        String texto = "Buen medicamento";
        LocalDateTime fecha = LocalDateTime.now();
        Long parentId = 4L;
        String nombreUsuario = "Sofi";

        List<ComentarioMedicamento> respuestas = new ArrayList<>();
        ComentarioMedicamento respuesta1 = new ComentarioMedicamento();
        respuesta1.setTexto("Gracias por tu comentario");
        respuestas.add(respuesta1);

        comentario.setIdComentario(idComentario);
        comentario.setIdMedicamento(idMedicamento);
        comentario.setIdUsuario(idUsuario);
        comentario.setTexto(texto);
        comentario.setFecha(fecha);
        comentario.setParentId(parentId);
        comentario.setNombreUsuario(nombreUsuario);
        comentario.setRespuestas(respuestas);

        assertEquals(idComentario, comentario.getIdComentario());
        assertEquals(idMedicamento, comentario.getIdMedicamento());
        assertEquals(idUsuario, comentario.getIdUsuario());
        assertEquals(texto, comentario.getTexto());
        assertEquals(fecha, comentario.getFecha());
        assertEquals(parentId, comentario.getParentId());
        assertEquals(nombreUsuario, comentario.getNombreUsuario());
        assertEquals(respuestas, comentario.getRespuestas());
    }

    @Test
    void testValoresPorDefecto() {
        ComentarioMedicamento comentario = new ComentarioMedicamento();

        assertNull(comentario.getIdComentario());
        assertNull(comentario.getIdMedicamento());
        assertNull(comentario.getIdUsuario());
        assertNull(comentario.getTexto());
        assertNull(comentario.getFecha());
        assertNull(comentario.getParentId());
        assertNull(comentario.getNombreUsuario());

        assertNotNull(comentario.getRespuestas());
        assertTrue(comentario.getRespuestas().isEmpty());
    }
}
