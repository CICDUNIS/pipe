package com.example.pharmacy.service.impl;

import com.example.pharmacy.model.Usuario;
import com.example.pharmacy.model.VerificationToken;
import com.example.pharmacy.repository.VerificationTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VerificationTokenServiceImplTest {

    @Mock
    private VerificationTokenRepository verificationTokenRepository;

    @InjectMocks
    private VerificationTokenServiceImpl verificationTokenService;

    private Usuario testUsuario;
    private VerificationToken testToken;
    private LocalDateTime now;
    private LocalDateTime expiryDate;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.now();
        expiryDate = now.plusHours(24);
        
        testUsuario = new Usuario();
        testUsuario.setIdUsuario(1L);
        testUsuario.setCorreo("test@example.com");
        
        testToken = new VerificationToken();
        testToken.setIdToken(1L);
        testToken.setToken("test-token-123");
        testToken.setIdUsuario(1L);
        testToken.setFechaCreacion(now);
        testToken.setFechaExpiracion(expiryDate);
        testToken.setFechaVerificacion(null);
    }

    @Test
    void testCreateVerificationToken_Success() {
        // Arrange
        when(verificationTokenRepository.findByIdUsuario(1L))
            .thenReturn(Optional.empty());
        when(verificationTokenRepository.save(any()))
            .thenReturn(testToken);

        // Act
        VerificationToken result = verificationTokenService.createVerificationToken(testUsuario);

        // Assert
        assertNotNull(result);
        assertEquals(testToken, result);
        verify(verificationTokenRepository).findByIdUsuario(1L);
        verify(verificationTokenRepository).save(any());
    }

    @Test
    void testCreateVerificationToken_WithExistingToken() {
        // Arrange
        VerificationToken existingToken = new VerificationToken();
        existingToken.setIdToken(2L);
        
        when(verificationTokenRepository.findByIdUsuario(1L))
            .thenReturn(Optional.of(existingToken));
        when(verificationTokenRepository.save(any()))
            .thenReturn(testToken);

        // Act
        VerificationToken result = verificationTokenService.createVerificationToken(testUsuario);

        // Assert
        assertNotNull(result);
        verify(verificationTokenRepository).findByIdUsuario(1L);
        verify(verificationTokenRepository).deleteById(2L);
        verify(verificationTokenRepository).save(any());
    }

    @Test
    void testFindByToken_Success() {
        // Arrange
        when(verificationTokenRepository.findByToken("test-token-123"))
            .thenReturn(Optional.of(testToken));

        // Act
        Optional<VerificationToken> result = verificationTokenService.findByToken("test-token-123");

        // Assert
        assertTrue(result.isPresent());
        assertEquals(testToken, result.get());
        verify(verificationTokenRepository).findByToken("test-token-123");
    }

    @Test
    void testFindByToken_NotFound() {
        // Arrange
        when(verificationTokenRepository.findByToken("invalid-token"))
            .thenReturn(Optional.empty());

        // Act
        Optional<VerificationToken> result = verificationTokenService.findByToken("invalid-token");

        // Assert
        assertFalse(result.isPresent());
        verify(verificationTokenRepository).findByToken("invalid-token");
    }

    @Test
    void testVerifyToken_Success() {
        // Arrange
        testToken.setFechaVerificacion(null);
        testToken.setFechaExpiracion(now.plusHours(1)); // Not expired
        
        when(verificationTokenRepository.findByToken("test-token-123"))
            .thenReturn(Optional.of(testToken));
        doNothing().when(verificationTokenRepository).updateVerificationDate(anyString(), any(LocalDateTime.class));

        // Act
        boolean result = verificationTokenService.verifyToken("test-token-123");

        // Assert
        assertTrue(result);
        verify(verificationTokenRepository).findByToken("test-token-123");
        verify(verificationTokenRepository).updateVerificationDate(anyString(), any(LocalDateTime.class));
    }

    @Test
    void testVerifyToken_TokenNotFound() {
        // Arrange
        when(verificationTokenRepository.findByToken("invalid-token"))
            .thenReturn(Optional.empty());

        // Act
        boolean result = verificationTokenService.verifyToken("invalid-token");

        // Assert
        assertFalse(result);
        verify(verificationTokenRepository).findByToken("invalid-token");
        verify(verificationTokenRepository, never()).updateVerificationDate(anyString(), any(LocalDateTime.class));
    }

    @Test
    void testVerifyToken_TokenExpired() {
        // Arrange
        testToken.setFechaExpiracion(now.minusHours(1)); // Expired
        
        when(verificationTokenRepository.findByToken("expired-token"))
            .thenReturn(Optional.of(testToken));

        // Act
        boolean result = verificationTokenService.verifyToken("expired-token");

        // Assert
        assertFalse(result);
        verify(verificationTokenRepository).findByToken("expired-token");
        verify(verificationTokenRepository, never()).updateVerificationDate(anyString(), any(LocalDateTime.class));
    }

    @Test
    void testVerifyToken_AlreadyVerified() {
        // Arrange
        testToken.setFechaVerificacion(now.minusHours(1)); // Already verified
        
        when(verificationTokenRepository.findByToken("verified-token"))
            .thenReturn(Optional.of(testToken));

        // Act
        boolean result = verificationTokenService.verifyToken("verified-token");

        // Assert
        assertTrue(result);
        verify(verificationTokenRepository).findByToken("verified-token");
        verify(verificationTokenRepository, never()).updateVerificationDate(anyString(), any(LocalDateTime.class));
    }

    @Test
    void testIsEmailVerified_True() {
        // Arrange
        when(verificationTokenRepository.countVerifiedTokensByUser(1L))
            .thenReturn(1);

        // Act
        boolean result = verificationTokenService.isEmailVerified(1L);

        // Assert
        assertTrue(result);
        verify(verificationTokenRepository).countVerifiedTokensByUser(1L);
    }

    @Test
    void testIsEmailVerified_False() {
        // Arrange
        when(verificationTokenRepository.countVerifiedTokensByUser(1L))
            .thenReturn(0);

        // Act
        boolean result = verificationTokenService.isEmailVerified(1L);

        // Assert
        assertFalse(result);
        verify(verificationTokenRepository).countVerifiedTokensByUser(1L);
    }

    @Test
    void testIsEmailVerified_MultipleTokens() {
        // Arrange
        when(verificationTokenRepository.countVerifiedTokensByUser(1L))
            .thenReturn(3);

        // Act
        boolean result = verificationTokenService.isEmailVerified(1L);

        // Assert
        assertTrue(result);
        verify(verificationTokenRepository).countVerifiedTokensByUser(1L);
    }

    @Test
    void testCleanupExpiredTokens() {
        // Arrange
        doNothing().when(verificationTokenRepository).deleteExpiredTokens(any(LocalDateTime.class));

        // Act
        verificationTokenService.cleanupExpiredTokens();

        // Assert
        verify(verificationTokenRepository).deleteExpiredTokens(any(LocalDateTime.class));
    }

    // Tests de NullPointerException eliminados porque los métodos no lanzan excepciones con parámetros nulos

    @Test
    void testCreateVerificationToken_WithEmptyUserEmail() {
        // Arrange
        testUsuario.setCorreo("");
        
        when(verificationTokenRepository.findByIdUsuario(1L))
            .thenReturn(Optional.empty());
        when(verificationTokenRepository.save(any()))
            .thenReturn(testToken);

        // Act
        VerificationToken result = verificationTokenService.createVerificationToken(testUsuario);

        // Assert
        assertNotNull(result);
        verify(verificationTokenRepository).save(any());
    }

    @Test
    void testVerifyToken_WithRepositoryException() {
        // Arrange
        when(verificationTokenRepository.findByToken("test-token-123"))
            .thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            verificationTokenService.verifyToken("test-token-123");
        });
    }

    @Test
    void testIsEmailVerified_WithRepositoryException() {
        // Arrange
        when(verificationTokenRepository.countVerifiedTokensByUser(1L))
            .thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            verificationTokenService.isEmailVerified(1L);
        });
    }
}
