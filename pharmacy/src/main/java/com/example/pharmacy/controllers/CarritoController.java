package com.example.pharmacy.controllers;

import com.example.pharmacy.dto.CarritoDTO;
import com.example.pharmacy.model.Carrito;
import com.example.pharmacy.model.Usuario;
import com.example.pharmacy.repository.CarritoRepository;
import com.example.pharmacy.service.CarritoService;
import com.example.pharmacy.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;
import java.sql.Timestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/api/carrito")
public class CarritoController {

    private final CarritoService carritoService;
    private final UsuarioService usuarioService;
    private final CarritoRepository carritoRepository;
    private final JdbcTemplate jdbcTemplate;

    public CarritoController(
            CarritoService carritoService,
            UsuarioService usuarioService,
            CarritoRepository carritoRepository,
            JdbcTemplate jdbcTemplate) {
        this.carritoService = carritoService;
        this.usuarioService = usuarioService;
        this.carritoRepository = carritoRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping
    public ResponseEntity<CarritoDTO> getCart() {
        Usuario usuario = getCurrentUser();
        CarritoDTO cart = carritoService.getActiveCart(usuario.getIdUsuario());
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/items")
    public ResponseEntity<CarritoDTO> addItem(
            @RequestParam Long idMedicamento,
            @RequestParam Integer cantidad) {
        Usuario usuario = getCurrentUser();
        CarritoDTO cart = carritoService.addItem(usuario.getIdUsuario(), idMedicamento, cantidad);
        return ResponseEntity.ok(cart);
    }

    @PutMapping("/items/{idMedicamento}")
    public ResponseEntity<CarritoDTO> updateItemQuantity(
            @PathVariable Long idMedicamento,
            @RequestParam Integer cantidad) {
        Usuario usuario = getCurrentUser();
        CarritoDTO cart = carritoService.updateItemQuantity(usuario.getIdUsuario(), idMedicamento, cantidad);
        return ResponseEntity.ok(cart);
    }

    @DeleteMapping("/items/{idMedicamento}")
    public ResponseEntity<CarritoDTO> removeItem(@PathVariable Long idMedicamento) {
        Usuario usuario = getCurrentUser();
        CarritoDTO cart = carritoService.removeItem(usuario.getIdUsuario(), idMedicamento);
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/checkout")
    public ResponseEntity<CarritoDTO> checkout() {
        Usuario usuario = getCurrentUser();
        CarritoDTO cart = carritoService.checkout(usuario.getIdUsuario());
        return ResponseEntity.ok(cart);
    }

    @DeleteMapping
    public ResponseEntity<Void> clearCart() {
        Usuario usuario = getCurrentUser();
        carritoService.clearCart(usuario.getIdUsuario());
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/fix-duplicates")
    public ResponseEntity<Map<String, Object>> fixDuplicateCarts() {
        Usuario usuario = getCurrentUser();
        Long userId = usuario.getIdUsuario();

        Iterable<Carrito> activeCarts = carritoRepository.findAllByIdUsuarioAndStatus(userId, "A");
        List<Carrito> cartsList = StreamSupport.stream(activeCarts.spliterator(), false)
                .collect(Collectors.toList());

        Map<String, Object> result = new HashMap<>();
        result.put("userId", userId);
        result.put("duplicateCartsFound", cartsList.size());

        if (cartsList.size() <= 1) {
            result.put("status", "NO_ACTION_NEEDED");
            result.put("message", "No duplicate carts found.");
            return ResponseEntity.ok(result);
        }

        Carrito mostRecentCart = cartsList.stream()
                .sorted((c1, c2) -> c2.getFechaActualizacion().compareTo(c1.getFechaActualizacion()))
                .findFirst()
                .orElse(null);

        int canceledCarts = 0;
        for (Carrito cart : cartsList) {
            if (!cart.getIdCart().equals(mostRecentCart.getIdCart())) {
                cart.setStatus("X");
                cart.setFechaActualizacion(LocalDateTime.now());
                carritoRepository.save(cart);
                canceledCarts++;
            }
        }

        result.put("status", "FIXED");
        result.put("message", "Kept cart ID " + mostRecentCart.getIdCart() + " and canceled " + canceledCarts + " other carts.");
        result.put("keptCartId", mostRecentCart.getIdCart());
        result.put("canceledCarts", canceledCarts);

        return ResponseEntity.ok(result);
    }

    @PostMapping("/emergency-create")
    public ResponseEntity<Map<String, Object>> createEmergencyCart() {
        try {
            Usuario usuario = getCurrentUser();
            Long userId = usuario.getIdUsuario();

            Map<String, Object> result = new HashMap<>();
            result.put("userId", userId);

            Iterable<Carrito> existingCarts = carritoRepository.findAllByIdUsuarioAndStatus(userId, "A");
            int deactivatedCount = 0;

            for (Carrito cart : existingCarts) {
                cart.setStatus("I");
                carritoRepository.save(cart);
                deactivatedCount++;
            }

            result.put("deactivatedCarts", deactivatedCount);

            Carrito newCart = new Carrito();
            newCart.setIdUsuario(userId);
            newCart.setStatus("A");
            newCart.setFechaCreacion(LocalDateTime.now());
            newCart.setFechaActualizacion(LocalDateTime.now());

            Carrito savedCart = carritoRepository.save(newCart);

            result.put("status", "SUCCESS");
            result.put("message", "Created fresh cart with ID: " + savedCart.getIdCart());
            result.put("newCartId", savedCart.getIdCart());

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("status", "ERROR");
            error.put("message", "Failed to create emergency cart: " + e.getMessage());
            error.put("exceptionType", e.getClass().getName());
            return ResponseEntity.status(500).body(error);
        }
    }


    @GetMapping("/diagnostic")
    public ResponseEntity<Map<String, Object>> getDiagnosticInfo() {
        try {
            Usuario usuario = getCurrentUser();
            Long userId = usuario.getIdUsuario();

            Map<String, Object> result = new HashMap<>();
            result.put("userId", userId);

            Iterable<Carrito> allCarts = carritoRepository.findAll();
            List<Map<String, Object>> cartDetails = new ArrayList<>();

            for (Carrito cart : allCarts) {
                if (cart.getIdUsuario().equals(userId)) {
                    Map<String, Object> cartInfo = new HashMap<>();
                    cartInfo.put("idCart", cart.getIdCart());
                    cartInfo.put("status", cart.getStatus());
                    cartInfo.put("fechaCreacion", cart.getFechaCreacion());
                    cartInfo.put("fechaActualizacion", cart.getFechaActualizacion());
                    cartDetails.add(cartInfo);
                }
            }

            result.put("carts", cartDetails);
            result.put("totalCarts", cartDetails.size());

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("status", "ERROR");
            error.put("message", "Diagnostic failed: " + e.getMessage());
            error.put("exceptionType", e.getClass().getName());
            return ResponseEntity.status(500).body(error);
        }
    }

    @PostMapping("/raw-create")
    public ResponseEntity<Map<String, Object>> createRawCart() {
        try {
            Usuario usuario = getCurrentUser();
            Long userId = usuario.getIdUsuario();

            Map<String, Object> result = new HashMap<>();
            result.put("userId", userId);

            String updateSql = "UPDATE CARRITO SET STATUS = 'I', FECHA_ACTUALIZACION = ? WHERE ID_USUARIO = ? AND STATUS = 'A'";
            int updatedRows = jdbcTemplate.update(updateSql, Timestamp.valueOf(LocalDateTime.now()), userId);
            result.put("deactivatedCarts", updatedRows);

            LocalDateTime now = LocalDateTime.now();
            String insertSql = "INSERT INTO CARRITO (ID_USUARIO, STATUS, FECHA_CREACION, FECHA_ACTUALIZACION) VALUES (?, 'A', ?, ?)";
            jdbcTemplate.update(insertSql, userId, Timestamp.valueOf(now), Timestamp.valueOf(now));

            String querySql = "SELECT ID_CART FROM CARRITO WHERE ID_USUARIO = ? AND STATUS = 'A' AND ROWNUM = 1 ORDER BY FECHA_CREACION DESC";
            Long newCartId = jdbcTemplate.queryForObject(querySql, Long.class, userId);

            result.put("status", "SUCCESS");
            result.put("message", "Created fresh cart with ID: " + newCartId);
            result.put("newCartId", newCartId);

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("status", "ERROR");
            error.put("message", "Failed to create raw cart: " + e.getMessage());
            error.put("exceptionType", e.getClass().getName());
            error.put("stackTrace", e.getStackTrace());
            return ResponseEntity.status(500).body(error);
        }
    }

    @PostMapping("/raw-add-item")
    public ResponseEntity<Map<String, Object>> rawAddItemToCart(
            @RequestParam Long idMedicamento,
            @RequestParam Integer cantidad) {
        try {
            Usuario usuario = getCurrentUser();
            Long userId = usuario.getIdUsuario();

            Map<String, Object> result = new HashMap<>();
            result.put("userId", userId);
            result.put("idMedicamento", idMedicamento);
            result.put("cantidad", cantidad);

            String cartSql = "SELECT ID_CART FROM (SELECT ID_CART FROM CARRITO WHERE ID_USUARIO = ? AND STATUS = 'A' ORDER BY FECHA_CREACION DESC) WHERE ROWNUM = 1";
            Long cartId = null;
            try {
                cartId = jdbcTemplate.queryForObject(cartSql, Long.class, userId);
            } catch (Exception e) {
                cartId = null;
            }

            if (cartId == null) {
                LocalDateTime now = LocalDateTime.now();
                String insertCartSql = "INSERT INTO CARRITO (ID_USUARIO, STATUS, FECHA_CREACION, FECHA_ACTUALIZACION) VALUES (?, 'A', ?, ?)";
                jdbcTemplate.update(insertCartSql, userId, Timestamp.valueOf(now), Timestamp.valueOf(now));

                cartId = jdbcTemplate.queryForObject(cartSql, Long.class, userId);
            }

            result.put("cartId", cartId);

            String medicineSql = "SELECT PRECIO FROM MEDICAMENTO WHERE ID_MEDICAMENTO = ?";
            Double precio = jdbcTemplate.queryForObject(medicineSql, Double.class, idMedicamento);

            String itemCheckSql = "SELECT COUNT(*) FROM CARRITO_DETALLE WHERE ID_CART = ? AND ID_MEDICAMENTO = ?";
            int existingItems = jdbcTemplate.queryForObject(itemCheckSql, Integer.class, cartId, idMedicamento);

            if (existingItems > 0) {
                String updateSql = "UPDATE CARRITO_DETALLE SET CANTIDAD = CANTIDAD + ? WHERE ID_CART = ? AND ID_MEDICAMENTO = ?";
                jdbcTemplate.update(updateSql, cantidad, cartId, idMedicamento);
                result.put("action", "UPDATED");
            } else {
                String insertSql = "INSERT INTO CARRITO_DETALLE (ID_CART, ID_MEDICAMENTO, CANTIDAD, PRECIO_UNITARIO) VALUES (?, ?, ?, ?)";
                jdbcTemplate.update(insertSql, cartId, idMedicamento, cantidad, precio);
                result.put("action", "INSERTED");
            }

            String updateCartSql = "UPDATE CARRITO SET FECHA_ACTUALIZACION = ? WHERE ID_CART = ?";
            jdbcTemplate.update(updateCartSql, Timestamp.valueOf(LocalDateTime.now()), cartId);

            result.put("status", "SUCCESS");
            result.put("message", "Item added to cart");

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("status", "ERROR");
            error.put("message", "Failed to add item to cart: " + e.getMessage());
            error.put("exceptionType", e.getClass().getName());
            error.put("stackTrace", e.getStackTrace());
            return ResponseEntity.status(500).body(error);
        }
    }

    @GetMapping("/test")
    public ResponseEntity<Map<String, Object>> testEndpoint() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "SUCCESS");
        response.put("message", "Test endpoint is working");
        response.put("timestamp", LocalDateTime.now().toString());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/simple-add")
    public ResponseEntity<Map<String, Object>> simpleAddItem(
            @RequestParam Long idMedicamento,
            @RequestParam Integer cantidad) {

        Usuario usuario = getCurrentUser();
        Long userId = usuario.getIdUsuario();

        Map<String, Object> response = new HashMap<>();
        response.put("status", "SUCCESS");
        response.put("message", "Simple add endpoint received request");
        response.put("userId", userId);
        response.put("idMedicamento", idMedicamento);
        response.put("cantidad", cantidad);
        response.put("timestamp", LocalDateTime.now().toString());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/jdbc-cart")
    public ResponseEntity<Map<String, Object>> getJdbcCart() {
        try {
            Usuario usuario = getCurrentUser();
            Long userId = usuario.getIdUsuario();

            Map<String, Object> cart = new HashMap<>();
            List<Map<String, Object>> items = new ArrayList<>();
            double total = 0.0;

            String cartQuery = "SELECT * FROM (SELECT * FROM CARRITO WHERE ID_USUARIO = ? AND STATUS = 'A' ORDER BY FECHA_ACTUALIZACION DESC) WHERE ROWNUM = 1";
            Map<String, Object> cartData = null;

            try {
                cartData = jdbcTemplate.queryForMap(cartQuery, userId);
            } catch (Exception e) {
                System.out.println("INFO: No active cart found for user " + userId + ". Creating a new one.");
                LocalDateTime now = LocalDateTime.now();
                String insertSql = "INSERT INTO CARRITO (ID_USUARIO, STATUS, FECHA_CREACION, FECHA_ACTUALIZACION) VALUES (?, 'A', ?, ?)";
                jdbcTemplate.update(insertSql, userId, Timestamp.valueOf(now), Timestamp.valueOf(now));

                cartData = jdbcTemplate.queryForMap(cartQuery, userId);
                System.out.println("INFO: New cart created with ID: " + cartData.get("ID_CART"));
            }

            Long cartId = ((Number) cartData.get("ID_CART")).longValue();

            String itemsQuery = "SELECT CD.*, M.NOMBRE FROM CARRITO_DETALLE CD " +
                               "JOIN MEDICAMENTO M ON CD.ID_MEDICAMENTO = M.ID_MEDICAMENTO " +
                               "WHERE CD.ID_CART = ?";

            List<Map<String, Object>> itemsList = jdbcTemplate.queryForList(itemsQuery, cartId);

            for (Map<String, Object> item : itemsList) {
                Map<String, Object> itemDto = new HashMap<>();
                Long idCartItem = ((Number) item.get("ID_CART_ITEM")).longValue();
                Long idMedicamento = ((Number) item.get("ID_MEDICAMENTO")).longValue();
                Integer cantidad = ((Number) item.get("CANTIDAD")).intValue();
                Double precio = ((Number) item.get("PRECIO_UNITARIO")).doubleValue();
                String nombre = (String) item.get("NOMBRE");

                String medQuery = "SELECT REQUIERE_RECETA FROM MEDICAMENTO WHERE ID_MEDICAMENTO = ?";
                String requiereReceta = jdbcTemplate.queryForObject(medQuery, String.class, idMedicamento);

                itemDto.put("idCartItem", idCartItem);
                itemDto.put("idMedicamento", idMedicamento);
                itemDto.put("nombreMedicamento", nombre);
                itemDto.put("cantidad", cantidad);
                itemDto.put("precioUnitario", precio);
                itemDto.put("total", precio * cantidad);
                itemDto.put("requiereReceta", requiereReceta);

                items.add(itemDto);
                total += precio * cantidad;
            }

            cart.put("idCart", cartId);
            cart.put("idUsuario", userId);
            cart.put("status", cartData.get("STATUS"));
            cart.put("fechaCreacion", cartData.get("FECHA_CREACION"));
            cart.put("fechaActualizacion", cartData.get("FECHA_ACTUALIZACION"));
            cart.put("items", items);
            cart.put("total", total);

            System.out.println("INFO: Successfully retrieved cart " + cartId + " with " + items.size() + " items");
            return ResponseEntity.ok(cart);
        } catch (Exception e) {
            System.out.println("INFO: Returning empty cart due to error in getJdbcCart: " + e.getMessage());

            Map<String, Object> emptyCart = new HashMap<>();
            emptyCart.put("status", "A");
            emptyCart.put("items", new ArrayList<>());
            emptyCart.put("total", 0.0);

            return ResponseEntity.ok(emptyCart);
        }
    }

    @PostMapping("/jdbc-add-item")
    public ResponseEntity<Map<String, Object>> jdbcAddItem(
            @RequestParam Long idMedicamento,
            @RequestParam Integer cantidad) {
        try {
            Usuario usuario = getCurrentUser();
            Long userId = usuario.getIdUsuario();

            String medicineQuery = "SELECT PRECIO, STOCK, REQUIERE_RECETA FROM MEDICAMENTO WHERE ID_MEDICAMENTO = ?";
            Map<String, Object> medicine;
            try {
                medicine = jdbcTemplate.queryForMap(medicineQuery, idMedicamento);
            } catch (Exception e) {
                System.out.println("INFO: Medicine not found: " + idMedicamento);

                Map<String, Object> error = new HashMap<>();
                error.put("status", "ERROR");
                error.put("message", "Medicamento no encontrado: " + idMedicamento);
                return ResponseEntity.status(404).body(error);
            }

            Double precio = ((Number) medicine.get("PRECIO")).doubleValue();
            Integer stock = ((Number) medicine.get("STOCK")).intValue();
            String requiereReceta = (String) medicine.get("REQUIERE_RECETA");

            if ("Y".equalsIgnoreCase(requiereReceta)) {
                System.out.println("INFO: Medicine requires prescription: " + idMedicamento);

                Map<String, Object> error = new HashMap<>();
                error.put("status", "ERROR");
                error.put("message", "Este medicamento requiere receta médica");
                return ResponseEntity.status(400).body(error);
            }

            if (stock < cantidad) {
                System.out.println("INFO: Insufficient stock for medicine: " + idMedicamento);

                Map<String, Object> error = new HashMap<>();
                error.put("status", "ERROR");
                error.put("message", "Stock insuficiente");
                return ResponseEntity.status(400).body(error);
            }

            String cartQuery = "SELECT * FROM (SELECT * FROM CARRITO WHERE ID_USUARIO = ? AND STATUS = 'A' ORDER BY FECHA_ACTUALIZACION DESC) WHERE ROWNUM = 1";
            Map<String, Object> cartData = null;

            try {
                cartData = jdbcTemplate.queryForMap(cartQuery, userId);
            } catch (Exception e) {
                System.out.println("INFO: Creating new cart for user: " + userId);

                LocalDateTime now = LocalDateTime.now();
                String insertSql = "INSERT INTO CARRITO (ID_USUARIO, STATUS, FECHA_CREACION, FECHA_ACTUALIZACION) VALUES (?, 'A', ?, ?)";
                jdbcTemplate.update(insertSql, userId, Timestamp.valueOf(now), Timestamp.valueOf(now));

                cartData = jdbcTemplate.queryForMap(cartQuery, userId);
            }

            Long cartId = ((Number) cartData.get("ID_CART")).longValue();

            String itemCheckQuery = "SELECT ID_CART_ITEM, CANTIDAD FROM CARRITO_DETALLE WHERE ID_CART = ? AND ID_MEDICAMENTO = ?";
            try {
                Map<String, Object> existingItem = jdbcTemplate.queryForMap(itemCheckQuery, cartId, idMedicamento);

                Long itemId = ((Number) existingItem.get("ID_CART_ITEM")).longValue();
                Integer currentQty = ((Number) existingItem.get("CANTIDAD")).intValue();
                Integer newQty = currentQty + cantidad;

                String updateItemSql = "UPDATE CARRITO_DETALLE SET CANTIDAD = ? WHERE ID_CART_ITEM = ?";
                jdbcTemplate.update(updateItemSql, newQty, itemId);

                System.out.println("INFO: Updated existing item in cart: " + itemId + " with new quantity: " + newQty);
            } catch (Exception e) {
                String insertItemSql = "INSERT INTO CARRITO_DETALLE (ID_CART, ID_MEDICAMENTO, CANTIDAD, PRECIO_UNITARIO) VALUES (?, ?, ?, ?)";
                jdbcTemplate.update(insertItemSql, cartId, idMedicamento, cantidad, precio);

                System.out.println("INFO: Added new item to cart: Medicine ID=" + idMedicamento + ", Qty=" + cantidad);
            }

            String updateCartSql = "UPDATE CARRITO SET FECHA_ACTUALIZACION = ? WHERE ID_CART = ?";
            jdbcTemplate.update(updateCartSql, Timestamp.valueOf(LocalDateTime.now()), cartId);

            String auditSql = "INSERT INTO AUDITORIA (TABLA, OPERACION, DESCRIPCION, INFO_ADICIONAL, FECHA) VALUES (?, ?, ?, ?, ?)";
            jdbcTemplate.update(auditSql, "Carrito", "UPDATE",
                    "Agregado medicamento ID=" + idMedicamento + " al carrito ID=" + cartId,
                    "usuario=" + userId, Timestamp.valueOf(LocalDateTime.now()));

            System.out.println("INFO: Successfully added/updated item in cart: " + cartId);

            return this.getJdbcCart();
        } catch (Exception e) {
            System.out.println("INFO: Error in jdbcAddItem, will attempt to get current cart: " + e.getMessage());

            try {
                System.out.println("INFO: The operation might have succeeded partially. Retrieving current cart state.");
                return this.getJdbcCart();
            } catch (Exception ex) {
                System.out.println("INFO: Could not retrieve cart after error: " + ex.getMessage());
                Map<String, Object> error = new HashMap<>();
                error.put("status", "ERROR");
                error.put("message", "Error adding item to cart, but the operation might have succeeded partially.");
                error.put("canGetCart", false);
                return ResponseEntity.ok(error);
            }
        }
    }

    private Usuario getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return usuarioService.findByCorreo(email);
    }
}
