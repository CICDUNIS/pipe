package com.example.pharmacy.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RecetaDTOTest {

    private RecetaDTO recetaDTO;
    private LocalDateTime testDateTime;
    private RecetaDetalleDTO testDetalle;

    @BeforeEach
    void setUp() {
        recetaDTO = new RecetaDTO();
        testDateTime = LocalDateTime.of(2024, 1, 1, 12, 0, 0);
        
        testDetalle = new RecetaDetalleDTO();
        testDetalle.setIdDetalle(1L);
        testDetalle.setIdMedicamento(1L);
        testDetalle.setCantidadRequerida(2);
        testDetalle.setDosis("1 tableta cada 8 horas");
    }

    @Test
    @DisplayName("Test setIdReceta and getIdReceta")
    void testIdReceta() {
        // Arrange
        Long expectedId = 1L;

        // Act
        recetaDTO.setIdReceta(expectedId);
        Long actualId = recetaDTO.getIdReceta();

        // Assert
        assertEquals(expectedId, actualId);
    }

    @Test
    @DisplayName("Test setIdReceta with null")
    void testIdRecetaWithNull() {
        // Act
        recetaDTO.setIdReceta(null);
        Long actualId = recetaDTO.getIdReceta();

        // Assert
        assertNull(actualId);
    }

    @Test
    @DisplayName("Test setCodigoReceta and getCodigoReceta")
    void testCodigoReceta() {
        // Arrange
        String expectedCodigo = "REC001";

        // Act
        recetaDTO.setCodigoReceta(expectedCodigo);
        String actualCodigo = recetaDTO.getCodigoReceta();

        // Assert
        assertEquals(expectedCodigo, actualCodigo);
    }

    @Test
    @DisplayName("Test setCodigoReceta with empty string")
    void testCodigoRecetaWithEmptyString() {
        // Arrange
        String expectedCodigo = "";

        // Act
        recetaDTO.setCodigoReceta(expectedCodigo);
        String actualCodigo = recetaDTO.getCodigoReceta();

        // Assert
        assertEquals(expectedCodigo, actualCodigo);
    }

    @Test
    @DisplayName("Test setCodigoReceta with null")
    void testCodigoRecetaWithNull() {
        // Act
        recetaDTO.setCodigoReceta(null);
        String actualCodigo = recetaDTO.getCodigoReceta();

        // Assert
        assertNull(actualCodigo);
    }

    @Test
    @DisplayName("Test setCodigoReceta with different values")
    void testCodigoRecetaWithDifferentValues() {
        // Test different codigo values
        String[] codigos = {"REC001", "REC002", "REC999", "REC-2024-001"};
        
        for (String codigo : codigos) {
            recetaDTO.setCodigoReceta(codigo);
            assertEquals(codigo, recetaDTO.getCodigoReceta());
        }
    }

    @Test
    @DisplayName("Test setFecha and getFecha")
    void testFecha() {
        // Act
        recetaDTO.setFecha(testDateTime);
        LocalDateTime actualFecha = recetaDTO.getFecha();

        // Assert
        assertEquals(testDateTime, actualFecha);
    }

    @Test
    @DisplayName("Test setFecha with null")
    void testFechaWithNull() {
        // Act
        recetaDTO.setFecha(null);
        LocalDateTime actualFecha = recetaDTO.getFecha();

        // Assert
        assertNull(actualFecha);
    }

    @Test
    @DisplayName("Test setIdUsuario and getIdUsuario")
    void testIdUsuario() {
        // Arrange
        Long expectedId = 123L;

        // Act
        recetaDTO.setIdUsuario(expectedId);
        Long actualId = recetaDTO.getIdUsuario();

        // Assert
        assertEquals(expectedId, actualId);
    }

    @Test
    @DisplayName("Test setIdUsuario with null")
    void testIdUsuarioWithNull() {
        // Act
        recetaDTO.setIdUsuario(null);
        Long actualId = recetaDTO.getIdUsuario();

        // Assert
        assertNull(actualId);
    }

    @Test
    @DisplayName("Test setAprobadoSeguro and getAprobadoSeguro")
    void testAprobadoSeguro() {
        // Arrange
        String expectedAprobado = "SI";

        // Act
        recetaDTO.setAprobadoSeguro(expectedAprobado);
        String actualAprobado = recetaDTO.getAprobadoSeguro();

        // Assert
        assertEquals(expectedAprobado, actualAprobado);
    }

    @Test
    @DisplayName("Test setAprobadoSeguro with different values")
    void testAprobadoSeguroWithDifferentValues() {
        // Test different aprobado values
        String[] aprobados = {"SI", "NO", "PENDIENTE", "EN_REVISION"};
        
        for (String aprobado : aprobados) {
            recetaDTO.setAprobadoSeguro(aprobado);
            assertEquals(aprobado, recetaDTO.getAprobadoSeguro());
        }
    }

    @Test
    @DisplayName("Test setAprobadoSeguro with null")
    void testAprobadoSeguroWithNull() {
        // Act
        recetaDTO.setAprobadoSeguro(null);
        String actualAprobado = recetaDTO.getAprobadoSeguro();

        // Assert
        assertNull(actualAprobado);
    }

    @Test
    @DisplayName("Test setAprobadoSeguro with empty string")
    void testAprobadoSeguroWithEmptyString() {
        // Arrange
        String expectedAprobado = "";

        // Act
        recetaDTO.setAprobadoSeguro(expectedAprobado);
        String actualAprobado = recetaDTO.getAprobadoSeguro();

        // Assert
        assertEquals(expectedAprobado, actualAprobado);
    }

    @Test
    @DisplayName("Test setPdfUrl and getPdfUrl")
    void testPdfUrl() {
        // Arrange
        String expectedUrl = "https://example.com/recetas/receta123.pdf";

        // Act
        recetaDTO.setPdfUrl(expectedUrl);
        String actualUrl = recetaDTO.getPdfUrl();

        // Assert
        assertEquals(expectedUrl, actualUrl);
    }

    @Test
    @DisplayName("Test setPdfUrl with empty string")
    void testPdfUrlWithEmptyString() {
        // Arrange
        String expectedUrl = "";

        // Act
        recetaDTO.setPdfUrl(expectedUrl);
        String actualUrl = recetaDTO.getPdfUrl();

        // Assert
        assertEquals(expectedUrl, actualUrl);
    }

    @Test
    @DisplayName("Test setPdfUrl with null")
    void testPdfUrlWithNull() {
        // Act
        recetaDTO.setPdfUrl(null);
        String actualUrl = recetaDTO.getPdfUrl();

        // Assert
        assertNull(actualUrl);
    }

    @Test
    @DisplayName("Test setPdfUrl with relative path")
    void testPdfUrlWithRelativePath() {
        // Arrange
        String expectedUrl = "/recetas/receta123.pdf";

        // Act
        recetaDTO.setPdfUrl(expectedUrl);
        String actualUrl = recetaDTO.getPdfUrl();

        // Assert
        assertEquals(expectedUrl, actualUrl);
    }

    @Test
    @DisplayName("Test setDetalles and getDetalles")
    void testDetalles() {
        // Arrange
        List<RecetaDetalleDTO> expectedDetalles = Arrays.asList(testDetalle);

        // Act
        recetaDTO.setDetalles(expectedDetalles);
        List<RecetaDetalleDTO> actualDetalles = recetaDTO.getDetalles();

        // Assert
        assertEquals(expectedDetalles, actualDetalles);
        assertEquals(1, actualDetalles.size());
        assertEquals(testDetalle, actualDetalles.get(0));
    }

    @Test
    @DisplayName("Test setDetalles with empty list")
    void testDetallesWithEmptyList() {
        // Arrange
        List<RecetaDetalleDTO> expectedDetalles = new ArrayList<>();

        // Act
        recetaDTO.setDetalles(expectedDetalles);
        List<RecetaDetalleDTO> actualDetalles = recetaDTO.getDetalles();

        // Assert
        assertEquals(expectedDetalles, actualDetalles);
        assertEquals(0, actualDetalles.size());
    }

    @Test
    @DisplayName("Test setDetalles with null")
    void testDetallesWithNull() {
        // Act
        recetaDTO.setDetalles(null);
        List<RecetaDetalleDTO> actualDetalles = recetaDTO.getDetalles();

        // Assert
        assertNull(actualDetalles);
    }

    @Test
    @DisplayName("Test setDetalles with multiple detalles")
    void testDetallesWithMultipleDetalles() {
        // Arrange
        RecetaDetalleDTO detalle2 = new RecetaDetalleDTO();
        detalle2.setIdDetalle(1L);
        detalle2.setIdMedicamento(2L);
        detalle2.setCantidadRequerida(1);
        detalle2.setDosis("1 cápsula cada 12 horas");

        List<RecetaDetalleDTO> expectedDetalles = Arrays.asList(testDetalle, detalle2);

        // Act
        recetaDTO.setDetalles(expectedDetalles);
        List<RecetaDetalleDTO> actualDetalles = recetaDTO.getDetalles();

        // Assert
        assertEquals(expectedDetalles, actualDetalles);
        assertEquals(2, actualDetalles.size());
        assertEquals(testDetalle, actualDetalles.get(0));
        assertEquals(detalle2, actualDetalles.get(1));
    }

    @Test
    @DisplayName("Test complete receta setup")
    void testCompleteRecetaSetup() {
        // Arrange
        Long idReceta = 1L;
        String codigoReceta = "REC001";
        LocalDateTime fecha = LocalDateTime.now();
        Long idUsuario = 123L;
        String aprobadoSeguro = "SI";
        String pdfUrl = "https://example.com/recetas/receta1.pdf";
        List<RecetaDetalleDTO> detalles = Arrays.asList(testDetalle);

        // Act
        recetaDTO.setIdReceta(idReceta);
        recetaDTO.setCodigoReceta(codigoReceta);
        recetaDTO.setFecha(fecha);
        recetaDTO.setIdUsuario(idUsuario);
        recetaDTO.setAprobadoSeguro(aprobadoSeguro);
        recetaDTO.setPdfUrl(pdfUrl);
        recetaDTO.setDetalles(detalles);

        // Assert
        assertEquals(idReceta, recetaDTO.getIdReceta());
        assertEquals(codigoReceta, recetaDTO.getCodigoReceta());
        assertEquals(fecha, recetaDTO.getFecha());
        assertEquals(idUsuario, recetaDTO.getIdUsuario());
        assertEquals(aprobadoSeguro, recetaDTO.getAprobadoSeguro());
        assertEquals(pdfUrl, recetaDTO.getPdfUrl());
        assertEquals(detalles, recetaDTO.getDetalles());
    }

    @Test
    @DisplayName("Test detalles list modification")
    void testDetallesListModification() {
        // Arrange
        List<RecetaDetalleDTO> initialDetalles = new ArrayList<>(Arrays.asList(testDetalle));
        recetaDTO.setDetalles(initialDetalles);

        // Act - Modify the list
        List<RecetaDetalleDTO> detalles = recetaDTO.getDetalles();
        RecetaDetalleDTO newDetalle = new RecetaDetalleDTO();
        newDetalle.setIdDetalle(1L);
        newDetalle.setIdMedicamento(3L);
        newDetalle.setCantidadRequerida(1);
        newDetalle.setDosis("1 tableta cada 24 horas");
        detalles.add(newDetalle);

        // Assert
        assertEquals(2, recetaDTO.getDetalles().size());
        assertTrue(recetaDTO.getDetalles().contains(testDetalle));
        assertTrue(recetaDTO.getDetalles().contains(newDetalle));
    }

    @Test
    @DisplayName("Test edge case with very long strings")
    void testEdgeCaseWithVeryLongStrings() {
        // Arrange
        String veryLongCodigo = "REC" + "A".repeat(1000);
        String veryLongUrl = "https://example.com/recetas/" + "A".repeat(1000) + ".pdf";

        // Act
        recetaDTO.setCodigoReceta(veryLongCodigo);
        recetaDTO.setPdfUrl(veryLongUrl);

        // Assert
        assertEquals(veryLongCodigo, recetaDTO.getCodigoReceta());
        assertEquals(veryLongUrl, recetaDTO.getPdfUrl());
    }

    @Test
    @DisplayName("Test edge case with special characters")
    void testEdgeCaseWithSpecialCharacters() {
        // Arrange
        String specialCodigo = "REC!@#$%^&*()_+-=[]{}|;':\",./<>?";
        String specialUrl = "https://example.com/recetas/receta!@#$%^&*()_+-=[]{}|;':\",./<>?.pdf";

        // Act
        recetaDTO.setCodigoReceta(specialCodigo);
        recetaDTO.setPdfUrl(specialUrl);

        // Assert
        assertEquals(specialCodigo, recetaDTO.getCodigoReceta());
        assertEquals(specialUrl, recetaDTO.getPdfUrl());
    }

    @Test
    @DisplayName("Test edge case with very large numbers")
    void testEdgeCaseWithVeryLargeNumbers() {
        // Arrange
        Long largeId = Long.MAX_VALUE;

        // Act
        recetaDTO.setIdReceta(largeId);
        recetaDTO.setIdUsuario(largeId);

        // Assert
        assertEquals(largeId, recetaDTO.getIdReceta());
        assertEquals(largeId, recetaDTO.getIdUsuario());
    }

    @Test
    @DisplayName("Test edge case with very small numbers")
    void testEdgeCaseWithVerySmallNumbers() {
        // Arrange
        Long smallId = Long.MIN_VALUE;

        // Act
        recetaDTO.setIdReceta(smallId);
        recetaDTO.setIdUsuario(smallId);

        // Assert
        assertEquals(smallId, recetaDTO.getIdReceta());
        assertEquals(smallId, recetaDTO.getIdUsuario());
    }

    @Test
    @DisplayName("Test edge case with different date formats")
    void testEdgeCaseWithDifferentDateFormats() {
        // Arrange
        LocalDateTime pastDate = LocalDateTime.of(1900, 1, 1, 0, 0, 0);
        LocalDateTime futureDate = LocalDateTime.of(2100, 12, 31, 23, 59, 59);

        // Act
        recetaDTO.setFecha(pastDate);
        LocalDateTime actualPastDate = recetaDTO.getFecha();
        recetaDTO.setFecha(futureDate);
        LocalDateTime actualFutureDate = recetaDTO.getFecha();

        // Assert
        assertEquals(pastDate, actualPastDate);
        assertEquals(futureDate, actualFutureDate);
    }

    @Test
    @DisplayName("Test edge case with different URL protocols")
    void testEdgeCaseWithDifferentUrlProtocols() {
        // Arrange
        String[] urls = {
            "http://example.com/receta.pdf",
            "https://example.com/receta.pdf",
            "ftp://example.com/receta.pdf",
            "file:///local/path/receta.pdf"
        };

        // Act & Assert
        for (String url : urls) {
            recetaDTO.setPdfUrl(url);
            assertEquals(url, recetaDTO.getPdfUrl());
        }
    }

    @Test
    @DisplayName("Test edge case with detalles modification")
    void testEdgeCaseWithDetallesModification() {
        // Arrange
        recetaDTO.setDetalles(Arrays.asList(testDetalle));

        // Act - Modify the detalles
        List<RecetaDetalleDTO> detalles = recetaDTO.getDetalles();
        detalles.get(0).setCantidadRequerida(999);

        // Assert
        assertEquals(999, recetaDTO.getDetalles().get(0).getCantidadRequerida());
    }

    @Test
    @DisplayName("Test edge case with multiple detalles assignments")
    void testEdgeCaseWithMultipleDetallesAssignments() {
        // Arrange
        List<RecetaDetalleDTO> detalles1 = Arrays.asList(testDetalle);
        
        RecetaDetalleDTO detalle2 = new RecetaDetalleDTO();
        detalle2.setIdDetalle(2L);
        detalle2.setIdMedicamento(2L);
        detalle2.setCantidadRequerida(1);
        detalle2.setDosis("1 tableta cada 6 horas");
        List<RecetaDetalleDTO> detalles2 = Arrays.asList(detalle2);

        // Act & Assert
        recetaDTO.setDetalles(detalles1);
        assertEquals(1, recetaDTO.getDetalles().size());
        assertEquals(testDetalle, recetaDTO.getDetalles().get(0));

        recetaDTO.setDetalles(detalles2);
        assertEquals(1, recetaDTO.getDetalles().size());
        assertEquals(detalle2, recetaDTO.getDetalles().get(0));
    }

    @Test
    @DisplayName("Test edge case with null values in all fields")
    void testEdgeCaseWithNullValuesInAllFields() {
        // Act
        recetaDTO.setIdReceta(null);
        recetaDTO.setCodigoReceta(null);
        recetaDTO.setFecha(null);
        recetaDTO.setIdUsuario(null);
        recetaDTO.setAprobadoSeguro(null);
        recetaDTO.setPdfUrl(null);
        recetaDTO.setDetalles(null);

        // Assert
        assertNull(recetaDTO.getIdReceta());
        assertNull(recetaDTO.getCodigoReceta());
        assertNull(recetaDTO.getFecha());
        assertNull(recetaDTO.getIdUsuario());
        assertNull(recetaDTO.getAprobadoSeguro());
        assertNull(recetaDTO.getPdfUrl());
        assertNull(recetaDTO.getDetalles());
    }
}
