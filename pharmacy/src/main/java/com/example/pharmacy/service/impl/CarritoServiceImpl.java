package com.example.pharmacy.service.impl;

import com.example.pharmacy.dto.CarritoDTO;
import com.example.pharmacy.dto.CarritoDetalleDTO;
import com.example.pharmacy.dto.FacturaDTO;
import com.example.pharmacy.dto.VentaDTO;
import com.example.pharmacy.dto.VentaDetalleDTO;
import com.example.pharmacy.model.Carrito;
import com.example.pharmacy.model.CarritoDetalle;
import com.example.pharmacy.model.Medicamento;
import com.example.pharmacy.model.Venta;
import com.example.pharmacy.repository.CarritoRepository;
import com.example.pharmacy.repository.CarritoDetalleRepository;
import com.example.pharmacy.repository.MedicamentoRepository;
import com.example.pharmacy.repository.VentaRepository;
import com.example.pharmacy.service.CarritoService;
import com.example.pharmacy.service.AuditoriaService;
import com.example.pharmacy.service.FacturaService;
import com.example.pharmacy.service.VentaService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CarritoServiceImpl implements CarritoService {

    private final CarritoRepository carritoRepository;
    private final CarritoDetalleRepository detalleRepository;
    private final MedicamentoRepository medicamentoRepository;
    private final VentaRepository ventaRepository;
    private final AuditoriaService auditoriaService;
    private final VentaService ventaService;
    private final FacturaService facturaService;
    private final JdbcTemplate jdbcTemplate;

    public CarritoServiceImpl(
            CarritoRepository carritoRepository,
            CarritoDetalleRepository detalleRepository,
            MedicamentoRepository medicamentoRepository,
            VentaRepository ventaRepository,
            AuditoriaService auditoriaService,
            VentaService ventaService,
            FacturaService facturaService,
            JdbcTemplate jdbcTemplate) {
        this.carritoRepository = carritoRepository;
        this.detalleRepository = detalleRepository;
        this.medicamentoRepository = medicamentoRepository;
        this.ventaRepository = ventaRepository;
        this.auditoriaService = auditoriaService;
        this.ventaService = ventaService;
        this.facturaService = facturaService;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional(readOnly = true)
    public CarritoDTO getActiveCart(Long idUsuario) {
        Carrito carrito = getOrCreateCart(idUsuario);
        return buildCarritoDTO(carrito);
    }

    @Override
    @Transactional
    public CarritoDTO addItem(Long idUsuario, Long idMedicamento, Integer cantidad) {
        try {
            Medicamento medicamento = medicamentoRepository.findById(idMedicamento)
                    .orElseThrow(() -> new NoSuchElementException("Medicamento no encontrado: " + idMedicamento));

            if ("Y".equalsIgnoreCase(medicamento.getRequiereReceta())) {
                throw new IllegalArgumentException("Este medicamento requiere receta médica");
            }

            if (medicamento.getStock() < cantidad) {
                throw new IllegalArgumentException("Stock insuficiente");
            }

            Carrito carrito = getOrCreateCart(idUsuario);

            Optional<CarritoDetalle> existingItem = detalleRepository.findByIdCartAndIdMedicamento(carrito.getIdCart(), idMedicamento);

            if (existingItem.isPresent()) {
                CarritoDetalle detalle = existingItem.get();
                detalle.setCantidad(detalle.getCantidad() + cantidad);
                detalleRepository.save(detalle);
            } else {
                CarritoDetalle detalle = new CarritoDetalle();
                detalle.setIdCart(carrito.getIdCart());
                detalle.setIdMedicamento(idMedicamento);
                detalle.setCantidad(cantidad);
                detalle.setPrecioUnitario(medicamento.getPrecio());
                detalleRepository.save(detalle);
            }

            auditoriaService.registrar("Carrito", "UPDATE",
                "Agregado medicamento ID=" + idMedicamento + " al carrito ID=" + carrito.getIdCart(),
                "usuario=" + idUsuario);

            return buildCarritoDTO(carrito);
        } catch (Exception e) {
            auditoriaService.registrar("Carrito", "ERROR",
                "Error adding item to cart: " + e.getMessage(),
                "usuario=" + idUsuario);
            throw e;
        }
    }

    @Override
    @Transactional
    public CarritoDTO updateItemQuantity(Long idUsuario, Long idMedicamento, Integer cantidad) {
        if (cantidad <= 0) {
            return removeItem(idUsuario, idMedicamento);
        }

        Carrito carrito = getOrCreateCart(idUsuario);
        CarritoDetalle detalle = detalleRepository.findByIdCartAndIdMedicamento(carrito.getIdCart(), idMedicamento)
                .orElseThrow(() -> new NoSuchElementException("Item no encontrado en el carrito"));

        Medicamento medicamento = medicamentoRepository.findById(idMedicamento)
                .orElseThrow(() -> new NoSuchElementException("Medicamento no encontrado: " + idMedicamento));

        if (medicamento.getStock() < cantidad) {
            throw new IllegalArgumentException("Stock insuficiente");
        }

        detalle.setCantidad(cantidad);
        detalleRepository.save(detalle);

        auditoriaService.registrar("Carrito", "UPDATE",
            "Actualizada cantidad de medicamento ID=" + idMedicamento + " en carrito ID=" + carrito.getIdCart(),
            "usuario=" + idUsuario);

        return buildCarritoDTO(carrito);
    }

    @Override
    @Transactional
    public CarritoDTO removeItem(Long idUsuario, Long idMedicamento) {
        Carrito carrito = getOrCreateCart(idUsuario);
        CarritoDetalle detalle = detalleRepository.findByIdCartAndIdMedicamento(carrito.getIdCart(), idMedicamento)
                .orElseThrow(() -> new NoSuchElementException("Item no encontrado en el carrito"));

        detalleRepository.delete(detalle);

        auditoriaService.registrar("Carrito", "DELETE",
            "Eliminado medicamento ID=" + idMedicamento + " del carrito ID=" + carrito.getIdCart(),
            "usuario=" + idUsuario);

        return buildCarritoDTO(carrito);
    }

    @Override
    @Transactional
    public CarritoDTO checkout(Long idUsuario) {
        Carrito carrito = getOrCreateCart(idUsuario);
        List<CarritoDetalle> items = detalleRepository.findByIdCart(carrito.getIdCart());

        if (items.isEmpty()) {
            throw new IllegalStateException("El carrito está vacío");
        }

        for (CarritoDetalle item : items) {
            Medicamento medicamento = medicamentoRepository.findById(item.getIdMedicamento())
                    .orElseThrow(() -> new NoSuchElementException("Medicamento no encontrado: " + item.getIdMedicamento()));

            if (medicamento.getStock() < item.getCantidad()) {
                throw new IllegalStateException("Stock insuficiente para " + medicamento.getNombre());
            }

            medicamento.setStock(medicamento.getStock() - item.getCantidad());
            medicamentoRepository.save(medicamento);
        }

        carrito.setStatus("C");
        carrito.setFechaActualizacion(LocalDateTime.now());
        carritoRepository.save(carrito);

        auditoriaService.registrar("Carrito", "CHECKOUT",
            "Carrito ID=" + carrito.getIdCart() + " procesado",
            "usuario=" + idUsuario);

        return buildCarritoDTO(carrito);
    }

    @Override
    @Transactional
    public FacturaDTO checkoutWithDiscount(Long idUsuario, Double descuento) {
        System.out.println("\n=== CHECKOUT PROCESS START ===");
        System.out.println("User ID: " + idUsuario);
        System.out.println("Discount: " + (descuento != null ? descuento : "0.0"));

        if (ventaService == null || facturaService == null) {
            System.err.println("ERROR: Required services not injected");
            System.err.println("ventaService: " + (ventaService != null ? "OK" : "NULL"));
            System.err.println("facturaService: " + (facturaService != null ? "OK" : "NULL"));
            throw new IllegalStateException("ventaService y facturaService deben ser inyectados para el checkout con factura");
        }

        try {
            String itemsQuery = "SELECT CD.*, M.STOCK, M.PRECIO, M.NOMBRE FROM CARRITO_DETALLE CD " +
                               "JOIN MEDICAMENTO M ON CD.ID_MEDICAMENTO = M.ID_MEDICAMENTO " +
                               "JOIN CARRITO C ON CD.ID_CART = C.ID_CART " +
                               "WHERE C.ID_USUARIO = ? AND C.STATUS = 'A'";
            System.out.println("\n--- Executing cart items query ---");
            System.out.println("Query: " + itemsQuery);
            System.out.println("Parameters: [idUsuario=" + idUsuario + "]");

            List<Map<String, Object>> items = jdbcTemplate.queryForList(itemsQuery, idUsuario);
            System.out.println("Query result: " + items.size() + " items found");

            if (items.isEmpty()) {
                System.err.println("ERROR: No items found in active cart");
                throw new IllegalStateException("El carrito está vacío");
            }

            System.out.println("\n--- Cart Items Details ---");
            for (Map<String, Object> item : items) {
                System.out.println(String.format("Item: ID=%s, Name=%s, Quantity=%s, Price=%s, Stock=%s",
                    item.get("ID_MEDICAMENTO"),
                    item.get("NOMBRE"),
                    item.get("CANTIDAD"),
                    item.get("PRECIO"),
                    item.get("STOCK")));
            }

            System.out.println("\n--- Stock Validation ---");
            for (Map<String, Object> item : items) {
                Long idMedicamento = ((Number) item.get("ID_MEDICAMENTO")).longValue();
                Integer cantidad = ((Number) item.get("CANTIDAD")).intValue();
                Integer stock = ((Number) item.get("STOCK")).intValue();
                String nombre = (String) item.get("NOMBRE");

                System.out.println(String.format("Validating stock for %s (ID: %d): Required=%d, Available=%d",
                    nombre, idMedicamento, cantidad, stock));

                if (stock < cantidad) {
                    System.err.println(String.format("ERROR: Insufficient stock for %s (ID: %d). Required: %d, Available: %d",
                        nombre, idMedicamento, cantidad, stock));
                    throw new IllegalStateException("Stock insuficiente para " + nombre);
                }
            }

            System.out.println("\n--- Creating VentaDTO ---");
            VentaDTO ventaDTO = new VentaDTO();
            ventaDTO.setIdUsuario(idUsuario);
            ventaDTO.setDescuento(descuento != null ? descuento : 0.0);

            double subtotal = 0.0;
            List<VentaDetalleDTO> detalles = new ArrayList<>();

            System.out.println("\n--- Calculating Totals ---");
            for (Map<String, Object> item : items) {
                VentaDetalleDTO detalle = new VentaDetalleDTO();
                detalle.setIdMedicamento(((Number) item.get("ID_MEDICAMENTO")).longValue());
                detalle.setCantidad(((Number) item.get("CANTIDAD")).intValue());
                detalle.setPrecioUnitario(((Number) item.get("PRECIO")).doubleValue());
                detalle.setTotalLinea(detalle.getCantidad() * detalle.getPrecioUnitario());
                detalles.add(detalle);
                subtotal += detalle.getTotalLinea();

                System.out.println(String.format("Line item: ID=%d, Quantity=%d, Unit Price=%.2f, Line Total=%.2f",
                    detalle.getIdMedicamento(),
                    detalle.getCantidad(),
                    detalle.getPrecioUnitario(),
                    detalle.getTotalLinea()));
            }

            ventaDTO.setDetalles(detalles);
            ventaDTO.setTotal(subtotal);
            System.out.println(String.format("\nTotal Sale Amount: %.2f", subtotal));

            System.out.println("\n--- Creating Sale Record ---");
            System.out.println("VentaDTO details:");
            System.out.println("- User ID: " + ventaDTO.getIdUsuario());
            System.out.println("- Discount: " + ventaDTO.getDescuento());
            System.out.println("- Total: " + ventaDTO.getTotal());
            System.out.println("- Items: " + ventaDTO.getDetalles().size());

            final VentaDTO createdVenta;
            try {
                createdVenta = ventaService.createVenta(ventaDTO);
                System.out.println("Sale created successfully with ID: " + createdVenta.getIdVenta());
            } catch (Exception e) {
                System.err.println("ERROR during createVenta call: " + e.getClass().getName() + ": " + e.getMessage());
                e.printStackTrace();
                throw new RuntimeException("Error al crear la venta: " + e.getMessage(), e);
            }

            if (createdVenta == null || createdVenta.getIdVenta() == null) {
                throw new IllegalStateException("Venta created but no ID was returned");
            }

            System.out.println("\n--- Retrieving Created Venta ---");
            Venta venta = null;
            try {
                venta = ventaRepository.findById(createdVenta.getIdVenta())
                    .orElseThrow(() -> {
                        String errorMsg = "Venta not found after creation. ID: " + createdVenta.getIdVenta();
                        System.err.println("ERROR: " + errorMsg);
                        return new NoSuchElementException(errorMsg);
                    });
                System.out.println("Venta retrieved successfully");
            } catch (Exception e) {
                System.err.println("ERROR retrieving created Venta: " + e.getClass().getName() + ": " + e.getMessage());
                e.printStackTrace();
                throw new RuntimeException("Error al recuperar la venta creada: " + e.getMessage(), e);
            }

            System.out.println("\n--- Creating Invoice ---");
            FacturaDTO factura = null;
            try {
                factura = facturaService.createFactura(venta);
                System.out.println("Invoice created successfully with ID: " + factura.getIdFactura());
            } catch (Exception e) {
                System.err.println("ERROR creating invoice: " + e.getClass().getName() + ": " + e.getMessage());
                e.printStackTrace();
                throw new RuntimeException("Error al crear la factura: " + e.getMessage(), e);
            }

            System.out.println("\n--- Registering Audit ---");
            try {
                auditoriaService.registrar("Venta", "CREATE",
                    "Venta ID=" + venta.getIdVenta() + " creada con descuento " + descuento,
                    "usuario=" + idUsuario);
                System.out.println("Audit registered successfully");
            } catch (Exception e) {
                System.err.println("WARNING: Error registering audit: " + e.getMessage());
            }

            System.out.println("\n--- Marking Cart as Checked Out ---");
            try {
                Carrito carrito = getOrCreateCart(idUsuario);
                carrito.setStatus("C");
                carrito.setFechaActualizacion(LocalDateTime.now());
                carritoRepository.save(carrito);
                System.out.println("Cart marked as checked out successfully");
            } catch (Exception e) {
                System.err.println("ERROR updating cart status: " + e.getMessage());
                System.out.println("Trying direct JDBC update instead...");

                try {
                    Long cartId = jdbcTemplate.queryForObject(
                        "SELECT ID_CART FROM CARRITO WHERE ID_USUARIO = ? AND STATUS = 'A'",
                        Long.class, idUsuario);

                    if (cartId != null) {
                        boolean updated = jdbcTemplate.update(
                            "UPDATE CARRITO SET STATUS = 'C', FECHA_ACTUALIZACION = SYSTIMESTAMP WHERE ID_CART = ?",
                            cartId) > 0;

                        System.out.println("Direct JDBC update " + (updated ? "successful" : "failed"));
                    }
                } catch (Exception jdbcEx) {
                    System.err.println("WARNING: Could not update cart status via JDBC either: " + jdbcEx.getMessage());
                }
            }

            System.out.println("\n=== CHECKOUT PROCESS COMPLETED SUCCESSFULLY ===");
            return factura;

        } catch (Exception e) {
            System.err.println("\n=== CHECKOUT PROCESS FAILED ===");
            System.err.println("Error Type: " + e.getClass().getName());
            System.err.println("Error Message: " + e.getMessage());
            System.err.println("Stack Trace:");
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    @Transactional
    public void clearCart(Long idUsuario) {
        Carrito carrito = getOrCreateCart(idUsuario);
        List<CarritoDetalle> items = detalleRepository.findByIdCart(carrito.getIdCart());

        for (CarritoDetalle item : items) {
            detalleRepository.delete(item);
        }

        auditoriaService.registrar("Carrito", "CLEAR",
            "Carrito ID=" + carrito.getIdCart() + " vaciado",
            "usuario=" + idUsuario);
    }

    @Transactional
    private Carrito getOrCreateCart(Long idUsuario) {
        try {
            Iterable<Carrito> activeCarts = carritoRepository.findAllByIdUsuarioAndStatus(idUsuario, "A");
            List<Carrito> activeCartList = new ArrayList<>();
            activeCarts.forEach(activeCartList::add);

            if (activeCartList.isEmpty()) {
                Carrito newCart = new Carrito();
                newCart.setIdUsuario(idUsuario);
                newCart.setStatus("A");
                newCart.setFechaCreacion(LocalDateTime.now());
                newCart.setFechaActualizacion(LocalDateTime.now());

                return carritoRepository.save(newCart);
            }

            if (activeCartList.size() == 1) {
                return activeCartList.get(0);
            }

            activeCartList.sort((c1, c2) -> c2.getFechaActualizacion().compareTo(c1.getFechaActualizacion()));

            Carrito newestCart = activeCartList.get(0);

            for (int i = 1; i < activeCartList.size(); i++) {
                Carrito oldCart = activeCartList.get(i);
                oldCart.setStatus("I");
                carritoRepository.save(oldCart);

                auditoriaService.registrar("Carrito", "DEACTIVATE",
                    "Deactivated duplicate cart ID=" + oldCart.getIdCart(),
                    "usuario=" + idUsuario);
            }

            return newestCart;
        } catch (Exception e) {
            auditoriaService.registrar("Carrito", "ERROR",
                "Error in getOrCreateCart: " + e.getMessage(),
                "usuario=" + idUsuario);
            throw e;
        }
    }

    @Transactional
    public void migrateItemsFromDuplicateCarts(Long idUsuario) {
        try {
            Iterable<Carrito> allCarts = carritoRepository.findAllByIdUsuarioAndStatus(idUsuario, "A");
            List<Carrito> cartList = new ArrayList<>();
            allCarts.forEach(cartList::add);

            if (cartList.size() <= 1) {
                return;
            }

            cartList.sort((c1, c2) -> c2.getFechaActualizacion().compareTo(c1.getFechaActualizacion()));

            Carrito primaryCart = cartList.get(0);

            for (int i = 1; i < cartList.size(); i++) {
                Carrito oldCart = cartList.get(i);

                List<CarritoDetalle> itemsToMigrate = detalleRepository.findByIdCart(oldCart.getIdCart());

                for (CarritoDetalle item : itemsToMigrate) {
                    Optional<CarritoDetalle> existingItem = detalleRepository.findByIdCartAndIdMedicamento(
                            primaryCart.getIdCart(), item.getIdMedicamento());

                    if (existingItem.isPresent()) {
                        CarritoDetalle existing = existingItem.get();
                        existing.setCantidad(existing.getCantidad() + item.getCantidad());
                        detalleRepository.save(existing);

                        detalleRepository.delete(item);
                    } else {
                        item.setIdCart(primaryCart.getIdCart());
                        detalleRepository.save(item);
                    }
                }

                oldCart.setStatus("I");
                carritoRepository.save(oldCart);
            }

            auditoriaService.registrar("Carrito", "MIGRATE",
                "Migrated items from duplicate carts",
                "usuario=" + idUsuario);
        } catch (Exception e) {
            auditoriaService.registrar("Carrito", "ERROR",
                "Error migrating items: " + e.getMessage(),
                "usuario=" + idUsuario);
            throw e;
        }
    }

    private CarritoDTO buildCarritoDTO(Carrito carrito) {
        List<CarritoDetalle> detalles = detalleRepository.findByIdCart(carrito.getIdCart());
        List<CarritoDetalleDTO> detallesDTO = new ArrayList<>();
        double total = 0.0;

        for (CarritoDetalle detalle : detalles) {
            Medicamento medicamento = medicamentoRepository.findById(detalle.getIdMedicamento())
                    .orElseThrow(() -> new NoSuchElementException("Medicamento no encontrado: " + detalle.getIdMedicamento()));

            CarritoDetalleDTO detalleDTO = new CarritoDetalleDTO();
            detalleDTO.setIdCartItem(detalle.getIdCartItem());
            detalleDTO.setIdMedicamento(detalle.getIdMedicamento());
            detalleDTO.setNombreMedicamento(medicamento.getNombre());
            detalleDTO.setCantidad(detalle.getCantidad());
            detalleDTO.setPrecioUnitario(detalle.getPrecioUnitario());
            detalleDTO.setTotal(detalle.getCantidad() * detalle.getPrecioUnitario());
            detalleDTO.setRequiereReceta(medicamento.getRequiereReceta());

            detallesDTO.add(detalleDTO);
            total += detalleDTO.getTotal();
        }

        CarritoDTO dto = new CarritoDTO();
        dto.setIdCart(carrito.getIdCart());
        dto.setIdUsuario(carrito.getIdUsuario());
        dto.setStatus(carrito.getStatus());
        dto.setFechaCreacion(carrito.getFechaCreacion());
        dto.setFechaActualizacion(carrito.getFechaActualizacion());
        dto.setItems(detallesDTO);
        dto.setTotal(total);

        return dto;
    }
}
