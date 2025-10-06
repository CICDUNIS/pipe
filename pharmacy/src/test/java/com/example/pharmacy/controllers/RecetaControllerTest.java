package com.example.pharmacy.controllers;

import com.example.pharmacy.dto.RecetaDTO;
import com.example.pharmacy.service.RecetaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecetaControllerTest {

    @Mock
    private RecetaService recetaService;

    @InjectMocks
    private RecetaController recetaController;

    private RecetaDTO testRecetaDTO;

    @BeforeEach
    void setUp() {
        testRecetaDTO = new RecetaDTO();
        testRecetaDTO.setIdReceta(1L);
        testRecetaDTO.setCodigoReceta("REC001");
        testRecetaDTO.setIdUsuario(1L);
        testRecetaDTO.setFecha(LocalDateTime.now());
        testRecetaDTO.setAprobadoSeguro("Y");
        testRecetaDTO.setPdfUrl("http://example.com/receta.pdf");
    }

    @Test
    void testCreate_Success() {
        // Arrange
        when(recetaService.createReceta(any(RecetaDTO.class))).thenReturn(testRecetaDTO);

        // Act
        ResponseEntity<RecetaDTO> response = recetaController.create(testRecetaDTO);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(testRecetaDTO, response.getBody());
        verify(recetaService).createReceta(testRecetaDTO);
    }

    @Test
    void testCreate_WithNullInput() {
        // Arrange
        when(recetaService.createReceta(null)).thenReturn(null);

        // Act
        ResponseEntity<RecetaDTO> response = recetaController.create(null);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNull(response.getBody());
        verify(recetaService).createReceta(null);
    }

    @Test
    void testGetOne_Success() {
        // Arrange
        when(recetaService.getRecetaWithDetails(1L)).thenReturn(testRecetaDTO);

        // Act
        ResponseEntity<RecetaDTO> response = recetaController.getOne(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testRecetaDTO, response.getBody());
        verify(recetaService).getRecetaWithDetails(1L);
    }

    @Test
    void testGetOne_NotFound() {
        // Arrange
        when(recetaService.getRecetaWithDetails(999L)).thenReturn(null);

        // Act
        ResponseEntity<RecetaDTO> response = recetaController.getOne(999L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
        verify(recetaService).getRecetaWithDetails(999L);
    }

    @Test
    void testGetOne_WithZeroId() {
        // Arrange
        when(recetaService.getRecetaWithDetails(0L)).thenReturn(null);

        // Act
        ResponseEntity<RecetaDTO> response = recetaController.getOne(0L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
        verify(recetaService).getRecetaWithDetails(0L);
    }

    @Test
    void testUpdate_Success() {
        // Arrange
        RecetaDTO updatedReceta = new RecetaDTO();
        updatedReceta.setIdReceta(1L);
        updatedReceta.setCodigoReceta("REC001-UPDATED");
        updatedReceta.setAprobadoSeguro("N");

        when(recetaService.updateReceta(1L, updatedReceta)).thenReturn(updatedReceta);

        // Act
        ResponseEntity<RecetaDTO> response = recetaController.update(1L, updatedReceta);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedReceta, response.getBody());
        verify(recetaService).updateReceta(1L, updatedReceta);
    }

    @Test
    void testUpdate_WithNullInput() {
        // Arrange
        when(recetaService.updateReceta(1L, null)).thenReturn(null);

        // Act
        ResponseEntity<RecetaDTO> response = recetaController.update(1L, null);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
        verify(recetaService).updateReceta(1L, null);
    }

    @Test
    void testUpdate_WithDifferentId() {
        // Arrange
        RecetaDTO recetaWithDifferentId = new RecetaDTO();
        recetaWithDifferentId.setIdReceta(999L);
        recetaWithDifferentId.setCodigoReceta("REC999");

        when(recetaService.updateReceta(1L, recetaWithDifferentId)).thenReturn(recetaWithDifferentId);

        // Act
        ResponseEntity<RecetaDTO> response = recetaController.update(1L, recetaWithDifferentId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(recetaWithDifferentId, response.getBody());
        verify(recetaService).updateReceta(1L, recetaWithDifferentId);
    }

    @Test
    void testDelete_Success() {
        // Arrange
        doNothing().when(recetaService).deleteReceta(1L);

        // Act
        ResponseEntity<Void> response = recetaController.delete(1L);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(recetaService).deleteReceta(1L);
    }

    @Test
    void testDelete_WithZeroId() {
        // Arrange
        doNothing().when(recetaService).deleteReceta(0L);

        // Act
        ResponseEntity<Void> response = recetaController.delete(0L);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(recetaService).deleteReceta(0L);
    }

    @Test
    void testDelete_WithNegativeId() {
        // Arrange
        doNothing().when(recetaService).deleteReceta(-1L);

        // Act
        ResponseEntity<Void> response = recetaController.delete(-1L);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(recetaService).deleteReceta(-1L);
    }
}
