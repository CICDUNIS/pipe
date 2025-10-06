package com.example.pharmacy.service.impl;

import com.example.pharmacy.dto.FacturaDTO;
import com.example.pharmacy.dto.VentaDTO;
import com.example.pharmacy.model.Factura;
import com.example.pharmacy.model.Venta;
import com.example.pharmacy.repository.FacturaRepository;
import com.example.pharmacy.service.AuditoriaService;
import com.example.pharmacy.service.FacturaService;
import com.example.pharmacy.service.VentaService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.NoSuchElementException;

@Service
public class FacturaServiceImpl implements FacturaService {

    private final FacturaRepository facturaRepository;
    private final VentaService ventaService;
    private final AuditoriaService auditoriaService;
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public FacturaServiceImpl(
            FacturaRepository facturaRepository,
            VentaService ventaService,
            AuditoriaService auditoriaService,
            JavaMailSender mailSender) {
        this.facturaRepository = facturaRepository;
        this.ventaService = ventaService;
        this.auditoriaService = auditoriaService;
        this.mailSender = mailSender;
    }

    @Override
    @Transactional
    public FacturaDTO createFactura(Venta venta) {
        try {
            Factura factura = new Factura();
            factura.setIdVenta(venta.getIdVenta());
            factura.setFechaFactura(LocalDateTime.now());
            factura.setTotalFactura(venta.getMontoPagado());

            Factura savedFactura = facturaRepository.save(factura);

            ByteArrayOutputStream pdfOutputStream = generatePdfFactura(savedFactura.getIdFactura());
            String pdfPath = "/facturas/factura_" + savedFactura.getIdFactura() + ".pdf";

            savedFactura.setPdfUrl(pdfPath);
            facturaRepository.save(savedFactura);

            auditoriaService.registrar("Factura", "CREATE",
                    "Factura creada para venta ID=" + venta.getIdVenta(),
                    "usuario=" + venta.getIdUsuario());

            return convertToDTO(savedFactura);
        } catch (Exception e) {
            auditoriaService.registrar("Factura", "ERROR",
                    "Error al crear factura para venta ID=" + venta.getIdVenta() + ": " + e.getMessage(),
                    "usuario=" + venta.getIdUsuario());
            throw new RuntimeException("Error al crear la factura: " + e.getMessage(), e);
        }
    }

    @Override
    public FacturaDTO getFactura(Long idFactura) {
        Factura factura = facturaRepository.findById(idFactura)
                .orElseThrow(() -> new NoSuchElementException("Factura no encontrada: " + idFactura));

        return convertToDTO(factura);
    }

    @Override
    public FacturaDTO getFacturaByVenta(Long idVenta) {
        Factura factura = facturaRepository.findByIdVenta(idVenta)
                .orElseThrow(() -> new NoSuchElementException("Factura no encontrada para venta: " + idVenta));

        return convertToDTO(factura);
    }

    @Override
    public ByteArrayOutputStream generatePdfFactura(Long idFactura) {
        try {
            Factura factura = facturaRepository.findById(idFactura)
                    .orElseThrow(() -> new NoSuchElementException("Factura no encontrada: " + idFactura));

            VentaDTO venta = ventaService.getVentaWithDetails(factura.getIdVenta());

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, baos);

            document.open();

            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Paragraph header = new Paragraph("FACTURA", headerFont);
            header.setAlignment(Element.ALIGN_CENTER);
            document.add(header);
            document.add(Chunk.NEWLINE);

            document.add(new Paragraph("Factura #: " + factura.getIdFactura()));
            document.add(new Paragraph("Fecha: " + factura.getFechaFactura().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))));
            document.add(new Paragraph("Venta #: " + venta.getIdVenta()));
            document.add(Chunk.NEWLINE);

            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10);

            Font tableHeaderFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
            table.addCell(new PdfPCell(new Phrase("Medicamento", tableHeaderFont)));
            table.addCell(new PdfPCell(new Phrase("Cantidad", tableHeaderFont)));
            table.addCell(new PdfPCell(new Phrase("Precio Unitario", tableHeaderFont)));
            table.addCell(new PdfPCell(new Phrase("Total", tableHeaderFont)));
            table.addCell(new PdfPCell(new Phrase("Receta", tableHeaderFont)));

            Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
            venta.getDetalles().forEach(item -> {
                table.addCell(new Phrase(item.getIdMedicamento().toString(), cellFont));
                table.addCell(new Phrase(item.getCantidad().toString(), cellFont));
                table.addCell(new Phrase("$" + String.format("%.2f", item.getPrecioUnitario()), cellFont));
                table.addCell(new Phrase("$" + String.format("%.2f", item.getTotalLinea()), cellFont));
                table.addCell(new Phrase("N/A", cellFont));
            });

            document.add(table);
            document.add(Chunk.NEWLINE);

            Font totalFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
            PdfPTable totalsTable = new PdfPTable(2);
            totalsTable.setWidthPercentage(40);
            totalsTable.setHorizontalAlignment(Element.ALIGN_RIGHT);

            totalsTable.addCell(new PdfPCell(new Phrase("Subtotal:", totalFont)));
            totalsTable.addCell(new PdfPCell(new Phrase("$" + String.format("%.2f", venta.getTotal()), cellFont)));

            totalsTable.addCell(new PdfPCell(new Phrase("Impuesto:", totalFont)));
            totalsTable.addCell(new PdfPCell(new Phrase("$" + String.format("%.2f", venta.getImpuesto()), cellFont)));

            totalsTable.addCell(new PdfPCell(new Phrase("Descuento:", totalFont)));
            totalsTable.addCell(new PdfPCell(new Phrase("$" + String.format("%.2f", venta.getDescuento()), cellFont)));

            totalsTable.addCell(new PdfPCell(new Phrase("Total:", totalFont)));
            totalsTable.addCell(new PdfPCell(new Phrase("$" + String.format("%.2f", venta.getMontoPagado()), totalFont)));

            document.add(totalsTable);
            document.add(Chunk.NEWLINE);

            Paragraph footer = new Paragraph("¡Gracias por su compra!", headerFont);
            footer.setAlignment(Element.ALIGN_CENTER);
            document.add(footer);

            document.close();

            return baos;
        } catch (Exception e) {
            throw new RuntimeException("Error al generar PDF de factura: " + e.getMessage(), e);
        }
    }

    @Override
    public void sendFacturaEmail(Long idFactura, String email) {
        try {
            if (email == null || email.trim().isEmpty()) {
                throw new IllegalArgumentException("Email no puede estar vacío");
            }

            Factura factura = facturaRepository.findById(idFactura)
                    .orElseThrow(() -> new NoSuchElementException("Factura no encontrada: " + idFactura));

            ByteArrayOutputStream pdfStream = generatePdfFactura(idFactura);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(email);
            helper.setFrom(fromEmail);
            helper.setSubject("Su factura #" + factura.getIdFactura());
            helper.setText("Adjuntamos su factura de compra. ¡Gracias por su preferencia!");
            helper.addAttachment("factura_" + factura.getIdFactura() + ".pdf",
                    new org.springframework.core.io.ByteArrayResource(pdfStream.toByteArray()));

            mailSender.send(message);

            auditoriaService.registrar("Factura", "EMAIL",
                    "Factura " + idFactura + " enviada por email a " + email,
                    "email=" + email);
        } catch (MessagingException e) {
            auditoriaService.registrar("Factura", "ERROR",
                    "Error al enviar factura " + idFactura + " por email a " + email + ": " + e.getMessage(),
                    "email=" + email);
            throw new RuntimeException("Error al enviar email con factura: " + e.getMessage(), e);
        }
    }

    private FacturaDTO convertToDTO(Factura factura) {
        FacturaDTO dto = new FacturaDTO();
        dto.setIdFactura(factura.getIdFactura());
        dto.setIdVenta(factura.getIdVenta());
        dto.setFechaFactura(factura.getFechaFactura());
        dto.setTotalFactura(factura.getTotalFactura());
        dto.setPdfUrl(factura.getPdfUrl());

        try {
            VentaDTO ventaDTO = ventaService.getVentaWithDetails(factura.getIdVenta());
            dto.setVenta(ventaDTO);
        } catch (Exception e) {
        }

        return dto;
    }
}
