package com.example.pharmacy.repository;

import com.example.pharmacy.model.Medicamento;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicamentoRepository extends CrudRepository<Medicamento, Long> {

    Optional<Medicamento> findByCodigo(String codigo);

    List<Medicamento> findByCategoria(String categoria);

    @Query("SELECT * FROM MEDICAMENTO ORDER BY ID_MEDICAMENTO DESC FETCH FIRST 10 ROWS ONLY")
    List<Medicamento> findTop10ByOrderByIdDesc();

    @Query("SELECT * FROM Medicamento " +
    "WHERE LOWER(nombre) LIKE '%' || LOWER(:term) || '%' " +
    "   OR LOWER(principio_activo) LIKE '%' || LOWER(:term) || '%' " +
    "   OR LOWER(descripcion) LIKE '%' || LOWER(:term) || '%' " +
    "   OR LOWER(marca) LIKE '%' || LOWER(:term) || '%'")
	    List<Medicamento> searchByTerm(String term);

}

