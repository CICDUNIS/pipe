package com.example.pharmacy.controllers;

import com.example.pharmacy.dto.UserDTO;
import com.example.pharmacy.model.Usuario;
import com.example.pharmacy.service.UsuarioService;
import com.example.pharmacy.util.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private UserController userController;

    private MockHttpServletRequest request;
    private UserDTO testUserDTO;
    private Usuario testUsuario;
    private static final String VALID_TOKEN = "valid.jwt.token";
    private static final String VALID_EMAIL = "test@example.com";

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + VALID_TOKEN);

        testUserDTO = new UserDTO();
        testUserDTO.setId(1L);
        testUserDTO.setCorreo(VALID_EMAIL);
        testUserDTO.setNombre("Test User");

        testUsuario = new Usuario();
        testUsuario.setIdUsuario(1L);
        testUsuario.setCorreo(VALID_EMAIL);
        testUsuario.setNombre("Test User");
    }

    @Test
    void testGetMyProfile_Success() {
        // Arrange
        when(jwtUtils.validateToken(VALID_TOKEN)).thenReturn(true);
        when(jwtUtils.getCorreoFromToken(VALID_TOKEN)).thenReturn(VALID_EMAIL);
        when(usuarioService.getMyProfile(VALID_EMAIL)).thenReturn(testUserDTO);

        // Act
        ResponseEntity<?> response = userController.getMyProfile(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testUserDTO, response.getBody());
        verify(jwtUtils).validateToken(VALID_TOKEN);
        verify(jwtUtils).getCorreoFromToken(VALID_TOKEN);
        verify(usuarioService).getMyProfile(VALID_EMAIL);
    }

    @Test
    void testGetMyProfile_NoAuthorizationHeader() {
        // Arrange
        request.removeHeader("Authorization");

        // Act
        ResponseEntity<?> response = userController.getMyProfile(request);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        
        @SuppressWarnings("unchecked")
        Map<String, String> responseBody = (Map<String, String>) response.getBody();
        assertEquals("Failed to retrieve user profile", responseBody.get("error"));
        assertEquals("No Bearer token", responseBody.get("message"));
    }

    @Test
    void testGetMyProfile_InvalidToken() {
        // Arrange
        when(jwtUtils.validateToken(VALID_TOKEN)).thenReturn(false);

        // Act
        ResponseEntity<?> response = userController.getMyProfile(request);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        
        @SuppressWarnings("unchecked")
        Map<String, String> responseBody = (Map<String, String>) response.getBody();
        assertEquals("Failed to retrieve user profile", responseBody.get("error"));
        assertEquals("Invalid token", responseBody.get("message"));
    }

    @Test
    void testGetMyProfile_ServiceError() {
        // Arrange
        when(jwtUtils.validateToken(VALID_TOKEN)).thenReturn(true);
        when(jwtUtils.getCorreoFromToken(VALID_TOKEN)).thenReturn(VALID_EMAIL);
        when(usuarioService.getMyProfile(VALID_EMAIL)).thenThrow(new RuntimeException("Service error"));

        // Act
        ResponseEntity<?> response = userController.getMyProfile(request);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        
        @SuppressWarnings("unchecked")
        Map<String, String> responseBody = (Map<String, String>) response.getBody();
        assertEquals("Failed to retrieve user profile", responseBody.get("error"));
        assertEquals("Service error", responseBody.get("message"));
        assertEquals("java.lang.RuntimeException", responseBody.get("type"));
    }

    @Test
    void testUpdateMyProfile_Success() {
        // Arrange
        when(jwtUtils.validateToken(VALID_TOKEN)).thenReturn(true);
        when(jwtUtils.getCorreoFromToken(VALID_TOKEN)).thenReturn(VALID_EMAIL);
        when(usuarioService.updateMyProfile(VALID_EMAIL, testUserDTO)).thenReturn(testUsuario);
        when(usuarioService.findByCorreo(VALID_EMAIL)).thenReturn(testUsuario);
        when(usuarioService.getUserRoles(1L)).thenReturn(Arrays.asList("USUARIO"));

        // Act
        UserDTO result = userController.updateMyProfile(testUserDTO, request);

        // Assert
        assertNotNull(result);
        assertEquals(testUserDTO.getId(), result.getId());
        assertEquals(testUserDTO.getCorreo(), result.getCorreo());
        assertEquals(testUserDTO.getNombre(), result.getNombre());
        
        verify(jwtUtils).validateToken(VALID_TOKEN);
        verify(jwtUtils).getCorreoFromToken(VALID_TOKEN);
        verify(usuarioService).updateMyProfile(VALID_EMAIL, testUserDTO);
    }

    @Test
    void testUpdateMyProfile_NoAuthorizationHeader() {
        // Arrange
        request.removeHeader("Authorization");

        // Act & Assert
        assertThrows(RuntimeException.class, () -> userController.updateMyProfile(testUserDTO, request));
    }

    @Test
    void testUpdateMyProfile_InvalidToken() {
        // Arrange
        when(jwtUtils.validateToken(VALID_TOKEN)).thenReturn(false);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> userController.updateMyProfile(testUserDTO, request));
    }

    @Test
    void testExtractCorreoFromToken_Success() {
        // Arrange
        when(jwtUtils.validateToken(VALID_TOKEN)).thenReturn(true);
        when(jwtUtils.getCorreoFromToken(VALID_TOKEN)).thenReturn(VALID_EMAIL);

        // Act
        ResponseEntity<?> response = userController.getMyProfile(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(jwtUtils).validateToken(VALID_TOKEN);
        verify(jwtUtils).getCorreoFromToken(VALID_TOKEN);
    }

    @Test
    void testExtractCorreoFromToken_MalformedHeader() {
        // Arrange
        request.removeHeader("Authorization");
        request.addHeader("Authorization", "InvalidFormat");

        // Act
        ResponseEntity<?> response = userController.getMyProfile(request);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        
        @SuppressWarnings("unchecked")
        Map<String, String> responseBody = (Map<String, String>) response.getBody();
        assertEquals("Failed to retrieve user profile", responseBody.get("error"));
        assertEquals("No Bearer token", responseBody.get("message"));
    }
}
