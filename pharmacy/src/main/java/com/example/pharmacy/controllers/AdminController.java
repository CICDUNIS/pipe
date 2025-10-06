package com.example.pharmacy.controllers;

import com.example.pharmacy.dto.UserDTO;
import com.example.pharmacy.model.Rol;
import com.example.pharmacy.repository.RolRepository;
import com.example.pharmacy.service.UsuarioService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private static final Logger logger = Logger.getLogger(AdminController.class.getName());
    private final UsuarioService usuarioService;
    private final RolRepository rolRepository;

    public AdminController(UsuarioService usuarioService, RolRepository rolRepository) {
        this.usuarioService = usuarioService;
        this.rolRepository = rolRepository;
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        logger.info("Getting all users");
        List<UserDTO> users = usuarioService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/filter")
    public ResponseEntity<List<UserDTO>> filterUsers(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toDate,
            @RequestParam(required = false) String role) {
        
        logger.info("Filtering users with email: " + email + ", fromDate: " + fromDate + ", toDate: " + toDate + ", role: " + role);
        List<UserDTO> filteredUsers = usuarioService.findUsersByFilters(email, fromDate, toDate, role);
        return ResponseEntity.ok(filteredUsers);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        logger.info("Getting user with ID: " + id);
        try {
            UserDTO user = usuarioService.getMyProfile(usuarioService.findByCorreo(id.toString()).getCorreo());
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            logger.warning("User not found with ID: " + id);
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/users/{id}/activate")
    public ResponseEntity<Map<String, Object>> activateUser(@PathVariable Long id, @RequestBody Map<String, Long> activationData) {
        logger.info("Activating user with ID: " + id);
        
        Long roleId = activationData.get("roleId");
        if (roleId == null) {
            logger.warning("Role ID is required for activation");
            return ResponseEntity.badRequest().body(Map.of("error", "Role ID is required"));
        }
        
        try {
            boolean activated = usuarioService.activateUser(id, roleId);
            
            if (activated) {
                logger.info("User activated successfully: " + id);
                return ResponseEntity.ok(Map.of("message", "User activated successfully"));
            } else {
                logger.warning("Failed to activate user: " + id);
                return ResponseEntity.badRequest().body(Map.of("error", "Failed to activate user"));
            }
        } catch (Exception e) {
            logger.severe("Error activating user: " + e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/users/{id}/deactivate")
    public ResponseEntity<Map<String, Object>> deactivateUser(@PathVariable Long id) {
        logger.info("Deactivating user with ID: " + id);
        
        try {
            boolean deactivated = usuarioService.deactivateUser(id);
            
            if (deactivated) {
                logger.info("User deactivated successfully: " + id);
                return ResponseEntity.ok(Map.of("message", "User deactivated successfully"));
            } else {
                logger.warning("Failed to deactivate user: " + id);
                return ResponseEntity.badRequest().body(Map.of("error", "Failed to deactivate user"));
            }
        } catch (Exception e) {
            logger.severe("Error deactivating user: " + e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/users/{id}/roles")
    public ResponseEntity<Map<String, Object>> assignRoles(@PathVariable Long id, @RequestBody Map<String, List<Long>> rolesData) {
        logger.info("Assigning roles to user with ID: " + id);
        
        List<Long> roleIds = rolesData.get("roleIds");
        if (roleIds == null || roleIds.isEmpty()) {
            logger.warning("Role IDs are required");
            return ResponseEntity.badRequest().body(Map.of("error", "Role IDs are required"));
        }
        
        try {
            usuarioService.assignRolesToUser(id, roleIds);
            logger.info("Roles assigned successfully to user: " + id);
            return ResponseEntity.ok(Map.of("message", "Roles assigned successfully"));
        } catch (Exception e) {
            logger.severe("Error assigning roles: " + e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/users/{userId}/roles/{roleId}")
    public ResponseEntity<Map<String, Object>> removeRole(@PathVariable Long userId, @PathVariable Long roleId) {
        logger.info("Removing role " + roleId + " from user with ID: " + userId);
        
        try {
            usuarioService.removeRolFromUser(userId, roleId);
            logger.info("Role removed successfully from user: " + userId);
            return ResponseEntity.ok(Map.of("message", "Role removed successfully"));
        } catch (Exception e) {
            logger.severe("Error removing role: " + e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/roles")
    public ResponseEntity<List<Rol>> getAllRoles() {
        logger.info("Getting all roles");
        List<Rol> roles = StreamSupport.stream(rolRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
        return ResponseEntity.ok(roles);
    }
} 