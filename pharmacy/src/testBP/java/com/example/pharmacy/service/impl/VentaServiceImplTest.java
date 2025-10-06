package com.example.pharmacy.service.impl;

import com.example.pharmacy.model.Usuario;
import com.example.pharmacy.model.VerificationToken;
import com.example.pharmacy.repository.VerificationTokenRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VerificationTokenServiceImplTest {

    @Mock
    private VerificationTokenRepository repository;

    @InjectMocks
    private VerificationTokenServiceImpl service;

    @Test
    @DisplayName("createVerificationToken: elimina existente y guarda nuevo token")
    void createVerificationToken_deletesExistingAndSavesNew() {
        Usuario user = new Usuario();
        user.setIdUsuario(1L);
        user.setCorreo("user@test.com");

        // Token existente para el usuario
        VerificationToken oldToken = new VerificationToken(
            "oldValue",
            1L,
            LocalDateTime.now().minusHours(5),
            LocalDateTime.now().minusHours(1)
        );
        oldToken.setIdToken(100L);
        when(repository.findByIdUsuario(1L)).thenReturn(Optional.of(oldToken));

        // Nuevo token que simula repository.save
        VerificationToken newToken = new VerificationToken(
            "newValue",
            1L,
            LocalDateTime.now(),
            LocalDateTime.now().plusHours(24)
        );
        when(repository.save(any(VerificationToken.class))).thenReturn(newToken);

        VerificationToken result = service.createVerificationToken(user);

        // Verificaciones
        verify(repository).deleteById(100L);
        verify(repository).save(any(VerificationToken.class));
        assertSame(newToken, result);
    }

    @Test
    @DisplayName("findByToken: retorna optional del repositorio")
    void findByToken_returnsOptional() {
        VerificationToken token = new VerificationToken(
            "abc",
            2L,
            LocalDateTime.now(),
            LocalDateTime.now().plusHours(24)
        );
        when(repository.findByToken("abc")).thenReturn(Optional.of(token));

        Optional<VerificationToken> opt = service.findByToken("abc");

        assertTrue(opt.isPresent());
        assertSame(token, opt.get());
    }

    @Test
    @DisplayName("isEmailVerified: true si countVerifiedTokensByUser > 0")
    void isEmailVerified_trueWhenCountPositive() {
        when(repository.countVerifiedTokensByUser(5L)).thenReturn(3);
        assertTrue(service.isEmailVerified(5L));
    }

    @Test
    @DisplayName("isEmailVerified: false si countVerifiedTokensByUser == 0")
    void isEmailVerified_falseWhenZero() {
        when(repository.countVerifiedTokensByUser(6L)).thenReturn(0);
        assertFalse(service.isEmailVerified(6L));
    }
}
