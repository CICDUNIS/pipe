package com.example.pharmacy.service;

import java.util.List;

public interface CompraMedicamentoService {

    void validarReceta(String codigoReceta, List<String> codigosMedicamentos);

    void validarCobertura(String numeroAfiliacion, List<String> codigosMedicamentos);
}









