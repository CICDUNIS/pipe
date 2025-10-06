package com.example.pharmacy.repository;

import com.example.pharmacy.model.RecetaDetalle;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RecetaDetalleRepository extends CrudRepository<RecetaDetalle, Long> {
    List<RecetaDetalle> findByIdReceta(Long idReceta);
}



