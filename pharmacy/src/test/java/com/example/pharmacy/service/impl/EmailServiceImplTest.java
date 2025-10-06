package com.example.pharmacy.service.impl;

import com.example.pharmacy.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceImplTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private TemplateEngine templateEngine;

    @Mock
    private MimeMessage mimeMessage;

    @InjectMocks
    private EmailServiceImpl emailService;

    private Usuario testUser;

    @BeforeEach
    void setUp() {
        testUser = new Usuario();
        testUser.setIdUsuario(1L);
        testUser.setNombre("Test User");
        testUser.setCorreo("test@example.com");
        testUser.setFechaCreacion(LocalDateTime.now());

        // Configurar campos privados
        ReflectionTestUtils.setField(emailService, "fromEmail", "from@example.com");
        ReflectionTestUtils.setField(emailService, "adminEmail", "admin@example.com");
        ReflectionTestUtils.setField(emailService, "frontendUrl", "http://localhost:3000");
        
        // Configurar templateEngine mock para devolver contenido HTML válido
        lenient().when(templateEngine.process(anyString(), any(Context.class))).thenReturn("<html><body>Test email content</body></html>");
    }

    @Test
    void testSendVerificationEmail_Success() throws Exception {
        // Arrange
        String verificationToken = "test-token-123";
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        doNothing().when(mailSender).send(any(MimeMessage.class));

        // Act
        boolean result = emailService.sendVerificationEmail(testUser, verificationToken);

        // Assert
        assertTrue(result);
        verify(mailSender).createMimeMessage();
        verify(mailSender).send(any(MimeMessage.class));
    }

    @Test
    void testSendVerificationEmail_WithNullUser() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> emailService.sendVerificationEmail(null, "token"));
        verify(mailSender, never()).createMimeMessage();
    }

    @Test
    void testSendVerificationEmail_WithNullToken() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> emailService.sendVerificationEmail(testUser, null));
    }

    @Test
    void testSendVerificationEmail_WithEmptyToken() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> emailService.sendVerificationEmail(testUser, ""));
    }



    @Test
    void testSendWelcomeEmail_Success() throws Exception {
        // Arrange
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        doNothing().when(mailSender).send(any(MimeMessage.class));

        // Act
        boolean result = emailService.sendWelcomeEmail(testUser);

        // Assert
        assertTrue(result);
        verify(mailSender).createMimeMessage();
        verify(mailSender).send(any(MimeMessage.class));
    }

    @Test
    void testSendWelcomeEmail_WithNullUser() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> emailService.sendWelcomeEmail(null));
        verify(mailSender, never()).createMimeMessage();
    }



    @Test
    void testSendAdminNotificationEmail_Success() throws Exception {
        // Arrange
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        doNothing().when(mailSender).send(any(MimeMessage.class));

        // Act
        boolean result = emailService.sendAdminNotificationEmail(testUser);

        // Assert
        assertTrue(result);
        verify(mailSender).createMimeMessage();
        verify(mailSender).send(any(MimeMessage.class));
    }

    @Test
    void testSendAdminNotificationEmail_WithNullUser() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> emailService.sendAdminNotificationEmail(null));
        verify(mailSender, never()).createMimeMessage();
    }



    @Test
    void testSendAccountActivationEmail_Success() throws Exception {
        // Arrange
        String role = "USER";
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        doNothing().when(mailSender).send(any(MimeMessage.class));

        // Act
        boolean result = emailService.sendAccountActivationEmail(testUser, role);

        // Assert
        assertTrue(result);
        verify(mailSender).createMimeMessage();
        verify(mailSender).send(any(MimeMessage.class));
    }

    @Test
    void testSendAccountActivationEmail_WithNullUser() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> emailService.sendAccountActivationEmail(null, "USER"));
        verify(mailSender, never()).createMimeMessage();
    }

    @Test
    void testSendAccountActivationEmail_WithNullRole() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> emailService.sendAccountActivationEmail(testUser, null));
    }

    @Test
    void testSendAccountActivationEmail_WithEmptyRole() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> emailService.sendAccountActivationEmail(testUser, ""));
    }






}
