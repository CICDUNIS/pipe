package com.example.pharmacy.service.impl;

import com.example.pharmacy.model.Rol;
import com.example.pharmacy.repository.RolRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
class RolServiceImplTest {

    @Mock
    private RolRepository rolRepository;

    @InjectMocks
    private RolServiceImpl service;

    @Test
    @DisplayName("create: guarda y retorna el rol")
    void create_success() {
        Rol rol = new Rol();
        rol.setNombreRol("ADMIN");
        when(rolRepository.save(rol)).thenReturn(rol);

        Rol result = service.create(rol);
        assertEquals(rol, result);
        verify(rolRepository).save(rol);
    }

    @Test
    @DisplayName("update: rol existente se actualiza y retorna")
    void update_success() {
        Long id = 1L;
        Rol rol = new Rol();
        rol.setNombreRol("USER");

        when(rolRepository.existsById(id)).thenReturn(true);
        when(rolRepository.save(any(Rol.class))).thenAnswer(i -> i.getArgument(0));

        Rol result = service.update(id, rol);
        assertEquals(id, result.getIdRol());
        assertEquals("USER", result.getNombreRol());
        verify(rolRepository).existsById(id);
        verify(rolRepository).save(rol);
    }

    @Test
    @DisplayName("update: rol no existe lanza NoSuchElementException")
    void update_notFound() {
        Long id = 2L;
        Rol rol = new Rol();
        when(rolRepository.existsById(id)).thenReturn(false);

        NoSuchElementException ex = assertThrows(NoSuchElementException.class,
            () -> service.update(id, rol));
        assertTrue(ex.getMessage().contains("Rol not found with id: " + id));
        verify(rolRepository).existsById(id);
        verify(rolRepository, never()).save(any());
    }

    @Test
    @DisplayName("findById: existe retorna rol, no existe lanza NoSuchElementException")
    void findById_behavior() {
        Rol rol = new Rol(); rol.setIdRol(3L);
        when(rolRepository.findById(3L)).thenReturn(Optional.of(rol));
        Rol result = service.findById(3L);
        assertEquals(rol, result);

        when(rolRepository.findById(4L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> service.findById(4L));
    }

    @Test
    @DisplayName("findAll: retorna lista de roles")
    void findAll_success() {
        Rol a = new Rol(); a.setIdRol(5L);
        Rol b = new Rol(); b.setIdRol(6L);
        when(rolRepository.findAll()).thenReturn(Arrays.asList(a, b));

        List<Rol> list = service.findAll();
        assertEquals(2, list.size());
        assertTrue(list.contains(a));
        assertTrue(list.contains(b));
    }

    @Test
    @DisplayName("delete: rol existente elimina, no existe lanza NoSuchElementException")
    void delete_behavior() {
        Long id = 7L;
        when(rolRepository.existsById(id)).thenReturn(true);
        doNothing().when(rolRepository).deleteById(id);

        service.delete(id);
        verify(rolRepository).existsById(id);
        verify(rolRepository).deleteById(id);

        when(rolRepository.existsById(id)).thenReturn(false);
        assertThrows(NoSuchElementException.class, () -> service.delete(id));
    }

    @Test
    @DisplayName("findByNombreRol: delega al repositorio y retorna Optional")
    void findByNombreRol_success() {
        String nombre = "GUEST";
        Rol rol = new Rol(); rol.setNombreRol(nombre);
        when(rolRepository.findByNombreRol(nombre)).thenReturn(Optional.of(rol));

        Optional<Rol> opt = service.findByNombreRol(nombre);
        assertTrue(opt.isPresent());
        assertEquals(rol, opt.get());
        verify(rolRepository).findByNombreRol(nombre);
    }
}
