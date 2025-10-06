package com.example.pharmacy.controllers;

import com.example.pharmacy.dto.AutorizacionMedicamentoDTO;
import com.example.pharmacy.dto.CoberturaMedicamentoDTO;
import com.example.pharmacy.integration.SeguroMedicamentosClient;
import com.example.pharmacy.integration.SeguroReporteVentasClient;
import com.example.pharmacy.integration.SeguroClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SeguroIntegrationControllerTest {

    @Mock
    private SeguroMedicamentosClient medicamentosClient;

    @Mock
    private SeguroReporteVentasClient reporteVentasClient;

    @Mock
    private SeguroClient seguroClient;

    @InjectMocks
    private SeguroIntegrationController seguroIntegrationController;

    private Map<String, Object> testPayload;
    private AutorizacionMedicamentoDTO testAutorizacion;
    private CoberturaMedicamentoDTO testCobertura;
    private Map<String, Object> testReporteResponse;

    @BeforeEach
    void setUp() {
        testPayload = new HashMap<>();
        testPayload.put("codigoReceta", "REC-001");
        testPayload.put("numeroAfiliacion", "AFI-123");
        testPayload.put("costoTotal", 100.0);

        testAutorizacion = new AutorizacionMedicamentoDTO();
        testAutorizacion.setAutorizacion("AUT-001");
        testAutorizacion.setMontoAutorizado(80.0);
        testAutorizacion.setCopago(20.0);
        testAutorizacion.setEstado("APROBADO");
        testAutorizacion.setMensaje("Autorización aprobada");

        testCobertura = new CoberturaMedicamentoDTO();
        testCobertura.setCubierto(true);
        testCobertura.setMontoAutorizado(40.0);
        testCobertura.setCopago(10.0);
        testCobertura.setMensaje("Medicamento cubierto");

        testReporteResponse = new HashMap<>();
        testReporteResponse.put("success", true);
        testReporteResponse.put("message", "Reporte enviado correctamente");
    }

    @Test
    void testAutorizarMedicamento_Success() {
        // Arrange
        when(medicamentosClient.autorizarMedicamento("REC-001", "AFI-123", 100.0))
                .thenReturn(testAutorizacion);

        // Act
        AutorizacionMedicamentoDTO response = seguroIntegrationController.autorizarMedicamento(testPayload);

        // Assert
        assertNotNull(response);
        assertEquals(testAutorizacion, response);
        assertEquals("AUT-001", response.getAutorizacion());
        assertEquals(80.0, response.getMontoAutorizado());
        assertEquals(20.0, response.getCopago());
        verify(medicamentosClient).autorizarMedicamento("REC-001", "AFI-123", 100.0);
    }

    @Test
    void testAutorizarMedicamento_WithIntegerCostoTotal() {
        // Arrange
        Map<String, Object> payloadWithInteger = new HashMap<>();
        payloadWithInteger.put("codigoReceta", "REC-002");
        payloadWithInteger.put("numeroAfiliacion", "AFI-456");
        payloadWithInteger.put("costoTotal", 150);

        when(medicamentosClient.autorizarMedicamento("REC-002", "AFI-456", 150.0))
                .thenReturn(testAutorizacion);

        // Act
        AutorizacionMedicamentoDTO response = seguroIntegrationController.autorizarMedicamento(payloadWithInteger);

        // Assert
        assertNotNull(response);
        assertEquals(testAutorizacion, response);
        verify(medicamentosClient).autorizarMedicamento("REC-002", "AFI-456", 150.0);
    }

    @Test
    void testAutorizarMedicamento_WithStringCostoTotal() {
        // Arrange
        Map<String, Object> payloadWithString = new HashMap<>();
        payloadWithString.put("codigoReceta", "REC-003");
        payloadWithString.put("numeroAfiliacion", "AFI-789");
        payloadWithString.put("costoTotal", "200.50");

        when(medicamentosClient.autorizarMedicamento("REC-003", "AFI-789", 200.50))
                .thenReturn(testAutorizacion);

        // Act
        AutorizacionMedicamentoDTO response = seguroIntegrationController.autorizarMedicamento(payloadWithString);

        // Assert
        assertNotNull(response);
        assertEquals(testAutorizacion, response);
        verify(medicamentosClient).autorizarMedicamento("REC-003", "AFI-789", 200.50);
    }

    @Test
    void testAutorizarMedicamento_WithNullCostoTotal() {
        // Arrange
        Map<String, Object> payloadWithNull = new HashMap<>();
        payloadWithNull.put("codigoReceta", "REC-004");
        payloadWithNull.put("numeroAfiliacion", "AFI-101");
        payloadWithNull.put("costoTotal", null);

        when(medicamentosClient.autorizarMedicamento("REC-004", "AFI-101", null))
                .thenReturn(testAutorizacion);

        // Act
        AutorizacionMedicamentoDTO response = seguroIntegrationController.autorizarMedicamento(payloadWithNull);

        // Assert
        assertNotNull(response);
        assertEquals(testAutorizacion, response);
        verify(medicamentosClient).autorizarMedicamento("REC-004", "AFI-101", null);
    }

    @Test
    void testAutorizarMedicamento_WithMissingCostoTotal() {
        // Arrange
        Map<String, Object> payloadMissing = new HashMap<>();
        payloadMissing.put("codigoReceta", "REC-005");
        payloadMissing.put("numeroAfiliacion", "AFI-202");

        when(medicamentosClient.autorizarMedicamento("REC-005", "AFI-202", null))
                .thenReturn(testAutorizacion);

        // Act
        AutorizacionMedicamentoDTO response = seguroIntegrationController.autorizarMedicamento(payloadMissing);

        // Assert
        assertNotNull(response);
        assertEquals(testAutorizacion, response);
        verify(medicamentosClient).autorizarMedicamento("REC-005", "AFI-202", null);
    }

    @Test
    void testReporteVentas_Success() {
        // Arrange
        Map<String, Object> reportePayload = new HashMap<>();
        reportePayload.put("codigoReceta", "REC-006");
        reportePayload.put("numeroAfiliacion", "AFI-303");
        reportePayload.put("montoTotal", 150.0);
        
        List<Map<String, Object>> detalleMedicamentos = Arrays.asList(
            Map.of("codigoMedicamento", "MED-001", "cantidad", 2, "subtotal", 80.0),
            Map.of("codigoMedicamento", "MED-002", "cantidad", 1, "subtotal", 70.0)
        );
        reportePayload.put("detalleMedicamentos", detalleMedicamentos);

        when(reporteVentasClient.reporteVentas(reportePayload)).thenReturn(testReporteResponse);

        // Act
        Map<String, Object> response = seguroIntegrationController.reporteVentas(reportePayload);

        // Assert
        assertNotNull(response);
        assertEquals(testReporteResponse, response);
        assertTrue((Boolean) response.get("success"));
        assertEquals("Reporte enviado correctamente", response.get("message"));
        verify(reporteVentasClient).reporteVentas(reportePayload);
    }

    @Test
    void testReporteVentas_EmptyPayload() {
        // Arrange
        Map<String, Object> emptyPayload = new HashMap<>();
        when(reporteVentasClient.reporteVentas(emptyPayload)).thenReturn(testReporteResponse);

        // Act
        Map<String, Object> response = seguroIntegrationController.reporteVentas(emptyPayload);

        // Assert
        assertNotNull(response);
        assertEquals(testReporteResponse, response);
        verify(reporteVentasClient).reporteVentas(emptyPayload);
    }

    @Test
    void testValidarMedicamento_Success() {
        // Arrange
        String numeroAfiliacion = "AFI-404";
        String codigoMedicamento = "MED-003";
        
        when(seguroClient.validarMedicamento(numeroAfiliacion, codigoMedicamento))
                .thenReturn(testCobertura);

        // Act
        CoberturaMedicamentoDTO response = seguroIntegrationController.validarMedicamento(numeroAfiliacion, codigoMedicamento);

        // Assert
        assertNotNull(response);
        assertEquals(testCobertura, response);
        assertTrue(response.isCubierto());
        assertEquals(40.0, response.getMontoAutorizado());
        assertEquals(10.0, response.getCopago());
        verify(seguroClient).validarMedicamento(numeroAfiliacion, codigoMedicamento);
    }

    @Test
    void testValidarMedicamento_WithSpecialCharacters() {
        // Arrange
        String numeroAfiliacion = "AFI-505@";
        String codigoMedicamento = "MED-004#";
        
        when(seguroClient.validarMedicamento(numeroAfiliacion, codigoMedicamento))
                .thenReturn(testCobertura);

        // Act
        CoberturaMedicamentoDTO response = seguroIntegrationController.validarMedicamento(numeroAfiliacion, codigoMedicamento);

        // Assert
        assertNotNull(response);
        assertEquals(testCobertura, response);
        verify(seguroClient).validarMedicamento(numeroAfiliacion, codigoMedicamento);
    }

    @Test
    void testValidarMedicamento_WithEmptyStrings() {
        // Arrange
        String numeroAfiliacion = "";
        String codigoMedicamento = "";
        
        when(seguroClient.validarMedicamento(numeroAfiliacion, codigoMedicamento))
                .thenReturn(testCobertura);

        // Act
        CoberturaMedicamentoDTO response = seguroIntegrationController.validarMedicamento(numeroAfiliacion, codigoMedicamento);

        // Assert
        assertNotNull(response);
        assertEquals(testCobertura, response);
        verify(seguroClient).validarMedicamento(numeroAfiliacion, codigoMedicamento);
    }

    @Test
    void testValidarMedicamento_WithNullValues() {
        // Arrange
        String numeroAfiliacion = null;
        String codigoMedicamento = null;
        
        when(seguroClient.validarMedicamento(numeroAfiliacion, codigoMedicamento))
                .thenReturn(testCobertura);

        // Act
        CoberturaMedicamentoDTO response = seguroIntegrationController.validarMedicamento(numeroAfiliacion, codigoMedicamento);

        // Assert
        assertNotNull(response);
        assertEquals(testCobertura, response);
        verify(seguroClient).validarMedicamento(numeroAfiliacion, codigoMedicamento);
    }

    @Test
    void testAutorizarMedicamento_WithComplexPayload() {
        // Arrange
        Map<String, Object> complexPayload = new HashMap<>();
        complexPayload.put("codigoReceta", "REC-COMPLEX-001");
        complexPayload.put("numeroAfiliacion", "AFI-COMPLEX-123");
        complexPayload.put("costoTotal", 999.99);
        complexPayload.put("fechaReceta", "2024-01-15");
        complexPayload.put("medico", "Dr. García");
        complexPayload.put("diagnostico", "Dolor de cabeza");

        when(medicamentosClient.autorizarMedicamento("REC-COMPLEX-001", "AFI-COMPLEX-123", 999.99))
                .thenReturn(testAutorizacion);

        // Act
        AutorizacionMedicamentoDTO response = seguroIntegrationController.autorizarMedicamento(complexPayload);

        // Assert
        assertNotNull(response);
        assertEquals(testAutorizacion, response);
        verify(medicamentosClient).autorizarMedicamento("REC-COMPLEX-001", "AFI-COMPLEX-123", 999.99);
    }

    @Test
    void testReporteVentas_WithComplexDetalle() {
        // Arrange
        Map<String, Object> complexReportePayload = new HashMap<>();
        complexReportePayload.put("codigoReceta", "REC-COMPLEX-002");
        complexReportePayload.put("numeroAfiliacion", "AFI-COMPLEX-456");
        complexReportePayload.put("montoTotal", 500.0);
        complexReportePayload.put("fechaVenta", "2024-01-15T10:30:00");
        complexReportePayload.put("farmacia", "Farmacia Central");
        
        List<Map<String, Object>> complexDetalle = Arrays.asList(
            Map.of("codigoMedicamento", "MED-COMPLEX-001", "cantidad", 3, "subtotal", 150.0, "precioUnitario", 50.0),
            Map.of("codigoMedicamento", "MED-COMPLEX-002", "cantidad", 2, "subtotal", 200.0, "precioUnitario", 100.0),
            Map.of("codigoMedicamento", "MED-COMPLEX-003", "cantidad", 1, "subtotal", 150.0, "precioUnitario", 150.0)
        );
        complexReportePayload.put("detalleMedicamentos", complexDetalle);

        when(reporteVentasClient.reporteVentas(complexReportePayload)).thenReturn(testReporteResponse);

        // Act
        Map<String, Object> response = seguroIntegrationController.reporteVentas(complexReportePayload);

        // Assert
        assertNotNull(response);
        assertEquals(testReporteResponse, response);
        verify(reporteVentasClient).reporteVentas(complexReportePayload);
    }
}

