package com.example.pharmacy.controllers;

import com.example.pharmacy.model.ComentarioMedicamento;
import com.example.pharmacy.model.Usuario;
import com.example.pharmacy.service.ComentarioMedicamentoService;
import com.example.pharmacy.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;

@RestController
@RequestMapping("/api/comentarios")
public class ComentarioMedicamentoController {

    @Autowired
    private ComentarioMedicamentoService comentarioService;
    
    @Autowired
    private UsuarioService usuarioService;
    
    @GetMapping("/medicamento/{idMedicamento}")
    public ResponseEntity<List<ComentarioMedicamento>> getByMedicamento(@PathVariable Long idMedicamento) {
        List<ComentarioMedicamento> comentarios = comentarioService.findByMedicamentoHierarchical(idMedicamento);
        return ResponseEntity.ok(comentarios);
    }
    

    @PostMapping
    public ResponseEntity<ComentarioMedicamento> create(@RequestBody ComentarioMedicamento comentario) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Usuario usuario = usuarioService.findByCorreo(email);
        
        comentario.setIdUsuario(usuario.getIdUsuario());
        
        ComentarioMedicamento created = comentarioService.create(comentario);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }
    
     @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String email = auth.getName();
            Usuario usuario = usuarioService.findByCorreo(email);
            
            ComentarioMedicamento comentario = comentarioService.findById(id);
            if (!comentario.getIdUsuario().equals(usuario.getIdUsuario()) && 
                !auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            
            comentarioService.delete(id);
            return ResponseEntity.noContent().build();
            
        } catch (DataIntegrityViolationException e) {
            System.err.println("Error deleting comment hierarchy: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (Exception e) {
            System.err.println("Unexpected error deleting comment: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
} 
