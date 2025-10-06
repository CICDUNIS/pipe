package com.example.pharmacy.service.impl;

import com.example.pharmacy.dto.FacturaDTO;
import com.example.pharmacy.dto.VentaDTO;
import com.example.pharmacy.dto.VentaDetalleDTO;
import com.example.pharmacy.model.Factura;
import com.example.pharmacy.model.Venta;
import com.example.pharmacy.repository.FacturaRepository;
import com.example.pharmacy.service.AuditoriaService;
import com.example.pharmacy.service.VentaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.spy;

@ExtendWith(MockitoExtension.class)
class FacturaServiceImplTest {

    @Mock
    private FacturaRepository facturaRepository;

    @Mock
    private VentaService ventaService;

    @Mock
    private AuditoriaService auditoriaService;

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private MimeMessage mimeMessage;

    @Mock
    private MimeMessageHelper mimeMessageHelper;

    @InjectMocks
    private FacturaServiceImpl facturaService;

    private Factura testFactura;
    private FacturaDTO testFacturaDTO;
    private Venta testVenta;
    private VentaDTO testVentaDTO;

    @BeforeEach
    void setUp() {
        testFactura = new Factura();
        testFactura.setIdFactura(1L);
        testFactura.setIdVenta(1L);
        testFactura.setFechaFactura(LocalDateTime.now());
        testFactura.setTotalFactura(100.00);
        testFactura.setPdfUrl("test.pdf");

        testFacturaDTO = new FacturaDTO();
        testFacturaDTO.setIdFactura(1L);
        testFacturaDTO.setIdVenta(1L);
        testFacturaDTO.setFechaFactura(LocalDateTime.now());
        testFacturaDTO.setTotalFactura(100.00);
        testFacturaDTO.setPdfUrl("test.pdf");

        testVenta = new Venta();
        testVenta.setIdVenta(1L);
        testVenta.setTotal(100.00);
        testVenta.setImpuesto(16.00);
        testVenta.setDescuento(0.00);
        testVenta.setMontoPagado(116.00);

        testVentaDTO = new VentaDTO();
        testVentaDTO.setIdVenta(1L);
        testVentaDTO.setTotal(100.00);
        testVentaDTO.setImpuesto(16.00);
        testVentaDTO.setDescuento(0.00);
        testVentaDTO.setMontoPagado(116.00);
        
        // Crear detalles de venta para evitar NullPointerException
        VentaDetalleDTO detalle = new VentaDetalleDTO();
        detalle.setIdMedicamento(1L);
        detalle.setCantidad(2);
        detalle.setPrecioUnitario(50.00);
        detalle.setTotalLinea(100.00);
        testVentaDTO.setDetalles(Arrays.asList(detalle));
        
        // Configurar campos privados
        ReflectionTestUtils.setField(facturaService, "fromEmail", "from@example.com");
    }

    @Test
    void testCreateFactura_Success() {
        // Arrange
        when(facturaRepository.save(any(Factura.class))).thenReturn(testFactura);
        when(ventaService.getVentaWithDetails(1L)).thenReturn(testVentaDTO);
        
        // Usar spy para evitar la llamada recursiva a generatePdfFactura
        FacturaServiceImpl spyService = spy(facturaService);
        doReturn(new ByteArrayOutputStream()).when(spyService).generatePdfFactura(1L);

        // Act
        FacturaDTO result = spyService.createFactura(testVenta);

        // Assert
        assertNotNull(result);
        assertEquals(testFactura.getIdFactura(), result.getIdFactura());
        verify(facturaRepository, times(2)).save(any(Factura.class));
        verify(auditoriaService).registrar(eq("Factura"), eq("CREATE"), anyString(), anyString());
    }

    @Test
    void testCreateFactura_WithNullInput() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> facturaService.createFactura(null));
        verify(facturaRepository, never()).save(any());
        verify(auditoriaService, never()).registrar(anyString(), anyString(), anyString(), anyString());
    }

    @Test
    void testGetFactura_Success() {
        // Arrange
        when(facturaRepository.findById(1L)).thenReturn(Optional.of(testFactura));
        when(ventaService.getVentaWithDetails(1L)).thenReturn(testVentaDTO);

        // Act
        FacturaDTO result = facturaService.getFactura(1L);

        // Assert
        assertNotNull(result);
        assertEquals(testFactura.getIdFactura(), result.getIdFactura());
        verify(facturaRepository).findById(1L);
    }

    @Test
    void testGetFactura_NotFound() {
        // Arrange
        when(facturaRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> facturaService.getFactura(1L));
        verify(facturaRepository).findById(1L);
    }

    @Test
    void testGetFacturaByVenta_Success() {
        // Arrange
        when(facturaRepository.findByIdVenta(1L)).thenReturn(Optional.of(testFactura));
        when(ventaService.getVentaWithDetails(1L)).thenReturn(testVentaDTO);

        // Act
        FacturaDTO result = facturaService.getFacturaByVenta(1L);

        // Assert
        assertNotNull(result);
        assertEquals(testFactura.getIdFactura(), result.getIdFactura());
        verify(facturaRepository).findByIdVenta(1L);
    }

    @Test
    void testGetFacturaByVenta_NotFound() {
        // Arrange
        when(facturaRepository.findByIdVenta(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> facturaService.getFacturaByVenta(1L));
        verify(facturaRepository).findByIdVenta(1L);
    }



    @Test
    void testGeneratePdfFactura_Success() {
        // Arrange
        when(facturaRepository.findById(1L)).thenReturn(Optional.of(testFactura));
        when(ventaService.getVentaWithDetails(1L)).thenReturn(testVentaDTO);

        // Act
        ByteArrayOutputStream result = facturaService.generatePdfFactura(1L);

        // Assert
        assertNotNull(result);
        assertTrue(result.size() > 0);
        verify(facturaRepository).findById(1L);
        verify(ventaService).getVentaWithDetails(1L);
    }

    @Test
    void testGeneratePdfFactura_NotFound() {
        // Arrange
        when(facturaRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> facturaService.generatePdfFactura(1L));
        verify(facturaRepository).findById(1L);
    }

    @Test
    void testSendFacturaEmail_Success() throws Exception {
        // Arrange
        when(facturaRepository.findById(1L)).thenReturn(Optional.of(testFactura));
        when(ventaService.getVentaWithDetails(1L)).thenReturn(testVentaDTO);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        doNothing().when(mailSender).send(any(MimeMessage.class));

        // Act
        facturaService.sendFacturaEmail(1L, "test@example.com");

        // Assert
        verify(facturaRepository, times(2)).findById(1L);
        verify(mailSender).createMimeMessage();
        verify(mailSender).send(any(MimeMessage.class));
        verify(auditoriaService).registrar(eq("Factura"), eq("EMAIL"), anyString(), anyString());
    }

    @Test
    void testSendFacturaEmail_WithNullEmail() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> facturaService.sendFacturaEmail(1L, null));
        verify(facturaRepository, never()).findById(any());
        verify(mailSender, never()).createMimeMessage();
    }

    @Test
    void testSendFacturaEmail_WithEmptyEmail() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> facturaService.sendFacturaEmail(1L, ""));
        verify(facturaRepository, never()).findById(any());
        verify(mailSender, never()).createMimeMessage();
    }

    @Test
    void testSendFacturaEmail_WithWhitespaceEmail() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> facturaService.sendFacturaEmail(1L, "   "));
        verify(facturaRepository, never()).findById(any());
        verify(mailSender, never()).createMimeMessage();
    }

    @Test
    void testSendFacturaEmail_FacturaNotFound() {
        // Arrange
        when(facturaRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> facturaService.sendFacturaEmail(1L, "test@example.com"));
        verify(facturaRepository).findById(1L);
        verify(mailSender, never()).createMimeMessage();
    }

    @Test
    void testSendFacturaEmail_MessagingException() throws Exception {
        // Arrange
        when(facturaRepository.findById(1L)).thenReturn(Optional.of(testFactura));
        when(ventaService.getVentaWithDetails(1L)).thenReturn(testVentaDTO);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        doThrow(new RuntimeException("Test error")).when(mailSender).send(any(MimeMessage.class));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> facturaService.sendFacturaEmail(1L, "test@example.com"));
        verify(facturaRepository, times(2)).findById(1L);
        verify(mailSender).createMimeMessage();
        verify(mailSender).send(any(MimeMessage.class));
    }
}
