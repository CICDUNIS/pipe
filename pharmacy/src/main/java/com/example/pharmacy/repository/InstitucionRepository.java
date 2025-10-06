package com.example.pharmacy.repository;

import com.example.pharmacy.model.Institucion;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InstitucionRepository extends CrudRepository<Institucion, Long> {

    Optional<Institucion> findByCodigoInstitucion(String codigoInstitucion);
}


