package com.example.pharmacy.service.impl;

import com.example.pharmacy.dto.CoberturaMedicamentoDTO;
import com.example.pharmacy.dto.RecetaValidadaDTO;
import com.example.pharmacy.integration.HospitalClient;
import com.example.pharmacy.integration.SeguroClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompraMedicamentoServiceImplTest {

    @Mock
    private HospitalClient hospitalClient;

    @Mock
    private SeguroClient seguroClient;

    @InjectMocks
    private CompraMedicamentoServiceImpl service;

    @Test
    @DisplayName("validarReceta: receta inválida lanza IllegalArgumentException")
    void validarReceta_recetaInvalida_lanzaException() {
        String codigoReceta = "REC123";
        List<String> codigos = List.of("MED1", "MED2");
        RecetaValidadaDTO receta = new RecetaValidadaDTO();
        receta.setValida(false);

        when(hospitalClient.validarReceta(codigoReceta)).thenReturn(receta);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> service.validarReceta(codigoReceta, codigos));

        assertTrue(ex.getMessage().contains("Receta inválida: " + codigoReceta));
        verify(hospitalClient).validarReceta(codigoReceta);
        verifyNoMoreInteractions(seguroClient);
    }

    @Test
    @DisplayName("validarReceta: medicamento no autorizado lanza IllegalArgumentException")
    void validarReceta_medicamentoNoAutorizado_lanzaException() {
        String codigoReceta = "REC456";
        List<String> codigos = List.of("MED1", "MEDX");
        RecetaValidadaDTO receta = new RecetaValidadaDTO();
        receta.setValida(true);
        receta.setMedicamentos(List.of("MED1", "MED2"));

        when(hospitalClient.validarReceta(codigoReceta)).thenReturn(receta);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> service.validarReceta(codigoReceta, codigos));

        assertTrue(ex.getMessage().contains("Medicamento no autorizado en la receta: MEDX"));
        verify(hospitalClient).validarReceta(codigoReceta);
    }

    @Test
    @DisplayName("validarReceta: receta válida y todos los medicamentos autorizados no lanza excepción")
    void validarReceta_exito_noException() {
        String codigoReceta = "REC789";
        List<String> codigos = List.of("MEDA", "MEDB");
        RecetaValidadaDTO receta = new RecetaValidadaDTO();
        receta.setValida(true);
        receta.setMedicamentos(List.of("MEDA", "MEDB", "MEDC"));

        when(hospitalClient.validarReceta(codigoReceta)).thenReturn(receta);

        assertDoesNotThrow(() -> service.validarReceta(codigoReceta, codigos));
        verify(hospitalClient).validarReceta(codigoReceta);
    }

    @Test
    @DisplayName("validarCobertura: medicamento sin cobertura lanza IllegalArgumentException")
    void validarCobertura_sinCobertura_lanzaException() {
        String numeroAfiliacion = "AFI123";
        List<String> codigos = List.of("M1", "M2");
        CoberturaMedicamentoDTO cov1 = new CoberturaMedicamentoDTO();
        cov1.setCubierto(true);
        CoberturaMedicamentoDTO cov2 = new CoberturaMedicamentoDTO();
        cov2.setCubierto(false);

        when(seguroClient.validarMedicamento(numeroAfiliacion, "M1")).thenReturn(cov1);
        when(seguroClient.validarMedicamento(numeroAfiliacion, "M2")).thenReturn(cov2);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> service.validarCobertura(numeroAfiliacion, codigos));

        assertTrue(ex.getMessage().contains("El medicamento M2 no tiene cobertura."));
        verify(seguroClient).validarMedicamento(numeroAfiliacion, "M1");
        verify(seguroClient).validarMedicamento(numeroAfiliacion, "M2");
    }

    @Test
    @DisplayName("validarCobertura: todos los medicamentos con cobertura no lanza excepción")
    void validarCobertura_exito_noException() {
        String numeroAfiliacion = "AFI456";
        List<String> codigos = List.of("X1", "X2");
        CoberturaMedicamentoDTO cov = new CoberturaMedicamentoDTO(); cov.setCubierto(true);

        when(seguroClient.validarMedicamento(eq(numeroAfiliacion), anyString())).thenReturn(cov);

        assertDoesNotThrow(() -> service.validarCobertura(numeroAfiliacion, codigos));
        verify(seguroClient, times(2)).validarMedicamento(eq(numeroAfiliacion), anyString());
    }
}
