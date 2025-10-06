package com.example.pharmacy.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserDTOTest {

    private UserDTO userDTO;
    private LocalDateTime testDateTime;

    @BeforeEach
    void setUp() {
        userDTO = new UserDTO();
        testDateTime = LocalDateTime.of(2024, 1, 1, 12, 0, 0);
    }

    @Test
    @DisplayName("Test setId and getId")
    void testId() {
        // Arrange
        Long expectedId = 1L;

        // Act
        userDTO.setId(expectedId);
        Long actualId = userDTO.getId();

        // Assert
        assertEquals(expectedId, actualId);
    }

    @Test
    @DisplayName("Test setId with null")
    void testIdWithNull() {
        // Act
        userDTO.setId(null);
        Long actualId = userDTO.getId();

        // Assert
        assertNull(actualId);
    }

    @Test
    @DisplayName("Test setNombre and getNombre")
    void testNombre() {
        // Arrange
        String expectedNombre = "Juan Pérez";

        // Act
        userDTO.setNombre(expectedNombre);
        String actualNombre = userDTO.getNombre();

        // Assert
        assertEquals(expectedNombre, actualNombre);
    }

    @Test
    @DisplayName("Test setNombre with empty string")
    void testNombreWithEmptyString() {
        // Arrange
        String expectedNombre = "";

        // Act
        userDTO.setNombre(expectedNombre);
        String actualNombre = userDTO.getNombre();

        // Assert
        assertEquals(expectedNombre, actualNombre);
    }

    @Test
    @DisplayName("Test setNombre with null")
    void testNombreWithNull() {
        // Act
        userDTO.setNombre(null);
        String actualNombre = userDTO.getNombre();

        // Assert
        assertNull(actualNombre);
    }

    @Test
    @DisplayName("Test setCorreo and getCorreo")
    void testCorreo() {
        // Arrange
        String expectedCorreo = "juan.perez@example.com";

        // Act
        userDTO.setCorreo(expectedCorreo);
        String actualCorreo = userDTO.getCorreo();

        // Assert
        assertEquals(expectedCorreo, actualCorreo);
    }

    @Test
    @DisplayName("Test setCorreo with special characters")
    void testCorreoWithSpecialCharacters() {
        // Arrange
        String expectedCorreo = "juan.perez+test@example.co.uk";

        // Act
        userDTO.setCorreo(expectedCorreo);
        String actualCorreo = userDTO.getCorreo();

        // Assert
        assertEquals(expectedCorreo, actualCorreo);
    }

    @Test
    @DisplayName("Test setRol and getRol")
    void testRol() {
        // Arrange
        String expectedRol = "ADMIN";

        // Act
        userDTO.setRol(expectedRol);
        String actualRol = userDTO.getRol();

        // Assert
        assertEquals(expectedRol, actualRol);
    }

    @Test
    @DisplayName("Test setRol with lowercase")
    void testRolWithLowercase() {
        // Arrange
        String expectedRol = "usuario";

        // Act
        userDTO.setRol(expectedRol);
        String actualRol = userDTO.getRol();

        // Assert
        assertEquals(expectedRol, actualRol);
    }

    @Test
    @DisplayName("Test setRoles and getRoles")
    void testRoles() {
        // Arrange
        List<String> expectedRoles = Arrays.asList("ADMIN", "USUARIO", "FARMACIA");

        // Act
        userDTO.setRoles(expectedRoles);
        List<String> actualRoles = userDTO.getRoles();

        // Assert
        assertEquals(expectedRoles, actualRoles);
        assertEquals(3, actualRoles.size());
        assertTrue(actualRoles.contains("ADMIN"));
        assertTrue(actualRoles.contains("USUARIO"));
        assertTrue(actualRoles.contains("FARMACIA"));
    }

    @Test
    @DisplayName("Test setRoles with empty list")
    void testRolesWithEmptyList() {
        // Arrange
        List<String> expectedRoles = Arrays.asList();

        // Act
        userDTO.setRoles(expectedRoles);
        List<String> actualRoles = userDTO.getRoles();

        // Assert
        assertEquals(expectedRoles, actualRoles);
        assertEquals(0, actualRoles.size());
    }

    @Test
    @DisplayName("Test setRoles with null")
    void testRolesWithNull() {
        // Act
        userDTO.setRoles(null);
        List<String> actualRoles = userDTO.getRoles();

        // Assert
        assertNull(actualRoles);
    }

    @Test
    @DisplayName("Test setActivo and getActivo")
    void testActivo() {
        // Arrange
        String expectedActivo = "Y";

        // Act
        userDTO.setActivo(expectedActivo);
        String actualActivo = userDTO.getActivo();

        // Assert
        assertEquals(expectedActivo, actualActivo);
    }

    @Test
    @DisplayName("Test setActivo with N")
    void testActivoWithN() {
        // Arrange
        String expectedActivo = "N";

        // Act
        userDTO.setActivo(expectedActivo);
        String actualActivo = userDTO.getActivo();

        // Assert
        assertEquals(expectedActivo, actualActivo);
    }

    @Test
    @DisplayName("Test setPassword and getPassword")
    void testPassword() {
        // Arrange
        String expectedPassword = "password123";

        // Act
        userDTO.setPassword(expectedPassword);
        String actualPassword = userDTO.getPassword();

        // Assert
        assertEquals(expectedPassword, actualPassword);
    }

    @Test
    @DisplayName("Test setPassword with special characters")
    void testPasswordWithSpecialCharacters() {
        // Arrange
        String expectedPassword = "P@ssw0rd!";

        // Act
        userDTO.setPassword(expectedPassword);
        String actualPassword = userDTO.getPassword();

        // Assert
        assertEquals(expectedPassword, actualPassword);
    }

    @Test
    @DisplayName("Test setPerfilCompleto and isPerfilCompleto")
    void testPerfilCompleto() {
        // Arrange
        boolean expectedPerfilCompleto = true;

        // Act
        userDTO.setPerfilCompleto(expectedPerfilCompleto);
        boolean actualPerfilCompleto = userDTO.isPerfilCompleto();

        // Assert
        assertEquals(expectedPerfilCompleto, actualPerfilCompleto);
    }

    @Test
    @DisplayName("Test setPerfilCompleto with false")
    void testPerfilCompletoWithFalse() {
        // Arrange
        boolean expectedPerfilCompleto = false;

        // Act
        userDTO.setPerfilCompleto(expectedPerfilCompleto);
        boolean actualPerfilCompleto = userDTO.isPerfilCompleto();

        // Assert
        assertEquals(expectedPerfilCompleto, actualPerfilCompleto);
    }

    @Test
    @DisplayName("Test setPrimerLogin and isPrimerLogin")
    void testPrimerLogin() {
        // Arrange
        boolean expectedPrimerLogin = true;

        // Act
        userDTO.setPrimerLogin(expectedPrimerLogin);
        boolean actualPrimerLogin = userDTO.isPrimerLogin();

        // Assert
        assertEquals(expectedPrimerLogin, actualPrimerLogin);
    }

    @Test
    @DisplayName("Test setPrimerLogin with false")
    void testPrimerLoginWithFalse() {
        // Arrange
        boolean expectedPrimerLogin = false;

        // Act
        userDTO.setPrimerLogin(expectedPrimerLogin);
        boolean actualPrimerLogin = userDTO.isPrimerLogin();

        // Assert
        assertEquals(expectedPrimerLogin, actualPrimerLogin);
    }

    @Test
    @DisplayName("Test setFechaCreacion and getFechaCreacion")
    void testFechaCreacion() {
        // Act
        userDTO.setFechaCreacion(testDateTime);
        LocalDateTime actualFechaCreacion = userDTO.getFechaCreacion();

        // Assert
        assertEquals(testDateTime, actualFechaCreacion);
    }

    @Test
    @DisplayName("Test setFechaCreacion with null")
    void testFechaCreacionWithNull() {
        // Act
        userDTO.setFechaCreacion(null);
        LocalDateTime actualFechaCreacion = userDTO.getFechaCreacion();

        // Assert
        assertNull(actualFechaCreacion);
    }

    @Test
    @DisplayName("Test complete user setup")
    void testCompleteUserSetup() {
        // Arrange
        Long id = 1L;
        String nombre = "María García";
        String correo = "maria.garcia@example.com";
        String rol = "FARMACIA";
        List<String> roles = Arrays.asList("FARMACIA", "USUARIO");
        String activo = "Y";
        String password = "securePassword123";
        boolean perfilCompleto = true;
        boolean primerLogin = false;
        LocalDateTime fechaCreacion = LocalDateTime.now();

        // Act
        userDTO.setId(id);
        userDTO.setNombre(nombre);
        userDTO.setCorreo(correo);
        userDTO.setRol(rol);
        userDTO.setRoles(roles);
        userDTO.setActivo(activo);
        userDTO.setPassword(password);
        userDTO.setPerfilCompleto(perfilCompleto);
        userDTO.setPrimerLogin(primerLogin);
        userDTO.setFechaCreacion(fechaCreacion);

        // Assert
        assertEquals(id, userDTO.getId());
        assertEquals(nombre, userDTO.getNombre());
        assertEquals(correo, userDTO.getCorreo());
        assertEquals(rol, userDTO.getRol());
        assertEquals(roles, userDTO.getRoles());
        assertEquals(activo, userDTO.getActivo());
        assertEquals(password, userDTO.getPassword());
        assertEquals(perfilCompleto, userDTO.isPerfilCompleto());
        assertEquals(primerLogin, userDTO.isPrimerLogin());
        assertEquals(fechaCreacion, userDTO.getFechaCreacion());
    }

    @Test
    @DisplayName("Test roles list modification")
    void testRolesListModification() {
        // Arrange
        List<String> initialRoles = new ArrayList<>(Arrays.asList("USUARIO"));
        userDTO.setRoles(initialRoles);

        // Act - Modify the list
        List<String> roles = userDTO.getRoles();
        roles.add("ADMIN");

        // Assert
        assertEquals(2, userDTO.getRoles().size());
        assertTrue(userDTO.getRoles().contains("USUARIO"));
        assertTrue(userDTO.getRoles().contains("ADMIN"));
    }

    @Test
    @DisplayName("Test default roles initialization")
    void testDefaultRolesInitialization() {
        // Act - Create new instance
        UserDTO newUserDTO = new UserDTO();

        // Assert
        assertNotNull(newUserDTO.getRoles());
        assertTrue(newUserDTO.getRoles().isEmpty());
    }

    @Test
    @DisplayName("Test edge case with very long strings")
    void testEdgeCaseWithVeryLongStrings() {
        // Arrange
        String veryLongString = "A".repeat(1000);

        // Act
        userDTO.setNombre(veryLongString);
        userDTO.setCorreo(veryLongString);

        // Assert
        assertEquals(veryLongString, userDTO.getNombre());
        assertEquals(veryLongString, userDTO.getCorreo());
    }

    @Test
    @DisplayName("Test edge case with special characters in all string fields")
    void testEdgeCaseWithSpecialCharacters() {
        // Arrange
        String specialString = "!@#$%^&*()_+-=[]{}|;':\",./<>?";

        // Act
        userDTO.setNombre(specialString);
        userDTO.setCorreo(specialString);
        userDTO.setRol(specialString);
        userDTO.setPassword(specialString);

        // Assert
        assertEquals(specialString, userDTO.getNombre());
        assertEquals(specialString, userDTO.getCorreo());
        assertEquals(specialString, userDTO.getRol());
        assertEquals(specialString, userDTO.getPassword());
    }
}
