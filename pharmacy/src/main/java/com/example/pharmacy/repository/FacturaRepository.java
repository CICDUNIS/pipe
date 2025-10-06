package com.example.pharmacy.repository;

import com.example.pharmacy.model.Factura;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FacturaRepository extends CrudRepository<Factura, Long> {
    Optional<Factura> findByIdVenta(Long idVenta);
} 