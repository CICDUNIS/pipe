package com.example.pharmacy.integration;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
public class HospitalEntregaClient {

	/*
	   String url = "http://hospital001.midominio.com/hospital/confirmar-entrega";
	   Map<String, Object> payload = new HashMap<>();
	   payload.put("codigoReceta", codigoReceta);
	   ...
	   Map resp = restTemplate.postForObject(url, payload, Map.class);
	   return resp;
	   */

    public Map<String, Object> confirmarEntrega(String codigoReceta, LocalDateTime fechaEntrega, String paciente, String entregadoPor) {
        Map<String, Object> resp = new HashMap<>();
        resp.put("recibido", true);
        resp.put("mensaje", "Entrega confirmada en Hospital SIM para receta " + codigoReceta);
        return resp;
    }
}


