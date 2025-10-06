package com.example.pharmacy.service.impl;

import com.example.pharmacy.config.JdbcConfiguration;
import com.example.pharmacy.dto.VentaDTO;
import com.example.pharmacy.dto.VentaDetalleDTO;
import com.example.pharmacy.model.Venta;
import com.example.pharmacy.model.VentaDetalle;
import com.example.pharmacy.repository.VentaRepository;
import com.example.pharmacy.repository.VentaDetalleRepository;
import com.example.pharmacy.repository.MedicamentoRepository;
import com.example.pharmacy.repository.RecetaRepository;
import com.example.pharmacy.service.CompraMedicamentoService;
import com.example.pharmacy.service.VentaService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class VentaServiceImpl implements VentaService {

    private final VentaRepository ventaRepository;
    private final VentaDetalleRepository detalleRepository;
    private final MedicamentoRepository medicamentoRepository;
    private final RecetaRepository recetaRepository;
    private final CompraMedicamentoService compraMedicamentoService;
    private final JdbcTemplate jdbcTemplate;

    public VentaServiceImpl(
            VentaRepository ventaRepository,
            VentaDetalleRepository detalleRepository,
            MedicamentoRepository medicamentoRepository,
            RecetaRepository recetaRepository,
            CompraMedicamentoService compraMedicamentoService,
            JdbcTemplate jdbcTemplate) {
        this.ventaRepository = ventaRepository;
        this.detalleRepository = detalleRepository;
        this.medicamentoRepository = medicamentoRepository;
        this.recetaRepository = recetaRepository;
        this.compraMedicamentoService = compraMedicamentoService;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public VentaDTO createVenta(VentaDTO dto) {
        try {
            System.out.println("Starting createVenta with DTO: " + dto);

            if (!dto.isDetallesValidos()) {
                String errorMsg = "Detalles de la venta son inválidos.";
                System.err.println(errorMsg);
                throw new IllegalArgumentException(errorMsg);
            }

            if (dto.getCodigoReceta() != null) {
                System.out.println("Processing prescription code: " + dto.getCodigoReceta());
                compraMedicamentoService.validarReceta(dto.getCodigoReceta(), obtenerCodigosMedicamentos(dto));
                dto.setIdReceta(obtenerIdRecetaDesdeCodigo(dto.getCodigoReceta()));
            }

            if (dto.getNumeroAfiliacion() != null) {
                System.out.println("Processing affiliation number: " + dto.getNumeroAfiliacion());
                compraMedicamentoService.validarCobertura(dto.getNumeroAfiliacion(), obtenerCodigosMedicamentos(dto));
            }

            double subTotal = 0.0;
            for (VentaDetalleDTO dDto : dto.getDetalles()) {
                System.out.println("Processing detail for medicamento ID: " + dDto.getIdMedicamento());
                var med = medicamentoRepository.findById(dDto.getIdMedicamento())
                        .orElseThrow(() -> {
                            String errorMsg = "Medicamento no encontrado: " + dDto.getIdMedicamento();
                            System.err.println(errorMsg);
                            return new NoSuchElementException(errorMsg);
                        });

                double precioUnit = med.getPrecio() != null ? med.getPrecio() : 0.0;
                double totalLinea = precioUnit * dDto.getCantidad();

                dDto.setPrecioUnitario(precioUnit);
                dDto.setTotalLinea(totalLinea);

                subTotal += totalLinea;
            }

            double impuesto = subTotal * 0.12;
            double descuento = dto.getDescuento() != null ? dto.getDescuento() : 0.0;
            double total = subTotal + impuesto - descuento;
            double montoPagado = total;

            dto.setTotal(subTotal);
            dto.setImpuesto(impuesto);
            dto.setDescuento(descuento);
            dto.setMontoPagado(montoPagado);

            try {
                System.out.println("Attempting to create venta using Spring Data repositories");

                Venta venta = new Venta();
                venta.setIdVenta(null);
                venta.setIdUsuario(dto.getIdUsuario());
                venta.setIdReceta(dto.getIdReceta());
                venta.setFechaVenta(LocalDateTime.now());
                venta.setTotal(subTotal);
                venta.setImpuesto(impuesto);
                venta.setDescuento(descuento);
                venta.setMontoPagado(montoPagado);

                System.out.println("Saving initial Venta entity");
                Venta savedVenta = ventaRepository.save(venta);
                System.out.println("Venta saved with ID: " + savedVenta.getIdVenta());

                for (VentaDetalleDTO dDto : dto.getDetalles()) {
                    VentaDetalle vd = new VentaDetalle();
                    vd.setIdVentaDetalle(null);
                    vd.setIdVenta(savedVenta.getIdVenta());
                    vd.setIdMedicamento(dDto.getIdMedicamento());
                    vd.setCantidad(dDto.getCantidad());
                    vd.setPrecioUnitario(dDto.getPrecioUnitario());
                    vd.setTotalLinea(dDto.getTotalLinea());

                    System.out.println("Saving VentaDetalle: medicamentoId=" + vd.getIdMedicamento() + ", cantidad=" + vd.getCantidad());
                    VentaDetalle savedDetail = detalleRepository.save(vd);
                    System.out.println("VentaDetalle saved with ID: " + savedDetail.getIdVentaDetalle());
                }

                VentaDTO result = getVentaWithDetails(savedVenta.getIdVenta());
                System.out.println("createVenta completed successfully using Spring Data, returning DTO with ID: " + result.getIdVenta());
                return result;

            } catch (Exception springDataEx) {
                System.err.println("ERROR in Spring Data approach: " + springDataEx.getMessage());
                springDataEx.printStackTrace();

                System.out.println("Falling back to direct JDBC approach");

                try {
                    Map<String, Object> ventaResult = JdbcConfiguration.insertVentaJdbc(jdbcTemplate, dto);

                    if (!(Boolean)ventaResult.get("success")) {
                        throw new RuntimeException("Failed to insert Venta using direct JDBC: " + ventaResult.get("error"));
                    }

                    Long ventaId = (Long)ventaResult.get("idVenta");
                    System.out.println("Venta inserted via JDBC with ID: " + ventaId);

                    List<VentaDetalleDTO> savedDetails = new ArrayList<>();
                    for (VentaDetalleDTO detalle : dto.getDetalles()) {
                        Map<String, Object> detalleResult =
                            JdbcConfiguration.insertVentaDetalleJdbc(jdbcTemplate, ventaId, detalle);

                        if (!(Boolean)detalleResult.get("success")) {
                            throw new RuntimeException("Failed to insert VentaDetalle using direct JDBC: " + detalleResult.get("error"));
                        }

                        detalle.setIdVentaDetalle((Long)detalleResult.get("idVentaDetalle"));
                        savedDetails.add(detalle);
                    }

                    VentaDTO resultDto = new VentaDTO();
                    resultDto.setIdVenta(ventaId);
                    resultDto.setIdUsuario(dto.getIdUsuario());
                    resultDto.setIdReceta(dto.getIdReceta());
                    resultDto.setFechaVenta(LocalDateTime.now());
                    resultDto.setTotal(dto.getTotal());
                    resultDto.setImpuesto(dto.getImpuesto());
                    resultDto.setDescuento(dto.getDescuento());
                    resultDto.setMontoPagado(dto.getMontoPagado());
                    resultDto.setDetalles(savedDetails);

                    System.out.println("createVenta completed successfully using direct JDBC, returning DTO with ID: " + resultDto.getIdVenta());
                    return resultDto;

                } catch (Exception jdbcEx) {
                    System.err.println("ERROR in direct JDBC approach: " + jdbcEx.getMessage());
                    jdbcEx.printStackTrace();
                    throw new RuntimeException("Error al crear la venta (both Spring Data and JDBC methods failed): " +
                                               springDataEx.getMessage() + " / " + jdbcEx.getMessage(), jdbcEx);
                }
            }

        } catch (Exception e) {
            System.err.println("ERROR in createVenta: " + e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error al crear la venta: " + e.getMessage(), e);
        }
    }

    @Override
    public VentaDTO getVentaWithDetails(Long idVenta) {
        Venta venta = ventaRepository.findById(idVenta)
                .orElseThrow(() -> new NoSuchElementException("Venta no encontrada: " + idVenta));

        List<VentaDetalle> detalles = detalleRepository.findByIdVenta(idVenta);

        VentaDTO dto = new VentaDTO();
        dto.setIdVenta(venta.getIdVenta());
        dto.setIdUsuario(venta.getIdUsuario());
        dto.setIdReceta(venta.getIdReceta());
        dto.setFechaVenta(venta.getFechaVenta());
        dto.setTotal(venta.getTotal());
        dto.setImpuesto(venta.getImpuesto());
        dto.setDescuento(venta.getDescuento());
        dto.setMontoPagado(venta.getMontoPagado());

        List<VentaDetalleDTO> detalleDTOs = detalles.stream()
                .map(d -> {
                    VentaDetalleDTO dd = new VentaDetalleDTO();
                    dd.setIdVentaDetalle(d.getIdVentaDetalle());
                    dd.setIdMedicamento(d.getIdMedicamento());
                    dd.setCantidad(d.getCantidad());
                    dd.setPrecioUnitario(d.getPrecioUnitario());
                    dd.setTotalLinea(d.getTotalLinea());
                    return dd;
                })
                .collect(Collectors.toList());

        dto.setDetalles(detalleDTOs);
        return dto;
    }

    @Override
    @Transactional
    public VentaDTO updateVenta(Long idVenta, VentaDTO dto) {
        Venta existing = ventaRepository.findById(idVenta)
                .orElseThrow(() -> new NoSuchElementException("Venta no encontrada: " + idVenta));

        existing.setIdReceta(dto.getIdReceta());
        existing.setDescuento(dto.getDescuento() != null ? dto.getDescuento() : 0.0);

        ventaRepository.save(existing);

        return getVentaWithDetails(existing.getIdVenta());
    }

    @Override
    @Transactional
    public void deleteVenta(Long idVenta) {
        Venta venta = ventaRepository.findById(idVenta)
                .orElseThrow(() -> new NoSuchElementException("Venta no encontrada: " + idVenta));

        ventaRepository.delete(venta);
    }


    private List<String> obtenerCodigosMedicamentos(VentaDTO dto) {
        return dto.getDetalles().stream()
                .map(d -> String.valueOf(d.getIdMedicamento()))
                .collect(Collectors.toList());
    }

    private Long obtenerIdRecetaDesdeCodigo(String codigoReceta) {
        return recetaRepository.findByCodigoReceta(codigoReceta)
                .map(receta -> receta.getIdReceta())
                .orElseThrow(() -> new NoSuchElementException("Receta no encontrada con código: " + codigoReceta));
    }
}
