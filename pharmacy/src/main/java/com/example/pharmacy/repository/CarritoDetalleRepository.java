package com.example.pharmacy.repository;

import com.example.pharmacy.model.CarritoDetalle;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CarritoDetalleRepository extends CrudRepository<CarritoDetalle, Long> {
    List<CarritoDetalle> findByIdCart(Long idCart);
    Optional<CarritoDetalle> findByIdCartAndIdMedicamento(Long idCart, Long idMedicamento);
} 