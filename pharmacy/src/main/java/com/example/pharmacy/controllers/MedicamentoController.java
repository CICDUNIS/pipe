package com.example.pharmacy.controllers;

import com.example.pharmacy.model.Medicamento;
import com.example.pharmacy.service.MedicamentoService;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.pharmacy.dto.MedicamentoListWrapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpServletResponse;
import com.example.pharmacy.dto.MedicamentoResponseDTO;
import com.example.pharmacy.dto.PrecioMedicamentoCompania;
import org.springframework.web.client.RestTemplate;
import java.util.stream.Collectors;

import java.io.IOException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/medicamentos")
public class MedicamentoController {

    private static final Logger logger = LoggerFactory.getLogger(MedicamentoController.class);
    private final MedicamentoService service;
    private final ObjectMapper objectMapper;

    public MedicamentoController(MedicamentoService service, ObjectMapper objectMapper) {
        this.service = service;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/export/xml")
    public void exportMedicamentosXml(HttpServletResponse response) throws IOException {
	    response.setContentType("application/xml");
	    response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=medicamentos.xml");
	    List<Medicamento> meds = service.findAll();

	    MedicamentoListWrapper wrapper = new MedicamentoListWrapper(meds);

	    XmlMapper xmlMapper = new XmlMapper();
	    xmlMapper.writeValue(response.getOutputStream(), wrapper);
    }

    @PostMapping("/import/xml")
    public ResponseEntity<String> importMedicamentosXml(@RequestParam("file") MultipartFile file) {
	    try {
		    XmlMapper xmlMapper = new XmlMapper();
		    MedicamentoListWrapper wrapper = xmlMapper.readValue(file.getInputStream(), new TypeReference<MedicamentoListWrapper>() {});
		    List<Medicamento> meds = wrapper.getMedicamentos();

		    for (Medicamento m : meds) {
			    if (m.getIdMedicamento() != null && service.existsById(m.getIdMedicamento())) {
				    service.update(m.getIdMedicamento(), m);
			    } else {
				    service.create(m);
			    }
		    }
		    return ResponseEntity.ok("Importación exitosa!");
	    } catch (IOException e) {
		    e.printStackTrace();
		    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
			    .body("Error en la importación: " + e.getMessage());
	    }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Medicamento> getById(@PathVariable Long id) {
	    Medicamento med = service.findById(id);
	    return ResponseEntity.ok(med);
    }

    @PostMapping
    public ResponseEntity<Medicamento> create(@RequestBody Medicamento med) {
        try {
            logger.info("Received medication data: {}", objectMapper.writeValueAsString(med));
            logger.info("requiereReceta type: {}, value: {}", 
                        med.getRequiereReceta() != null ? med.getRequiereReceta().getClass().getName() : "null", 
                        med.getRequiereReceta());
            
            Medicamento created = service.create(med);
            logger.info("Successfully created medication with ID: {}", created.getIdMedicamento());
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Error creating medication", e);
            throw new RuntimeException("Error creating medication: " + e.getMessage(), e);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Medicamento> update(@PathVariable Long id, @RequestBody Medicamento med) {
        try {
            logger.info("Updating medication ID {}: {}", id, objectMapper.writeValueAsString(med));
            Medicamento updated = service.update(id, med);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            logger.error("Error updating medication", e);
            throw new RuntimeException("Error updating medication: " + e.getMessage(), e);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public List<Medicamento> searchMedicamento(@RequestParam String term) {
	    return service.search(term);
    }

    @GetMapping("/latest")
    public List<Medicamento> getLastMedicamentos() {
	    return service.findLastTen();
    }

}



