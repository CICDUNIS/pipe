package com.example.pharmacy.controllers;

import com.example.pharmacy.dto.VentaDTO;
import com.example.pharmacy.service.VentaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ventas")
public class VentaController {

    private final VentaService ventaService;

    public VentaController(VentaService ventaService) {
        this.ventaService = ventaService;
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody VentaDTO dto) {
	System.out.println("HOOOOLAAAAA ACAAAAA");
	System.out.println(dto);
        VentaDTO created = ventaService.createVenta(dto);

	if (created != null) {
		return new ResponseEntity<>(created, HttpStatus.CREATED);
	} else {
		return new ResponseEntity<>("Esto fallo bestia", HttpStatus.BAD_REQUEST);
	}
    }

    @GetMapping("/{id}")
    public ResponseEntity<VentaDTO> getOne(@PathVariable Long id) {
        VentaDTO dto = ventaService.getVentaWithDetails(id);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VentaDTO> update(@PathVariable Long id, @RequestBody VentaDTO dto) {
        VentaDTO updated = ventaService.updateVenta(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        ventaService.deleteVenta(id);
    }
}

