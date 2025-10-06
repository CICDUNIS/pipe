package com.example.pharmacy.repository;

import com.example.pharmacy.model.VentaDetalle;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VentaDetalleRepository extends CrudRepository<VentaDetalle, Long> {
    List<VentaDetalle> findByIdVenta(Long idVenta);
}


