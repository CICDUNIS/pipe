package com.example.pharmacy.service.impl;

import com.example.pharmacy.model.Medicamento;
import com.example.pharmacy.repository.MedicamentoRepository;
import com.example.pharmacy.service.AuditoriaService;
import com.example.pharmacy.util.UserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MedicamentoServiceImplTest {

    @Mock
    private MedicamentoRepository medicamentoRepository;

    @Mock
    private AuditoriaService auditoriaService;

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private MedicamentoServiceImpl medicamentoService;

    private Medicamento testMedicamento;

    @BeforeEach
    void setUp() {
        testMedicamento = new Medicamento();
        testMedicamento.setIdMedicamento(1L);
        testMedicamento.setCodigo("MED001");
        testMedicamento.setNombre("Paracetamol");
        testMedicamento.setCategoria("Analgésico");
        testMedicamento.setPrincipioActivo("Paracetamol");
        testMedicamento.setDescripcion("Analgésico y antipirético");
        testMedicamento.setFotoUrl("paracetamol.jpg");
        testMedicamento.setConcentracion("500mg");
        testMedicamento.setPresentacion("Tableta");
        testMedicamento.setNumeroUnidades(20);
        testMedicamento.setMarca("Genérico");
        testMedicamento.setStock(100);
        testMedicamento.setPrecio(5.99);
        testMedicamento.setRequiereReceta("N");
    }

    @Test
    void testCreate_Success() {
        // Arrange
        when(userDetails.getUsuarioActual()).thenReturn("testuser");
        when(medicamentoRepository.save(any(Medicamento.class))).thenReturn(testMedicamento);

        // Act
        Medicamento result = medicamentoService.create(testMedicamento);

        // Assert
        assertNotNull(result);
        assertEquals("MED001", result.getCodigo());
        assertEquals("Paracetamol", result.getNombre());
        assertEquals("N", result.getRequiereReceta());

        verify(medicamentoRepository).save(any(Medicamento.class));
        verify(auditoriaService).registrar(eq("Medicamento"), eq("INSERT"), anyString(), anyString());
    }

    @Test
    void testCreate_WithNullRequiereReceta() {
        // Arrange
        testMedicamento.setRequiereReceta(null);
        when(userDetails.getUsuarioActual()).thenReturn("testuser");
        when(medicamentoRepository.save(any(Medicamento.class))).thenReturn(testMedicamento);

        // Act
        Medicamento result = medicamentoService.create(testMedicamento);

        // Assert
        assertEquals("N", result.getRequiereReceta());
        verify(medicamentoRepository).save(any(Medicamento.class));
    }

    @Test
    void testCreate_WithTrueRequiereReceta() {
        // Arrange
        testMedicamento.setRequiereReceta("true");
        when(userDetails.getUsuarioActual()).thenReturn("testuser");
        when(medicamentoRepository.save(any(Medicamento.class))).thenReturn(testMedicamento);

        // Act
        Medicamento result = medicamentoService.create(testMedicamento);

        // Assert
        assertEquals("Y", result.getRequiereReceta());
        verify(medicamentoRepository).save(any(Medicamento.class));
    }

    @Test
    void testCreate_WithFalseRequiereReceta() {
        // Arrange
        testMedicamento.setRequiereReceta("false");
        when(userDetails.getUsuarioActual()).thenReturn("testuser");
        when(medicamentoRepository.save(any(Medicamento.class))).thenReturn(testMedicamento);

        // Act
        Medicamento result = medicamentoService.create(testMedicamento);

        // Assert
        assertEquals("N", result.getRequiereReceta());
        verify(medicamentoRepository).save(any(Medicamento.class));
    }

    @Test
    void testCreate_WithUpperCaseRequiereReceta() {
        // Arrange
        testMedicamento.setRequiereReceta("TRUE");
        when(userDetails.getUsuarioActual()).thenReturn("testuser");
        when(medicamentoRepository.save(any(Medicamento.class))).thenReturn(testMedicamento);

        // Act
        Medicamento result = medicamentoService.create(testMedicamento);

        // Assert
        assertEquals("Y", result.getRequiereReceta());
        verify(medicamentoRepository).save(any(Medicamento.class));
    }

    @Test
    void testCreate_WithLowerCaseRequiereReceta() {
        // Arrange
        testMedicamento.setRequiereReceta("y");
        when(userDetails.getUsuarioActual()).thenReturn("testuser");
        when(medicamentoRepository.save(any(Medicamento.class))).thenReturn(testMedicamento);

        // Act
        Medicamento result = medicamentoService.create(testMedicamento);

        // Assert
        assertEquals("Y", result.getRequiereReceta());
        verify(medicamentoRepository).save(any(Medicamento.class));
    }

    @Test
    void testCreate_WithInvalidRequiereReceta() {
        // Arrange
        testMedicamento.setRequiereReceta("invalid");
        when(userDetails.getUsuarioActual()).thenReturn("testuser");
        when(medicamentoRepository.save(any(Medicamento.class))).thenReturn(testMedicamento);

        // Act
        Medicamento result = medicamentoService.create(testMedicamento);

        // Assert
        assertEquals("N", result.getRequiereReceta());
        verify(medicamentoRepository).save(any(Medicamento.class));
    }

    @Test
    void testCreate_WithNegativePrecio() {
        // Arrange
        testMedicamento.setPrecio(-5.99);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            medicamentoService.create(testMedicamento);
        });

        verify(medicamentoRepository, never()).save(any());
        verify(auditoriaService, never()).registrar(anyString(), anyString(), anyString(), anyString());
    }

    @Test
    void testCreate_WithNegativeStock() {
        // Arrange
        testMedicamento.setStock(-100);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            medicamentoService.create(testMedicamento);
        });

        verify(medicamentoRepository, never()).save(any());
        verify(auditoriaService, never()).registrar(anyString(), anyString(), anyString(), anyString());
    }

    @Test
    void testFindById_Success() {
        // Arrange
        when(medicamentoRepository.findById(1L)).thenReturn(Optional.of(testMedicamento));

        // Act
        Medicamento result = medicamentoService.findById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getIdMedicamento());
        assertEquals("Paracetamol", result.getNombre());
        verify(medicamentoRepository).findById(1L);
    }

    @Test
    void testFindById_NotFound() {
        // Arrange
        when(medicamentoRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> {
            medicamentoService.findById(999L);
        });

        verify(medicamentoRepository).findById(999L);
    }

    @Test
    void testFindAll_Success() {
        // Arrange
        List<Medicamento> medicamentos = Arrays.asList(testMedicamento);
        when(medicamentoRepository.findAll()).thenReturn(medicamentos);

        // Act
        List<Medicamento> result = medicamentoService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Paracetamol", result.get(0).getNombre());
        verify(medicamentoRepository).findAll();
    }

    @Test
    void testUpdate_Success() {
        // Arrange
        Medicamento updateData = new Medicamento();
        updateData.setCodigo("MED001-UPDATED");
        updateData.setNombre("Paracetamol Plus");
        updateData.setCategoria("Analgésico Plus");
        updateData.setPrincipioActivo("Paracetamol + Cafeína");
        updateData.setDescripcion("Analgésico mejorado");
        updateData.setFotoUrl("paracetamol-plus.jpg");
        updateData.setConcentracion("650mg");
        updateData.setPresentacion("Cápsula");
        updateData.setNumeroUnidades(30);
        updateData.setMarca("Premium");
        updateData.setStock(150);
        updateData.setPrecio(8.99);
        updateData.setRequiereReceta("Y");

        when(medicamentoRepository.findById(1L)).thenReturn(Optional.of(testMedicamento));
        when(medicamentoRepository.save(any(Medicamento.class))).thenReturn(testMedicamento);
        when(userDetails.getUsuarioActual()).thenReturn("testuser");

        // Act
        Medicamento result = medicamentoService.update(1L, updateData);

        // Assert
        assertNotNull(result);
        verify(medicamentoRepository).save(any(Medicamento.class));
        verify(auditoriaService).registrar(eq("Medicamento"), eq("UPDATE"), anyString(), anyString());
    }

    @Test
    void testUpdate_NotFound() {
        // Arrange
        when(medicamentoRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> {
            medicamentoService.update(999L, testMedicamento);
        });

        verify(medicamentoRepository).findById(999L);
        verify(medicamentoRepository, never()).save(any());
    }

    @Test
    void testDelete_Success() {
        // Act
        medicamentoService.delete(1L);

        // Assert
        verify(medicamentoRepository).deleteById(1L);
    }

    @Test
    void testExistsById_True() {
        // Arrange
        when(medicamentoRepository.existsById(1L)).thenReturn(true);

        // Act
        boolean result = medicamentoService.existsById(1L);

        // Assert
        assertTrue(result);
        verify(medicamentoRepository).existsById(1L);
    }

    @Test
    void testExistsById_False() {
        // Arrange
        when(medicamentoRepository.existsById(999L)).thenReturn(false);

        // Act
        boolean result = medicamentoService.existsById(999L);

        // Assert
        assertFalse(result);
        verify(medicamentoRepository).existsById(999L);
    }

    @Test
    void testSearch_WithTerm() {
        // Arrange
        List<Medicamento> medicamentos = Arrays.asList(testMedicamento);
        when(medicamentoRepository.searchByTerm("paracetamol")).thenReturn(medicamentos);

        // Act
        List<Medicamento> result = medicamentoService.search("paracetamol");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(medicamentoRepository).searchByTerm("paracetamol");
    }

    @Test
    void testSearch_WithNullTerm() {
        // Arrange
        List<Medicamento> medicamentos = Arrays.asList(testMedicamento);
        when(medicamentoRepository.searchByTerm("")).thenReturn(medicamentos);

        // Act
        List<Medicamento> result = medicamentoService.search(null);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(medicamentoRepository).searchByTerm("");
    }

    @Test
    void testFindLastTen_Success() {
        // Arrange
        List<Medicamento> medicamentos = Arrays.asList(testMedicamento);
        when(medicamentoRepository.findTop10ByOrderByIdDesc()).thenReturn(medicamentos);

        // Act
        List<Medicamento> result = medicamentoService.findLastTen();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(medicamentoRepository).findTop10ByOrderByIdDesc();
    }
}
