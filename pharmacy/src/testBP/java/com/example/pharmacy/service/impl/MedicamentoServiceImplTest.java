package com.example.pharmacy.service.impl;

import com.example.pharmacy.model.Medicamento;
import com.example.pharmacy.repository.MedicamentoRepository;
import com.example.pharmacy.service.AuditoriaService;
import com.example.pharmacy.util.UserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class MedicamentoServiceImplTest {

    @Mock
    private MedicamentoRepository repo;

    @Mock
    private AuditoriaService auditoriaService;

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private MedicamentoServiceImpl service;

    @BeforeEach
    void setUp() {
        // Lenient stub para toda la clase
        lenient().when(userDetails.getUsuarioActual()).thenReturn("tester");
    }

    @Test
    @DisplayName("create: válido guarda y convierte requiereReceta correctamente")
    void create_valid() {
        Medicamento med = new Medicamento();
        med.setPrecio(10.0);
        med.setStock(5);
        med.setRequiereReceta("true");

        when(repo.save(any(Medicamento.class))).thenAnswer(inv -> inv.getArgument(0));

        Medicamento result = service.create(med);

        assertNotNull(result);
        assertEquals("Y", result.getRequiereReceta());
        verify(repo).save(med);
        verify(auditoriaService).registrar(eq("Medicamento"), eq("INSERT"), contains("Creacion medicamento"), contains("tester"));
    }

    @Test
    @DisplayName("create: precio negativo lanza IllegalArgumentException")
    void create_negativePrice() {
        Medicamento med = new Medicamento();
        med.setPrecio(-5.0);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> service.create(med));
        assertTrue(ex.getMessage().toLowerCase().contains("precio"));
        verifyNoInteractions(repo);
    }

    @Test
    @DisplayName("create: stock negativo lanza IllegalArgumentException")
    void create_negativeStock() {
        Medicamento med = new Medicamento();
        med.setPrecio(5.0);
        med.setStock(-3);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> service.create(med));
        assertTrue(ex.getMessage().toLowerCase().contains("stock"));
        verifyNoInteractions(repo);
    }

    @Test
    @DisplayName("findById: existe retorna, no existe lanza NoSuchElementException")
    void findById_behavior() {
        Medicamento med = new Medicamento(); med.setIdMedicamento(1L);
        when(repo.findById(1L)).thenReturn(Optional.of(med));
        assertEquals(med, service.findById(1L));

        when(repo.findById(2L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> service.findById(2L));
    }

    @Test
    @DisplayName("update: existente actualiza requiereReceta y guarda")
    void update_valid() {
        Medicamento existing = new Medicamento(); existing.setIdMedicamento(1L);
        when(repo.findById(1L)).thenReturn(Optional.of(existing));
        when(repo.save(any(Medicamento.class))).thenAnswer(inv -> inv.getArgument(0));

        Medicamento upd = new Medicamento();
        upd.setRequiereReceta("false");
        Medicamento result = service.update(1L, upd);

        assertEquals("N", result.getRequiereReceta());
        verify(repo).save(existing);
        verify(auditoriaService).registrar(eq("Medicamento"), eq("UPDATE"), contains("Actualizacion del medicamento"), contains("tester"));
    }

    @Test
    @DisplayName("update: no existente lanza NoSuchElementException")
    void update_notFound() {
        when(repo.findById(10L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> service.update(10L, new Medicamento()));
    }

    @Test
    @DisplayName("delete: elimina correctamente")
    void delete_success() {
        doNothing().when(repo).deleteById(5L);
        service.delete(5L);
        verify(repo).deleteById(5L);
    }

    @Test
    @DisplayName("existsById delega en repo")
    void existsById_success() {
        when(repo.existsById(7L)).thenReturn(true);
        assertTrue(service.existsById(7L));
        verify(repo).existsById(7L);
    }
}
