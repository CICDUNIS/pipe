package com.example.pharmacy.service;

import com.example.pharmacy.dto.RecetaDTO;

public interface RecetaService {

    RecetaDTO createReceta(RecetaDTO dto);

    RecetaDTO getRecetaWithDetails(Long idReceta);

    RecetaDTO updateReceta(Long idReceta, RecetaDTO dto);

    void deleteReceta(Long idReceta);
}


