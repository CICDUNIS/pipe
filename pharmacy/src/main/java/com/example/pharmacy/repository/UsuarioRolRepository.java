package com.example.pharmacy.repository;

import com.example.pharmacy.model.UsuarioRol;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface UsuarioRolRepository extends CrudRepository<UsuarioRol, Long> {
    @Query("SELECT * FROM USUARIOROL WHERE ID_USUARIO = :idUsuario")
    List<UsuarioRol> findByUsuarioId(@Param("idUsuario") Long idUsuario);
    
    @Query("SELECT * FROM USUARIOROL WHERE ID_USUARIO = :idUsuario AND ID_ROL = :idRol")
    List<UsuarioRol> findByIdUsuarioAndIdRol(@Param("idUsuario") Long idUsuario, @Param("idRol") Long idRol);
    
    @Modifying
    @Query("DELETE FROM USUARIOROL WHERE ID_USUARIO = :idUsuario AND ID_ROL = :idRol")
    void deleteUsuarioRol(@Param("idUsuario") Long idUsuario, @Param("idRol") Long idRol);
    
    @Modifying
    @Query("INSERT INTO USUARIOROL (ID_USUARIO, ID_ROL) VALUES (:idUsuario, :idRol)")
    void insertUsuarioRol(@Param("idUsuario") Long idUsuario, @Param("idRol") Long idRol);
} 