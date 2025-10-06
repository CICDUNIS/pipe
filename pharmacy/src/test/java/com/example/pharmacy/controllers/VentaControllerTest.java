package com.example.pharmacy.controllers;

import com.example.pharmacy.dto.VentaDTO;
import com.example.pharmacy.service.VentaService;
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
class VentaControllerTest {

    @Mock
    private VentaService ventaService;

    @InjectMocks
    private VentaController ventaController;

    private VentaDTO testVentaDTO;
    private List<VentaDTO> testVentaList;

    @BeforeEach
    void setUp() {
        testVentaDTO = new VentaDTO();
        testVentaDTO.setIdVenta(1L);
        testVentaDTO.setIdUsuario(1L);
        testVentaDTO.setTotal(100.0);

        testVentaList = Arrays.asList(testVentaDTO);
    }
    @Test
    void testCreate_Success() {
        // Arrange
        when(ventaService.createVenta(any(VentaDTO.class))).thenReturn(testVentaDTO);

        // Act
        ResponseEntity<Object> response = ventaController.create(testVentaDTO);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(testVentaDTO, response.getBody());
        verify(ventaService).createVenta(testVentaDTO);
    }

    @Test
    void testCreate_Failure() {
        // Arrange
        when(ventaService.createVenta(any(VentaDTO.class))).thenReturn(null);

        // Act
        ResponseEntity<Object> response = ventaController.create(testVentaDTO);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Esto fallo bestia", response.getBody());
        verify(ventaService).createVenta(testVentaDTO);
    }

    @Test
    void testGetOne_Success() {
        // Arrange
        when(ventaService.getVentaWithDetails(1L)).thenReturn(testVentaDTO);

        // Act
        ResponseEntity<VentaDTO> response = ventaController.getOne(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testVentaDTO, response.getBody());
        verify(ventaService).getVentaWithDetails(1L);
    }

    @Test
    void testGetOne_NotFound() {
        // Arrange
        when(ventaService.getVentaWithDetails(999L)).thenReturn(null);

        // Act
        ResponseEntity<VentaDTO> response = ventaController.getOne(999L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
        verify(ventaService).getVentaWithDetails(999L);
    }

    @Test
    void testUpdate_Success() {
        // Arrange
        VentaDTO updatedVenta = new VentaDTO();
        updatedVenta.setIdVenta(1L);
        updatedVenta.setIdUsuario(1L);
        updatedVenta.setTotal(150.0);

        when(ventaService.updateVenta(1L, updatedVenta)).thenReturn(updatedVenta);

        // Act
        ResponseEntity<VentaDTO> response = ventaController.update(1L, updatedVenta);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedVenta, response.getBody());
        verify(ventaService).updateVenta(1L, updatedVenta);
    }

    @Test
    void testDelete_Success() {
        // Act
        ventaController.delete(1L);

        // Assert
        verify(ventaService).deleteVenta(1L);
    }
}
