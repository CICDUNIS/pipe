package com.example.pharmacy.service.impl;

import com.example.pharmacy.dto.CarritoDTO;
import com.example.pharmacy.model.Carrito;
import com.example.pharmacy.model.CarritoDetalle;
import com.example.pharmacy.model.Medicamento;
import com.example.pharmacy.repository.CarritoDetalleRepository;
import com.example.pharmacy.repository.CarritoRepository;
import com.example.pharmacy.repository.MedicamentoRepository;
import com.example.pharmacy.service.AuditoriaService;
import com.example.pharmacy.service.FacturaService;
import com.example.pharmacy.service.VentaService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarritoServiceImplTest {

    @Mock
    private CarritoRepository carritoRepo;

    @Mock
    private CarritoDetalleRepository detalleRepo;

    @Mock
    private MedicamentoRepository medicamentoRepo;

    @Mock
    private VentaService ventaService;

    @Mock
    private FacturaService facturaService;

    @Mock
    private AuditoriaService auditoriaService;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private CarritoServiceImpl service;

    @Test
    @DisplayName("getActiveCart: crea nuevo carrito cuando no existe")
    void getActiveCart_creaNuevo() {
        Long userId = 1L;
        when(carritoRepo.findAllByIdUsuarioAndStatus(userId, "A"))
            .thenReturn(Collections.emptyList());

        Carrito saved = new Carrito();
        saved.setIdCart(100L);
        saved.setIdUsuario(userId);
        saved.setStatus("A");
        when(carritoRepo.save(any(Carrito.class))).thenReturn(saved);

        when(detalleRepo.findByIdCart(100L)).thenReturn(Collections.emptyList());

        CarritoDTO dto = service.getActiveCart(userId);

        assertNotNull(dto);
        assertEquals(100L, dto.getIdCart());
        assertTrue(dto.getItems().isEmpty());
    }

    @Test
    @DisplayName("addItem: medicamento no encontrado lanza NoSuchElementException")
    void addItem_medNotFound() {
        Long userId = 2L, medId = 5L;
        when(medicamentoRepo.findById(medId)).thenReturn(Optional.empty());

        NoSuchElementException ex = assertThrows(NoSuchElementException.class,
            () -> service.addItem(userId, medId, 1));
        assertTrue(ex.getMessage().contains("Medicamento no encontrado"));

        verify(auditoriaService).registrar(
            eq("Carrito"), eq("ERROR"),
            contains("Medicamento no encontrado"),
            contains("usuario=" + userId));
    }

    @Test
    @DisplayName("addItem: éxito guarda detalle y audita UPDATE")
    void addItem_success() {
        Long userId = 3L, medId = 7L;
        Medicamento med = new Medicamento();
        med.setIdMedicamento(medId);
        med.setRequiereReceta("N");
        med.setStock(10);
        med.setPrecio(2.5);
        when(medicamentoRepo.findById(medId)).thenReturn(Optional.of(med));

        Carrito cart = new Carrito();
        cart.setIdCart(200L);
        cart.setIdUsuario(userId);
        cart.setStatus("A");
        when(carritoRepo.findAllByIdUsuarioAndStatus(userId, "A"))
            .thenReturn(Collections.singletonList(cart));

        when(detalleRepo.findByIdCartAndIdMedicamento(200L, medId))
            .thenReturn(Optional.empty());
        when(detalleRepo.findByIdCart(200L)).thenReturn(Collections.emptyList());

        CarritoDTO dto = service.addItem(userId, medId, 2);
        assertEquals(200L, dto.getIdCart());

        ArgumentCaptor<CarritoDetalle> captor = ArgumentCaptor.forClass(CarritoDetalle.class);
        verify(detalleRepo).save(captor.capture());
        CarritoDetalle saved = captor.getValue();
        assertEquals(medId, saved.getIdMedicamento());
        assertEquals(2, saved.getCantidad());

        verify(auditoriaService).registrar(
            eq("Carrito"), eq("UPDATE"),
            contains("Agregado medicamento ID=" + medId),
            contains("usuario=" + userId));
    }

    
}
