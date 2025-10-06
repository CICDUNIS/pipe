package com.example.pharmacy.repository;

import com.example.pharmacy.model.Carrito;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CarritoRepository extends CrudRepository<Carrito, Long> {
    Optional<Carrito> findByIdUsuarioAndStatus(Long idUsuario, String status);
    Iterable<Carrito> findAllByIdUsuarioAndStatus(Long idUsuario, String status);
} 