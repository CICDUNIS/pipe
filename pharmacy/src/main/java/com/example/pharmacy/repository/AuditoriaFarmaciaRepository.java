package com.example.pharmacy.repository;

import com.example.pharmacy.model.AuditoriaFarmacia;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditoriaFarmaciaRepository extends CrudRepository<AuditoriaFarmacia, Long> {
}



