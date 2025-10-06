package com.example.pharmacy.controllers;

import com.example.pharmacy.model.Usuario;
import com.example.pharmacy.model.Rol;
import com.example.pharmacy.service.UsuarioService;
import com.example.pharmacy.service.VerificationTokenService;
import com.example.pharmacy.util.JwtUtils;
import com.example.pharmacy.repository.RolRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = Logger.getLogger(AuthController.class.getName());
    private final UsuarioService usuarioService;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final RolRepository rolRepository;
    private final VerificationTokenService verificationTokenService;

    public AuthController(UsuarioService usuarioService, JwtUtils jwtUtils, PasswordEncoder passwordEncoder,
                         RolRepository rolRepository, VerificationTokenService verificationTokenService) {
        this.usuarioService = usuarioService;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = passwordEncoder;
        this.rolRepository = rolRepository;
        this.verificationTokenService = verificationTokenService;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody Map<String, String> registrationData) {
        logger.info("Received registration request for email: " + registrationData.get("correo"));

        String correo = registrationData.get("correo");
        String password = registrationData.get("password");
        String nombre = registrationData.get("nombre");
        String rolStr = registrationData.get("rol");
	

        if (correo == null || correo.isBlank() || password == null || password.isBlank()) {
            logger.warning("Registration failed: missing email or password");
            return ResponseEntity.badRequest().body(Map.of("error", "Correo y contraseña requeridos"));
        }

        try {
            Usuario newUser = new Usuario();
            if (nombre == null || nombre.isBlank()) {
                String defaultName = correo.split("@")[0];
                newUser.setNombre(defaultName);
                logger.info("No name provided, using default name from email: " + defaultName);
            } else {
                newUser.setNombre(nombre);
            }
            newUser.setCorreo(correo);
            newUser.setPasswordHash(password);

            Usuario saved = usuarioService.register(newUser);
            logger.info("User registered successfully: " + saved.getCorreo());

            if (rolStr != null && !rolStr.isBlank()) {
                try {
                    Long requestedRolId = Long.parseLong(rolStr);

                    if (!requestedRolId.equals(18L)) {
                        if (rolRepository.existsById(requestedRolId)) {
                            usuarioService.assignRolesToUser(saved.getIdUsuario(), List.of(requestedRolId));
                            logger.info("Assigned custom role ID " + requestedRolId + " to user: " + saved.getCorreo());
                        } else {
                            logger.warning("Requested role ID " + requestedRolId + " not found");
                        }
                    }
                } catch (NumberFormatException e) {
                    logger.warning("Invalid role ID format: " + rolStr);
                } catch (Exception e) {
                    logger.warning("Failed to assign custom role: " + e.getMessage());
                }
            }

            List<String> roles = usuarioService.getUserRoles(saved.getIdUsuario());
            String primaryRole = roles.isEmpty() ? "INVITADO" : roles.get(0);

            String token = jwtUtils.generateToken(saved.getCorreo(), primaryRole, roles);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Usuario registrado exitosamente. Por favor, verifica tu correo electrónico.");
            response.put("correo", saved.getCorreo());
            response.put("rol", primaryRole);
            response.put("roles", roles);
            response.put("token", token);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.severe("Registration failed: " + e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> creds) {
        logger.info("Received login request");

        if (!creds.containsKey("correo") || !creds.containsKey("password")) {
            logger.warning("Login failed: missing email or password");
            return ResponseEntity.badRequest().body(Map.of("error", "Correo y contraseña requeridos"));
        }

        String correo = creds.get("correo");
        String rawPassword = creds.get("password");

        logger.info("Login attempt for email: " + correo);

        try {
            Usuario usuario = usuarioService.findByCorreo(correo);

            if (!passwordEncoder.matches(rawPassword, usuario.getPasswordHash())) {
                logger.warning("Login failed for " + correo + ": invalid credentials");
                return ResponseEntity.status(401).body(Map.of("error", "Credenciales inválidas"));
            }

            if (!usuario.isActivo()) {
                logger.warning("Login failed for " + correo + ": inactive user");
                return ResponseEntity.status(403).body(Map.of("error", "Usuario inactivo"));
            }

//            if (!verificationTokenService.isEmailVerified(usuario.getIdUsuario())) {
//                logger.warning("Login failed for " + correo + ": email not verified");
//                return ResponseEntity.status(403).body(Map.of(
//                    "error", "Email no verificado",
//                    "requiresAction", "VERIFY_EMAIL"
//                ));
//            }

            List<String> roles = usuarioService.getUserRoles(usuario.getIdUsuario());

            if (roles.isEmpty()) {
                logger.warning("Login failed for " + correo + ": no roles assigned");
                return ResponseEntity.status(403).body(Map.of(
                    "error", "Usuario sin roles asignados",
                    "requiresAction", "AWAIT_ROLE_ASSIGNMENT"
                ));
            }

            String primaryRole = roles.get(0);

            if (!usuario.isPerfilCompleto()) {
                logger.warning("Login successful for " + correo + " but profile incomplete");
                logger.info("Profile complete status: " + usuario.getPerfilCompleto());

                String token = jwtUtils.generateToken(correo, primaryRole, roles);

                Map<String, Object> response = new HashMap<>();
                response.put("token", token);

                Map<String, Object> userData = new HashMap<>();
                userData.put("id", usuario.getIdUsuario());
                userData.put("nombre", usuario.getNombre());
                userData.put("correo", usuario.getCorreo());
                userData.put("rol", primaryRole);
                userData.put("roles", roles);
                userData.put("perfilCompleto", "N");
                userData.put("primerLogin", usuario.isPrimerLogin() ? "Y" : "N");

                response.put("user", userData);
                response.put("requiresAction", "COMPLETE_PROFILE");

                return ResponseEntity.ok(response);
            }

            logger.info("User " + correo + " authenticated successfully with role: " + primaryRole);

            boolean primerLogin = usuario.isPrimerLogin();
            if (primerLogin) {
                usuarioService.updateFirstLogin(usuario.getIdUsuario());
            }

            String token = jwtUtils.generateToken(correo, primaryRole, roles);

            Map<String, Object> response = new HashMap<>();
            response.put("token", token);

            Map<String, Object> userData = new HashMap<>();
            userData.put("id", usuario.getIdUsuario());
            userData.put("nombre", usuario.getNombre());
            userData.put("correo", usuario.getCorreo());
            userData.put("rol", primaryRole);
            userData.put("roles", roles);
            userData.put("perfilCompleto", "Y");
            userData.put("primerLogin", primerLogin ? "Y" : "N");

            response.put("user", userData);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.severe("Login failed: " + e.getMessage());
            return ResponseEntity.status(401).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/verify-email")
    public ResponseEntity<Map<String, Object>> verifyEmail(@RequestParam String token) {
        logger.info("Received email verification request with token: " + token);

        try {
            boolean verified = usuarioService.verifyEmail(token);

            if (verified) {
                logger.info("Email verified successfully with token: " + token);
                return ResponseEntity.ok(Map.of(
                    "message", "Email verificado exitosamente. Ahora puedes iniciar sesión."
                ));
            } else {
                logger.warning("Email verification failed with token: " + token);
                return ResponseEntity.badRequest().body(Map.of(
                    "error", "No se pudo verificar el email. El token puede ser inválido o haber expirado."
                ));
            }
        } catch (Exception e) {
            logger.severe("Email verification failed: " + e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/complete-profile")
    public ResponseEntity<Map<String, Object>> completeProfile(@RequestBody Map<String, Object> profileData) {
        logger.info("Received profile completion request");

        try {
            String correo = (String) profileData.get("correo");
            String nombre = (String) profileData.get("nombre");

            if (correo == null || correo.isBlank()) {
                logger.warning("Profile completion failed: missing email");
                return ResponseEntity.badRequest().body(Map.of("error", "Correo requerido"));
            }

            if (nombre == null || nombre.isBlank()) {
                logger.warning("Profile completion failed: missing name");
                return ResponseEntity.badRequest().body(Map.of("error", "Nombre requerido"));
            }

            Usuario usuario = usuarioService.findByCorreo(correo);

            usuario.setNombre(nombre);

            boolean completed = usuarioService.completeProfile(usuario.getIdUsuario());

            if (completed) {
                logger.info("Profile completed successfully for: " + correo);

                List<String> roles = usuarioService.getUserRoles(usuario.getIdUsuario());
                String primaryRole = roles.isEmpty() ? "INVITADO" : roles.get(0);

                String token = jwtUtils.generateToken(correo, primaryRole, roles);

                Map<String, Object> response = new HashMap<>();
                response.put("message", "Perfil completado exitosamente");
                response.put("token", token);

                Map<String, Object> userData = new HashMap<>();
                userData.put("id", usuario.getIdUsuario());
                userData.put("nombre", usuario.getNombre());
                userData.put("correo", usuario.getCorreo());
                userData.put("rol", primaryRole);
                userData.put("roles", roles);
                userData.put("perfilCompleto", "Y");
                userData.put("primerLogin", "N");

                response.put("user", userData);

                return ResponseEntity.ok(response);
            } else {
                logger.warning("Profile completion failed for: " + correo);
                return ResponseEntity.badRequest().body(Map.of("error", "No se pudo completar el perfil"));
            }
        } catch (Exception e) {
            logger.severe("Profile completion failed: " + e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
