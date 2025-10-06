package com.example.pharmacy.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class FacturaTest {

    @Test
    public void testGettersAndSetters() {
        Factura factura = new Factura();

        Long idFactura = 1L;
        Long idVenta = 100L;
        LocalDateTime fechaFactura = LocalDateTime.of(2025, 8, 7, 14, 30);
        Double totalFactura = 299.99;
        String pdfUrl = "https://example.com/factura.pdf";

        factura.setIdFactura(idFactura);
        factura.setIdVenta(idVenta);
        factura.setFechaFactura(fechaFactura);
        factura.setTotalFactura(totalFactura);
        factura.setPdfUrl(pdfUrl);

        assertThat(factura.getIdFactura()).isEqualTo(idFactura);
        assertThat(factura.getIdVenta()).isEqualTo(idVenta);
        assertThat(factura.getFechaFactura()).isEqualTo(fechaFactura);
        assertThat(factura.getTotalFactura()).isEqualTo(totalFactura);
        assertThat(factura.getPdfUrl()).isEqualTo(pdfUrl);
    }
}
