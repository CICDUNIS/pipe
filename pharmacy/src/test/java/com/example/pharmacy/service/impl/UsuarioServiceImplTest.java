package com.example.pharmacy.service.impl;

import com.example.pharmacy.model.Usuario;
import com.example.pharmacy.model.UsuarioRol;
import com.example.pharmacy.model.Rol;
import com.example.pharmacy.model.VerificationToken;
import com.example.pharmacy.dto.UserDTO;
import com.example.pharmacy.repository.UsuarioRepository;
import com.example.pharmacy.repository.UsuarioRolRepository;
import com.example.pharmacy.repository.RolRepository;
import com.example.pharmacy.service.EmailService;
import com.example.pharmacy.service.VerificationTokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private UsuarioRolRepository usuarioRolRepository;

    @Mock
    private RolRepository rolRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EmailService emailService;

    @Mock
    private VerificationTokenService verificationTokenService;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    private Usuario testUsuario;
    private UserDTO testUserDTO;
    private Rol testRol;
    private VerificationToken testToken;

    @BeforeEach
    void setUp() {
        testUsuario = new Usuario();
        testUsuario.setIdUsuario(1L);
        testUsuario.setNombre("Test User");
        testUsuario.setCorreo("test@example.com");
        testUsuario.setPasswordHash("password123");
        testUsuario.setActivo("N");
        testUsuario.setPerfilCompleto("N");
        testUsuario.setPrimerLogin("Y");
        testUsuario.setFechaCreacion(LocalDateTime.now());

        testUserDTO = new UserDTO();
        testUserDTO.setId(1L);
        testUserDTO.setNombre("Test User");
        testUserDTO.setCorreo("test@example.com");
        testUserDTO.setPassword("newpassword");

        testRol = new Rol();
        testRol.setIdRol(1L);
        testRol.setNombreRol("USUARIO");

        testToken = new VerificationToken();
        testToken.setIdUsuario(1L);
        testToken.setToken("test-token");
    }

    @Test
    void testRegister_Success() {
        // Arrange
        when(usuarioRepository.findByCorreo("test@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn("hashedPassword");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(testUsuario);
        when(verificationTokenService.createVerificationToken(testUsuario)).thenReturn(testToken);
        when(emailService.sendVerificationEmail(testUsuario, "test-token")).thenReturn(true);
        when(emailService.sendAdminNotificationEmail(testUsuario)).thenReturn(true);

        // Act
        Usuario result = usuarioService.register(testUsuario);

        // Assert
        assertNotNull(result);
        assertEquals("hashedPassword", testUsuario.getPasswordHash());
        assertEquals("N", testUsuario.getActivo());
        assertEquals("N", testUsuario.getPerfilCompleto());
        assertEquals("Y", testUsuario.getPrimerLogin());
        assertNotNull(testUsuario.getFechaCreacion());

        verify(usuarioRepository).findByCorreo("test@example.com");
        verify(passwordEncoder).encode("password123");
        verify(usuarioRepository).save(testUsuario);
        verify(verificationTokenService).createVerificationToken(testUsuario);
        verify(emailService).sendVerificationEmail(testUsuario, "test-token");
        verify(emailService).sendAdminNotificationEmail(testUsuario);
    }

    @Test
    void testRegister_UserAlreadyExists() {
        // Arrange
        when(usuarioRepository.findByCorreo("test@example.com")).thenReturn(Optional.of(testUsuario));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.register(testUsuario);
        });

        assertEquals("Ya existe un usuario con correo: test@example.com", exception.getMessage());
        verify(usuarioRepository).findByCorreo("test@example.com");
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void testRegister_EmailServiceFailure() {
        // Arrange
        when(usuarioRepository.findByCorreo("test@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn("hashedPassword");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(testUsuario);
        when(verificationTokenService.createVerificationToken(testUsuario)).thenReturn(testToken);
        when(emailService.sendVerificationEmail(testUsuario, "test-token")).thenReturn(false);
        when(emailService.sendAdminNotificationEmail(testUsuario)).thenReturn(false);

        // Act
        Usuario result = usuarioService.register(testUsuario);

        // Assert
        assertNotNull(result);
        verify(emailService).sendVerificationEmail(testUsuario, "test-token");
        verify(emailService).sendAdminNotificationEmail(testUsuario);
    }

    @Test
    void testVerifyEmail_Success() {
        // Arrange
        when(verificationTokenService.verifyToken("test-token")).thenReturn(true);
        when(verificationTokenService.findByToken("test-token")).thenReturn(Optional.of(testToken));
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(testUsuario));
        when(emailService.sendWelcomeEmail(testUsuario)).thenReturn(true);

        // Act
        boolean result = usuarioService.verifyEmail("test-token");

        // Assert
        assertTrue(result);
        verify(verificationTokenService).verifyToken("test-token");
        verify(verificationTokenService).findByToken("test-token");
        verify(usuarioRepository).findById(1L);
        verify(emailService).sendWelcomeEmail(testUsuario);
    }

    @Test
    void testVerifyEmail_TokenNotFound() {
        // Arrange
        when(verificationTokenService.verifyToken("test-token")).thenReturn(true);
        when(verificationTokenService.findByToken("test-token")).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.verifyEmail("test-token");
        });

        assertEquals("Token not found: test-token", exception.getMessage());
    }

    @Test
    void testVerifyEmail_UserNotFound() {
        // Arrange
        when(verificationTokenService.verifyToken("test-token")).thenReturn(true);
        when(verificationTokenService.findByToken("test-token")).thenReturn(Optional.of(testToken));
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.verifyEmail("test-token");
        });

        assertEquals("User not found for token: test-token", exception.getMessage());
    }

    @Test
    void testActivateUser_Success() {
        // Arrange
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(testUsuario));
        when(rolRepository.findById(1L)).thenReturn(Optional.of(testRol));
        when(usuarioRepository.save(testUsuario)).thenReturn(testUsuario);
        when(emailService.sendAccountActivationEmail(testUsuario, "USUARIO")).thenReturn(true);
        when(usuarioRepository.existsById(1L)).thenReturn(true);
        when(rolRepository.existsById(1L)).thenReturn(true);
        when(usuarioRolRepository.findByIdUsuarioAndIdRol(1L, 1L)).thenReturn(Arrays.asList());

        // Act
        boolean result = usuarioService.activateUser(1L, 1L);

        // Assert
        assertTrue(result);
        assertEquals("Y", testUsuario.getActivo());
        verify(usuarioRepository).findById(1L);
        verify(rolRepository).findById(1L);
        verify(usuarioRepository).save(testUsuario);
        verify(emailService).sendAccountActivationEmail(testUsuario, "USUARIO");
    }

    @Test
    void testActivateUser_UserNotFound() {
        // Arrange
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            usuarioService.activateUser(1L, 1L);
        });

        assertEquals("Usuario no encontrado con id: 1", exception.getMessage());
    }

    @Test
    void testActivateUser_RolNotFound() {
        // Arrange
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(testUsuario));
        when(rolRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            usuarioService.activateUser(1L, 1L);
        });

        assertEquals("Rol no encontrado con id: 1", exception.getMessage());
    }

    @Test
    void testDeactivateUser_Success() {
        // Arrange
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(testUsuario));
        when(usuarioRepository.save(testUsuario)).thenReturn(testUsuario);

        // Act
        boolean result = usuarioService.deactivateUser(1L);

        // Assert
        assertTrue(result);
        assertEquals("N", testUsuario.getActivo());
        verify(usuarioRepository).findById(1L);
        verify(usuarioRepository).save(testUsuario);
    }

    @Test
    void testDeactivateUser_UserNotFound() {
        // Arrange
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            usuarioService.deactivateUser(1L);
        });

        assertEquals("Usuario no encontrado con id: 1", exception.getMessage());
    }

    @Test
    void testCompleteProfile_Success() {
        // Arrange
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(testUsuario));
        when(usuarioRepository.save(testUsuario)).thenReturn(testUsuario);

        // Act
        boolean result = usuarioService.completeProfile(1L);

        // Assert
        assertTrue(result);
        assertEquals("Y", testUsuario.getPerfilCompleto());
        assertEquals("N", testUsuario.getPrimerLogin());
        verify(usuarioRepository).findById(1L);
        verify(usuarioRepository).save(testUsuario);
    }

    @Test
    void testUpdateFirstLogin_Success() {
        // Arrange
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(testUsuario));
        when(usuarioRepository.save(testUsuario)).thenReturn(testUsuario);

        // Act
        boolean result = usuarioService.updateFirstLogin(1L);

        // Assert
        assertTrue(result);
        assertEquals("N", testUsuario.getPrimerLogin());
        verify(usuarioRepository).findById(1L);
        verify(usuarioRepository).save(testUsuario);
    }

    @Test
    void testFindByCorreo_Success() {
        // Arrange
        when(usuarioRepository.findByCorreo("test@example.com")).thenReturn(Optional.of(testUsuario));

        // Act
        Usuario result = usuarioService.findByCorreo("test@example.com");

        // Assert
        assertNotNull(result);
        assertEquals(testUsuario, result);
        verify(usuarioRepository).findByCorreo("test@example.com");
    }

    @Test
    void testFindByCorreo_UserNotFound() {
        // Arrange
        when(usuarioRepository.findByCorreo("test@example.com")).thenReturn(Optional.empty());

        // Act & Assert
        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            usuarioService.findByCorreo("test@example.com");
        });

        assertEquals("Usuario no encontrado con correo: test@example.com", exception.getMessage());
    }

    @Test
    void testUpdateMyProfile_Success() {
        // Arrange
        when(usuarioRepository.findByCorreo("test@example.com")).thenReturn(Optional.of(testUsuario));
        when(passwordEncoder.encode("newpassword")).thenReturn("hashedNewPassword");
        when(usuarioRepository.save(testUsuario)).thenReturn(testUsuario);

        // Act
        Usuario result = usuarioService.updateMyProfile("test@example.com", testUserDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Test User", testUsuario.getNombre());
        assertEquals("test@example.com", testUsuario.getCorreo());
        assertEquals("hashedNewPassword", testUsuario.getPasswordHash());
        verify(usuarioRepository).findByCorreo("test@example.com");
        verify(passwordEncoder).encode("newpassword");
        verify(usuarioRepository).save(testUsuario);
    }

    @Test
    void testUpdateMyProfile_NoPasswordChange() {
        // Arrange
        testUserDTO.setPassword(null);
        when(usuarioRepository.findByCorreo("test@example.com")).thenReturn(Optional.of(testUsuario));
        when(usuarioRepository.save(testUsuario)).thenReturn(testUsuario);

        // Act
        Usuario result = usuarioService.updateMyProfile("test@example.com", testUserDTO);

        // Assert
        assertNotNull(result);
        verify(passwordEncoder, never()).encode(any());
    }

    @Test
    void testGetMyProfile_Success() {
        // Arrange
        when(usuarioRepository.findByCorreo("test@example.com")).thenReturn(Optional.of(testUsuario));
        when(usuarioRolRepository.findByUsuarioId(1L)).thenReturn(Arrays.asList(new UsuarioRol()));
        when(rolRepository.findById(any())).thenReturn(Optional.of(testRol));

        // Act
        UserDTO result = usuarioService.getMyProfile("test@example.com");

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test User", result.getNombre());
        assertEquals("test@example.com", result.getCorreo());
        verify(usuarioRepository).findByCorreo("test@example.com");
    }

    @Test
    void testGetAllUsers_Success() {
        // Arrange
        List<Usuario> usuarios = Arrays.asList(testUsuario);
        when(usuarioRepository.findAll()).thenReturn(usuarios);
        when(usuarioRolRepository.findByUsuarioId(1L)).thenReturn(Arrays.asList(new UsuarioRol()));
        when(rolRepository.findById(any())).thenReturn(Optional.of(testRol));

        // Act
        List<UserDTO> result = usuarioService.getAllUsers();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        verify(usuarioRepository).findAll();
    }

    @Test
    void testFindUsersByFilters_WithEmailFilter() {
        // Arrange
        List<Usuario> usuarios = Arrays.asList(testUsuario);
        when(usuarioRepository.findAll()).thenReturn(usuarios);
        when(usuarioRolRepository.findByUsuarioId(1L)).thenReturn(Arrays.asList(new UsuarioRol()));
        when(rolRepository.findById(any())).thenReturn(Optional.of(testRol));

        // Act
        List<UserDTO> result = usuarioService.findUsersByFilters("test", null, null, null);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(usuarioRepository).findAll();
    }

    @Test
    void testFindUsersByFilters_WithDateFilter() {
        // Arrange
        List<Usuario> usuarios = Arrays.asList(testUsuario);
        when(usuarioRepository.findAll()).thenReturn(usuarios);
        when(usuarioRolRepository.findByUsuarioId(1L)).thenReturn(Arrays.asList(new UsuarioRol()));
        when(rolRepository.findById(any())).thenReturn(Optional.of(testRol));

        LocalDateTime fromDate = LocalDateTime.now().minusDays(1);
        LocalDateTime toDate = LocalDateTime.now().plusDays(1);

        // Act
        List<UserDTO> result = usuarioService.findUsersByFilters(null, fromDate, toDate, null);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testAssignRolesToUser_Success() {
        // Arrange
        when(usuarioRepository.existsById(1L)).thenReturn(true);
        when(rolRepository.existsById(1L)).thenReturn(true);
        when(usuarioRolRepository.findByIdUsuarioAndIdRol(1L, 1L)).thenReturn(Arrays.asList());

        // Act
        usuarioService.assignRolesToUser(1L, Arrays.asList(1L));

        // Assert
        verify(usuarioRepository).existsById(1L);
        verify(rolRepository).existsById(1L);
        verify(usuarioRolRepository).findByIdUsuarioAndIdRol(1L, 1L);
        verify(usuarioRolRepository).insertUsuarioRol(1L, 1L);
    }

    @Test
    void testAssignRolesToUser_UserNotFound() {
        // Arrange
        when(usuarioRepository.existsById(1L)).thenReturn(false);

        // Act & Assert
        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            usuarioService.assignRolesToUser(1L, Arrays.asList(1L));
        });

        assertEquals("Usuario no encontrado con id: 1", exception.getMessage());
    }

    @Test
    void testAssignRolesToUser_RolNotFound() {
        // Arrange
        when(usuarioRepository.existsById(1L)).thenReturn(true);
        when(rolRepository.existsById(1L)).thenReturn(false);

        // Act & Assert
        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            usuarioService.assignRolesToUser(1L, Arrays.asList(1L));
        });

        assertEquals("Rol no encontrado con id: 1", exception.getMessage());
    }

    @Test
    void testRemoveRolFromUser_Success() {
        // Arrange
        when(usuarioRepository.existsById(1L)).thenReturn(true);
        when(rolRepository.existsById(1L)).thenReturn(true);

        // Act
        usuarioService.removeRolFromUser(1L, 1L);

        // Assert
        verify(usuarioRepository).existsById(1L);
        verify(rolRepository).existsById(1L);
        verify(usuarioRolRepository).deleteUsuarioRol(1L, 1L);
    }

    @Test
    void testGetUserRoles_Success() {
        // Arrange
        UsuarioRol usuarioRol = new UsuarioRol();
        usuarioRol.setIdRol(1L);
        when(usuarioRolRepository.findByUsuarioId(1L)).thenReturn(Arrays.asList(usuarioRol));
        when(rolRepository.findById(1L)).thenReturn(Optional.of(testRol));

        // Act
        List<String> result = usuarioService.getUserRoles(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("USUARIO", result.get(0));
        verify(usuarioRolRepository).findByUsuarioId(1L);
        verify(rolRepository).findById(1L);
    }

    @Test
    void testHasRole_True() {
        // Arrange
        when(usuarioRolRepository.findByUsuarioId(1L)).thenReturn(Arrays.asList(new UsuarioRol()));
        when(rolRepository.findById(any())).thenReturn(Optional.of(testRol));

        // Act
        boolean result = usuarioService.hasRole(1L, "USUARIO");

        // Assert
        assertTrue(result);
    }

    @Test
    void testHasRole_False() {
        // Arrange
        when(usuarioRolRepository.findByUsuarioId(1L)).thenReturn(Arrays.asList());

        // Act
        boolean result = usuarioService.hasRole(1L, "ADMIN");

        // Assert
        assertFalse(result);
    }
}
