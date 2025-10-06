package com.example.pharmacy.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class FacturaDTOTest {

    private FacturaDTO facturaDTO;
    private LocalDateTime testDateTime;
    private VentaDTO testVenta;

    @BeforeEach
    void setUp() {
        facturaDTO = new FacturaDTO();
        testDateTime = LocalDateTime.of(2024, 1, 1, 12, 0, 0);
        
        testVenta = new VentaDTO();
        testVenta.setIdVenta(1L);
        testVenta.setIdUsuario(123L);
        testVenta.setTotal(100.0);
    }

    @Test
    @DisplayName("Test setIdFactura and getIdFactura")
    void testIdFactura() {
        // Arrange
        Long expectedId = 1L;

        // Act
        facturaDTO.setIdFactura(expectedId);
        Long actualId = facturaDTO.getIdFactura();

        // Assert
        assertEquals(expectedId, actualId);
    }

    @Test
    @DisplayName("Test setIdFactura with null")
    void testIdFacturaWithNull() {
        // Act
        facturaDTO.setIdFactura(null);
        Long actualId = facturaDTO.getIdFactura();

        // Assert
        assertNull(actualId);
    }

    @Test
    @DisplayName("Test setIdVenta and getIdVenta")
    void testIdVenta() {
        // Arrange
        Long expectedId = 456L;

        // Act
        facturaDTO.setIdVenta(expectedId);
        Long actualId = facturaDTO.getIdVenta();

        // Assert
        assertEquals(expectedId, actualId);
    }

    @Test
    @DisplayName("Test setIdVenta with null")
    void testIdVentaWithNull() {
        // Act
        facturaDTO.setIdVenta(null);
        Long actualId = facturaDTO.getIdVenta();

        // Assert
        assertNull(actualId);
    }

    @Test
    @DisplayName("Test setVenta and getVenta")
    void testVenta() {
        // Act
        facturaDTO.setVenta(testVenta);
        VentaDTO actualVenta = facturaDTO.getVenta();

        // Assert
        assertEquals(testVenta, actualVenta);
        assertEquals(1L, actualVenta.getIdVenta());
        assertEquals(123L, actualVenta.getIdUsuario());
        assertEquals(100.0, actualVenta.getTotal());
    }

    @Test
    @DisplayName("Test setVenta with null")
    void testVentaWithNull() {
        // Act
        facturaDTO.setVenta(null);
        VentaDTO actualVenta = facturaDTO.getVenta();

        // Assert
        assertNull(actualVenta);
    }

    @Test
    @DisplayName("Test setVenta with new instance")
    void testVentaWithNewInstance() {
        // Arrange
        VentaDTO newVenta = new VentaDTO();
        newVenta.setIdVenta(999L);
        newVenta.setIdUsuario(888L);
        newVenta.setTotal(250.0);

        // Act
        facturaDTO.setVenta(newVenta);
        VentaDTO actualVenta = facturaDTO.getVenta();

        // Assert
        assertEquals(newVenta, actualVenta);
        assertEquals(999L, actualVenta.getIdVenta());
        assertEquals(888L, actualVenta.getIdUsuario());
        assertEquals(250.0, actualVenta.getTotal());
    }

    @Test
    @DisplayName("Test setFechaFactura and getFechaFactura")
    void testFechaFactura() {
        // Act
        facturaDTO.setFechaFactura(testDateTime);
        LocalDateTime actualFecha = facturaDTO.getFechaFactura();

        // Assert
        assertEquals(testDateTime, actualFecha);
    }

    @Test
    @DisplayName("Test setFechaFactura with null")
    void testFechaFacturaWithNull() {
        // Act
        facturaDTO.setFechaFactura(null);
        LocalDateTime actualFecha = facturaDTO.getFechaFactura();

        // Assert
        assertNull(actualFecha);
    }

    @Test
    @DisplayName("Test setTotalFactura and getTotalFactura")
    void testTotalFactura() {
        // Arrange
        Double expectedTotal = 150.75;

        // Act
        facturaDTO.setTotalFactura(expectedTotal);
        Double actualTotal = facturaDTO.getTotalFactura();

        // Assert
        assertEquals(expectedTotal, actualTotal);
    }

    @Test
    @DisplayName("Test setTotalFactura with zero")
    void testTotalFacturaWithZero() {
        // Arrange
        Double expectedTotal = 0.0;

        // Act
        facturaDTO.setTotalFactura(expectedTotal);
        Double actualTotal = facturaDTO.getTotalFactura();

        // Assert
        assertEquals(expectedTotal, actualTotal);
    }

    @Test
    @DisplayName("Test setTotalFactura with null")
    void testTotalFacturaWithNull() {
        // Act
        facturaDTO.setTotalFactura(null);
        Double actualTotal = facturaDTO.getTotalFactura();

        // Assert
        assertNull(actualTotal);
    }

    @Test
    @DisplayName("Test setTotalFactura with negative value")
    void testTotalFacturaWithNegativeValue() {
        // Arrange
        Double expectedTotal = -50.0;

        // Act
        facturaDTO.setTotalFactura(expectedTotal);
        Double actualTotal = facturaDTO.getTotalFactura();

        // Assert
        assertEquals(expectedTotal, actualTotal);
    }

    @Test
    @DisplayName("Test setPdfUrl and getPdfUrl")
    void testPdfUrl() {
        // Arrange
        String expectedUrl = "https://example.com/facturas/factura123.pdf";

        // Act
        facturaDTO.setPdfUrl(expectedUrl);
        String actualUrl = facturaDTO.getPdfUrl();

        // Assert
        assertEquals(expectedUrl, actualUrl);
    }

    @Test
    @DisplayName("Test setPdfUrl with empty string")
    void testPdfUrlWithEmptyString() {
        // Arrange
        String expectedUrl = "";

        // Act
        facturaDTO.setPdfUrl(expectedUrl);
        String actualUrl = facturaDTO.getPdfUrl();

        // Assert
        assertEquals(expectedUrl, actualUrl);
    }

    @Test
    @DisplayName("Test setPdfUrl with null")
    void testPdfUrlWithNull() {
        // Act
        facturaDTO.setPdfUrl(null);
        String actualUrl = facturaDTO.getPdfUrl();

        // Assert
        assertNull(actualUrl);
    }

    @Test
    @DisplayName("Test setPdfUrl with relative path")
    void testPdfUrlWithRelativePath() {
        // Arrange
        String expectedUrl = "/facturas/factura123.pdf";

        // Act
        facturaDTO.setPdfUrl(expectedUrl);
        String actualUrl = facturaDTO.getPdfUrl();

        // Assert
        assertEquals(expectedUrl, actualUrl);
    }

    @Test
    @DisplayName("Test complete factura setup")
    void testCompleteFacturaSetup() {
        // Arrange
        Long idFactura = 1L;
        Long idVenta = 456L;
        LocalDateTime fechaFactura = LocalDateTime.now();
        Double totalFactura = 200.50;
        String pdfUrl = "https://example.com/facturas/factura1.pdf";

        // Act
        facturaDTO.setIdFactura(idFactura);
        facturaDTO.setIdVenta(idVenta);
        facturaDTO.setVenta(testVenta);
        facturaDTO.setFechaFactura(fechaFactura);
        facturaDTO.setTotalFactura(totalFactura);
        facturaDTO.setPdfUrl(pdfUrl);

        // Assert
        assertEquals(idFactura, facturaDTO.getIdFactura());
        assertEquals(idVenta, facturaDTO.getIdVenta());
        assertEquals(testVenta, facturaDTO.getVenta());
        assertEquals(fechaFactura, facturaDTO.getFechaFactura());
        assertEquals(totalFactura, facturaDTO.getTotalFactura());
        assertEquals(pdfUrl, facturaDTO.getPdfUrl());
    }

    @Test
    @DisplayName("Test edge case with very long strings")
    void testEdgeCaseWithVeryLongStrings() {
        // Arrange
        String veryLongUrl = "https://example.com/facturas/" + "A".repeat(1000) + ".pdf";

        // Act
        facturaDTO.setPdfUrl(veryLongUrl);

        // Assert
        assertEquals(veryLongUrl, facturaDTO.getPdfUrl());
    }

    @Test
    @DisplayName("Test edge case with special characters in URL")
    void testEdgeCaseWithSpecialCharacters() {
        // Arrange
        String specialUrl = "https://example.com/facturas/factura!@#$%^&*()_+-=[]{}|;':\",./<>?.pdf";

        // Act
        facturaDTO.setPdfUrl(specialUrl);

        // Assert
        assertEquals(specialUrl, facturaDTO.getPdfUrl());
    }

    @Test
    @DisplayName("Test edge case with very large numbers")
    void testEdgeCaseWithVeryLargeNumbers() {
        // Arrange
        Long largeId = Long.MAX_VALUE;
        Double largeTotal = Double.MAX_VALUE;

        // Act
        facturaDTO.setIdFactura(largeId);
        facturaDTO.setTotalFactura(largeTotal);

        // Assert
        assertEquals(largeId, facturaDTO.getIdFactura());
        assertEquals(largeTotal, facturaDTO.getTotalFactura());
    }

    @Test
    @DisplayName("Test edge case with very small numbers")
    void testEdgeCaseWithVerySmallNumbers() {
        // Arrange
        Long smallId = Long.MIN_VALUE;
        Double smallTotal = Double.MIN_VALUE;

        // Act
        facturaDTO.setIdFactura(smallId);
        facturaDTO.setTotalFactura(smallTotal);

        // Assert
        assertEquals(smallId, facturaDTO.getIdFactura());
        assertEquals(smallTotal, facturaDTO.getTotalFactura());
    }

    @Test
    @DisplayName("Test edge case with decimal precision")
    void testEdgeCaseWithDecimalPrecision() {
        // Arrange
        Double preciseTotal = 123.456789;

        // Act
        facturaDTO.setTotalFactura(preciseTotal);

        // Assert
        assertEquals(preciseTotal, facturaDTO.getTotalFactura());
    }

    @Test
    @DisplayName("Test edge case with different date formats")
    void testEdgeCaseWithDifferentDateFormats() {
        // Arrange
        LocalDateTime pastDate = LocalDateTime.of(1900, 1, 1, 0, 0, 0);
        LocalDateTime futureDate = LocalDateTime.of(2100, 12, 31, 23, 59, 59);

        // Act
        facturaDTO.setFechaFactura(pastDate);
        LocalDateTime actualPastDate = facturaDTO.getFechaFactura();
        facturaDTO.setFechaFactura(futureDate);
        LocalDateTime actualFutureDate = facturaDTO.getFechaFactura();

        // Assert
        assertEquals(pastDate, actualPastDate);
        assertEquals(futureDate, actualFutureDate);
    }

    @Test
    @DisplayName("Test edge case with different URL protocols")
    void testEdgeCaseWithDifferentUrlProtocols() {
        // Arrange
        String[] urls = {
            "http://example.com/factura.pdf",
            "https://example.com/factura.pdf",
            "ftp://example.com/factura.pdf",
            "file:///local/path/factura.pdf"
        };

        // Act & Assert
        for (String url : urls) {
            facturaDTO.setPdfUrl(url);
            assertEquals(url, facturaDTO.getPdfUrl());
        }
    }

    @Test
    @DisplayName("Test edge case with venta modification")
    void testEdgeCaseWithVentaModification() {
        // Arrange
        facturaDTO.setVenta(testVenta);

        // Act - Modify the venta
        VentaDTO venta = facturaDTO.getVenta();
        venta.setTotal(999.99);

        // Assert
        assertEquals(999.99, facturaDTO.getVenta().getTotal());
    }

    @Test
    @DisplayName("Test edge case with multiple venta assignments")
    void testEdgeCaseWithMultipleVentaAssignments() {
        // Arrange
        VentaDTO venta1 = new VentaDTO();
        venta1.setIdVenta(1L);
        venta1.setTotal(100.0);

        VentaDTO venta2 = new VentaDTO();
        venta2.setIdVenta(2L);
        venta2.setTotal(200.0);

        // Act & Assert
        facturaDTO.setVenta(venta1);
        assertEquals(1L, facturaDTO.getVenta().getIdVenta());
        assertEquals(100.0, facturaDTO.getVenta().getTotal());

        facturaDTO.setVenta(venta2);
        assertEquals(2L, facturaDTO.getVenta().getIdVenta());
        assertEquals(200.0, facturaDTO.getVenta().getTotal());
    }
}

