package com.example.pharmacy.service.impl;

import com.example.pharmacy.dto.RecetaDTO;
import com.example.pharmacy.dto.RecetaDetalleDTO;
import com.example.pharmacy.model.Receta;
import com.example.pharmacy.model.RecetaDetalle;
import com.example.pharmacy.repository.RecetaDetalleRepository;
import com.example.pharmacy.repository.RecetaRepository;
import com.example.pharmacy.service.AuditoriaService;
import com.example.pharmacy.util.UserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecetaServiceImplTest {

    @Mock
    private RecetaRepository recetaRepository;

    @Mock
    private RecetaDetalleRepository detalleRepository;

    @Mock
    private AuditoriaService auditoriaService;

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private RecetaServiceImpl recetaService;

    private Receta testReceta;
    private RecetaDTO testRecetaDTO;
    private RecetaDetalle testRecetaDetalle;
    private RecetaDetalleDTO testRecetaDetalleDTO;

    @BeforeEach
    void setUp() {
        // Setup test data
        testReceta = new Receta();
        testReceta.setIdReceta(1L);
        testReceta.setCodigoReceta("REC001");
        testReceta.setFecha(LocalDateTime.now());
        testReceta.setIdUsuario(1L);
        testReceta.setAprobadoSeguro("N");
        testReceta.setPdfUrl("test.pdf");

        testRecetaDetalle = new RecetaDetalle();
        testRecetaDetalle.setIdDetalle(1L);
        testRecetaDetalle.setIdReceta(1L);
        testRecetaDetalle.setIdMedicamento(1L);
        testRecetaDetalle.setDosis("1 tableta");
        testRecetaDetalle.setFrecuencia("cada 8 horas");
        testRecetaDetalle.setDuracion("7 días");
        testRecetaDetalle.setCantidadRequerida(21);
        testRecetaDetalle.setObservaciones("Tomar con alimentos");

        testRecetaDetalleDTO = new RecetaDetalleDTO();
        testRecetaDetalleDTO.setIdDetalle(1L);
        testRecetaDetalleDTO.setIdMedicamento(1L);
        testRecetaDetalleDTO.setDosis("1 tableta");
        testRecetaDetalleDTO.setFrecuencia("cada 8 horas");
        testRecetaDetalleDTO.setDuracion("7 días");
        testRecetaDetalleDTO.setCantidadRequerida(21);
        testRecetaDetalleDTO.setObservaciones("Tomar con alimentos");

        testRecetaDTO = new RecetaDTO();
        testRecetaDTO.setCodigoReceta("REC001");
        testRecetaDTO.setIdUsuario(1L);
        testRecetaDTO.setPdfUrl("test.pdf");
        testRecetaDTO.setDetalles(Arrays.asList(testRecetaDetalleDTO));


    }

    @Test
    void testCreateReceta_Success() {
        // Arrange
        when(userDetails.getUsuarioActual()).thenReturn("testuser");
        when(recetaRepository.save(any(Receta.class))).thenReturn(testReceta);
        when(detalleRepository.save(any(RecetaDetalle.class))).thenReturn(testRecetaDetalle);
        when(recetaRepository.findById(1L)).thenReturn(Optional.of(testReceta));
        when(detalleRepository.findByIdReceta(1L)).thenReturn(Arrays.asList(testRecetaDetalle));

        // Act
        RecetaDTO result = recetaService.createReceta(testRecetaDTO);

        // Assert
        assertNotNull(result);
        assertEquals("REC001", result.getCodigoReceta());
        assertEquals(1L, result.getIdUsuario());
        assertEquals("N", result.getAprobadoSeguro());
        assertEquals("test.pdf", result.getPdfUrl());
        assertEquals(1, result.getDetalles().size());

        verify(recetaRepository).save(any(Receta.class));
        verify(detalleRepository, times(1)).save(any(RecetaDetalle.class));
        verify(auditoriaService).registrar(eq("Receta"), eq("INSERT"), anyString(), anyString());
    }

    @Test
    void testGetRecetaWithDetails_Success() {
        // Arrange
        when(recetaRepository.findById(1L)).thenReturn(Optional.of(testReceta));
        when(detalleRepository.findByIdReceta(1L)).thenReturn(Arrays.asList(testRecetaDetalle));

        // Act
        RecetaDTO result = recetaService.getRecetaWithDetails(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getIdReceta());
        assertEquals("REC001", result.getCodigoReceta());
        assertEquals(1L, result.getIdUsuario());
        assertEquals("N", result.getAprobadoSeguro());
        assertEquals("test.pdf", result.getPdfUrl());
        assertEquals(1, result.getDetalles().size());

        RecetaDetalleDTO detalle = result.getDetalles().get(0);
        assertEquals(1L, detalle.getIdDetalle());
        assertEquals(1L, detalle.getIdMedicamento());
        assertEquals("1 tableta", detalle.getDosis());
        assertEquals("cada 8 horas", detalle.getFrecuencia());
        assertEquals("7 días", detalle.getDuracion());
        assertEquals(21, detalle.getCantidadRequerida());
        assertEquals("Tomar con alimentos", detalle.getObservaciones());
    }

    @Test
    void testGetRecetaWithDetails_NotFound() {
        // Arrange
        when(recetaRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> {
            recetaService.getRecetaWithDetails(999L);
        });

        verify(recetaRepository).findById(999L);
        verify(detalleRepository, never()).findByIdReceta(anyLong());
    }

    @Test
    void testUpdateReceta_Success() {
        // Arrange
        RecetaDTO updateDTO = new RecetaDTO();
        updateDTO.setCodigoReceta("REC001-UPDATED");
        updateDTO.setPdfUrl("updated.pdf");
        updateDTO.setDetalles(Arrays.asList(testRecetaDetalleDTO));

        when(userDetails.getUsuarioActual()).thenReturn("testuser");
        when(recetaRepository.findById(1L)).thenReturn(Optional.of(testReceta));
        when(recetaRepository.save(any(Receta.class))).thenReturn(testReceta);
        when(detalleRepository.findByIdReceta(1L)).thenReturn(Arrays.asList(testRecetaDetalle));
        when(detalleRepository.save(any(RecetaDetalle.class))).thenReturn(testRecetaDetalle);
        when(recetaRepository.findById(1L)).thenReturn(Optional.of(testReceta));
        when(detalleRepository.findByIdReceta(1L)).thenReturn(Arrays.asList(testRecetaDetalle));

        // Act
        RecetaDTO result = recetaService.updateReceta(1L, updateDTO);

        // Assert
        assertNotNull(result);
        verify(recetaRepository).save(any(Receta.class));
        verify(detalleRepository).deleteById(1L);
        verify(detalleRepository).save(any(RecetaDetalle.class));
        verify(auditoriaService).registrar(eq("Receta"), eq("UPDATE"), anyString(), anyString());
    }

    @Test
    void testUpdateReceta_NotFound() {
        // Arrange
        when(recetaRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> {
            recetaService.updateReceta(999L, testRecetaDTO);
        });

        verify(recetaRepository).findById(999L);
        verify(recetaRepository, never()).save(any());
        verify(detalleRepository, never()).findByIdReceta(anyLong());
    }

    @Test
    void testDeleteReceta_Success() {
        // Arrange
        when(detalleRepository.findByIdReceta(1L)).thenReturn(Arrays.asList(testRecetaDetalle));

        // Act
        recetaService.deleteReceta(1L);

        // Assert
        verify(detalleRepository).deleteById(1L);
        verify(recetaRepository).deleteById(1L);
    }

    @Test
    void testDeleteReceta_NoDetalles() {
        // Arrange
        when(detalleRepository.findByIdReceta(1L)).thenReturn(Arrays.asList());

        // Act
        recetaService.deleteReceta(1L);

        // Assert
        verify(detalleRepository).findByIdReceta(1L);
        verify(detalleRepository, never()).deleteById(anyLong());
        verify(recetaRepository).deleteById(1L);
    }

    @Test
    void testCreateReceta_MultipleDetalles() {
        // Arrange
        RecetaDetalleDTO detalle2 = new RecetaDetalleDTO();
        detalle2.setIdMedicamento(2L);
        detalle2.setDosis("2 tabletas");
        detalle2.setFrecuencia("cada 12 horas");
        detalle2.setDuracion("5 días");
        detalle2.setCantidadRequerida(10);
        detalle2.setObservaciones("Tomar en ayunas");

        testRecetaDTO.setDetalles(Arrays.asList(testRecetaDetalleDTO, detalle2));

        when(userDetails.getUsuarioActual()).thenReturn("testuser");
        when(recetaRepository.save(any(Receta.class))).thenReturn(testReceta);
        when(detalleRepository.save(any(RecetaDetalle.class))).thenReturn(testRecetaDetalle);
        when(recetaRepository.findById(1L)).thenReturn(Optional.of(testReceta));
        when(detalleRepository.findByIdReceta(1L)).thenReturn(Arrays.asList(testRecetaDetalle));

        // Act
        RecetaDTO result = recetaService.createReceta(testRecetaDTO);

        // Assert
        assertNotNull(result);
        assertEquals(2, testRecetaDTO.getDetalles().size());
        verify(detalleRepository, times(2)).save(any(RecetaDetalle.class));
    }

    @Test
    void testGetRecetaWithDetails_EmptyDetalles() {
        // Arrange
        when(recetaRepository.findById(1L)).thenReturn(Optional.of(testReceta));
        when(detalleRepository.findByIdReceta(1L)).thenReturn(Arrays.asList());

        // Act
        RecetaDTO result = recetaService.getRecetaWithDetails(1L);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.getDetalles().size());
        verify(detalleRepository).findByIdReceta(1L);
    }
}
