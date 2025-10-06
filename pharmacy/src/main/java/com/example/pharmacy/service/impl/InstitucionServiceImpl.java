package com.example.pharmacy.service.impl;

import com.example.pharmacy.model.Institucion;
import com.example.pharmacy.repository.InstitucionRepository;
import com.example.pharmacy.service.InstitucionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class InstitucionServiceImpl implements InstitucionService {

    private final InstitucionRepository repo;

    public InstitucionServiceImpl(InstitucionRepository repo) {
        this.repo = repo;
    }

    @Override
    public Institucion create(Institucion institucion) {
        return repo.save(institucion);
    }

    @Override
    public Institucion update(Long id, Institucion inst) {
        Institucion existing = repo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Institución no encontrada con ID=" + id));

        existing.setCodigoInstitucion(inst.getCodigoInstitucion());
        existing.setNombreInstitucion(inst.getNombreInstitucion());
        existing.setTipoInstitucion(inst.getTipoInstitucion());

        return repo.save(existing);
    }

    @Override
    public Institucion findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Institución no encontrada con ID=" + id));
    }

    @Override
    public List<Institucion> findAll() {
        return (List<Institucion>) repo.findAll();
    }

    @Override
    public void delete(Long id) {
        repo.deleteById(id);
    }
}


