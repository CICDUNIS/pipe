package com.example.pharmacy.service.impl;



import com.example.pharmacy.model.AuditoriaFarmacia;
import com.example.pharmacy.repository.AuditoriaFarmaciaRepository;
import com.example.pharmacy.service.AuditoriaService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuditoriaServiceImpl implements AuditoriaService {

    private final AuditoriaFarmaciaRepository repo;

    public AuditoriaServiceImpl(AuditoriaFarmaciaRepository repo) {
        this.repo = repo;
    }

    @Override
    public void registrar(String tablaAfectada, String tipoCambio, String descripcion, String usuario) {
        AuditoriaFarmacia af = new AuditoriaFarmacia();
        af.setTablaAfectada(tablaAfectada);
        af.setTipoCambio(tipoCambio);
        af.setDescripcion(descripcion);
        af.setUsuario(usuario);
        af.setFecha(LocalDateTime.now());

        repo.save(af);
    }
}


