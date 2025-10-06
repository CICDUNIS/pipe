package com.example.pharmacy.service;

import com.example.pharmacy.model.Institucion;

import java.util.List;

public interface InstitucionService {

    Institucion create(Institucion institucion);
    Institucion update(Long id, Institucion institucion);
    Institucion findById(Long id);
    List<Institucion> findAll();
    void delete(Long id);
}



