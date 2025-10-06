package com.example.pharmacy.config;

import com.example.pharmacy.util.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final Logger logger = Logger.getLogger(JwtAuthFilter.class.getName());
    private final JwtUtils jwtUtils;
    private final ObjectMapper objectMapper;

    public JwtAuthFilter(JwtUtils jwtUtils, ObjectMapper objectMapper) {
        this.jwtUtils = jwtUtils;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) 
            throws ServletException, IOException {
        logger.info("Processing request: " + request.getMethod() + " " + request.getRequestURI());
        
        String authHeader = request.getHeader("Authorization");
        logger.info("Authorization header: " + (authHeader != null ? "present" : "not present"));

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.info("No JWT token found in request, continuing filter chain");
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);
        try {
            if (jwtUtils.validateToken(token)) {
                String correo = jwtUtils.getCorreoFromToken(token);
                String primaryRole = jwtUtils.getRolFromToken(token);
                List<String> roles = jwtUtils.getRolesFromToken(token);
                
                logger.info("Valid token for user: " + correo + " with primary role: " + primaryRole);
                logger.info("All roles: " + String.join(", ", roles));

                List<SimpleGrantedAuthority> authorities = new ArrayList<>();
                authorities.add(new SimpleGrantedAuthority("ROLE_" + primaryRole.toUpperCase()));
                
                logger.info("Added authority: ROLE_" + primaryRole.toUpperCase());
                
                for (String role : roles) {
                    if (!role.equals(primaryRole)) {  
                        authorities.add(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()));
                        logger.info("Added authority: ROLE_" + role.toUpperCase());
                    }
                }

                UsernamePasswordAuthenticationToken authentication = 
                    new UsernamePasswordAuthenticationToken(correo, null, authorities);
                
                SecurityContextHolder.getContext().setAuthentication(authentication);
                logger.info("Authentication set in SecurityContext for: " + correo);
            } else {
                logger.warning("Invalid JWT token");
                sendErrorResponse(request, response, HttpStatus.UNAUTHORIZED, "unauthorized", "Token inválido");
                return;
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error processing JWT token", e);
            sendErrorResponse(request, response, HttpStatus.UNAUTHORIZED, "unauthorized", e.getMessage());
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void sendErrorResponse(HttpServletRequest request, HttpServletResponse response, HttpStatus status, String error, String message) 
            throws IOException {
        response.setStatus(status.value());
        response.setContentType("application/json");
        
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("status", status.value());
        errorDetails.put("error", error);
        errorDetails.put("message", message);
        
        Throwable thrown = (Throwable) request.getAttribute("jakarta.servlet.error.exception");
        if (thrown != null) {
            errorDetails.put("exception", thrown.getClass().getName());
            errorDetails.put("exceptionMessage", thrown.getMessage());
            
            List<String> stackTrace = new ArrayList<>();
            for (int i = 0; i < Math.min(5, thrown.getStackTrace().length); i++) {
                stackTrace.add(thrown.getStackTrace()[i].toString());
            }
            errorDetails.put("trace", stackTrace);
        }
        
        logger.warning("Sending error response: " + errorDetails);
        objectMapper.writeValue(response.getOutputStream(), errorDetails);
    }
}


