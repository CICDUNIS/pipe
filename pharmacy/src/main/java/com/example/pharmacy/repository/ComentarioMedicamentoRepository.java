package com.example.pharmacy.repository;

import com.example.pharmacy.model.ComentarioMedicamento;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComentarioMedicamentoRepository extends CrudRepository<ComentarioMedicamento, Long> {
    
    List<ComentarioMedicamento> findByIdMedicamento(Long idMedicamento);
    
    List<ComentarioMedicamento> findByIdMedicamentoAndParentIdIsNull(Long idMedicamento);
    
    List<ComentarioMedicamento> findByParentId(Long parentId);
} 
