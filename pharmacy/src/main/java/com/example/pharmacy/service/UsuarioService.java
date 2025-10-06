package com.example.pharmacy.service;

import com.example.pharmacy.model.Usuario;
import com.example.pharmacy.dto.UserDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface UsuarioService {
    Usuario register(Usuario usuarioRaw);
    Usuario findByCorreo(String correo);

    Usuario updateMyProfile(String correo, UserDTO dto);
    UserDTO getMyProfile(String correo);

    boolean verifyEmail(String token);

    boolean activateUser(Long idUsuario, Long idRol);

    boolean deactivateUser(Long idUsuario);

    boolean completeProfile(Long idUsuario);


    boolean updateFirstLogin(Long idUsuario);

    List<UserDTO> getAllUsers();

    List<UserDTO> findUsersByFilters(String email, LocalDateTime fromDate, LocalDateTime toDate, String role);

    void assignRolesToUser(Long idUsuario, List<Long> roleIds);
    void removeRolFromUser(Long idUsuario, Long idRol);
    List<String> getUserRoles(Long idUsuario);
    boolean hasRole(Long idUsuario, String roleName);
}
