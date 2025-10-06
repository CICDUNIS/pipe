package com.example.pharmacy.repository;

import com.example.pharmacy.model.Receta;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface RecetaRepository extends CrudRepository<Receta, Long> {
	Optional<Receta> findByCodigoReceta(String codigoReceta);
}
