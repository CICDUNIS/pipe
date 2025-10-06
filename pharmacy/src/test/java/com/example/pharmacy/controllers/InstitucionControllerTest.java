package com.example.pharmacy.controllers;

import com.example.pharmacy.model.Institucion;
import com.example.pharmacy.service.InstitucionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InstitucionControllerTest {

    @Mock
    private InstitucionService institucionService;

    @InjectMocks
    private InstitucionController institucionController;

    private Institucion testInstitucion;
    private List<Institucion> testInstituciones;

    @BeforeEach
    void setUp() {
        testInstitucion = new Institucion();
        testInstitucion.setIdInstitucion(1L);
        testInstitucion.setCodigoInstitucion("HOSP001");
        testInstitucion.setNombreInstitucion("Hospital General");
        testInstitucion.setTipoInstitucion("Hospital");

        testInstituciones = Arrays.asList(testInstitucion);
    }

    @Test
    void testGetAll_Success() {
        // Arrange
        when(institucionService.findAll()).thenReturn(testInstituciones);

        // Act
        ResponseEntity<List<Institucion>> response = institucionController.getAll();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testInstituciones, response.getBody());
        verify(institucionService).findAll();
    }

    @Test
    void testGetAll_EmptyList() {
        // Arrange
        when(institucionService.findAll()).thenReturn(Arrays.asList());

        // Act
        ResponseEntity<List<Institucion>> response = institucionController.getAll();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
        verify(institucionService).findAll();
    }

    @Test
    void testGetById_Success() {
        // Arrange
        when(institucionService.findById(1L)).thenReturn(testInstitucion);

        // Act
        ResponseEntity<Institucion> response = institucionController.getById(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testInstitucion, response.getBody());
        verify(institucionService).findById(1L);
    }

    @Test
    void testGetById_NotFound() {
        // Arrange
        when(institucionService.findById(999L)).thenReturn(null);

        // Act
        ResponseEntity<Institucion> response = institucionController.getById(999L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
        verify(institucionService).findById(999L);
    }

    @Test
    void testGetById_WithZeroId() {
        // Arrange
        when(institucionService.findById(0L)).thenReturn(null);

        // Act
        ResponseEntity<Institucion> response = institucionController.getById(0L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
        verify(institucionService).findById(0L);
    }

    @Test
    void testCreate_Success() {
        // Arrange
        when(institucionService.create(any(Institucion.class))).thenReturn(testInstitucion);

        // Act
        ResponseEntity<Institucion> response = institucionController.create(testInstitucion);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testInstitucion, response.getBody());
        verify(institucionService).create(testInstitucion);
    }

    @Test
    void testCreate_WithNullInput() {
        // Arrange
        when(institucionService.create(null)).thenReturn(null);

        // Act
        ResponseEntity<Institucion> response = institucionController.create(null);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
        verify(institucionService).create(null);
    }

    @Test
    void testUpdate_Success() {
        // Arrange
        Institucion updatedInstitucion = new Institucion();
        updatedInstitucion.setIdInstitucion(1L);
        updatedInstitucion.setNombreInstitucion("Hospital General Actualizado");
        updatedInstitucion.setTipoInstitucion("Hospital Especializado");

        when(institucionService.update(1L, updatedInstitucion)).thenReturn(updatedInstitucion);

        // Act
        ResponseEntity<Institucion> response = institucionController.update(1L, updatedInstitucion);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedInstitucion, response.getBody());
        verify(institucionService).update(1L, updatedInstitucion);
    }

    @Test
    void testUpdate_WithNullInput() {
        // Arrange
        when(institucionService.update(1L, null)).thenReturn(null);

        // Act
        ResponseEntity<Institucion> response = institucionController.update(1L, null);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
        verify(institucionService).update(1L, null);
    }

    @Test
    void testUpdate_WithDifferentId() {
        // Arrange
        Institucion institucionWithDifferentId = new Institucion();
        institucionWithDifferentId.setIdInstitucion(999L);
        institucionWithDifferentId.setNombreInstitucion("Hospital Diferente");

        when(institucionService.update(1L, institucionWithDifferentId)).thenReturn(institucionWithDifferentId);

        // Act
        ResponseEntity<Institucion> response = institucionController.update(1L, institucionWithDifferentId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(institucionWithDifferentId, response.getBody());
        verify(institucionService).update(1L, institucionWithDifferentId);
    }

    @Test
    void testDelete_Success() {
        // Arrange
        doNothing().when(institucionService).delete(1L);

        // Act
        ResponseEntity<Void> response = institucionController.delete(1L);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(institucionService).delete(1L);
    }

    @Test
    void testDelete_WithZeroId() {
        // Arrange
        doNothing().when(institucionService).delete(0L);

        // Act
        ResponseEntity<Void> response = institucionController.delete(0L);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(institucionService).delete(0L);
    }

    @Test
    void testDelete_WithNegativeId() {
        // Arrange
        doNothing().when(institucionService).delete(-1L);

        // Act
        ResponseEntity<Void> response = institucionController.delete(-1L);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(institucionService).delete(-1L);
    }
}
