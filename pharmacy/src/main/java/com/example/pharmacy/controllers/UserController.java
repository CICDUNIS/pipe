package com.example.pharmacy.controllers;

import com.example.pharmacy.dto.UserDTO;
import com.example.pharmacy.model.Usuario;
import com.example.pharmacy.service.UsuarioService;
import com.example.pharmacy.util.JwtUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Logger logger = Logger.getLogger(UserController.class.getName());
    private final UsuarioService usuarioService;
    private final JwtUtils jwtUtils;

    public UserController(UsuarioService usuarioService, JwtUtils jwtUtils) {
        this.usuarioService = usuarioService;
        this.jwtUtils = jwtUtils;
    }

    @GetMapping("/me")
    public ResponseEntity<?> getMyProfile(HttpServletRequest request) {
        logger.info("GET /api/users/me called");
        try {
            logRequestHeaders(request);

            String correo = extractCorreoFromToken(request);
            logger.info("Fetching profile for user: " + correo);

            UserDTO profile = usuarioService.getMyProfile(correo);
            logger.info("Profile retrieved successfully");

            return ResponseEntity.ok(profile);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error in getMyProfile: " + e.getMessage(), e);
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to retrieve user profile");
            errorResponse.put("message", e.getMessage());
            errorResponse.put("type", e.getClass().getName());

            StringBuilder stackTrace = new StringBuilder();
            for (StackTraceElement element : e.getStackTrace()) {
                stackTrace.append(element.toString()).append("\n");
                if (stackTrace.length() > 500) break;
            }
            errorResponse.put("stackTrace", stackTrace.toString());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PutMapping("/me")
    public UserDTO updateMyProfile(@RequestBody UserDTO dto, HttpServletRequest request) {
        logger.info("PUT /api/users/me called with data: " + dto);
        logRequestHeaders(request);

        String correo = extractCorreoFromToken(request);
        logger.info("Updating profile for user: " + correo);

        Usuario updated = usuarioService.updateMyProfile(correo, dto);
        logger.info("Profile updated successfully");

        return toDTO(updated);
    }

    private String extractCorreoFromToken(HttpServletRequest request) {
        logger.info("Extracting email from token");

        String authHeader = request.getHeader("Authorization");
        logger.info("Authorization header: " + (authHeader != null ? "present" : "missing"));

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.severe("No Bearer token found in request");
            throw new RuntimeException("No Bearer token");
        }

        String token = authHeader.substring(7);
        logger.info("Token extracted from header: " + token.substring(0, Math.min(20, token.length())) + "...");

        if (!jwtUtils.validateToken(token)) {
            logger.severe("Token validation failed");
            throw new RuntimeException("Invalid token");
        }

        String correo = jwtUtils.getCorreoFromToken(token);
        logger.info("Email extracted from token: " + correo);

        return correo;
    }

    private UserDTO toDTO(Usuario user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getIdUsuario());
        dto.setNombre(user.getNombre());
        dto.setCorreo(user.getCorreo());

        Usuario usuario = usuarioService.findByCorreo(user.getCorreo());
        List<String> roles = usuarioService.getUserRoles(usuario.getIdUsuario());
        String primaryRole = roles.isEmpty() ? "INVITADO" : roles.get(0);
        dto.setRol(primaryRole);
        dto.setRoles(roles);

        dto.setActivo(user.getActivo());
        return dto;
    }

    private void logRequestHeaders(HttpServletRequest request) {
        logger.info("Request headers:");
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            if (headerName.equalsIgnoreCase("Authorization")) {
                headerValue = headerValue != null ? "Bearer ***" : null;
            }
            logger.info(headerName + ": " + headerValue);
        }
    }
}
