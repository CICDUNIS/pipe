package com.example.pharmacy.controllers;

import com.example.pharmacy.integration.HospitalEntregaClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HospitalIntegrationControllerTest {

    @Mock
    private HospitalEntregaClient entregaClient;

    @InjectMocks
    private HospitalIntegrationController hospitalIntegrationController;

    private Map<String, String> testBody;
    private Map<String, Object> expectedResponse;

    @BeforeEach
    void setUp() {
        testBody = new HashMap<>();
        testBody.put("codigoReceta", "REC-999");
        testBody.put("paciente", "Juan Pérez");
        testBody.put("entregadoPor", "Farmacia Central");
        testBody.put("fechaEntrega", "2025-08-26T10:30:00");

        expectedResponse = new HashMap<>();
        expectedResponse.put("success", true);
        expectedResponse.put("message", "Entrega confirmada exitosamente");
        expectedResponse.put("codigoReceta", "REC-999");
        expectedResponse.put("fechaEntrega", "2025-08-26T10:30:00");
    }

    @Test
    void testConfirmarEntrega_Success() {
        // Arrange
        when(entregaClient.confirmarEntrega(
            eq("REC-999"), 
            any(LocalDateTime.class), 
            eq("Juan Pérez"), 
            eq("Farmacia Central")
        )).thenReturn(expectedResponse);

        // Act
        Map<String, Object> response = hospitalIntegrationController.confirmarEntrega(testBody);

        // Assert
        assertNotNull(response);
        assertEquals(true, response.get("success"));
        assertEquals("Entrega confirmada exitosamente", response.get("message"));
        assertEquals("REC-999", response.get("codigoReceta"));
        verify(entregaClient).confirmarEntrega(
            eq("REC-999"), 
            any(LocalDateTime.class), 
            eq("Juan Pérez"), 
            eq("Farmacia Central")
        );
    }

    @Test
    void testConfirmarEntrega_WithDifferentData() {
        // Arrange
        Map<String, String> differentBody = new HashMap<>();
        differentBody.put("codigoReceta", "REC-888");
        differentBody.put("paciente", "María García");
        differentBody.put("entregadoPor", "Farmacia Norte");
        differentBody.put("fechaEntrega", "2025-08-27T15:45:00");

        Map<String, Object> differentResponse = new HashMap<>();
        differentResponse.put("success", true);
        differentResponse.put("message", "Entrega confirmada");
        differentResponse.put("codigoReceta", "REC-888");

        when(entregaClient.confirmarEntrega(
            eq("REC-888"), 
            any(LocalDateTime.class), 
            eq("María García"), 
            eq("Farmacia Norte")
        )).thenReturn(differentResponse);

        // Act
        Map<String, Object> response = hospitalIntegrationController.confirmarEntrega(differentBody);

        // Assert
        assertNotNull(response);
        assertEquals(true, response.get("success"));
        assertEquals("Entrega confirmada", response.get("message"));
        assertEquals("REC-888", response.get("codigoReceta"));
        verify(entregaClient).confirmarEntrega(
            eq("REC-888"), 
            any(LocalDateTime.class), 
            eq("María García"), 
            eq("Farmacia Norte")
        );
    }

    @Test
    void testConfirmarEntrega_WithNullValues() {
        // Arrange
        Map<String, String> nullBody = new HashMap<>();
        nullBody.put("codigoReceta", null);
        nullBody.put("paciente", null);
        nullBody.put("entregadoPor", null);
        nullBody.put("fechaEntrega", null);



        // Act & Assert
        assertThrows(Exception.class, () -> {
            hospitalIntegrationController.confirmarEntrega(nullBody);
        });
    }

    @Test
    void testConfirmarEntrega_WithEmptyValues() {
        // Arrange
        Map<String, String> emptyBody = new HashMap<>();
        emptyBody.put("codigoReceta", "");
        emptyBody.put("paciente", "");
        emptyBody.put("entregadoPor", "");
        emptyBody.put("fechaEntrega", "");

        // Act & Assert
        assertThrows(Exception.class, () -> {
            hospitalIntegrationController.confirmarEntrega(emptyBody);
        });
    }

    @Test
    void testConfirmarEntrega_WithInvalidDateFormat() {
        // Arrange
        Map<String, String> invalidDateBody = new HashMap<>();
        invalidDateBody.put("codigoReceta", "REC-999");
        invalidDateBody.put("paciente", "Juan Pérez");
        invalidDateBody.put("entregadoPor", "Farmacia Central");
        invalidDateBody.put("fechaEntrega", "fecha-invalida");

        // Act & Assert
        assertThrows(Exception.class, () -> {
            hospitalIntegrationController.confirmarEntrega(invalidDateBody);
        });
    }

    @Test
    void testConfirmarEntrega_WithMissingFields() {
        // Arrange
        Map<String, String> incompleteBody = new HashMap<>();
        incompleteBody.put("codigoReceta", "REC-999");
        // Missing paciente, entregadoPor, fechaEntrega

        // Act & Assert
        assertThrows(Exception.class, () -> {
            hospitalIntegrationController.confirmarEntrega(incompleteBody);
        });
    }

    @Test
    void testConfirmarEntrega_WithSpecialCharacters() {
        // Arrange
        Map<String, String> specialCharBody = new HashMap<>();
        specialCharBody.put("codigoReceta", "REC-999-@#$%");
        specialCharBody.put("paciente", "Juan Pérez-O'Connor");
        specialCharBody.put("entregadoPor", "Farmacia & Más");
        specialCharBody.put("fechaEntrega", "2025-08-26T10:30:00");

        Map<String, Object> specialCharResponse = new HashMap<>();
        specialCharResponse.put("success", true);
        specialCharResponse.put("message", "Entrega confirmada con caracteres especiales");

        when(entregaClient.confirmarEntrega(
            eq("REC-999-@#$%"), 
            any(LocalDateTime.class), 
            eq("Juan Pérez-O'Connor"), 
            eq("Farmacia & Más")
        )).thenReturn(specialCharResponse);

        // Act
        Map<String, Object> response = hospitalIntegrationController.confirmarEntrega(specialCharBody);

        // Assert
        assertNotNull(response);
        assertEquals(true, response.get("success"));
        assertEquals("Entrega confirmada con caracteres especiales", response.get("message"));
        verify(entregaClient).confirmarEntrega(
            eq("REC-999-@#$%"), 
            any(LocalDateTime.class), 
            eq("Juan Pérez-O'Connor"), 
            eq("Farmacia & Más")
        );
    }

    @Test
    void testConfirmarEntrega_WithLongValues() {
        // Arrange
        Map<String, String> longBody = new HashMap<>();
        longBody.put("codigoReceta", "REC-" + "9".repeat(100));
        longBody.put("paciente", "Juan Pérez " + "A".repeat(100));
        longBody.put("entregadoPor", "Farmacia " + "B".repeat(100));
        longBody.put("fechaEntrega", "2025-08-26T10:30:00");

        Map<String, Object> longResponse = new HashMap<>();
        longResponse.put("success", true);
        longResponse.put("message", "Entrega confirmada con valores largos");

        when(entregaClient.confirmarEntrega(
            eq("REC-" + "9".repeat(100)), 
            any(LocalDateTime.class), 
            eq("Juan Pérez " + "A".repeat(100)), 
            eq("Farmacia " + "B".repeat(100))
        )).thenReturn(longResponse);

        // Act
        Map<String, Object> response = hospitalIntegrationController.confirmarEntrega(longBody);

        // Assert
        assertNotNull(response);
        assertEquals(true, response.get("success"));
        assertEquals("Entrega confirmada con valores largos", response.get("message"));
        verify(entregaClient).confirmarEntrega(
            eq("REC-" + "9".repeat(100)), 
            any(LocalDateTime.class), 
            eq("Juan Pérez " + "A".repeat(100)), 
            eq("Farmacia " + "B".repeat(100))
        );
    }
}
