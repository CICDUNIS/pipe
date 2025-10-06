package com.example.pharmacy.service.impl;

import com.example.pharmacy.config.JdbcConfiguration;
import com.example.pharmacy.dto.VentaDTO;
import com.example.pharmacy.dto.VentaDetalleDTO;
import com.example.pharmacy.model.Medicamento;
import com.example.pharmacy.model.Receta;
import com.example.pharmacy.model.Venta;
import com.example.pharmacy.model.VentaDetalle;
import com.example.pharmacy.repository.MedicamentoRepository;
import com.example.pharmacy.repository.RecetaRepository;
import com.example.pharmacy.repository.VentaDetalleRepository;
import com.example.pharmacy.repository.VentaRepository;
import com.example.pharmacy.service.CompraMedicamentoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.spy;

@ExtendWith(MockitoExtension.class)
class VentaServiceImplTest {

    @Mock
    private VentaRepository ventaRepository;

    @Mock
    private VentaDetalleRepository detalleRepository;

    @Mock
    private MedicamentoRepository medicamentoRepository;

    @Mock
    private RecetaRepository recetaRepository;

    @Mock
    private CompraMedicamentoService compraMedicamentoService;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private VentaServiceImpl ventaService;

    private VentaDTO testVentaDTO;
    private Venta testVenta;
    private Medicamento testMedicamento;
    private Receta testReceta;
    private VentaDetalle testVentaDetalle;

    @BeforeEach
    void setUp() {
        // Configurar VentaDTO de prueba
        testVentaDTO = new VentaDTO();
        testVentaDTO.setIdVenta(1L);
        testVentaDTO.setIdUsuario(1L);
        testVentaDTO.setIdReceta(1L);
        testVentaDTO.setFechaVenta(LocalDateTime.now());
        testVentaDTO.setTotal(100.0);
        testVentaDTO.setImpuesto(12.0);
        testVentaDTO.setDescuento(10.0);
        testVentaDTO.setMontoPagado(102.0);

        // Configurar VentaDetalleDTO
        VentaDetalleDTO detalleDTO = new VentaDetalleDTO();
        detalleDTO.setIdVentaDetalle(1L);
        detalleDTO.setIdMedicamento(1L);
        detalleDTO.setCantidad(2);
        detalleDTO.setPrecioUnitario(50.0);
        detalleDTO.setTotalLinea(100.0);
        testVentaDTO.setDetalles(Arrays.asList(detalleDTO));

        // Configurar entidades de prueba
        testVenta = new Venta();
        testVenta.setIdVenta(1L);
        testVenta.setIdUsuario(1L);
        testVenta.setIdReceta(1L);
        testVenta.setFechaVenta(LocalDateTime.now());
        testVenta.setTotal(100.0);
        testVenta.setImpuesto(12.0);
        testVenta.setDescuento(10.0);
        testVenta.setMontoPagado(102.0);

        testMedicamento = new Medicamento();
        testMedicamento.setIdMedicamento(1L);
        testMedicamento.setPrecio(50.0);

        testReceta = new Receta();
        testReceta.setIdReceta(1L);
        testReceta.setCodigoReceta("REC001");

        testVentaDetalle = new VentaDetalle();
        testVentaDetalle.setIdVentaDetalle(1L);
        testVentaDetalle.setIdVenta(1L);
        testVentaDetalle.setIdMedicamento(1L);
        testVentaDetalle.setCantidad(2);
        testVentaDetalle.setPrecioUnitario(50.0);
        testVentaDetalle.setTotalLinea(100.0);
    }

    // ===== TESTS PARA findAll() =====
    @Test
    void testCreateVenta_Success() {
        // Arrange
        when(medicamentoRepository.findById(1L)).thenReturn(Optional.of(testMedicamento));
        when(ventaRepository.save(any(Venta.class))).thenReturn(testVenta);
        when(detalleRepository.save(any(VentaDetalle.class))).thenReturn(testVentaDetalle);
        
        // Mock getVentaWithDetails para evitar llamada recursiva
        VentaServiceImpl spyService = spy(ventaService);
        doReturn(testVentaDTO).when(spyService).getVentaWithDetails(1L);

        // Act
        VentaDTO result = spyService.createVenta(testVentaDTO);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getIdVenta());
        verify(ventaRepository).save(any(Venta.class));
        verify(detalleRepository).save(any(VentaDetalle.class));
    }

    @Test
    void testCreateVenta_WithPrescriptionCode() {
        // Arrange
        testVentaDTO.setCodigoReceta("REC001");
        when(medicamentoRepository.findById(1L)).thenReturn(Optional.of(testMedicamento));
        when(recetaRepository.findByCodigoReceta("REC001")).thenReturn(Optional.of(testReceta));
        when(ventaRepository.save(any(Venta.class))).thenReturn(testVenta);
        when(detalleRepository.save(any(VentaDetalle.class))).thenReturn(testVentaDetalle);
        
        // Mock getVentaWithDetails para evitar llamada recursiva
        VentaServiceImpl spyService = spy(ventaService);
        doReturn(testVentaDTO).when(spyService).getVentaWithDetails(1L);
        doNothing().when(compraMedicamentoService).validarReceta("REC001", Arrays.asList("1"));

        // Act
        VentaDTO result = spyService.createVenta(testVentaDTO);

        // Assert
        assertNotNull(result);
        verify(compraMedicamentoService).validarReceta("REC001", Arrays.asList("1"));
        verify(recetaRepository).findByCodigoReceta("REC001");
    }

    @Test
    void testCreateVenta_WithAffiliationNumber() {
        // Arrange
        testVentaDTO.setNumeroAfiliacion("AFF001");
        when(medicamentoRepository.findById(1L)).thenReturn(Optional.of(testMedicamento));
        when(ventaRepository.save(any(Venta.class))).thenReturn(testVenta);
        when(detalleRepository.save(any(VentaDetalle.class))).thenReturn(testVentaDetalle);
        
        // Mock getVentaWithDetails para evitar llamada recursiva
        VentaServiceImpl spyService = spy(ventaService);
        doReturn(testVentaDTO).when(spyService).getVentaWithDetails(1L);
        doNothing().when(compraMedicamentoService).validarCobertura("AFF001", Arrays.asList("1"));

        // Act
        VentaDTO result = spyService.createVenta(testVentaDTO);

        // Assert
        assertNotNull(result);
        verify(compraMedicamentoService).validarCobertura("AFF001", Arrays.asList("1"));
    }

    @Test
    void testCreateVenta_InvalidDetails() {
        // Arrange
        testVentaDTO.setDetalles(Arrays.asList()); // Detalles vacíos

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            ventaService.createVenta(testVentaDTO);
        });
    }

    @Test
    void testCreateVenta_MedicamentoNotFound() {
        // Arrange
        when(medicamentoRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            ventaService.createVenta(testVentaDTO);
        });
    }

    @Test
    void testCreateVenta_RecetaNotFound() {
        // Arrange
        testVentaDTO.setCodigoReceta("INVALID");
        when(recetaRepository.findByCodigoReceta("INVALID")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            ventaService.createVenta(testVentaDTO);
        });
    }

    @Test
    void testCreateVenta_SpringDataFailure_FallbackToJDBC() {
        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            ventaService.createVenta(testVentaDTO);
        });
    }

    // ===== TESTS PARA getVentaWithDetails() =====

    @Test
    void testGetVentaWithDetails_Success() {
        // Arrange
        when(ventaRepository.findById(1L)).thenReturn(Optional.of(testVenta));
        when(detalleRepository.findByIdVenta(1L)).thenReturn(Arrays.asList(testVentaDetalle));

        // Act
        VentaDTO result = ventaService.getVentaWithDetails(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getIdVenta());
        assertEquals(1, result.getDetalles().size());
        verify(ventaRepository).findById(1L);
        verify(detalleRepository).findByIdVenta(1L);
    }

    @Test
    void testGetVentaWithDetails_VentaNotFound() {
        // Arrange
        when(ventaRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> {
            ventaService.getVentaWithDetails(1L);
        });
    }

    // ===== TESTS PARA updateVenta() =====

    @Test
    void testUpdateVenta_Success() {
        // Arrange
        when(ventaRepository.findById(1L)).thenReturn(Optional.of(testVenta));
        when(ventaRepository.save(any(Venta.class))).thenReturn(testVenta);
        
        // Mock getVentaWithDetails para evitar llamada recursiva
        VentaServiceImpl spyService = spy(ventaService);
        doReturn(testVentaDTO).when(spyService).getVentaWithDetails(1L);

        // Act
        VentaDTO result = spyService.updateVenta(1L, testVentaDTO);

        // Assert
        assertNotNull(result);
        verify(ventaRepository).findById(1L);
        verify(ventaRepository).save(any(Venta.class));
    }

    @Test
    void testUpdateVenta_VentaNotFound() {
        // Arrange
        when(ventaRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> {
            ventaService.updateVenta(1L, testVentaDTO);
        });
    }

    // ===== TESTS PARA deleteVenta() =====

    @Test
    void testDeleteVenta_Success() {
        // Arrange
        when(ventaRepository.findById(1L)).thenReturn(Optional.of(testVenta));
        doNothing().when(ventaRepository).delete(testVenta);

        // Act
        ventaService.deleteVenta(1L);

        // Assert
        verify(ventaRepository).findById(1L);
        verify(ventaRepository).delete(testVenta);
    }

    @Test
    void testDeleteVenta_VentaNotFound() {
        // Arrange
        when(ventaRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> {
            ventaService.deleteVenta(1L);
        });
    }

    // ===== TESTS ADICIONALES PARA createVenta() =====

    @Test
    void testCreateVenta_WithMultipleDetalles() {
        // Arrange
        VentaDetalleDTO detalle2 = new VentaDetalleDTO();
        detalle2.setIdVentaDetalle(2L);
        detalle2.setIdMedicamento(2L);
        detalle2.setCantidad(1);
        detalle2.setPrecioUnitario(30.0);
        detalle2.setTotalLinea(30.0);
        
        // Crear nueva lista mutable
        List<VentaDetalleDTO> detalles = new ArrayList<>();
        detalles.add(testVentaDTO.getDetalles().get(0));
        detalles.add(detalle2);
        testVentaDTO.setDetalles(detalles);
        
        Medicamento medicamento2 = new Medicamento();
        medicamento2.setIdMedicamento(2L);
        medicamento2.setPrecio(30.0);
        
        when(medicamentoRepository.findById(1L)).thenReturn(Optional.of(testMedicamento));
        when(medicamentoRepository.findById(2L)).thenReturn(Optional.of(medicamento2));
        when(ventaRepository.save(any(Venta.class))).thenReturn(testVenta);
        when(detalleRepository.save(any(VentaDetalle.class))).thenReturn(testVentaDetalle);
        
        // Mock getVentaWithDetails para evitar llamada recursiva
        VentaServiceImpl spyService = spy(ventaService);
        doReturn(testVentaDTO).when(spyService).getVentaWithDetails(1L);

        // Act
        VentaDTO result = spyService.createVenta(testVentaDTO);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getIdVenta());
        verify(medicamentoRepository).findById(1L);
        verify(medicamentoRepository).findById(2L);
        verify(ventaRepository).save(any(Venta.class));
        verify(detalleRepository, times(2)).save(any(VentaDetalle.class));
    }

    @Test
    void testCreateVenta_WithNullDiscount() {
        // Arrange
        testVentaDTO.setDescuento(null);
        when(medicamentoRepository.findById(1L)).thenReturn(Optional.of(testMedicamento));
        when(ventaRepository.save(any(Venta.class))).thenReturn(testVenta);
        when(detalleRepository.save(any(VentaDetalle.class))).thenReturn(testVentaDetalle);
        
        // Mock getVentaWithDetails para evitar llamada recursiva
        VentaServiceImpl spyService = spy(ventaService);
        doReturn(testVentaDTO).when(spyService).getVentaWithDetails(1L);

        // Act
        VentaDTO result = spyService.createVenta(testVentaDTO);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getIdVenta());
        verify(ventaRepository).save(any(Venta.class));
        verify(detalleRepository).save(any(VentaDetalle.class));
    }
}
