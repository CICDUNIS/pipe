package com.example.pharmacy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final Logger logger = Logger.getLogger(SecurityConfig.class.getName());
    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        logger.info("Configuring security filter chain");

        http
            .csrf(csrf -> {
                logger.info("Disabling CSRF protection");
                csrf.disable();
            })
            .cors(cors -> {
                logger.info("Configuring CORS");
                cors.configurationSource(corsConfigurationSource());
            })
            .sessionManagement(session -> {
                logger.info("Setting session management to stateless");
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
            })
            .authorizeHttpRequests(auth -> {
                logger.info("Configuring authorization rules");
                auth
                    .requestMatchers("/api/auth/register").permitAll()
                    .requestMatchers("/api/auth/login").permitAll()
                    .requestMatchers("/api/auth/verify-email").permitAll()
                    .requestMatchers("/api/medicamentos").permitAll()
                    .requestMatchers("/api/medicamentos/search").permitAll()
                    .requestMatchers("/api/medicamentos/latest").permitAll()
                    .requestMatchers("/api/medicamentos/{id}").permitAll()
                    .requestMatchers("/api/comentarios/medicamento/{idMedicamento}").permitAll()

                    .requestMatchers("/api/admin/**").hasRole("ADMINISTRADOR")

                    .requestMatchers("/api/farmacia/**").hasAnyRole("ADMINISTRADOR", "EMPLEADO")

                    .requestMatchers("/api/paciente/**").hasAnyRole("ADMINISTRADOR", "PACIENTE")

                    .requestMatchers("/api/interconexion/**").hasAnyRole("ADMINISTRADOR", "USUARIO DE INTERCONEXIÓN")

                    .requestMatchers("/api/medicamentos/**").authenticated()
                    .requestMatchers("/api/users/**").authenticated()
                    .requestMatchers("/api/auth/complete-profile").authenticated()

                    .anyRequest().authenticated();
            })
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4300"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With", "Skip-Jwt-Interceptor"));
        configuration.setExposedHeaders(List.of("Authorization"));
        configuration.setAllowCredentials(true);

        logger.info("CORS configured with allowed origins: " + configuration.getAllowedOrigins());

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        logger.info("Creating BCryptPasswordEncoder bean");
        return new BCryptPasswordEncoder();
    }
}
