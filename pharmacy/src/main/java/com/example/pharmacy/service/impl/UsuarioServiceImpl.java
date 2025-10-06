package com.example.pharmacy.service.impl;

import com.example.pharmacy.model.Usuario;
import com.example.pharmacy.model.UsuarioRol;
import com.example.pharmacy.model.Rol;
import com.example.pharmacy.model.VerificationToken;
import com.example.pharmacy.dto.UserDTO;
import com.example.pharmacy.repository.UsuarioRepository;
import com.example.pharmacy.repository.UsuarioRolRepository;
import com.example.pharmacy.repository.RolRepository;
import com.example.pharmacy.service.UsuarioService;
import com.example.pharmacy.service.EmailService;
import com.example.pharmacy.service.VerificationTokenService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.logging.Logger;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private static final Logger logger = Logger.getLogger(UsuarioServiceImpl.class.getName());

    private final UsuarioRepository usuarioRepository;
    private final UsuarioRolRepository usuarioRolRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final VerificationTokenService verificationTokenService;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository,
                            UsuarioRolRepository usuarioRolRepository,
                            RolRepository rolRepository,
                            PasswordEncoder passwordEncoder,
                            EmailService emailService,
                            VerificationTokenService verificationTokenService) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioRolRepository = usuarioRolRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.verificationTokenService = verificationTokenService;
    }

    @Override
    @Transactional
    public Usuario register(Usuario usuarioRaw) {
        if (usuarioRepository.findByCorreo(usuarioRaw.getCorreo()).isPresent()) {
            throw new RuntimeException("Ya existe un usuario con correo: " + usuarioRaw.getCorreo());
        }

        String hashed = passwordEncoder.encode(usuarioRaw.getPasswordHash());
        usuarioRaw.setPasswordHash(hashed);

        usuarioRaw.setActivo("N");
        usuarioRaw.setPerfilCompleto("N");
        usuarioRaw.setPrimerLogin("Y");
        usuarioRaw.setFechaCreacion(LocalDateTime.now());

        Usuario savedUser = usuarioRepository.save(usuarioRaw);
        logger.info("User registered successfully: " + savedUser.getCorreo());

        VerificationToken verificationToken = verificationTokenService.createVerificationToken(savedUser);

        boolean emailSent = emailService.sendVerificationEmail(savedUser, verificationToken.getToken());
        if (!emailSent) {
            logger.warning("Failed to send verification email to: " + savedUser.getCorreo());
        }

        boolean adminNotified = emailService.sendAdminNotificationEmail(savedUser);
        if (!adminNotified) {
            logger.warning("Failed to send admin notification about new user: " + savedUser.getCorreo());
        }

        return savedUser;
    }

    @Override
    @Transactional
    public boolean verifyEmail(String token) {
        boolean verified = verificationTokenService.verifyToken(token);

        if (verified) {
            VerificationToken verificationToken = verificationTokenService.findByToken(token)
                    .orElseThrow(() -> new RuntimeException("Token not found: " + token));

            Long userId = verificationToken.getIdUsuario();
            Usuario user = usuarioRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found for token: " + token));

            boolean emailSent = emailService.sendWelcomeEmail(user);
            if (!emailSent) {
                logger.warning("Failed to send welcome email to: " + user.getCorreo());
            }
        }

        return verified;
    }

    @Override
    @Transactional
    public boolean activateUser(Long idUsuario, Long idRol) {
        Usuario user = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado con id: " + idUsuario));

        Rol rol = rolRepository.findById(idRol)
                .orElseThrow(() -> new NoSuchElementException("Rol no encontrado con id: " + idRol));

        user.setActivo("Y");
        usuarioRepository.save(user);

        assignRolesToUser(idUsuario, List.of(idRol));

        boolean emailSent = emailService.sendAccountActivationEmail(user, rol.getNombreRol());
        if (!emailSent) {
            logger.warning("Failed to send account activation email to: " + user.getCorreo());
        }

        return true;
    }

    @Override
    @Transactional
    public boolean deactivateUser(Long idUsuario) {
        Usuario user = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado con id: " + idUsuario));

        user.setActivo("N");
        usuarioRepository.save(user);

        return true;
    }

    @Override
    @Transactional
    public boolean completeProfile(Long idUsuario) {
        Usuario user = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado con id: " + idUsuario));

        user.setPerfilCompleto("Y");
        user.setPrimerLogin("N");
        usuarioRepository.save(user);

        return true;
    }

    @Override
    @Transactional
    public boolean updateFirstLogin(Long idUsuario) {
        Usuario user = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado con id: " + idUsuario));

        user.setPrimerLogin("N");
        usuarioRepository.save(user);

        return true;
    }

    @Override
    public Usuario findByCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado con correo: " + correo));
    }

    @Override
    public Usuario updateMyProfile(String correo, UserDTO dto) {
        Usuario user = usuarioRepository.findByCorreo(correo)
            .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado con correo: " + correo));

        user.setNombre(dto.getNombre());
        user.setCorreo(dto.getCorreo());
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            String hashed = passwordEncoder.encode(dto.getPassword());
            user.setPasswordHash(hashed);
        }

        return usuarioRepository.save(user);
    }

    @Override
    public UserDTO getMyProfile(String correo) {
        Usuario user = usuarioRepository.findByCorreo(correo)
            .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado con correo: " + correo));

        UserDTO dto = toDTO(user);
        List<String> roles = getUserRoles(user.getIdUsuario());
        if (!roles.isEmpty()) {
            dto.setRol(roles.get(0));
        }
        dto.setRoles(roles);

        return dto;
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<UserDTO> userDTOs = new ArrayList<>();
        usuarioRepository.findAll().forEach(user -> {
            UserDTO dto = toDTO(user);
            List<String> roles = getUserRoles(user.getIdUsuario());
            if (!roles.isEmpty()) {
                dto.setRol(roles.get(0));
            }
            dto.setRoles(roles);
            userDTOs.add(dto);
        });
        return userDTOs;
    }

    @Override
    public List<UserDTO> findUsersByFilters(String email, LocalDateTime fromDate, LocalDateTime toDate, String role) {
        List<UserDTO> allUsers = getAllUsers();

        return allUsers.stream()
            .filter(user -> email == null || email.isEmpty() || user.getCorreo().toLowerCase().contains(email.toLowerCase()))
            .filter(user -> fromDate == null || user.getFechaCreacion() == null || !user.getFechaCreacion().isBefore(fromDate))
            .filter(user -> toDate == null || user.getFechaCreacion() == null || !user.getFechaCreacion().isAfter(toDate))
            .filter(user -> role == null || role.isEmpty() || user.getRoles().contains(role))
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void assignRolesToUser(Long idUsuario, List<Long> roleIds) {
        if (!usuarioRepository.existsById(idUsuario)) {
            throw new NoSuchElementException("Usuario no encontrado con id: " + idUsuario);
        }

        for (Long roleId : roleIds) {
            if (!rolRepository.existsById(roleId)) {
                throw new NoSuchElementException("Rol no encontrado con id: " + roleId);
            }

            List<UsuarioRol> existingRoles = usuarioRolRepository.findByIdUsuarioAndIdRol(idUsuario, roleId);
            if (existingRoles.isEmpty()) {
                try {
                    usuarioRolRepository.insertUsuarioRol(idUsuario, roleId);
                    logger.info("Role " + roleId + " assigned to user " + idUsuario);
                } catch (Exception e) {
                    logger.severe("Error assigning role " + roleId + " to user " + idUsuario + ": " + e.getMessage());
                    throw new RuntimeException("Error assigning role to user", e);
                }
            }
        }
    }

    @Override
    @Transactional
    public void removeRolFromUser(Long idUsuario, Long idRol) {
        if (!usuarioRepository.existsById(idUsuario)) {
            throw new NoSuchElementException("Usuario no encontrado con id: " + idUsuario);
        }

        if (!rolRepository.existsById(idRol)) {
            throw new NoSuchElementException("Rol no encontrado con id: " + idRol);
        }

        usuarioRolRepository.deleteUsuarioRol(idUsuario, idRol);
    }

    @Override
    public List<String> getUserRoles(Long idUsuario) {
        List<UsuarioRol> userRoles = usuarioRolRepository.findByUsuarioId(idUsuario);

        List<String> roleNames = new ArrayList<>();
        for (UsuarioRol userRole : userRoles) {
            rolRepository.findById(userRole.getIdRol()).ifPresent(role -> {
                roleNames.add(role.getNombreRol());
            });
        }

        return roleNames;
    }

    @Override
    public boolean hasRole(Long idUsuario, String roleName) {
        List<String> roles = getUserRoles(idUsuario);
        return roles.contains(roleName);
    }

    private UserDTO toDTO(Usuario user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getIdUsuario());
        dto.setNombre(user.getNombre());
        dto.setCorreo(user.getCorreo());
        dto.setActivo(user.getActivo());
        dto.setPerfilCompleto(user.isPerfilCompleto());
        dto.setPrimerLogin(user.isPrimerLogin());
        dto.setFechaCreacion(user.getFechaCreacion());
        return dto;
    }
}
