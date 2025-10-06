package com.example.pharmacy.service;

import com.example.pharmacy.model.ComentarioMedicamento;
import java.util.List;

public interface ComentarioMedicamentoService {
    
    ComentarioMedicamento create(ComentarioMedicamento comentario);
    
    ComentarioMedicamento findById(Long id);
    
    List<ComentarioMedicamento> findByMedicamento(Long idMedicamento);
    
    List<ComentarioMedicamento> findByMedicamentoHierarchical(Long idMedicamento);
    
    void delete(Long id);
} 
