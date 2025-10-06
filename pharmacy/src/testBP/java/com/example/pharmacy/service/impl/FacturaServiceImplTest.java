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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FacturaServiceImplTest {

    @Mock
    private FacturaRepository facturaRepository;
    @Mock
    private VentaService ventaService;
    @Mock
    private AuditoriaService auditoriaService;

    @Spy
    @InjectMocks
    private FacturaServiceImpl service;

    @BeforeEach
    void setUp() {
        // Inyectar valor de fromEmail
        ReflectionTestUtils.setField(service, "fromEmail", "from@test.com");
    }

    @Test
    @DisplayName("createFactura: éxito crea y retorna DTO con ID asignado")
    void createFactura_success() {
        // Prevenir generación real de PDF
        doReturn(new ByteArrayOutputStream()).when(service).generatePdfFactura(anyLong());

        // Simular guardado del repositorio
        when(facturaRepository.save(any(Factura.class))).thenAnswer(inv -> {
            Factura f = inv.getArgument(0);
            f.setIdFactura(123L);
            return f;
        });

        Venta venta = new Venta();
        venta.setIdVenta(45L);
        venta.setMontoPagado(500.0);
        venta.setIdUsuario(7L);

        FacturaDTO dto = service.createFactura(venta);

        assertNotNull(dto);
        assertEquals(123L, dto.getIdFactura());
        assertEquals(45L, dto.getIdVenta());
        assertEquals(500.0, dto.getTotalFactura());
    }

    @Test
    @DisplayName("getFactura: existe retorna DTO")
    void getFactura_exists() {
        Factura f = new Factura();
        f.setIdFactura(10L);
        f.setIdVenta(20L);
        f.setTotalFactura(100.0);
        when(facturaRepository.findById(10L)).thenReturn(Optional.of(f));

        FacturaDTO dto = service.getFactura(10L);
        assertEquals(10L, dto.getIdFactura());
        assertEquals(20L, dto.getIdVenta());
    }

    @Test
    @DisplayName("getFactura: no existe lanza NoSuchElementException")
    void getFactura_notFound() {
        when(facturaRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> service.getFactura(99L));
    }

    @Test
    @DisplayName("getFacturaByVenta: existe retorna DTO")
    void getFacturaByVenta_exists() {
        Factura f = new Factura();
        f.setIdFactura(11L);
        f.setIdVenta(21L);
        when(facturaRepository.findByIdVenta(21L)).thenReturn(Optional.of(f));

        FacturaDTO dto = service.getFacturaByVenta(21L);
        assertEquals(11L, dto.getIdFactura());
        assertEquals(21L, dto.getIdVenta());
    }

    @Test
    @DisplayName("getFacturaByVenta: no existe lanza NoSuchElementException")
    void getFacturaByVenta_notFound() {
        when(facturaRepository.findByIdVenta(30L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> service.getFacturaByVenta(30L));
    }

    @Test
    @DisplayName("generatePdfFactura: retorna stream con contenido")
    void generatePdfFactura_success() throws Exception {
        Factura f = new Factura();
        f.setIdFactura(50L);
        f.setIdVenta(40L);
        f.setFechaFactura(LocalDateTime.now());
        when(facturaRepository.findById(50L)).thenReturn(Optional.of(f));

        VentaDTO vdto = new VentaDTO();
        vdto.setIdVenta(40L);
        vdto.setTotal(100.0);
        vdto.setImpuesto(12.0);
        vdto.setDescuento(0.0);
        vdto.setMontoPagado(112.0);
        // Añadir un detalle para evitar NPE
        VentaDetalleDTO det = new VentaDetalleDTO();
        det.setIdMedicamento(1L);
        det.setCantidad(2);
        det.setPrecioUnitario(5.0);
        det.setTotalLinea(10.0);
        vdto.setDetalles(List.of(det));
        when(ventaService.getVentaWithDetails(40L)).thenReturn(vdto);

        ByteArrayOutputStream baos = service.generatePdfFactura(50L);
        assertNotNull(baos);
        assertTrue(baos.size() > 0);
    }

}
