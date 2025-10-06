package com.example.pharmacy.service.impl;

import com.example.pharmacy.model.Rol;
import com.example.pharmacy.repository.RolRepository;
import com.example.pharmacy.service.RolService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class RolServiceImpl implements RolService {

    private final RolRepository rolRepository;

    public RolServiceImpl(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    @Override
    public Rol create(Rol rol) {
        return rolRepository.save(rol);
    }

    @Override
    public Rol update(Long id, Rol rol) {
        if (!rolRepository.existsById(id)) {
            throw new NoSuchElementException("Rol not found with id: " + id);
        }
        rol.setIdRol(id);
        return rolRepository.save(rol);
    }

    @Override
    public Rol findById(Long id) {
        return rolRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Rol not found with id: " + id));
    }

    @Override
    public List<Rol> findAll() {
        List<Rol> roles = new ArrayList<>();
        rolRepository.findAll().forEach(roles::add);
        return roles;
    }

    @Override
    public void delete(Long id) {
        if (!rolRepository.existsById(id)) {
            throw new NoSuchElementException("Rol not found with id: " + id);
        }
        rolRepository.deleteById(id);
    }

    @Override
    public Optional<Rol> findByNombreRol(String nombreRol) {
        return rolRepository.findByNombreRol(nombreRol);
    }
} 