package com.example.pharmacy.service.impl;

import com.example.pharmacy.model.Usuario;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceImplTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private TemplateEngine templateEngine;

    @InjectMocks
    private EmailServiceImpl service;

    private MimeMessage mockMessage;

    @BeforeEach
    void setUp() {
        // Establece valores en campos anotados con @Value
        ReflectionTestUtils.setField(service, "fromEmail", "from@example.com");
        ReflectionTestUtils.setField(service, "adminEmail", "admin@example.com");
        ReflectionTestUtils.setField(service, "frontendUrl", "http://app.test");

        // Prepara un MimeMessage simulado
        mockMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mockMessage);
    }

        }
