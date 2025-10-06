package com.example.pharmacy.service.impl;

import com.example.pharmacy.model.Rol;
import com.example.pharmacy.model.Usuario;
import com.example.pharmacy.repository.RolRepository;
import com.example.pharmacy.repository.UsuarioRepository;
import com.example.pharmacy.repository.UsuarioRolRepository;
import com.example.pharmacy.dto.UserDTO;
import com.example.pharmacy.service.EmailService;
import com.example.pharmacy.service.VerificationTokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;

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
    private UsuarioServiceImpl service;

    private Usuario existingUser;

    @BeforeEach
    void setUp() {
        existingUser = new Usuario();
        existingUser.setIdUsuario(1L);
        existingUser.setNombre("Old Name");
        existingUser.setCorreo("old@test.com");
        existingUser.setPasswordHash("oldhash");
        existingUser.setFechaCreacion(LocalDateTime.now());
    }

    @Test
    @DisplayName("updateMyProfile: actualiza nombre y correo sin cambiar contraseña")
    void updateMyProfile_withoutPassword() {
        UserDTO dto = new UserDTO();
        dto.setNombre("New Name");
        dto.setCorreo("new@test.com");
        dto.setPassword(null);

        when(usuarioRepository.findByCorreo("old@test.com")).thenReturn(Optional.of(existingUser));
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));

        Usuario result = service.updateMyProfile("old@test.com", dto);

        assertEquals("New Name", result.getNombre());
        assertEquals("new@test.com", result.getCorreo());
        assertEquals("oldhash", result.getPasswordHash());
        verify(passwordEncoder, never()).encode(any());
        verify(usuarioRepository).save(result);
    }

    @Test
    @DisplayName("updateMyProfile: actualiza contraseña cuando se provee")
    void updateMyProfile_withPassword() {
        UserDTO dto = new UserDTO();
        dto.setNombre("Name");
        dto.setCorreo("old@test.com");
        dto.setPassword("newpwd");

        when(usuarioRepository.findByCorreo("old@test.com")).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.encode("newpwd")).thenReturn("newhash");
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));

        Usuario result = service.updateMyProfile("old@test.com", dto);

        assertEquals("newhash", result.getPasswordHash());
        verify(passwordEncoder).encode("newpwd");
        verify(usuarioRepository).save(result);
    }

    @Test
    @DisplayName("assignRolesToUser: asigna rol correctamente cuando no existe")
    void assignRolesToUser_success() {
        Long userId = 1L;
        Long roleId = 2L;
        when(usuarioRepository.existsById(userId)).thenReturn(true);
        when(rolRepository.existsById(roleId)).thenReturn(true);
        when(usuarioRolRepository.findByIdUsuarioAndIdRol(userId, roleId)).thenReturn(List.of());
        doNothing().when(usuarioRolRepository).insertUsuarioRol(userId, roleId);

        assertDoesNotThrow(() -> service.assignRolesToUser(userId, List.of(roleId)));

        verify(usuarioRolRepository).insertUsuarioRol(userId, roleId);
    }

    @Test
    @DisplayName("assignRolesToUser: error cuando usuario no existe")
    void assignRolesToUser_userNotFound() {
        when(usuarioRepository.existsById(5L)).thenReturn(false);
        assertThrows(Exception.class, () -> service.assignRolesToUser(5L, List.of(1L)));
    }

    @Test
    @DisplayName("removeRolFromUser: elimina correctamente")
    void removeRolFromUser_success() {
        Long userId = 1L, roleId = 3L;
        when(usuarioRepository.existsById(userId)).thenReturn(true);
        when(rolRepository.existsById(roleId)).thenReturn(true);
        doNothing().when(usuarioRolRepository).deleteUsuarioRol(userId, roleId);

        assertDoesNotThrow(() -> service.removeRolFromUser(userId, roleId));
        verify(usuarioRolRepository).deleteUsuarioRol(userId, roleId);
    }
}
