package com.example.pharmacy.service.impl;

import com.example.pharmacy.dto.RecetaDTO;
import com.example.pharmacy.dto.RecetaDetalleDTO;
import com.example.pharmacy.model.Receta;
import com.example.pharmacy.model.RecetaDetalle;
import com.example.pharmacy.repository.RecetaDetalleRepository;
import com.example.pharmacy.repository.RecetaRepository;
import com.example.pharmacy.service.RecetaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.pharmacy.service.AuditoriaService;
import org.springframework.security.core.context.SecurityContextHolder;
import com.example.pharmacy.util.UserDetails;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class RecetaServiceImpl implements RecetaService {

    private final RecetaRepository recetaRepository;
    private final RecetaDetalleRepository detalleRepository;
    private final AuditoriaService auditoriaService;
    private final UserDetails userDetails;

    public RecetaServiceImpl(RecetaRepository recetaRepository, RecetaDetalleRepository detalleRepository, AuditoriaService auditoriaService, UserDetails userDetails) {
        this.recetaRepository = recetaRepository;
        this.detalleRepository = detalleRepository;
	this.auditoriaService = auditoriaService;
	this.userDetails = userDetails;
    }

    @Override
    @Transactional
    public RecetaDTO createReceta(RecetaDTO dto) {
        Receta receta = new Receta();
        receta.setCodigoReceta(dto.getCodigoReceta());
        receta.setFecha(LocalDateTime.now());        
        receta.setIdUsuario(dto.getIdUsuario());
        receta.setAprobadoSeguro("N");              
        receta.setPdfUrl(dto.getPdfUrl());

        Receta savedReceta = recetaRepository.save(receta);

        for (RecetaDetalleDTO detDto : dto.getDetalles()) {
            RecetaDetalle det = new RecetaDetalle();
            det.setIdReceta(savedReceta.getIdReceta()); 
            det.setIdMedicamento(detDto.getIdMedicamento());
            det.setDosis(detDto.getDosis());
            det.setFrecuencia(detDto.getFrecuencia());
            det.setDuracion(detDto.getDuracion());
            det.setCantidadRequerida(detDto.getCantidadRequerida());
            det.setObservaciones(detDto.getObservaciones());

	    detalleRepository.save(det);
        }

	auditoriaService.registrar("Receta", "INSERT", "Creacion receta con ID = " + receta.getIdReceta(), "usuario =" + userDetails.getUsuarioActual());


        return getRecetaWithDetails(savedReceta.getIdReceta());
    }

    @Override
    public RecetaDTO getRecetaWithDetails(Long idReceta) {
        Receta receta = recetaRepository.findById(idReceta)
                .orElseThrow(() -> new NoSuchElementException("Receta no encontrada: " + idReceta));

        List<RecetaDetalle> detalles = detalleRepository.findByIdReceta(idReceta);

        RecetaDTO dto = new RecetaDTO();
        dto.setIdReceta(receta.getIdReceta());
        dto.setCodigoReceta(receta.getCodigoReceta());
        dto.setFecha(receta.getFecha());
        dto.setIdUsuario(receta.getIdUsuario());
        dto.setAprobadoSeguro(receta.getAprobadoSeguro());
        dto.setPdfUrl(receta.getPdfUrl());

        List<RecetaDetalleDTO> detDtoList = detalles.stream().map(d -> {
            RecetaDetalleDTO dd = new RecetaDetalleDTO();
            dd.setIdDetalle(d.getIdDetalle());
            dd.setIdMedicamento(d.getIdMedicamento());
            dd.setDosis(d.getDosis());
            dd.setFrecuencia(d.getFrecuencia());
            dd.setDuracion(d.getDuracion());
            dd.setCantidadRequerida(d.getCantidadRequerida());
            dd.setObservaciones(d.getObservaciones());
            return dd;
        }).collect(Collectors.toList());
        dto.setDetalles(detDtoList);

        return dto;
    }

    @Override
    public RecetaDTO updateReceta(Long idReceta, RecetaDTO dto) {
        Receta receta = recetaRepository.findById(idReceta)
                .orElseThrow(() -> new NoSuchElementException("Receta no encontrada: " + idReceta));

        receta.setCodigoReceta(dto.getCodigoReceta());
        receta.setPdfUrl(dto.getPdfUrl());
        recetaRepository.save(receta);

        List<RecetaDetalle> actuales = detalleRepository.findByIdReceta(idReceta);
        for (RecetaDetalle rd : actuales) {
            detalleRepository.deleteById(rd.getIdDetalle());
        }

        for (RecetaDetalleDTO detDto : dto.getDetalles()) {
            RecetaDetalle det = new RecetaDetalle();
            det.setIdReceta(idReceta);
            det.setIdMedicamento(detDto.getIdMedicamento());
            det.setDosis(detDto.getDosis());
            det.setFrecuencia(detDto.getFrecuencia());
            det.setDuracion(detDto.getDuracion());
            det.setCantidadRequerida(detDto.getCantidadRequerida());
            det.setObservaciones(detDto.getObservaciones());
            detalleRepository.save(det);
        }

	auditoriaService.registrar("Receta", "UPDATE", "Actualizacion receta con ID = " + receta.getIdReceta(), "usuario =" + userDetails.getUsuarioActual());


        return getRecetaWithDetails(idReceta);
    }

    @Override
    public void deleteReceta(Long idReceta) {
        List<RecetaDetalle> detalles = detalleRepository.findByIdReceta(idReceta);
        for (RecetaDetalle d : detalles) {
            detalleRepository.deleteById(d.getIdDetalle());
        }
        recetaRepository.deleteById(idReceta);
    }

//    private String getUsuarioActual() {
//	    if (SecurityContextHolder.getContext().getAuthentication() != null) {
//		    return SecurityContextHolder.getContext().getAuthentication().getName();
//	    }
//	    return "anonymous";
//    }
}


