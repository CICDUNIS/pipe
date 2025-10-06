package com.example.pharmacy.config;

import com.example.pharmacy.dto.VentaDTO;
import com.example.pharmacy.dto.VentaDetalleDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JdbcConfigurationTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    private VentaDTO testVentaDTO;
    private VentaDetalleDTO testVentaDetalleDTO;

    @BeforeEach
    void setUp() {
        testVentaDTO = new VentaDTO();
        testVentaDTO.setIdUsuario(1L);
        testVentaDTO.setIdReceta(1L);
        testVentaDTO.setTotal(100.0);
        testVentaDTO.setImpuesto(16.0);
        testVentaDTO.setDescuento(10.0);
        testVentaDTO.setMontoPagado(106.0);

        testVentaDetalleDTO = new VentaDetalleDTO();
        testVentaDetalleDTO.setIdMedicamento(1L);
        testVentaDetalleDTO.setCantidad(2);
        testVentaDetalleDTO.setPrecioUnitario(50.0);
        testVentaDetalleDTO.setTotalLinea(100.0);
    }

    @Test
    void testJdbcConfigurationCreation() {
        assertNotNull(new JdbcConfiguration());
    }

    @Test
    void testUpdateCartStatus_Success() {
        // Arrange
        Long cartId = 1L;
        String status = "ACTIVE";
        when(jdbcTemplate.update(anyString(), eq(status), eq(cartId))).thenReturn(1);

        // Act
        boolean result = JdbcConfiguration.updateCartStatus(jdbcTemplate, cartId, status);

        // Assert
        assertTrue(result);
        verify(jdbcTemplate).update(anyString(), eq(status), eq(cartId));
    }

    @Test
    void testUpdateCartStatus_NoRowsUpdated() {
        // Arrange
        Long cartId = 1L;
        String status = "INACTIVE";
        when(jdbcTemplate.update(anyString(), eq(status), eq(cartId))).thenReturn(0);

        // Act
        boolean result = JdbcConfiguration.updateCartStatus(jdbcTemplate, cartId, status);

        // Assert
        assertFalse(result);
        verify(jdbcTemplate).update(anyString(), eq(status), eq(cartId));
    }

    @Test
    void testUpdateCartStatus_Exception() {
        // Arrange
        Long cartId = 1L;
        String status = "ERROR";
        when(jdbcTemplate.update(anyString(), eq(status), eq(cartId)))
            .thenThrow(new RuntimeException("Database error"));

        // Act
        boolean result = JdbcConfiguration.updateCartStatus(jdbcTemplate, cartId, status);

        // Assert
        assertFalse(result);
        verify(jdbcTemplate).update(anyString(), eq(status), eq(cartId));
    }

    @Test
    void testUpdateCartStatus_WithNullCartId() {
        // Arrange
        Long cartId = null;
        String status = "ACTIVE";
        when(jdbcTemplate.update(anyString(), eq(status), eq(cartId)))
            .thenThrow(new IllegalArgumentException("Cart ID cannot be null"));

        // Act
        boolean result = JdbcConfiguration.updateCartStatus(jdbcTemplate, cartId, status);

        // Assert
        // El método tiene try-catch y retorna false en caso de error
        assertFalse(result);
        verify(jdbcTemplate).update(anyString(), eq(status), eq(cartId));
    }

    @Test
    void testUpdateCartStatus_WithNullStatus() {
        // Arrange
        Long cartId = 1L;
        String status = null;
        when(jdbcTemplate.update(anyString(), eq(status), eq(cartId)))
            .thenThrow(new IllegalArgumentException("Status cannot be null"));

        // Act
        boolean result = JdbcConfiguration.updateCartStatus(jdbcTemplate, cartId, status);

        // Assert
        // El método tiene try-catch y retorna false en caso de error
        assertFalse(result);
        verify(jdbcTemplate).update(anyString(), eq(status), eq(cartId));
    }

    @Test
    void testUpdateCartStatus_WithEmptyStatus() {
        // Arrange
        Long cartId = 1L;
        String status = "";
        when(jdbcTemplate.update(anyString(), eq(status), eq(cartId))).thenReturn(1);

        // Act
        boolean result = JdbcConfiguration.updateCartStatus(jdbcTemplate, cartId, status);

        // Assert
        assertTrue(result);
        verify(jdbcTemplate).update(anyString(), eq(status), eq(cartId));
    }

    @Test
    void testUpdateCartStatus_WithWhitespaceStatus() {
        // Arrange
        Long cartId = 1L;
        String status = "   ";
        when(jdbcTemplate.update(anyString(), eq(status), eq(cartId))).thenReturn(1);

        // Act
        boolean result = JdbcConfiguration.updateCartStatus(jdbcTemplate, cartId, status);

        // Assert
        assertTrue(result);
        verify(jdbcTemplate).update(anyString(), eq(status), eq(cartId));
    }

    @Test
    void testUpdateCartStatus_WithZeroCartId() {
        // Arrange
        Long cartId = 0L;
        String status = "ACTIVE";
        when(jdbcTemplate.update(anyString(), eq(status), eq(cartId))).thenReturn(1);

        // Act
        boolean result = JdbcConfiguration.updateCartStatus(jdbcTemplate, cartId, status);

        // Assert
        assertTrue(result);
        verify(jdbcTemplate).update(anyString(), eq(status), eq(cartId));
    }

    @Test
    void testUpdateCartStatus_WithNegativeCartId() {
        // Arrange
        Long cartId = -1L;
        String status = "ACTIVE";
        when(jdbcTemplate.update(anyString(), eq(status), eq(cartId))).thenReturn(1);

        // Act
        boolean result = JdbcConfiguration.updateCartStatus(jdbcTemplate, cartId, status);

        // Assert
        assertTrue(result);
        verify(jdbcTemplate).update(anyString(), eq(status), eq(cartId));
    }

    @Test
    void testInsertVentaJdbc_Success() {
        // Arrange
        when(jdbcTemplate.queryForObject("SELECT SEQ_VENTA.NEXTVAL FROM DUAL", Long.class))
            .thenReturn(1L);
        when(jdbcTemplate.update(anyString(), any(), any(), any(), any(), any(), any(), any(), any()))
            .thenReturn(1);

        // Act
        Map<String, Object> result = JdbcConfiguration.insertVentaJdbc(jdbcTemplate, testVentaDTO);

        // Assert
        assertNotNull(result);
        assertTrue((Boolean) result.get("success"));
        assertEquals(1L, result.get("idVenta"));
        verify(jdbcTemplate).queryForObject("SELECT SEQ_VENTA.NEXTVAL FROM DUAL", Long.class);
        verify(jdbcTemplate).update(anyString(), any(), any(), any(), any(), any(), any(), any(), any());
    }

    @Test
    void testInsertVentaJdbc_WithNullValues() {
        // Arrange
        VentaDTO ventaWithNulls = new VentaDTO();
        ventaWithNulls.setIdUsuario(1L);
        ventaWithNulls.setIdReceta(1L);
        // Los demás campos son null por defecto

        when(jdbcTemplate.queryForObject("SELECT SEQ_VENTA.NEXTVAL FROM DUAL", Long.class))
            .thenReturn(1L);
        when(jdbcTemplate.update(anyString(), any(), any(), any(), any(), any(), any(), any(), any()))
            .thenReturn(1);

        // Act
        Map<String, Object> result = JdbcConfiguration.insertVentaJdbc(jdbcTemplate, ventaWithNulls);

        // Assert
        assertNotNull(result);
        assertTrue((Boolean) result.get("success"));
        assertEquals(1L, result.get("idVenta"));
    }

    @Test
    void testInsertVentaJdbc_Exception() {
        // Arrange
        when(jdbcTemplate.queryForObject("SELECT SEQ_VENTA.NEXTVAL FROM DUAL", Long.class))
            .thenThrow(new RuntimeException("Database error"));

        // Act
        Map<String, Object> result = JdbcConfiguration.insertVentaJdbc(jdbcTemplate, testVentaDTO);

        // Assert
        assertNotNull(result);
        assertFalse((Boolean) result.get("success"));
        assertEquals("Database error", result.get("error"));
    }

    @Test
    void testInsertVentaJdbc_WithNullVentaDTO() {
        // Arrange
        when(jdbcTemplate.queryForObject("SELECT SEQ_VENTA.NEXTVAL FROM DUAL", Long.class))
            .thenReturn(1L);

        // Act
        Map<String, Object> result = JdbcConfiguration.insertVentaJdbc(jdbcTemplate, null);

        // Assert
        assertNotNull(result);
        // El método lanza NullPointerException al intentar acceder a propiedades del DTO nulo
        // pero el try-catch lo captura y retorna success: false
        assertFalse((Boolean) result.get("success"));
        assertNotNull(result.get("error"));
    }

    @Test
    void testInsertVentaDetalleJdbc_Success() {
        // Arrange
        Long idVenta = 1L;
        when(jdbcTemplate.queryForObject("SELECT SEQ_VENTA_DETALLE.NEXTVAL FROM DUAL", Long.class))
            .thenReturn(1L);
        when(jdbcTemplate.update(anyString(), any(), any(), any(), any(), any(), any()))
            .thenReturn(1);

        // Act
        Map<String, Object> result = JdbcConfiguration.insertVentaDetalleJdbc(
            jdbcTemplate, idVenta, testVentaDetalleDTO);

        // Assert
        assertNotNull(result);
        assertTrue((Boolean) result.get("success"));
        assertEquals(1L, result.get("idVentaDetalle"));
        verify(jdbcTemplate).queryForObject("SELECT SEQ_VENTA_DETALLE.NEXTVAL FROM DUAL", Long.class);
        verify(jdbcTemplate).update(anyString(), any(), any(), any(), any(), any(), any());
    }

    @Test
    void testInsertVentaDetalleJdbc_WithNullValues() {
        // Arrange
        Long idVenta = 1L;
        VentaDetalleDTO detalleWithNulls = new VentaDetalleDTO();
        detalleWithNulls.setIdMedicamento(1L);
        // Los demás campos son null por defecto

        when(jdbcTemplate.queryForObject("SELECT SEQ_VENTA_DETALLE.NEXTVAL FROM DUAL", Long.class))
            .thenReturn(1L);
        when(jdbcTemplate.update(anyString(), any(), any(), any(), any(), any(), any()))
            .thenReturn(1);

        // Act
        Map<String, Object> result = JdbcConfiguration.insertVentaDetalleJdbc(
            jdbcTemplate, idVenta, detalleWithNulls);

        // Assert
        assertNotNull(result);
        assertTrue((Boolean) result.get("success"));
        assertEquals(1L, result.get("idVentaDetalle"));
    }

    @Test
    void testInsertVentaDetalleJdbc_Exception() {
        // Arrange
        Long idVenta = 1L;
        when(jdbcTemplate.queryForObject("SELECT SEQ_VENTA_DETALLE.NEXTVAL FROM DUAL", Long.class))
            .thenThrow(new RuntimeException("Database error"));

        // Act
        Map<String, Object> result = JdbcConfiguration.insertVentaDetalleJdbc(
            jdbcTemplate, idVenta, testVentaDetalleDTO);

        // Assert
        assertNotNull(result);
        assertFalse((Boolean) result.get("success"));
        assertEquals("Database error", result.get("error"));
    }

    @Test
    void testInsertVentaDetalleJdbc_WithNullIdVenta() {
        // Arrange
        Long idVenta = null;
        when(jdbcTemplate.queryForObject("SELECT SEQ_VENTA_DETALLE.NEXTVAL FROM DUAL", Long.class))
            .thenReturn(1L);
        when(jdbcTemplate.update(anyString(), any(), any(), any(), any(), any(), any()))
            .thenReturn(1);

        // Act
        Map<String, Object> result = JdbcConfiguration.insertVentaDetalleJdbc(
            jdbcTemplate, idVenta, testVentaDetalleDTO);

        // Assert
        assertNotNull(result);
        assertTrue((Boolean) result.get("success"));
        assertEquals(1L, result.get("idVentaDetalle"));
    }

    @Test
    void testInsertVentaDetalleJdbc_WithNullDetalleDTO() {
        // Arrange
        Long idVenta = 1L;
        when(jdbcTemplate.queryForObject("SELECT SEQ_VENTA_DETALLE.NEXTVAL FROM DUAL", Long.class))
            .thenReturn(1L);

        // Act
        Map<String, Object> result = JdbcConfiguration.insertVentaDetalleJdbc(
            jdbcTemplate, idVenta, null);

        // Assert
        assertNotNull(result);
        // El método lanza NullPointerException al intentar acceder a propiedades del DTO nulo
        // pero el try-catch lo captura y retorna success: false
        assertFalse((Boolean) result.get("success"));
        assertNotNull(result.get("error"));
    }

    @Test
    void testInsertVentaJdbc_WithZeroValues() {
        // Arrange
        VentaDTO ventaWithZeros = new VentaDTO();
        ventaWithZeros.setIdUsuario(1L);
        ventaWithZeros.setIdReceta(1L);
        ventaWithZeros.setTotal(0.0);
        ventaWithZeros.setImpuesto(0.0);
        ventaWithZeros.setDescuento(0.0);
        ventaWithZeros.setMontoPagado(0.0);

        when(jdbcTemplate.queryForObject("SELECT SEQ_VENTA.NEXTVAL FROM DUAL", Long.class))
            .thenReturn(1L);
        when(jdbcTemplate.update(anyString(), any(), any(), any(), any(), any(), any(), any(), any()))
            .thenReturn(1);

        // Act
        Map<String, Object> result = JdbcConfiguration.insertVentaJdbc(jdbcTemplate, ventaWithZeros);

        // Assert
        assertNotNull(result);
        assertTrue((Boolean) result.get("success"));
        assertEquals(1L, result.get("idVenta"));
    }

    @Test
    void testInsertVentaJdbc_WithNegativeValues() {
        // Arrange
        VentaDTO ventaWithNegatives = new VentaDTO();
        ventaWithNegatives.setIdUsuario(1L);
        ventaWithNegatives.setIdReceta(1L);
        ventaWithNegatives.setTotal(-100.0);
        ventaWithNegatives.setImpuesto(-16.0);
        ventaWithNegatives.setDescuento(-10.0);
        ventaWithNegatives.setMontoPagado(-106.0);

        when(jdbcTemplate.queryForObject("SELECT SEQ_VENTA.NEXTVAL FROM DUAL", Long.class))
            .thenReturn(1L);
        when(jdbcTemplate.update(anyString(), any(), any(), any(), any(), any(), any(), any(), any()))
            .thenReturn(1);

        // Act
        Map<String, Object> result = JdbcConfiguration.insertVentaJdbc(jdbcTemplate, ventaWithNegatives);

        // Assert
        assertNotNull(result);
        assertTrue((Boolean) result.get("success"));
        assertEquals(1L, result.get("idVenta"));
    }

    @Test
    void testInsertVentaDetalleJdbc_WithZeroValues() {
        // Arrange
        Long idVenta = 1L;
        VentaDetalleDTO detalleWithZeros = new VentaDetalleDTO();
        detalleWithZeros.setIdMedicamento(1L);
        detalleWithZeros.setCantidad(0);
        detalleWithZeros.setPrecioUnitario(0.0);
        detalleWithZeros.setTotalLinea(0.0);

        when(jdbcTemplate.queryForObject("SELECT SEQ_VENTA_DETALLE.NEXTVAL FROM DUAL", Long.class))
            .thenReturn(1L);
        when(jdbcTemplate.update(anyString(), any(), any(), any(), any(), any(), any()))
            .thenReturn(1);

        // Act
        Map<String, Object> result = JdbcConfiguration.insertVentaDetalleJdbc(
            jdbcTemplate, idVenta, detalleWithZeros);

        // Assert
        assertNotNull(result);
        assertTrue((Boolean) result.get("success"));
        assertEquals(1L, result.get("idVentaDetalle"));
    }

    @Test
    void testInsertVentaDetalleJdbc_WithNegativeValues() {
        // Arrange
        Long idVenta = 1L;
        VentaDetalleDTO detalleWithNegatives = new VentaDetalleDTO();
        detalleWithNegatives.setIdMedicamento(1L);
        detalleWithNegatives.setCantidad(-2);
        detalleWithNegatives.setPrecioUnitario(-50.0);
        detalleWithNegatives.setTotalLinea(-100.0);

        when(jdbcTemplate.queryForObject("SELECT SEQ_VENTA_DETALLE.NEXTVAL FROM DUAL", Long.class))
            .thenReturn(1L);
        when(jdbcTemplate.update(anyString(), any(), any(), any(), any(), any(), any()))
            .thenReturn(1);

        // Act
        Map<String, Object> result = JdbcConfiguration.insertVentaDetalleJdbc(
            jdbcTemplate, idVenta, detalleWithNegatives);

        // Assert
        assertNotNull(result);
        assertTrue((Boolean) result.get("success"));
        assertEquals(1L, result.get("idVentaDetalle"));
    }
}
