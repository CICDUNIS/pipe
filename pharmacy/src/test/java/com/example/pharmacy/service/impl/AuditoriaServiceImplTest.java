package com.example.pharmacy.service.impl;

import com.example.pharmacy.model.AuditoriaFarmacia;
import com.example.pharmacy.repository.AuditoriaFarmaciaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuditoriaServiceImplTest {

    @Mock
    private AuditoriaFarmaciaRepository repo;

    @InjectMocks
    private AuditoriaServiceImpl service;

    @Test
    void registrar_deberiaConstruirYGuardarEntidadConDatosCorrectos() {
        // dado
        String tabla     = "mi_tabla";
        String tipo      = "INSERT";
        String descripcion = "Prueba de auditoría";
        String usuario   = "usuario123";

        // guardamos el instante antes y después de la llamada
        LocalDateTime antes  = LocalDateTime.now();
        service.registrar(tabla, tipo, descripcion, usuario);
        LocalDateTime despues = LocalDateTime.now();

        // capturamos el objeto que se pasó a repo.save(...)
        ArgumentCaptor<AuditoriaFarmacia> captor =
            ArgumentCaptor.forClass(AuditoriaFarmacia.class);
        verify(repo).save(captor.capture());

        AuditoriaFarmacia saved = captor.getValue();
        assertNotNull(saved);
        assertEquals(tabla,       saved.getTablaAfectada());
        assertEquals(tipo,        saved.getTipoCambio());
        assertEquals(descripcion, saved.getDescripcion());
        assertEquals(usuario,     saved.getUsuario());
        assertNotNull(saved.getFecha(),
            "La fecha no debe ser nula");

        // la fecha debe estar dentro del rango [antes, despues]
        assertFalse(saved.getFecha().isBefore(antes),
            "Fecha anterior al inicio del método");
        assertFalse(saved.getFecha().isAfter(despues),
            "Fecha posterior al fin del método");
    }
}
