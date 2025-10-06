package com.example.pharmacy.controllers;

import com.example.pharmacy.model.Usuario;
import com.example.pharmacy.model.Rol;
import com.example.pharmacy.service.UsuarioService;
import com.example.pharmacy.util.JwtUtils;
import com.example.pharmacy.repository.RolRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RolRepository rolRepository;

    @InjectMocks
    private AuthController authController;

    private Usuario testUsuario;
    private Map<String, String> validRegistrationData;
    private Map<String, String> validLoginData;

    @BeforeEach
    void setUp() {
        testUsuario = new Usuario();
        testUsuario.setIdUsuario(1L);
        testUsuario.setCorreo("test@example.com");
        testUsuario.setNombre("Test User");
        testUsuario.setPasswordHash("hashedPassword");
        testUsuario.setActivo("Y");
        testUsuario.setPerfilCompleto("Y");
        testUsuario.setPrimerLogin("N");

        validRegistrationData = new HashMap<>();
        validRegistrationData.put("correo", "test@example.com");
        validRegistrationData.put("password", "password123");
        validRegistrationData.put("nombre", "Test User");

        validLoginData = new HashMap<>();
        validLoginData.put("correo", "test@example.com");
        validLoginData.put("password", "password123");
    }

    @Test
    void testRegister_Success() {
        // Arrange
        when(usuarioService.register(any(Usuario.class))).thenReturn(testUsuario);
        when(usuarioService.getUserRoles(anyLong())).thenReturn(Arrays.asList("USUARIO"));
        when(jwtUtils.generateToken(anyString(), anyString(), anyList())).thenReturn("jwt-token");

        // Act
        ResponseEntity<Map<String, Object>> response = authController.register(validRegistrationData);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Usuario registrado exitosamente. Por favor, verifica tu correo electrónico.", 
                    response.getBody().get("message"));
        assertEquals("test@example.com", response.getBody().get("correo"));
        assertEquals("USUARIO", response.getBody().get("rol"));
        assertEquals("jwt-token", response.getBody().get("token"));
        
        verify(usuarioService).register(any(Usuario.class));
        verify(jwtUtils).generateToken(anyString(), anyString(), anyList());
    }

    @Test
    void testRegister_WithCustomRole() {
        // Arrange
        validRegistrationData.put("rol", "2");
        when(usuarioService.register(any(Usuario.class))).thenReturn(testUsuario);
        when(rolRepository.existsById(2L)).thenReturn(true);
        when(usuarioService.getUserRoles(anyLong())).thenReturn(Arrays.asList("ADMIN"));
        when(jwtUtils.generateToken(anyString(), anyString(), anyList())).thenReturn("jwt-token");

        // Act
        ResponseEntity<Map<String, Object>> response = authController.register(validRegistrationData);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(usuarioService).assignRolesToUser(anyLong(), eq(Arrays.asList(2L)));
    }

    @Test
    void testRegister_WithInvalidRole() {
        // Arrange
        validRegistrationData.put("rol", "invalid");
        when(usuarioService.register(any(Usuario.class))).thenReturn(testUsuario);
        when(usuarioService.getUserRoles(anyLong())).thenReturn(Arrays.asList("USUARIO"));
        when(jwtUtils.generateToken(anyString(), anyString(), anyList())).thenReturn("jwt-token");

        // Act
        ResponseEntity<Map<String, Object>> response = authController.register(validRegistrationData);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(usuarioService, never()).assignRolesToUser(anyLong(), anyList());
    }

    @Test
    void testRegister_WithNonExistentRole() {
        // Arrange
        validRegistrationData.put("rol", "999");
        when(usuarioService.register(any(Usuario.class))).thenReturn(testUsuario);
        when(rolRepository.existsById(999L)).thenReturn(false);
        when(usuarioService.getUserRoles(anyLong())).thenReturn(Arrays.asList("USUARIO"));
        when(jwtUtils.generateToken(anyString(), anyString(), anyList())).thenReturn("jwt-token");

        // Act
        ResponseEntity<Map<String, Object>> response = authController.register(validRegistrationData);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(usuarioService, never()).assignRolesToUser(anyLong(), anyList());
    }

    @Test
    void testRegister_WithDefaultName() {
        // Arrange
        validRegistrationData.remove("nombre");
        when(usuarioService.register(any(Usuario.class))).thenReturn(testUsuario);
        when(usuarioService.getUserRoles(anyLong())).thenReturn(Arrays.asList("USUARIO"));
        when(jwtUtils.generateToken(anyString(), anyString(), anyList())).thenReturn("jwt-token");

        // Act
        ResponseEntity<Map<String, Object>> response = authController.register(validRegistrationData);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(usuarioService).register(argThat(usuario -> "test".equals(usuario.getNombre())));
    }

    @Test
    void testRegister_MissingEmail() {
        // Arrange
        validRegistrationData.remove("correo");

        // Act
        ResponseEntity<Map<String, Object>> response = authController.register(validRegistrationData);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Correo y contraseña requeridos", response.getBody().get("error"));
    }

    @Test
    void testRegister_MissingPassword() {
        // Arrange
        validRegistrationData.remove("password");

        // Act
        ResponseEntity<Map<String, Object>> response = authController.register(validRegistrationData);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Correo y contraseña requeridos", response.getBody().get("error"));
    }

    @Test
    void testRegister_ServiceException() {
        // Arrange
        when(usuarioService.register(any(Usuario.class))).thenThrow(new RuntimeException("Service error"));

        // Act
        ResponseEntity<Map<String, Object>> response = authController.register(validRegistrationData);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Service error", response.getBody().get("error"));
    }

    @Test
    void testLogin_Success() {
        // Arrange
        when(usuarioService.findByCorreo("test@example.com")).thenReturn(testUsuario);
        when(passwordEncoder.matches("password123", "hashedPassword")).thenReturn(true);
        when(usuarioService.getUserRoles(1L)).thenReturn(Arrays.asList("USUARIO"));
        when(jwtUtils.generateToken("test@example.com", "USUARIO", Arrays.asList("USUARIO"))).thenReturn("jwt-token");

        // Act
        ResponseEntity<Map<String, Object>> response = authController.login(validLoginData);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("jwt-token", response.getBody().get("token"));
        assertNotNull(response.getBody().get("user"));
        
        @SuppressWarnings("unchecked")
        Map<String, Object> userData = (Map<String, Object>) response.getBody().get("user");
        assertEquals("Y", userData.get("perfilCompleto"));
        assertEquals("N", userData.get("primerLogin"));
    }

    @Test
    void testLogin_FirstLogin() {
        // Arrange
        testUsuario.setPrimerLogin("Y");
        when(usuarioService.findByCorreo("test@example.com")).thenReturn(testUsuario);
        when(passwordEncoder.matches("password123", "hashedPassword")).thenReturn(true);
        when(usuarioService.getUserRoles(1L)).thenReturn(Arrays.asList("USUARIO"));
        when(jwtUtils.generateToken("test@example.com", "USUARIO", Arrays.asList("USUARIO"))).thenReturn("jwt-token");

        // Act
        ResponseEntity<Map<String, Object>> response = authController.login(validLoginData);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(usuarioService).updateFirstLogin(1L);
    }

    @Test
    void testLogin_IncompleteProfile() {
        // Arrange
        testUsuario.setPerfilCompleto("N");
        when(usuarioService.findByCorreo("test@example.com")).thenReturn(testUsuario);
        when(passwordEncoder.matches("password123", "hashedPassword")).thenReturn(true);
        when(usuarioService.getUserRoles(1L)).thenReturn(Arrays.asList("USUARIO"));
        when(jwtUtils.generateToken("test@example.com", "USUARIO", Arrays.asList("USUARIO"))).thenReturn("jwt-token");

        // Act
        ResponseEntity<Map<String, Object>> response = authController.login(validLoginData);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("COMPLETE_PROFILE", response.getBody().get("requiresAction"));
        assertEquals("N", ((Map<?, ?>) response.getBody().get("user")).get("perfilCompleto"));
    }

    @Test
    void testLogin_MissingCredentials() {
        // Arrange
        Map<String, String> invalidData = new HashMap<>();
        invalidData.put("correo", "test@example.com");
        // Missing password

        // Act
        ResponseEntity<Map<String, Object>> response = authController.login(invalidData);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Correo y contraseña requeridos", response.getBody().get("error"));
    }

    @Test
    void testLogin_InvalidCredentials() {
        // Arrange
        when(usuarioService.findByCorreo("test@example.com")).thenReturn(testUsuario);
        when(passwordEncoder.matches("password123", "hashedPassword")).thenReturn(false);

        // Act
        ResponseEntity<Map<String, Object>> response = authController.login(validLoginData);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Credenciales inválidas", response.getBody().get("error"));
    }

    @Test
    void testLogin_InactiveUser() {
        // Arrange
        testUsuario.setActivo("N");
        when(usuarioService.findByCorreo("test@example.com")).thenReturn(testUsuario);
        when(passwordEncoder.matches("password123", "hashedPassword")).thenReturn(true);

        // Act
        ResponseEntity<Map<String, Object>> response = authController.login(validLoginData);

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Usuario inactivo", response.getBody().get("error"));
    }

    @Test
    void testLogin_NoRoles() {
        // Arrange
        when(usuarioService.findByCorreo("test@example.com")).thenReturn(testUsuario);
        when(passwordEncoder.matches("password123", "hashedPassword")).thenReturn(true);
        when(usuarioService.getUserRoles(1L)).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<Map<String, Object>> response = authController.login(validLoginData);

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Usuario sin roles asignados", response.getBody().get("error"));
        assertEquals("AWAIT_ROLE_ASSIGNMENT", response.getBody().get("requiresAction"));
    }

    @Test
    void testLogin_ServiceException() {
        // Arrange
        when(usuarioService.findByCorreo("test@example.com")).thenThrow(new RuntimeException("Service error"));

        // Act
        ResponseEntity<Map<String, Object>> response = authController.login(validLoginData);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Service error", response.getBody().get("error"));
    }

    @Test
    void testCompleteProfile_Success() {
        // Arrange
        Map<String, Object> profileData = new HashMap<>();
        profileData.put("correo", "test@example.com");
        profileData.put("nombre", "Updated Name");

        when(usuarioService.findByCorreo("test@example.com")).thenReturn(testUsuario);
        when(usuarioService.completeProfile(1L)).thenReturn(true);
        when(usuarioService.getUserRoles(1L)).thenReturn(Arrays.asList("USUARIO"));
        when(jwtUtils.generateToken("test@example.com", "USUARIO", Arrays.asList("USUARIO"))).thenReturn("jwt-token");

        // Act
        ResponseEntity<Map<String, Object>> response = authController.completeProfile(profileData);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Perfil completado exitosamente", response.getBody().get("message"));
        assertEquals("jwt-token", response.getBody().get("token"));
        assertEquals("Updated Name", testUsuario.getNombre());
    }

    @Test
    void testCompleteProfile_MissingEmail() {
        // Arrange
        Map<String, Object> profileData = new HashMap<>();
        profileData.put("nombre", "Updated Name");

        // Act
        ResponseEntity<Map<String, Object>> response = authController.completeProfile(profileData);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Correo requerido", response.getBody().get("error"));
    }

    @Test
    void testCompleteProfile_MissingName() {
        // Arrange
        Map<String, Object> profileData = new HashMap<>();
        profileData.put("correo", "test@example.com");

        // Act
        ResponseEntity<Map<String, Object>> response = authController.completeProfile(profileData);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Nombre requerido", response.getBody().get("error"));
    }

    @Test
    void testCompleteProfile_ServiceFailure() {
        // Arrange
        Map<String, Object> profileData = new HashMap<>();
        profileData.put("correo", "test@example.com");
        profileData.put("nombre", "Updated Name");

        when(usuarioService.findByCorreo("test@example.com")).thenReturn(testUsuario);
        when(usuarioService.completeProfile(1L)).thenReturn(false);

        // Act
        ResponseEntity<Map<String, Object>> response = authController.completeProfile(profileData);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("No se pudo completar el perfil", response.getBody().get("error"));
    }

    @Test
    void testCompleteProfile_ServiceException() {
        // Arrange
        Map<String, Object> profileData = new HashMap<>();
        profileData.put("correo", "test@example.com");
        profileData.put("nombre", "Updated Name");

        when(usuarioService.findByCorreo("test@example.com")).thenThrow(new RuntimeException("Service error"));

        // Act
        ResponseEntity<Map<String, Object>> response = authController.completeProfile(profileData);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Service error", response.getBody().get("error"));
    }
}
