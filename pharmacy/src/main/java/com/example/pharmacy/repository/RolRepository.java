package com.example.pharmacy.repository;

import com.example.pharmacy.model.Rol;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolRepository extends CrudRepository<Rol, Long> {
    Optional<Rol> findByNombreRol(String nombreRol);
} 