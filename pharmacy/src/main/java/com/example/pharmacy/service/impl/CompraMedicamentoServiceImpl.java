package com.example.pharmacy.service.impl;

import com.example.pharmacy.dto.CoberturaMedicamentoDTO;
import com.example.pharmacy.dto.RecetaValidadaDTO;
import com.example.pharmacy.integration.HospitalClient;
import com.example.pharmacy.integration.SeguroClient;
import com.example.pharmacy.service.CompraMedicamentoService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompraMedicamentoServiceImpl implements CompraMedicamentoService {

    private final HospitalClient hospitalClient;
    private final SeguroClient seguroClient;

    public CompraMedicamentoServiceImpl(HospitalClient hospitalClient, SeguroClient seguroClient) {
        this.hospitalClient = hospitalClient;
        this.seguroClient = seguroClient;
    }

    @Override
    public void validarReceta(String codigoReceta, List<String> codigosMedicamentos) {
	System.out.println("Entra aca tambien, este es validar receta dentro de CompraMedicamentoServiceImpl");
	System.out.println(codigoReceta);
	System.out.println(codigosMedicamentos);

        RecetaValidadaDTO recetaValidada = hospitalClient.validarReceta(codigoReceta);

        if (!recetaValidada.isValida()) {
            throw new IllegalArgumentException("Receta inválida: " + codigoReceta);
        }

	System.out.println(recetaValidada);

	for (String codMed : codigosMedicamentos) {
		System.out.println(codMed);
		System.out.print(recetaValidada.getMedicamentos());
		if (!recetaValidada.getMedicamentos().contains(codMed)) {
			throw new IllegalArgumentException("Medicamento no autorizado en la receta: " + codMed);
		}  
	}
    }

    @Override
    public void validarCobertura(String numeroAfiliacion, List<String> codigosMedicamentos) {
        for (String codMed : codigosMedicamentos) {
            CoberturaMedicamentoDTO cobertura = seguroClient.validarMedicamento(numeroAfiliacion, codMed);

            if (!cobertura.isCubierto()) {
                throw new IllegalArgumentException("El medicamento " + codMed + " no tiene cobertura.");
            }
        }
    }
}



