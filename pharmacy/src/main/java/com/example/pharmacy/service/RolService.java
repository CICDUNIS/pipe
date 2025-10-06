package com.example.pharmacy.service;

import com.example.pharmacy.model.Rol;

import java.util.List;
import java.util.Optional;

public interface RolService {
    Rol create(Rol rol);
    Rol update(Long id, Rol rol);
    Rol findById(Long id);
    List<Rol> findAll();
    void delete(Long id);
    Optional<Rol> findByNombreRol(String nombreRol);
} 