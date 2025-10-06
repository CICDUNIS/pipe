package com.example.pharmacy.service.impl;

import com.example.pharmacy.model.ComentarioMedicamento;
import com.example.pharmacy.model.Usuario;
import com.example.pharmacy.repository.ComentarioMedicamentoRepository;
import com.example.pharmacy.repository.UsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ComentarioMedicamentoServiceImplTest {

    @Mock
    private ComentarioMedicamentoRepository comentarioRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private ComentarioMedicamentoServiceImpl service;

    @Test
    @DisplayName("create: cuando fecha es null, asigna fecha y guarda")
    void create_fechaNull_asignaFechaYGuarda() {
        ComentarioMedicamento in = new ComentarioMedicamento();
        in.setTexto("Texto prueba");
        in.setFecha(null);

        ComentarioMedicamento saved = new ComentarioMedicamento();
        saved.setIdComentario(1L);
        when(comentarioRepository.save(any(ComentarioMedicamento.class))).thenReturn(saved);

        ComentarioMedicamento result = service.create(in);

        ArgumentCaptor<ComentarioMedicamento> captor = ArgumentCaptor.forClass(ComentarioMedicamento.class);
        verify(comentarioRepository).save(captor.capture());
        assertNotNull(captor.getValue().getFecha());
        assertTrue(captor.getValue().getFecha().isBefore(LocalDateTime.now().plusSeconds(1)));
        assertEquals(saved, result);
    }

    @Test
    @DisplayName("create: cuando fecha existe, no la sobreescribe")
    void create_fechaExistente_noSobreescribe() {
        ComentarioMedicamento in = new ComentarioMedicamento();
        LocalDateTime fecha = LocalDateTime.of(2020,1,1,0,0);
        in.setFecha(fecha);
        when(comentarioRepository.save(any(ComentarioMedicamento.class))).thenReturn(in);

        ComentarioMedicamento result = service.create(in);
        ArgumentCaptor<ComentarioMedicamento> captor = ArgumentCaptor.forClass(ComentarioMedicamento.class);
        verify(comentarioRepository).save(captor.capture());
        assertEquals(fecha, captor.getValue().getFecha());
        assertEquals(in, result);
    }

    @Test
    @DisplayName("findById: existoso y no existente")
    void findById_behavior() {
        ComentarioMedicamento c = new ComentarioMedicamento();
        c.setIdComentario(5L);
        when(comentarioRepository.findById(5L)).thenReturn(Optional.of(c));
        ComentarioMedicamento res = service.findById(5L);
        assertEquals(c, res);

        when(comentarioRepository.findById(99L)).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.findById(99L));
        assertTrue(ex.getMessage().contains("Comment not found"));
    }

    @Test
    @DisplayName("findByMedicamento: asigna nombreUsuario correctamente")
    void findByMedicamento_asignaNombre() {
        ComentarioMedicamento c1 = new ComentarioMedicamento();
        c1.setIdComentario(1L);
        c1.setIdMedicamento(10L);
        c1.setIdUsuario(100L);
        c1.setTexto("texto1");
        ComentarioMedicamento c2 = new ComentarioMedicamento();
        c2.setIdComentario(2L);
        c2.setIdMedicamento(10L);
        c2.setIdUsuario(null);
        c2.setTexto("texto2");

        when(comentarioRepository.findByIdMedicamento(10L)).thenReturn(Arrays.asList(c1, c2));
        Usuario u = new Usuario(); u.setIdUsuario(100L); u.setNombre("UserX");
        when(usuarioRepository.findById(100L)).thenReturn(Optional.of(u));

        List<ComentarioMedicamento> list = service.findByMedicamento(10L);
        assertEquals(2, list.size());
        assertEquals("UserX", list.get(0).getNombreUsuario());
        assertNull(list.get(1).getNombreUsuario());
    }

    @Test
    @DisplayName("findByMedicamentoHierarchical: construye jerarquía correctamente sin NPE")
    void findByMedicamentoHierarchical_structure() {
        ComentarioMedicamento root = new ComentarioMedicamento();
        root.setIdComentario(1L); root.setParentId(null); root.setTexto("rootText");
        ComentarioMedicamento child = new ComentarioMedicamento();
        child.setIdComentario(2L); child.setParentId(1L); child.setTexto("childText");
        ComentarioMedicamento root2 = new ComentarioMedicamento();
        root2.setIdComentario(3L); root2.setParentId(null); root2.setTexto("root2Text");

        when(comentarioRepository.findByIdMedicamento(20L))
            .thenReturn(Arrays.asList(root, child, root2));

        List<ComentarioMedicamento> hier = service.findByMedicamentoHierarchical(20L);
        assertEquals(2, hier.size());
        // root debe tener child
        assertEquals(1, hier.get(0).getRespuestas().size());
        assertEquals(child, hier.get(0).getRespuestas().get(0));
        // root2 sin hijos
        assertTrue(hier.get(1).getRespuestas().isEmpty());
    }

    @Test
    @DisplayName("delete: elimina en orden post-order")
    void delete_eliminaOrdenado() {
        ComentarioMedicamento base = new ComentarioMedicamento();
        base.setIdComentario(1L); base.setIdMedicamento(30L); base.setTexto("base");
        ComentarioMedicamento c2 = new ComentarioMedicamento();
        c2.setIdComentario(2L); c2.setParentId(1L); c2.setTexto("c2");
        ComentarioMedicamento c3 = new ComentarioMedicamento();
        c3.setIdComentario(3L); c3.setParentId(2L); c3.setTexto("c3");

        when(comentarioRepository.findById(1L)).thenReturn(Optional.of(base));
        when(comentarioRepository.findByIdMedicamento(30L))
            .thenReturn(Arrays.asList(base, c2, c3));

        service.delete(1L);

        InOrder order = inOrder(comentarioRepository);
        order.verify(comentarioRepository).deleteById(3L);
        order.verify(comentarioRepository).deleteById(2L);
        order.verify(comentarioRepository).deleteById(1L);
    }
}
