package com.example.pharmacy.service;

import com.example.pharmacy.model.Medicamento;

import java.util.List;

public interface MedicamentoService {
    Medicamento create(Medicamento medicamento);
    Medicamento findById(Long id);
    List<Medicamento> findAll();
    Medicamento update(Long id, Medicamento med);
    void delete(Long id);
    boolean existsById(Long id);
    List<Medicamento> search(String term);
    List<Medicamento> findLastTen();

}
