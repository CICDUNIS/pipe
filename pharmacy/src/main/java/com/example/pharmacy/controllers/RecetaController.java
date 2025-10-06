package com.example.pharmacy.controllers;

import com.example.pharmacy.dto.RecetaDTO;
import com.example.pharmacy.service.RecetaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recetas")
public class RecetaController {

    private final RecetaService recetaService;

    public RecetaController(RecetaService recetaService) {
        this.recetaService = recetaService;
    }

    @PostMapping
    public ResponseEntity<RecetaDTO> create(@RequestBody RecetaDTO dto) {
        RecetaDTO created = recetaService.createReceta(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecetaDTO> getOne(@PathVariable Long id) {
        RecetaDTO receta = recetaService.getRecetaWithDetails(id);
        return ResponseEntity.ok(receta);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecetaDTO> update(@PathVariable Long id, @RequestBody RecetaDTO dto) {
        RecetaDTO updated = recetaService.updateReceta(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        recetaService.deleteReceta(id);
        return ResponseEntity.noContent().build();
    }
}


