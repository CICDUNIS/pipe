package com.example.pharmacy.controllers;

import com.example.pharmacy.dto.CheckoutDTO;
import com.example.pharmacy.dto.FacturaDTO;
import com.example.pharmacy.model.Usuario;
import com.example.pharmacy.service.CarritoService;
import com.example.pharmacy.service.FacturaService;
import com.example.pharmacy.service.UsuarioService;
import com.example.pharmacy.service.AuditoriaService;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.data.relational.core.conversion.DbActionExecutionException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/checkout")
public class CheckoutController {

    private final CarritoService carritoService;
    private final FacturaService facturaService;
    private final UsuarioService usuarioService;
    private final AuditoriaService auditoriaService;

    public CheckoutController(
            CarritoService carritoService,
            FacturaService facturaService,
            UsuarioService usuarioService,
            AuditoriaService auditoriaService) {
        this.carritoService = carritoService;
        this.facturaService = facturaService;
        this.usuarioService = usuarioService;
        this.auditoriaService = auditoriaService;
    }

    @PostMapping
    public ResponseEntity<?> checkout(@RequestBody CheckoutDTO checkoutDTO) {
        try {
            System.out.println("DEBUG: Checkout controller - Processing checkout request");
            Usuario usuario = getCurrentUser();
            System.out.println("DEBUG: Checkout controller - User authenticated: " + usuario.getCorreo());

            FacturaDTO factura = carritoService.checkoutWithDiscount(usuario.getIdUsuario(), checkoutDTO.getDescuento());
            System.out.println("DEBUG: Checkout controller - Checkout successful, invoice ID: " + factura.getIdFactura());

            if (checkoutDTO.getEmail() != null && !checkoutDTO.getEmail().trim().isEmpty()) {
                System.out.println("DEBUG: Checkout controller - Sending email to: " + checkoutDTO.getEmail());
                try {
                    facturaService.sendFacturaEmail(factura.getIdFactura(), checkoutDTO.getEmail());
                    System.out.println("DEBUG: Checkout controller - Email sent successfully");
                } catch (Exception e) {
                    System.err.println("ERROR: Failed to send email: " + e.getMessage());
                    auditoriaService.registrar("Checkout", "EMAIL_ERROR",
                        "Failed to send invoice email: " + e.getMessage(),
                        "usuario=" + usuario.getIdUsuario());
                }
            }

            return ResponseEntity.ok(factura);
        } catch (DbActionExecutionException e) {
            System.err.println("ERROR: Database action execution error in checkout: " + e.getMessage());
            e.printStackTrace();

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Database error during checkout");
            errorResponse.put("message", "Error updating cart status. Your order may have been processed. Please check your order history.");
            errorResponse.put("details", e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        } catch (IllegalStateException e) {
            System.err.println("ERROR: Illegal state error in checkout: " + e.getMessage());

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Invalid operation");
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (Exception e) {
            System.err.println("ERROR: Unexpected error in checkout: " + e.getMessage());
            e.printStackTrace();

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Checkout failed");
            errorResponse.put("message", "An unexpected error occurred during checkout: " + e.getMessage());
            errorResponse.put("type", e.getClass().getName());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/{idFactura}/pdf")
    public ResponseEntity<?> downloadInvoicePdf(@PathVariable Long idFactura) {
        try {
            ByteArrayOutputStream pdfStream = facturaService.generatePdfFactura(idFactura);
            ByteArrayResource resource = new ByteArrayResource(pdfStream.toByteArray());

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=factura_" + idFactura + ".pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .contentLength(pdfStream.size())
                    .body(resource);
        } catch (Exception e) {
            System.err.println("ERROR: Failed to generate PDF for invoice: " + idFactura + " - " + e.getMessage());

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "PDF generation failed");
            errorResponse.put("message", "Failed to generate PDF for invoice: " + e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("/{idFactura}/email")
    public ResponseEntity<?> sendInvoiceEmail(
            @PathVariable Long idFactura,
            @RequestParam String email) {
        try {
            facturaService.sendFacturaEmail(idFactura, email);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.err.println("ERROR: Failed to send invoice email: " + e.getMessage());

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Email sending failed");
            errorResponse.put("message", "Failed to send invoice email: " + e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    private Usuario getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return usuarioService.findByCorreo(username);
    }
}
