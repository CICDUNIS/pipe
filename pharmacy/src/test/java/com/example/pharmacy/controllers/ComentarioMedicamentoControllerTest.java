package com.example.pharmacy.controllers;

import com.example.pharmacy.model.ComentarioMedicamento;
import com.example.pharmacy.model.Usuario;
import com.example.pharmacy.service.ComentarioMedicamentoService;
import com.example.pharmacy.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ComentarioMedicamentoControllerTest {

    @Mock
    private ComentarioMedicamentoService comentarioService;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private ComentarioMedicamentoController comentarioMedicamentoController;

    private Usuario testUsuario;
    private ComentarioMedicamento testComentario;
    private List<ComentarioMedicamento> testComentarios;

    @BeforeEach
    void setUp() {
        testUsuario = new Usuario();
        testUsuario.setIdUsuario(1L);
        testUsuario.setCorreo("test@example.com");
        testUsuario.setNombre("Test User");

        testComentario = new ComentarioMedicamento();
        testComentario.setIdComentario(1L);
        testComentario.setIdMedicamento(1L);
        testComentario.setIdUsuario(1L);
        testComentario.setTexto("Excelente medicamento");
        testComentario.setFecha(LocalDateTime.now());

        testComentarios = Arrays.asList(testComentario);

        // Mock SecurityContext
        lenient().when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testGetByMedicamento_Success() {
        // Arrange
        Long idMedicamento = 1L;
        when(comentarioService.findByMedicamentoHierarchical(idMedicamento)).thenReturn(testComentarios);

        // Act
        ResponseEntity<List<ComentarioMedicamento>> response = comentarioMedicamentoController.getByMedicamento(idMedicamento);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testComentarios, response.getBody());
        assertEquals(1, response.getBody().size());
        verify(comentarioService).findByMedicamentoHierarchical(idMedicamento);
    }

    @Test
    void testGetByMedicamento_EmptyList() {
        // Arrange
        Long idMedicamento = 2L;
        when(comentarioService.findByMedicamentoHierarchical(idMedicamento)).thenReturn(Arrays.asList());

        // Act
        ResponseEntity<List<ComentarioMedicamento>> response = comentarioMedicamentoController.getByMedicamento(idMedicamento);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
        verify(comentarioService).findByMedicamentoHierarchical(idMedicamento);
    }

    @Test
    void testCreate_Success() {
        // Arrange
        when(authentication.getName()).thenReturn("test@example.com");
        when(usuarioService.findByCorreo("test@example.com")).thenReturn(testUsuario);
        when(comentarioService.create(any(ComentarioMedicamento.class))).thenReturn(testComentario);

        ComentarioMedicamento comentarioToCreate = new ComentarioMedicamento();
        comentarioToCreate.setIdMedicamento(1L);
        comentarioToCreate.setTexto("Nuevo comentario");

        // Act
        ResponseEntity<ComentarioMedicamento> response = comentarioMedicamentoController.create(comentarioToCreate);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(testComentario, response.getBody());
        assertEquals(1L, comentarioToCreate.getIdUsuario());
        verify(usuarioService).findByCorreo("test@example.com");
        verify(comentarioService).create(comentarioToCreate);
    }

    @Test
    void testDelete_Success_Owner() {
        // Arrange
        Long comentarioId = 1L;
        when(authentication.getName()).thenReturn("test@example.com");
        when(usuarioService.findByCorreo("test@example.com")).thenReturn(testUsuario);
        when(comentarioService.findById(comentarioId)).thenReturn(testComentario);
        doNothing().when(comentarioService).delete(comentarioId);

        // Act
        ResponseEntity<Void> response = comentarioMedicamentoController.delete(comentarioId);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(comentarioService).findById(comentarioId);
        verify(comentarioService).delete(comentarioId);
    }

    @Test
    void testDelete_Forbidden_NotOwner() {
        // Arrange
        Long comentarioId = 1L;
        Usuario otherUsuario = new Usuario();
        otherUsuario.setIdUsuario(2L);
        otherUsuario.setCorreo("other@example.com");
        
        when(authentication.getName()).thenReturn("other@example.com");
        when(usuarioService.findByCorreo("other@example.com")).thenReturn(otherUsuario);
        when(comentarioService.findById(comentarioId)).thenReturn(testComentario);

        // Act
        ResponseEntity<Void> response = comentarioMedicamentoController.delete(comentarioId);

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        verify(comentarioService).findById(comentarioId);
        verify(comentarioService, never()).delete(anyLong());
    }

    @Test
    void testDelete_DataIntegrityViolationException() {
        // Arrange
        Long comentarioId = 1L;
        when(authentication.getName()).thenReturn("test@example.com");
        when(usuarioService.findByCorreo("test@example.com")).thenReturn(testUsuario);
        when(comentarioService.findById(comentarioId)).thenReturn(testComentario);
        doThrow(new DataIntegrityViolationException("Constraint violation")).when(comentarioService).delete(comentarioId);

        // Act
        ResponseEntity<Void> response = comentarioMedicamentoController.delete(comentarioId);

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        verify(comentarioService).findById(comentarioId);
        verify(comentarioService).delete(comentarioId);
    }

    @Test
    void testDelete_GenericException() {
        // Arrange
        Long comentarioId = 1L;
        when(authentication.getName()).thenReturn("test@example.com");
        when(usuarioService.findByCorreo("test@example.com")).thenReturn(testUsuario);
        when(comentarioService.findById(comentarioId)).thenReturn(testComentario);
        doThrow(new RuntimeException("Unexpected error")).when(comentarioService).delete(comentarioId);

        // Act
        ResponseEntity<Void> response = comentarioMedicamentoController.delete(comentarioId);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        verify(comentarioService).findById(comentarioId);
        verify(comentarioService).delete(comentarioId);
    }
}
