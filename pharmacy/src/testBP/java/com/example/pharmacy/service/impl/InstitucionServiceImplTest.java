package com.example.pharmacy.service.impl;

import com.example.pharmacy.model.Institucion;
import com.example.pharmacy.repository.InstitucionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InstitucionServiceImplTest {

    @Mock
    private InstitucionRepository repo;

    @InjectMocks
    private InstitucionServiceImpl service;

    @Test
    @DisplayName("create: guarda y retorna la institución")
    void create_success() {
        Institucion inst = new Institucion();
        inst.setCodigoInstitucion("C1");
        inst.setNombreInstitucion("Inst A");
        inst.setTipoInstitucion("Tipo1");

        when(repo.save(inst)).thenReturn(inst);

        Institucion result = service.create(inst);
        assertEquals(inst, result);
        verify(repo).save(inst);
    }

    @Test
    @DisplayName("update: institución existente se actualiza y retorna")
    void update_success() {
        Long id = 10L;
        Institucion existing = new Institucion();
        existing.setIdInstitucion(id);
        existing.setCodigoInstitucion("C0");
        existing.setNombreInstitucion("Old");
        existing.setTipoInstitucion("T0");

        Institucion updates = new Institucion();
        updates.setCodigoInstitucion("C1");
        updates.setNombreInstitucion("New");
        updates.setTipoInstitucion("T1");

        when(repo.findById(id)).thenReturn(Optional.of(existing));
        when(repo.save(existing)).thenReturn(existing);

        Institucion result = service.update(id, updates);

        assertEquals("C1", result.getCodigoInstitucion());
        assertEquals("New", result.getNombreInstitucion());
        assertEquals("T1", result.getTipoInstitucion());
        verify(repo).findById(id);
        verify(repo).save(existing);
    }

    @Test
    @DisplayName("update: institución no existe lanza NoSuchElementException")
    void update_notFound() {
        Long id = 5L;
        Institucion inst = new Institucion();
        when(repo.findById(id)).thenReturn(Optional.empty());

        NoSuchElementException ex = assertThrows(NoSuchElementException.class,
            () -> service.update(id, inst));
        assertTrue(ex.getMessage().contains("Institución no encontrada con ID=" + id));
    }

    @Test
    @DisplayName("findById: existe retorna institución")
    void findById_success() {
        Long id = 7L;
        Institucion inst = new Institucion(); inst.setIdInstitucion(id);
        when(repo.findById(id)).thenReturn(Optional.of(inst));

        Institucion result = service.findById(id);
        assertEquals(inst, result);
    }

    @Test
    @DisplayName("findById: no existe lanza NoSuchElementException")
    void findById_notFound() {
        Long id = 8L;
        when(repo.findById(id)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> service.findById(id));
    }

    @Test
    @DisplayName("findAll: retorna lista de instituciones")
    void findAll_success() {
        Institucion a = new Institucion(); a.setIdInstitucion(1L);
        Institucion b = new Institucion(); b.setIdInstitucion(2L);
        when(repo.findAll()).thenReturn(Arrays.asList(a, b));

        List<Institucion> list = service.findAll();
        assertEquals(2, list.size());
        assertTrue(list.contains(a));
        assertTrue(list.contains(b));
    }

    @Test
    @DisplayName("delete: invoca deleteById")
    void delete_success() {
        Long id = 15L;
        doNothing().when(repo).deleteById(id);

        service.delete(id);
        verify(repo).deleteById(id);
    }
}
