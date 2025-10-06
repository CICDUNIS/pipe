package com.example.pharmacy.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CarritoDTOTest {

    private CarritoDTO carritoDTO;
    private LocalDateTime testDateTime;
    private CarritoDetalleDTO testItem;

    @BeforeEach
    void setUp() {
        carritoDTO = new CarritoDTO();
        testDateTime = LocalDateTime.of(2024, 1, 1, 12, 0, 0);
        
        testItem = new CarritoDetalleDTO();
        testItem.setIdCartItem(1L);
        testItem.setIdMedicamento(1L);
        testItem.setCantidad(2);
        testItem.setPrecioUnitario(10.0);
    }

    @Test
    @DisplayName("Test setIdCart and getIdCart")
    void testIdCart() {
        // Arrange
        Long expectedId = 1L;

        // Act
        carritoDTO.setIdCart(expectedId);
        Long actualId = carritoDTO.getIdCart();

        // Assert
        assertEquals(expectedId, actualId);
    }

    @Test
    @DisplayName("Test setIdCart with null")
    void testIdCartWithNull() {
        // Act
        carritoDTO.setIdCart(null);
        Long actualId = carritoDTO.getIdCart();

        // Assert
        assertNull(actualId);
    }

    @Test
    @DisplayName("Test setIdUsuario and getIdUsuario")
    void testIdUsuario() {
        // Arrange
        Long expectedId = 123L;

        // Act
        carritoDTO.setIdUsuario(expectedId);
        Long actualId = carritoDTO.getIdUsuario();

        // Assert
        assertEquals(expectedId, actualId);
    }

    @Test
    @DisplayName("Test setIdUsuario with null")
    void testIdUsuarioWithNull() {
        // Act
        carritoDTO.setIdUsuario(null);
        Long actualId = carritoDTO.getIdUsuario();

        // Assert
        assertNull(actualId);
    }

    @Test
    @DisplayName("Test setStatus and getStatus")
    void testStatus() {
        // Arrange
        String expectedStatus = "ACTIVO";

        // Act
        carritoDTO.setStatus(expectedStatus);
        String actualStatus = carritoDTO.getStatus();

        // Assert
        assertEquals(expectedStatus, actualStatus);
    }

    @Test
    @DisplayName("Test setStatus with empty string")
    void testStatusWithEmptyString() {
        // Arrange
        String expectedStatus = "";

        // Act
        carritoDTO.setStatus(expectedStatus);
        String actualStatus = carritoDTO.getStatus();

        // Assert
        assertEquals(expectedStatus, actualStatus);
    }

    @Test
    @DisplayName("Test setStatus with null")
    void testStatusWithNull() {
        // Act
        carritoDTO.setStatus(null);
        String actualStatus = carritoDTO.getStatus();

        // Assert
        assertNull(actualStatus);
    }

    @Test
    @DisplayName("Test setStatus with different values")
    void testStatusWithDifferentValues() {
        // Test different status values
        String[] statuses = {"ACTIVO", "COMPLETADO", "CANCELADO", "PENDIENTE"};
        
        for (String status : statuses) {
            carritoDTO.setStatus(status);
            assertEquals(status, carritoDTO.getStatus());
        }
    }

    @Test
    @DisplayName("Test setFechaCreacion and getFechaCreacion")
    void testFechaCreacion() {
        // Act
        carritoDTO.setFechaCreacion(testDateTime);
        LocalDateTime actualFecha = carritoDTO.getFechaCreacion();

        // Assert
        assertEquals(testDateTime, actualFecha);
    }

    @Test
    @DisplayName("Test setFechaCreacion with null")
    void testFechaCreacionWithNull() {
        // Act
        carritoDTO.setFechaCreacion(null);
        LocalDateTime actualFecha = carritoDTO.getFechaCreacion();

        // Assert
        assertNull(actualFecha);
    }

    @Test
    @DisplayName("Test setFechaActualizacion and getFechaActualizacion")
    void testFechaActualizacion() {
        // Act
        carritoDTO.setFechaActualizacion(testDateTime);
        LocalDateTime actualFecha = carritoDTO.getFechaActualizacion();

        // Assert
        assertEquals(testDateTime, actualFecha);
    }

    @Test
    @DisplayName("Test setFechaActualizacion with null")
    void testFechaActualizacionWithNull() {
        // Act
        carritoDTO.setFechaActualizacion(null);
        LocalDateTime actualFecha = carritoDTO.getFechaActualizacion();

        // Assert
        assertNull(actualFecha);
    }

    @Test
    @DisplayName("Test setItems and getItems")
    void testItems() {
        // Arrange
        List<CarritoDetalleDTO> expectedItems = Arrays.asList(testItem);

        // Act
        carritoDTO.setItems(expectedItems);
        List<CarritoDetalleDTO> actualItems = carritoDTO.getItems();

        // Assert
        assertEquals(expectedItems, actualItems);
        assertEquals(1, actualItems.size());
        assertEquals(testItem, actualItems.get(0));
    }

    @Test
    @DisplayName("Test setItems with empty list")
    void testItemsWithEmptyList() {
        // Arrange
        List<CarritoDetalleDTO> expectedItems = new ArrayList<>();

        // Act
        carritoDTO.setItems(expectedItems);
        List<CarritoDetalleDTO> actualItems = carritoDTO.getItems();

        // Assert
        assertEquals(expectedItems, actualItems);
        assertEquals(0, actualItems.size());
    }

    @Test
    @DisplayName("Test setItems with null")
    void testItemsWithNull() {
        // Act
        carritoDTO.setItems(null);
        List<CarritoDetalleDTO> actualItems = carritoDTO.getItems();

        // Assert
        assertNull(actualItems);
    }

    @Test
    @DisplayName("Test setItems with multiple items")
    void testItemsWithMultipleItems() {
        // Arrange
        CarritoDetalleDTO item2 = new CarritoDetalleDTO();
        item2.setIdCartItem(2L);
        item2.setIdMedicamento(2L);
        item2.setCantidad(1);
        item2.setPrecioUnitario(15.0);

        List<CarritoDetalleDTO> expectedItems = Arrays.asList(testItem, item2);

        // Act
        carritoDTO.setItems(expectedItems);
        List<CarritoDetalleDTO> actualItems = carritoDTO.getItems();

        // Assert
        assertEquals(expectedItems, actualItems);
        assertEquals(2, actualItems.size());
        assertEquals(testItem, actualItems.get(0));
        assertEquals(item2, actualItems.get(1));
    }

    @Test
    @DisplayName("Test setTotal and getTotal")
    void testTotal() {
        // Arrange
        Double expectedTotal = 25.50;

        // Act
        carritoDTO.setTotal(expectedTotal);
        Double actualTotal = carritoDTO.getTotal();

        // Assert
        assertEquals(expectedTotal, actualTotal);
    }

    @Test
    @DisplayName("Test setTotal with zero")
    void testTotalWithZero() {
        // Arrange
        Double expectedTotal = 0.0;

        // Act
        carritoDTO.setTotal(expectedTotal);
        Double actualTotal = carritoDTO.getTotal();

        // Assert
        assertEquals(expectedTotal, actualTotal);
    }

    @Test
    @DisplayName("Test setTotal with null")
    void testTotalWithNull() {
        // Act
        carritoDTO.setTotal(null);
        Double actualTotal = carritoDTO.getTotal();

        // Assert
        assertNull(actualTotal);
    }

    @Test
    @DisplayName("Test setTotal with negative value")
    void testTotalWithNegativeValue() {
        // Arrange
        Double expectedTotal = -10.0;

        // Act
        carritoDTO.setTotal(expectedTotal);
        Double actualTotal = carritoDTO.getTotal();

        // Assert
        assertEquals(expectedTotal, actualTotal);
    }

    @Test
    @DisplayName("Test complete carrito setup")
    void testCompleteCarritoSetup() {
        // Arrange
        Long idCart = 1L;
        Long idUsuario = 123L;
        String status = "ACTIVO";
        LocalDateTime fechaCreacion = LocalDateTime.now();
        LocalDateTime fechaActualizacion = LocalDateTime.now();
        List<CarritoDetalleDTO> items = Arrays.asList(testItem);
        Double total = 20.0;

        // Act
        carritoDTO.setIdCart(idCart);
        carritoDTO.setIdUsuario(idUsuario);
        carritoDTO.setStatus(status);
        carritoDTO.setFechaCreacion(fechaCreacion);
        carritoDTO.setFechaActualizacion(fechaActualizacion);
        carritoDTO.setItems(items);
        carritoDTO.setTotal(total);

        // Assert
        assertEquals(idCart, carritoDTO.getIdCart());
        assertEquals(idUsuario, carritoDTO.getIdUsuario());
        assertEquals(status, carritoDTO.getStatus());
        assertEquals(fechaCreacion, carritoDTO.getFechaCreacion());
        assertEquals(fechaActualizacion, carritoDTO.getFechaActualizacion());
        assertEquals(items, carritoDTO.getItems());
        assertEquals(total, carritoDTO.getTotal());
    }

    @Test
    @DisplayName("Test items list modification")
    void testItemsListModification() {
        // Arrange
        List<CarritoDetalleDTO> initialItems = new ArrayList<>(Arrays.asList(testItem));
        carritoDTO.setItems(initialItems);

        // Act - Modify the list
        List<CarritoDetalleDTO> items = carritoDTO.getItems();
        CarritoDetalleDTO newItem = new CarritoDetalleDTO();
        newItem.setIdCartItem(3L);
        newItem.setIdMedicamento(3L);
        newItem.setCantidad(1);
        newItem.setPrecioUnitario(5.0);
        items.add(newItem);

        // Assert
        assertEquals(2, carritoDTO.getItems().size());
        assertTrue(carritoDTO.getItems().contains(testItem));
        assertTrue(carritoDTO.getItems().contains(newItem));
    }

    @Test
    @DisplayName("Test edge case with very long strings")
    void testEdgeCaseWithVeryLongStrings() {
        // Arrange
        String veryLongString = "A".repeat(1000);

        // Act
        carritoDTO.setStatus(veryLongString);

        // Assert
        assertEquals(veryLongString, carritoDTO.getStatus());
    }

    @Test
    @DisplayName("Test edge case with special characters in status")
    void testEdgeCaseWithSpecialCharacters() {
        // Arrange
        String specialString = "!@#$%^&*()_+-=[]{}|;':\",./<>?";

        // Act
        carritoDTO.setStatus(specialString);

        // Assert
        assertEquals(specialString, carritoDTO.getStatus());
    }

    @Test
    @DisplayName("Test edge case with very large numbers")
    void testEdgeCaseWithVeryLargeNumbers() {
        // Arrange
        Long largeId = Long.MAX_VALUE;
        Double largeTotal = Double.MAX_VALUE;

        // Act
        carritoDTO.setIdCart(largeId);
        carritoDTO.setTotal(largeTotal);

        // Assert
        assertEquals(largeId, carritoDTO.getIdCart());
        assertEquals(largeTotal, carritoDTO.getTotal());
    }

    @Test
    @DisplayName("Test edge case with very small numbers")
    void testEdgeCaseWithVerySmallNumbers() {
        // Arrange
        Long smallId = Long.MIN_VALUE;
        Double smallTotal = Double.MIN_VALUE;

        // Act
        carritoDTO.setIdCart(smallId);
        carritoDTO.setTotal(smallTotal);

        // Assert
        assertEquals(smallId, carritoDTO.getIdCart());
        assertEquals(smallTotal, carritoDTO.getTotal());
    }

    @Test
    @DisplayName("Test edge case with decimal precision")
    void testEdgeCaseWithDecimalPrecision() {
        // Arrange
        Double preciseTotal = 123.456789;

        // Act
        carritoDTO.setTotal(preciseTotal);

        // Assert
        assertEquals(preciseTotal, carritoDTO.getTotal());
    }

    @Test
    @DisplayName("Test edge case with different date formats")
    void testEdgeCaseWithDifferentDateFormats() {
        // Arrange
        LocalDateTime pastDate = LocalDateTime.of(1900, 1, 1, 0, 0, 0);
        LocalDateTime futureDate = LocalDateTime.of(2100, 12, 31, 23, 59, 59);

        // Act
        carritoDTO.setFechaCreacion(pastDate);
        carritoDTO.setFechaActualizacion(futureDate);

        // Assert
        assertEquals(pastDate, carritoDTO.getFechaCreacion());
        assertEquals(futureDate, carritoDTO.getFechaActualizacion());
    }
}
