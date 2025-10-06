package com.example.pharmacy.controllers;

import com.example.pharmacy.model.Institucion;
import com.example.pharmacy.service.InstitucionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/instituciones")
public class InstitucionController {

    private final InstitucionService service;

    public InstitucionController(InstitucionService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Institucion>> getAll() {
        List<Institucion> list = service.findAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Institucion> getById(@PathVariable Long id) {
        Institucion inst = service.findById(id);
        return ResponseEntity.ok(inst);
    }

    @PostMapping
    public ResponseEntity<Institucion> create(@RequestBody Institucion inst) {
        Institucion created = service.create(inst);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Institucion> update(@PathVariable Long id, @RequestBody Institucion inst) {
        Institucion updated = service.update(id, inst);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}



