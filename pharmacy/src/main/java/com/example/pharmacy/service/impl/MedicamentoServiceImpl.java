package com.example.pharmacy.service.impl;

import com.example.pharmacy.model.Medicamento;
import com.example.pharmacy.repository.MedicamentoRepository;
import com.example.pharmacy.service.AuditoriaService;
import com.example.pharmacy.service.MedicamentoService;
import org.springframework.security.core.context.SecurityContextHolder;
import com.example.pharmacy.util.UserDetails;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class MedicamentoServiceImpl implements MedicamentoService {
    private static final Logger logger = LoggerFactory.getLogger(MedicamentoServiceImpl.class);
    private final MedicamentoRepository repo;
    private final AuditoriaService auditoriaService;
    private final UserDetails userDetails;

    public MedicamentoServiceImpl(MedicamentoRepository repo, AuditoriaService auditoriaService, UserDetails userDetails) {
        this.repo = repo;
	this.auditoriaService = auditoriaService;
	this.userDetails = userDetails;
    }

    @Override
    public Medicamento create(Medicamento medicamento) {
        logger.debug("Creating medicamento: {}", medicamento);
        
        if (medicamento.getPrecio() != null && medicamento.getPrecio() < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo");
        }
        if (medicamento.getStock() != null && medicamento.getStock() < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }
        
        ensureValidRequiereReceta(medicamento);

	Medicamento savedMedicamento = repo.save(medicamento);
	savedMedicamento.getNombre();
	logger.debug("Saved medicamento: {}", savedMedicamento);

	auditoriaService.registrar("Medicamento", "INSERT", "Creacion medicamento con ID = " + savedMedicamento.getIdMedicamento(), "usuario =" + userDetails.getUsuarioActual());

        return savedMedicamento;
    }
    
    private void ensureValidRequiereReceta(Medicamento medicamento) {
        String requiereReceta = medicamento.getRequiereReceta();
        if (requiereReceta == null) {
            medicamento.setRequiereReceta("N");
        } else if (requiereReceta.equals("true") || requiereReceta.equals("TRUE") || requiereReceta.equals("True")) {
            medicamento.setRequiereReceta("Y");
        } else if (requiereReceta.equals("false") || requiereReceta.equals("FALSE") || requiereReceta.equals("False")) {
            medicamento.setRequiereReceta("N");
        } else if (!requiereReceta.equals("Y") && !requiereReceta.equals("N") && 
                   !requiereReceta.equals("y") && !requiereReceta.equals("n")) {
            logger.warn("Invalid requiereReceta value: {}. Defaulting to 'N'", requiereReceta);
            medicamento.setRequiereReceta("N");
        } else if (requiereReceta.equals("y")) {
            medicamento.setRequiereReceta("Y");
        } else if (requiereReceta.equals("n")) {
            medicamento.setRequiereReceta("N");
        }
    }

    @Override
    public Medicamento findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Medicamento no encontrado con ID=" + id));
    }

    @Override
    public List<Medicamento> findAll() {
        return (List<Medicamento>) repo.findAll();
    }

    @Override
    public Medicamento update(Long id, Medicamento med) {
        Medicamento existing = findById(id);

        existing.setCodigo(med.getCodigo());
	existing.setNombre(med.getNombre());
        existing.setCategoria(med.getCategoria());
        existing.setPrincipioActivo(med.getPrincipioActivo());
        existing.setDescripcion(med.getDescripcion());
        existing.setFotoUrl(med.getFotoUrl());
        existing.setConcentracion(med.getConcentracion());
        existing.setPresentacion(med.getPresentacion());
        existing.setNumeroUnidades(med.getNumeroUnidades());
        existing.setMarca(med.getMarca());
        
        med.setRequiereReceta(med.getRequiereReceta());
        ensureValidRequiereReceta(med);
        existing.setRequiereReceta(med.getRequiereReceta());
        
        existing.setStock(med.getStock());
        existing.setPrecio(med.getPrecio());

	auditoriaService.registrar("Medicamento", "UPDATE", "Actualizacion del medicamento con ID" + id, "usuario =" + userDetails.getUsuarioActual());

        return repo.save(existing);
    }

    @Override
    public void delete(Long id) {
        repo.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
	    return repo.existsById(id);
    }

    @Override
    public List<Medicamento> search(String term) {
	    return repo.searchByTerm(term == null ? "" : term);
    }

    @Override
    public List<Medicamento> findLastTen() {
	    return repo.findTop10ByOrderByIdDesc();
    }

//    private String getUsuarioActual() {
//
//	    if (SecurityContextHolder.getContext().getAuthentication() != null) {
//		    return SecurityContextHolder.getContext().getAuthentication().getName();
//	    }
//	    return "anonymous";
//    }
}

