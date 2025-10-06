package com.example.pharmacy.service.impl;

import com.example.pharmacy.dto.CarritoDTO;
import com.example.pharmacy.dto.CarritoDetalleDTO;
import com.example.pharmacy.dto.FacturaDTO;
import com.example.pharmacy.dto.VentaDTO;
import com.example.pharmacy.model.Carrito;
import com.example.pharmacy.model.CarritoDetalle;
import com.example.pharmacy.model.Medicamento;
import com.example.pharmacy.model.Venta;
import com.example.pharmacy.model.VentaDetalle;
import com.example.pharmacy.repository.CarritoRepository;
import com.example.pharmacy.repository.CarritoDetalleRepository;
import com.example.pharmacy.repository.MedicamentoRepository;
import com.example.pharmacy.repository.VentaRepository;
import com.example.pharmacy.repository.VentaDetalleRepository;
import com.example.pharmacy.service.AuditoriaService;
import com.example.pharmacy.service.FacturaService;
import com.example.pharmacy.service.VentaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarritoServiceImplTest {

    @Mock
    private CarritoRepository carritoRepository;

    @Mock
    private CarritoDetalleRepository detalleRepository;

    @Mock
    private MedicamentoRepository medicamentoRepository;

    @Mock
    private VentaRepository ventaRepository;

    @Mock
    private VentaDetalleRepository ventaDetalleRepository;

    @Mock
    private AuditoriaService auditoriaService;

    @Mock
    private VentaService ventaService;

    @Mock
    private FacturaService facturaService;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private CarritoServiceImpl carritoService;

    private Carrito testCarrito;
    private CarritoDetalle testCarritoDetalle;
    private Medicamento testMedicamento;
    private Venta testVenta;
    private CarritoDTO testCarritoDTO;
    private CarritoDetalleDTO testCarritoDetalleDTO;

    @BeforeEach
    void setUp() {
        testCarrito = new Carrito();
        testCarrito.setIdCart(1L);
        testCarrito.setIdUsuario(1L);
        testCarrito.setStatus("A");
        testCarrito.setFechaCreacion(LocalDateTime.now());
        testCarrito.setFechaActualizacion(LocalDateTime.now());

        testCarritoDetalle = new CarritoDetalle();
        testCarritoDetalle.setIdCartItem(1L);
        testCarritoDetalle.setIdCart(1L);
        testCarritoDetalle.setIdMedicamento(1L);
        testCarritoDetalle.setCantidad(2);
        testCarritoDetalle.setPrecioUnitario(10.0);

        testMedicamento = new Medicamento();
        testMedicamento.setIdMedicamento(1L);
        testMedicamento.setNombre("Test Medicine");
        testMedicamento.setPrecio(10.0);
        testMedicamento.setStock(100);
        testMedicamento.setRequiereReceta("N");

        testVenta = new Venta();
        testVenta.setIdVenta(1L);
        testVenta.setIdUsuario(1L);
        testVenta.setFechaVenta(LocalDateTime.now());
        testVenta.setTotal(20.0);
        testVenta.setMontoPagado(20.0);

        testCarritoDetalleDTO = new CarritoDetalleDTO();
        testCarritoDetalleDTO.setIdCartItem(1L);
        testCarritoDetalleDTO.setIdMedicamento(1L);
        testCarritoDetalleDTO.setNombreMedicamento("Test Medicine");
        testCarritoDetalleDTO.setCantidad(2);
        testCarritoDetalleDTO.setPrecioUnitario(10.0);
        testCarritoDetalleDTO.setTotal(20.0);
        testCarritoDetalleDTO.setRequiereReceta("N");

        testCarritoDTO = new CarritoDTO();
        testCarritoDTO.setIdCart(1L);
        testCarritoDTO.setIdUsuario(1L);
        testCarritoDTO.setStatus("A");
        testCarritoDTO.setFechaCreacion(LocalDateTime.now());
        testCarritoDTO.setFechaActualizacion(LocalDateTime.now());
        testCarritoDTO.setItems(Arrays.asList(testCarritoDetalleDTO));
        testCarritoDTO.setTotal(20.0);
    }

    @Test
    void testGetActiveCart_Success() {
        // Arrange
        when(carritoRepository.findAllByIdUsuarioAndStatus(1L, "A"))
                .thenReturn(Arrays.asList(testCarrito));
        when(detalleRepository.findByIdCart(1L))
                .thenReturn(Arrays.asList(testCarritoDetalle));
        when(medicamentoRepository.findById(1L))
                .thenReturn(Optional.of(testMedicamento));

        // Act
        CarritoDTO result = carritoService.getActiveCart(1L);

        // Assert
        assertNotNull(result);
        assertEquals(testCarrito.getIdCart(), result.getIdCart());
        assertEquals(testCarrito.getIdUsuario(), result.getIdUsuario());
        verify(carritoRepository).findAllByIdUsuarioAndStatus(1L, "A");
    }

    @Test
    void testGetActiveCart_CreateNew() {
        // Arrange
        when(carritoRepository.findAllByIdUsuarioAndStatus(1L, "A"))
                .thenReturn(Arrays.asList());
        when(carritoRepository.save(any(Carrito.class)))
                .thenReturn(testCarrito);
        when(detalleRepository.findByIdCart(1L))
                .thenReturn(Arrays.asList());

        // Act
        CarritoDTO result = carritoService.getActiveCart(1L);

        // Assert
        assertNotNull(result);
        verify(carritoRepository).save(any(Carrito.class));
    }

    @Test
    void testAddItem_Success() {
        // Arrange
        when(medicamentoRepository.findById(1L))
                .thenReturn(Optional.of(testMedicamento));
        when(carritoRepository.findAllByIdUsuarioAndStatus(1L, "A"))
                .thenReturn(Arrays.asList(testCarrito));
        when(detalleRepository.findByIdCartAndIdMedicamento(1L, 1L))
                .thenReturn(Optional.empty());
        when(detalleRepository.save(any(CarritoDetalle.class)))
                .thenReturn(testCarritoDetalle);
        when(detalleRepository.findByIdCart(1L))
                .thenReturn(Arrays.asList(testCarritoDetalle));

        // Act
        CarritoDTO result = carritoService.addItem(1L, 1L, 2);

        // Assert
        assertNotNull(result);
        verify(medicamentoRepository, times(2)).findById(1L);
        verify(detalleRepository).save(any(CarritoDetalle.class));
        verify(auditoriaService).registrar(eq("Carrito"), eq("UPDATE"), anyString(), anyString());
    }

    @Test
    void testAddItem_MedicamentoNotFound() {
        // Arrange
        when(medicamentoRepository.findById(1L))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> carritoService.addItem(1L, 1L, 2));
        verify(medicamentoRepository).findById(1L);
        verify(detalleRepository, never()).save(any());
    }

    @Test
    void testAddItem_RequiereReceta() {
        // Arrange
        testMedicamento.setRequiereReceta("Y");
        when(medicamentoRepository.findById(1L))
                .thenReturn(Optional.of(testMedicamento));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> carritoService.addItem(1L, 1L, 2));
        verify(medicamentoRepository).findById(1L);
        verify(detalleRepository, never()).save(any());
    }

    @Test
    void testAddItem_StockInsuficiente() {
        // Arrange
        when(medicamentoRepository.findById(1L))
                .thenReturn(Optional.of(testMedicamento));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> carritoService.addItem(1L, 1L, 200));
        verify(medicamentoRepository).findById(1L);
        verify(detalleRepository, never()).save(any());
    }

    @Test
    void testUpdateItemQuantity_Success() {
        // Arrange
        when(carritoRepository.findAllByIdUsuarioAndStatus(1L, "A"))
                .thenReturn(Arrays.asList(testCarrito));
        when(detalleRepository.findByIdCartAndIdMedicamento(1L, 1L))
                .thenReturn(Optional.of(testCarritoDetalle));
        when(medicamentoRepository.findById(1L))
                .thenReturn(Optional.of(testMedicamento));
        when(detalleRepository.save(any(CarritoDetalle.class)))
                .thenReturn(testCarritoDetalle);
        when(detalleRepository.findByIdCart(1L))
                .thenReturn(Arrays.asList(testCarritoDetalle));

        // Act
        CarritoDTO result = carritoService.updateItemQuantity(1L, 1L, 3);

        // Assert
        assertNotNull(result);
        verify(detalleRepository).save(any(CarritoDetalle.class));
        verify(auditoriaService).registrar(eq("Carrito"), eq("UPDATE"), anyString(), anyString());
    }

    @Test
    void testUpdateItemQuantity_RemoveItem() {
        // Arrange
        when(carritoRepository.findAllByIdUsuarioAndStatus(1L, "A"))
                .thenReturn(Arrays.asList(testCarrito));
        when(detalleRepository.findByIdCartAndIdMedicamento(1L, 1L))
                .thenReturn(Optional.of(testCarritoDetalle));
        when(detalleRepository.findByIdCart(1L))
                .thenReturn(Arrays.asList());

        // Act
        CarritoDTO result = carritoService.updateItemQuantity(1L, 1L, 0);

        // Assert
        assertNotNull(result);
        verify(detalleRepository).delete(testCarritoDetalle);
    }

    @Test
    void testUpdateItemQuantity_ItemNotFound() {
        // Arrange
        when(carritoRepository.findAllByIdUsuarioAndStatus(1L, "A"))
                .thenReturn(Arrays.asList(testCarrito));
        when(detalleRepository.findByIdCartAndIdMedicamento(1L, 1L))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> carritoService.updateItemQuantity(1L, 1L, 3));
        verify(detalleRepository, never()).save(any());
    }

    @Test
    void testUpdateItemQuantity_StockInsuficiente() {
        // Arrange
        when(carritoRepository.findAllByIdUsuarioAndStatus(1L, "A"))
                .thenReturn(Arrays.asList(testCarrito));
        when(detalleRepository.findByIdCartAndIdMedicamento(1L, 1L))
                .thenReturn(Optional.of(testCarritoDetalle));
        when(medicamentoRepository.findById(1L))
                .thenReturn(Optional.of(testMedicamento));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> carritoService.updateItemQuantity(1L, 1L, 200));
        verify(detalleRepository, never()).save(any());
    }

    @Test
    void testRemoveItem_Success() {
        // Arrange
        when(carritoRepository.findAllByIdUsuarioAndStatus(1L, "A"))
                .thenReturn(Arrays.asList(testCarrito));
        when(detalleRepository.findByIdCartAndIdMedicamento(1L, 1L))
                .thenReturn(Optional.of(testCarritoDetalle));
        when(detalleRepository.findByIdCart(1L))
                .thenReturn(Arrays.asList());

        // Act
        CarritoDTO result = carritoService.removeItem(1L, 1L);

        // Assert
        assertNotNull(result);
        verify(detalleRepository).delete(testCarritoDetalle);
        verify(auditoriaService).registrar(eq("Carrito"), eq("DELETE"), anyString(), anyString());
    }

    @Test
    void testRemoveItem_ItemNotFound() {
        // Arrange
        when(carritoRepository.findAllByIdUsuarioAndStatus(1L, "A"))
                .thenReturn(Arrays.asList(testCarrito));
        when(detalleRepository.findByIdCartAndIdMedicamento(1L, 1L))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> carritoService.removeItem(1L, 1L));
        verify(detalleRepository, never()).delete(any());
    }
/*
    @Test
    void testCheckout_Success() {
        // Arrange
        when(carritoRepository.findAllByIdUsuarioAndStatus(1L, "A"))
                .thenReturn(Arrays.asList(testCarrito));
        when(detalleRepository.findByIdCart(1L))
                .thenReturn(Arrays.asList(testCarritoDetalle));
        when(medicamentoRepository.findById(1L))
                .thenReturn(Optional.of(testMedicamento));
        when(ventaRepository.save(any(Venta.class)))
                .thenReturn(testVenta);
        when(jdbcTemplate.update(anyString(), anyLong()))
                .thenReturn(1);

        // Act
        CarritoDTO result = carritoService.checkout(1L);

        // Assert
        assertNotNull(result);
        verify(ventaRepository).save(any(Venta.class));
        verify(ventaDetalleRepository).save(any(VentaDetalle.class));
        verify(jdbcTemplate).update(anyString(), anyLong());
        verify(auditoriaService).registrar(eq("Venta"), eq("CREATE"), anyString(), anyString());
    }

    @Test
    void testCheckout_CartEmpty() {
        // Arrange
        when(carritoRepository.findAllByIdUsuarioAndStatus(1L, "A"))
                .thenReturn(Arrays.asList(testCarrito));
        when(detalleRepository.findByIdCart(1L))
                .thenReturn(Arrays.asList());

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> carritoService.checkout(1L));
        verify(ventaRepository, never()).save(any());
    }





    @Test
    void testCheckoutWithDiscount_CartEmpty() {
        // Arrange
        when(jdbcTemplate.queryForList(anyString(), anyLong()))
                .thenReturn(Arrays.asList());

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> carritoService.checkoutWithDiscount(1L, 5.0));
        verify(ventaService, never()).createVenta(any());
    }
*/
    @Test
    void testCheckoutWithDiscount_StockInsuficiente() {
        // Arrange
        java.util.Map<String, Object> mockItem = createMockItem();
        mockItem.put("STOCK", 1); // Stock insuficiente
        when(jdbcTemplate.queryForList(anyString(), anyLong()))
                .thenReturn(Arrays.asList(mockItem));

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> carritoService.checkoutWithDiscount(1L, 5.0));
        verify(ventaService, never()).createVenta(any());
    }

    @Test
    void testClearCart_Success() {
        // Arrange
        when(carritoRepository.findAllByIdUsuarioAndStatus(1L, "A"))
                .thenReturn(Arrays.asList(testCarrito));
        when(detalleRepository.findByIdCart(1L))
                .thenReturn(Arrays.asList(testCarritoDetalle));

        // Act
        carritoService.clearCart(1L);

        // Assert
        verify(detalleRepository).delete(testCarritoDetalle);
        verify(auditoriaService).registrar(eq("Carrito"), eq("CLEAR"), anyString(), anyString());
    }

    @Test
    void testMigrateItemsFromDuplicateCarts_Success() {
        // Arrange
        Carrito oldCarrito = new Carrito();
        oldCarrito.setIdCart(2L);
        oldCarrito.setStatus("A");
        oldCarrito.setFechaActualizacion(LocalDateTime.now().minusHours(1));

        when(carritoRepository.findAllByIdUsuarioAndStatus(1L, "A"))
                .thenReturn(Arrays.asList(testCarrito, oldCarrito));
        when(detalleRepository.findByIdCart(2L))
                .thenReturn(Arrays.asList(testCarritoDetalle));
        when(detalleRepository.findByIdCartAndIdMedicamento(1L, 1L))
                .thenReturn(Optional.empty());

        // Act
        carritoService.migrateItemsFromDuplicateCarts(1L);

        // Assert
        verify(detalleRepository).save(any(CarritoDetalle.class));
        verify(carritoRepository).save(oldCarrito);
        verify(auditoriaService).registrar(eq("Carrito"), eq("MIGRATE"), anyString(), anyString());
    }

    @Test
    void testMigrateItemsFromDuplicateCarts_NoDuplicates() {
        // Arrange
        when(carritoRepository.findAllByIdUsuarioAndStatus(1L, "A"))
                .thenReturn(Arrays.asList(testCarrito));

        // Act
        carritoService.migrateItemsFromDuplicateCarts(1L);

        // Assert
        verify(detalleRepository, never()).save(any());
        verify(carritoRepository, never()).save(any());
    }

    private java.util.Map<String, Object> createMockItem() {
        java.util.Map<String, Object> item = new java.util.HashMap<>();
        item.put("ID_MEDICAMENTO", 1L);
        item.put("NOMBRE", "Test Medicine");
        item.put("CANTIDAD", 2);
        item.put("PRECIO", 10.0);
        item.put("STOCK", 100);
        return item;
    }

    private VentaDTO createMockVentaDTO() {
        VentaDTO ventaDTO = new VentaDTO();
        ventaDTO.setIdVenta(1L);
        ventaDTO.setIdUsuario(1L);
        ventaDTO.setTotal(20.0);
        return ventaDTO;
    }

    private FacturaDTO createMockFacturaDTO() {
        FacturaDTO facturaDTO = new FacturaDTO();
        facturaDTO.setIdFactura(1L);
        facturaDTO.setIdVenta(1L);
        return facturaDTO;
    }
}
