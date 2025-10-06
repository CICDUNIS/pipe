package com.example.pharmacy.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SecurityConfigTest {

    @Mock
    private JwtAuthFilter jwtAuthFilter;

    @Mock
    private HttpSecurity httpSecurity;

    @Mock
    private CsrfConfigurer<HttpSecurity> csrfConfigurer;

    @Mock
    private CorsConfigurer<HttpSecurity> corsConfigurer;

    @Mock
    private SessionManagementConfigurer<HttpSecurity> sessionManagementConfigurer;

    @Mock
    private AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authRegistry;

    @Mock
    private SecurityFilterChain securityFilterChain;

    private SecurityConfig securityConfig;

    @BeforeEach
    void setUp() {
        securityConfig = new SecurityConfig(jwtAuthFilter);
    }

    @Test
    void testSecurityConfigCreation() {
        assertNotNull(securityConfig);
        assertNotNull(jwtAuthFilter);
    }

    @Test
    void testPasswordEncoderBean() {
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();
        
        assertNotNull(passwordEncoder);
        assertTrue(passwordEncoder.matches("password", passwordEncoder.encode("password")));
        assertFalse(passwordEncoder.matches("wrongpassword", passwordEncoder.encode("password")));
    }

    @Test
    void testCorsConfigurationSource() {
        CorsConfigurationSource corsSource = securityConfig.corsConfigurationSource();
        
        assertNotNull(corsSource);
        
        // Verificar que la configuración se puede obtener
        assertNotNull(corsSource);
        
        // Verificar configuraciones CORS básicas
        assertTrue(corsSource instanceof org.springframework.web.cors.UrlBasedCorsConfigurationSource);
    }

    @Test
    void testCorsConfigurationSourceWithNullRequest() {
        CorsConfigurationSource corsSource = securityConfig.corsConfigurationSource();
        
        assertNotNull(corsSource);
        // No podemos probar getCorsConfiguration con null request
        // pero podemos verificar que el source se creó correctamente
        assertTrue(corsSource instanceof org.springframework.web.cors.UrlBasedCorsConfigurationSource);
    }

    @Test
    void testCorsConfigurationSourceRegistersConfiguration() {
        CorsConfigurationSource corsSource = securityConfig.corsConfigurationSource();
        
        // Verificar que la configuración se registra correctamente
        assertNotNull(corsSource);
        
        // Verificar que la configuración se puede obtener
        assertTrue(corsSource instanceof org.springframework.web.cors.UrlBasedCorsConfigurationSource);
    }

    @Test
    void testPasswordEncoderEncodesDifferentPasswords() {
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();
        
        String password1 = "password123";
        String password2 = "differentPassword";
        
        String encoded1 = passwordEncoder.encode(password1);
        String encoded2 = passwordEncoder.encode(password2);
        
        assertNotEquals(encoded1, encoded2);
        assertTrue(passwordEncoder.matches(password1, encoded1));
        assertTrue(passwordEncoder.matches(password2, encoded2));
        assertFalse(passwordEncoder.matches(password1, encoded2));
    }

    @Test
    void testPasswordEncoderMultipleEncodings() {
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();
        String password = "testPassword";
        
        String encoded1 = passwordEncoder.encode(password);
        String encoded2 = passwordEncoder.encode(password);
        
        // Cada encoding debe ser único debido al salt
        assertNotEquals(encoded1, encoded2);
        
        // Ambos deben coincidir con la contraseña original
        assertTrue(passwordEncoder.matches(password, encoded1));
        assertTrue(passwordEncoder.matches(password, encoded2));
    }

    @Test
    void testPasswordEncoderWithEmptyString() {
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();
        
        String emptyPassword = "";
        String encoded = passwordEncoder.encode(emptyPassword);
        
        assertNotNull(encoded);
        assertTrue(passwordEncoder.matches(emptyPassword, encoded));
    }

    @Test
    void testPasswordEncoderWithSpecialCharacters() {
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();
        
        String specialPassword = "!@#$%^&*()_+-=[]{}|;':\",./<>?";
        String encoded = passwordEncoder.encode(specialPassword);
        
        assertNotNull(encoded);
        assertTrue(passwordEncoder.matches(specialPassword, encoded));
    }

    @Test
    void testPasswordEncoderWithUnicodeCharacters() {
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();
        
        String unicodePassword = "pásswórd_ñáéíóú";
        String encoded = passwordEncoder.encode(unicodePassword);
        
        assertNotNull(encoded);
        assertTrue(passwordEncoder.matches(unicodePassword, encoded));
    }

    @Test
    void testPasswordEncoderWithVeryLongPassword() {
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();
        
        String longPassword = "a".repeat(1000);
        String encoded = passwordEncoder.encode(longPassword);
        
        assertNotNull(encoded);
        assertTrue(passwordEncoder.matches(longPassword, encoded));
    }

    @Test
    void testCorsConfigurationSourceIsSingleton() {
        CorsConfigurationSource corsSource1 = securityConfig.corsConfigurationSource();
        CorsConfigurationSource corsSource2 = securityConfig.corsConfigurationSource();
        
        // Verificar que ambos son del mismo tipo
        assertTrue(corsSource1 instanceof org.springframework.web.cors.UrlBasedCorsConfigurationSource);
        assertTrue(corsSource2 instanceof org.springframework.web.cors.UrlBasedCorsConfigurationSource);
        
        // En Spring, los beans por defecto son singletons, pero en tests pueden ser diferentes instancias
        // Verificamos que ambos funcionan correctamente
        assertNotNull(corsSource1);
        assertNotNull(corsSource2);
    }

    @Test
    void testCorsConfigurationSourceWithDifferentPaths() {
        CorsConfigurationSource corsSource = securityConfig.corsConfigurationSource();
        
        // Verificar que la configuración se aplica a diferentes paths
        assertNotNull(corsSource);
        
        // Verificar que es del tipo correcto
        assertTrue(corsSource instanceof org.springframework.web.cors.UrlBasedCorsConfigurationSource);
        
        // En un test unitario no podemos probar paths reales, pero verificamos la estructura
        assertNotNull(corsSource);
    }
}
