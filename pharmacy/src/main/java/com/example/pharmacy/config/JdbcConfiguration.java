package com.example.pharmacy.config;

import com.example.pharmacy.dto.VentaDTO;
import com.example.pharmacy.dto.VentaDetalleDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.HashMap;


@Configuration
@EnableTransactionManagement
public class JdbcConfiguration {

        public static boolean updateCartStatus(JdbcTemplate jdbcTemplate, Long cartId, String status) {
        try {
            System.out.println("JdbcConfiguration: Updating cart status with direct JDBC - Cart ID: " + cartId);
            String sql = "UPDATE CARRITO SET STATUS = ?, FECHA_ACTUALIZACION = SYSTIMESTAMP WHERE ID_CART = ?";
            int updatedRows = jdbcTemplate.update(sql, status, cartId);
            System.out.println("JdbcConfiguration: Direct JDBC update result - Updated rows: " + updatedRows);
            return updatedRows > 0;
        } catch (Exception e) {
            System.err.println("JdbcConfiguration: Error in direct JDBC update: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }


    public static Map<String, Object> insertVentaJdbc(JdbcTemplate jdbcTemplate, VentaDTO ventaDTO) {
        try {
            System.out.println("JdbcConfiguration: Inserting VENTA via direct JDBC");

            Long ventaId = jdbcTemplate.queryForObject(
                "SELECT SEQ_VENTA.NEXTVAL FROM DUAL", Long.class);

            System.out.println("JdbcConfiguration: Generated VENTA ID: " + ventaId);

            String sql = "INSERT INTO VENTA (ID_VENTA, ID_USUARIO, ID_RECETA, FECHA_VENTA, " +
                         "TOTAL, IMPUESTO, DESCUENTO, MONTO_PAGADO) " +
                         "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            int rowsInserted = jdbcTemplate.update(sql,
                ventaId,
                ventaDTO.getIdUsuario(),
                ventaDTO.getIdReceta(),
                Timestamp.valueOf(LocalDateTime.now()),
                ventaDTO.getTotal() != null ? ventaDTO.getTotal() : 0.0,
                ventaDTO.getImpuesto() != null ? ventaDTO.getImpuesto() : 0.0,
                ventaDTO.getDescuento() != null ? ventaDTO.getDescuento() : 0.0,
                ventaDTO.getMontoPagado() != null ? ventaDTO.getMontoPagado() : 0.0
            );

            System.out.println("JdbcConfiguration: VENTA insert result - Rows: " + rowsInserted);

            Map<String, Object> result = new HashMap<>();
            result.put("idVenta", ventaId);
            result.put("success", rowsInserted > 0);

            return result;
        } catch (Exception e) {
            System.err.println("JdbcConfiguration: Error in VENTA direct JDBC insert: " + e.getMessage());
            e.printStackTrace();
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("error", e.getMessage());
            return result;
        }
    }

    public static Map<String, Object> insertVentaDetalleJdbc(
            JdbcTemplate jdbcTemplate, Long idVenta, VentaDetalleDTO detalleDTO) {
        try {
            System.out.println("JdbcConfiguration: Inserting VENTADETALLE via direct JDBC");

            Long detalleId = jdbcTemplate.queryForObject(
                "SELECT SEQ_VENTA_DETALLE.NEXTVAL FROM DUAL", Long.class);

            System.out.println("JdbcConfiguration: Generated VENTADETALLE ID: " + detalleId);

            String sql = "INSERT INTO VENTADETALLE (ID_VENTA_DETALLE, ID_VENTA, ID_MEDICAMENTO, " +
                         "CANTIDAD, PRECIO_UNITARIO, TOTAL_LINEA) " +
                         "VALUES (?, ?, ?, ?, ?, ?)";

            int rowsInserted = jdbcTemplate.update(sql,
                detalleId,
                idVenta,
                detalleDTO.getIdMedicamento(),
                detalleDTO.getCantidad(),
                detalleDTO.getPrecioUnitario(),
                detalleDTO.getTotalLinea()
            );

            System.out.println("JdbcConfiguration: VENTADETALLE insert result - Rows: " + rowsInserted);

            Map<String, Object> result = new HashMap<>();
            result.put("idVentaDetalle", detalleId);
            result.put("success", rowsInserted > 0);

            return result;
        } catch (Exception e) {
            System.err.println("JdbcConfiguration: Error in VENTADETALLE direct JDBC insert: " + e.getMessage());
            e.printStackTrace();
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("error", e.getMessage());
            return result;
        }
    }
}
