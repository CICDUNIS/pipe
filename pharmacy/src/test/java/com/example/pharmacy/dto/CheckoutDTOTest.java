package com.example.pharmacy.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CheckoutDTOTest {

    private CheckoutDTO checkoutDTO;

    @BeforeEach
    void setUp() {
        checkoutDTO = new CheckoutDTO();
    }

    @Test
    void testGetIdCart() {
        assertNull(checkoutDTO.getIdCart());
        
        checkoutDTO.setIdCart(100L);
        assertEquals(100L, checkoutDTO.getIdCart());
    }

    @Test
    void testSetIdCart() {
        checkoutDTO.setIdCart(200L);
        assertEquals(200L, checkoutDTO.getIdCart());
        
        checkoutDTO.setIdCart(null);
        assertNull(checkoutDTO.getIdCart());
    }

    @Test
    void testGetDescuento() {
        assertNull(checkoutDTO.getDescuento());
        
        checkoutDTO.setDescuento(15.50);
        assertEquals(15.50, checkoutDTO.getDescuento());
    }

    @Test
    void testSetDescuento() {
        checkoutDTO.setDescuento(25.75);
        assertEquals(25.75, checkoutDTO.getDescuento());
        
        checkoutDTO.setDescuento(0.0);
        assertEquals(0.0, checkoutDTO.getDescuento());
        
        checkoutDTO.setDescuento(null);
        assertNull(checkoutDTO.getDescuento());
    }

    @Test
    void testGetEmail() {
        assertNull(checkoutDTO.getEmail());
        
        checkoutDTO.setEmail("test@example.com");
        assertEquals("test@example.com", checkoutDTO.getEmail());
    }

    @Test
    void testSetEmail() {
        checkoutDTO.setEmail("user@domain.com");
        assertEquals("user@domain.com", checkoutDTO.getEmail());
        
        checkoutDTO.setEmail("");
        assertEquals("", checkoutDTO.getEmail());
        
        checkoutDTO.setEmail(null);
        assertNull(checkoutDTO.getEmail());
    }

    @Test
    void testSetIdCart_WithDifferentValues() {
        checkoutDTO.setIdCart(1L);
        assertEquals(1L, checkoutDTO.getIdCart());
        
        checkoutDTO.setIdCart(999L);
        assertEquals(999L, checkoutDTO.getIdCart());
        
        checkoutDTO.setIdCart(0L);
        assertEquals(0L, checkoutDTO.getIdCart());
        
        checkoutDTO.setIdCart(-1L);
        assertEquals(-1L, checkoutDTO.getIdCart());
    }

    @Test
    void testSetDescuento_WithDifferentValues() {
        checkoutDTO.setDescuento(10.0);
        assertEquals(10.0, checkoutDTO.getDescuento());
        
        checkoutDTO.setDescuento(99.99);
        assertEquals(99.99, checkoutDTO.getDescuento());
        
        checkoutDTO.setDescuento(0.01);
        assertEquals(0.01, checkoutDTO.getDescuento());
        
        checkoutDTO.setDescuento(-5.0);
        assertEquals(-5.0, checkoutDTO.getDescuento());
    }

    @Test
    void testSetEmail_WithDifferentValues() {
        checkoutDTO.setEmail("admin@pharmacy.com");
        assertEquals("admin@pharmacy.com", checkoutDTO.getEmail());
        
        checkoutDTO.setEmail("user123@test.org");
        assertEquals("user123@test.org", checkoutDTO.getEmail());
        
        checkoutDTO.setEmail("a@b.c");
        assertEquals("a@b.c", checkoutDTO.getEmail());
        
        checkoutDTO.setEmail("test.email+tag@domain.co.uk");
        assertEquals("test.email+tag@domain.co.uk", checkoutDTO.getEmail());
    }

    @Test
    void testSetIdCart_WithEdgeCases() {
        checkoutDTO.setIdCart(Long.MAX_VALUE);
        assertEquals(Long.MAX_VALUE, checkoutDTO.getIdCart());
        
        checkoutDTO.setIdCart(Long.MIN_VALUE);
        assertEquals(Long.MIN_VALUE, checkoutDTO.getIdCart());
    }

    @Test
    void testSetDescuento_WithEdgeCases() {
        checkoutDTO.setDescuento(Double.MAX_VALUE);
        assertEquals(Double.MAX_VALUE, checkoutDTO.getDescuento());
        
        checkoutDTO.setDescuento(Double.MIN_VALUE);
        assertEquals(Double.MIN_VALUE, checkoutDTO.getDescuento());
        
        checkoutDTO.setDescuento(Double.POSITIVE_INFINITY);
        assertEquals(Double.POSITIVE_INFINITY, checkoutDTO.getDescuento());
        
        checkoutDTO.setDescuento(Double.NEGATIVE_INFINITY);
        assertEquals(Double.NEGATIVE_INFINITY, checkoutDTO.getDescuento());
        
        checkoutDTO.setDescuento(Double.NaN);
        assertTrue(Double.isNaN(checkoutDTO.getDescuento()));
    }

    @Test
    void testSetEmail_WithEdgeCases() {
        checkoutDTO.setEmail("very.long.email.address@very.long.domain.name.com");
        assertEquals("very.long.email.address@very.long.domain.name.com", checkoutDTO.getEmail());
        
        checkoutDTO.setEmail("123@456.789");
        assertEquals("123@456.789", checkoutDTO.getEmail());
        
        checkoutDTO.setEmail("test@test");
        assertEquals("test@test", checkoutDTO.getEmail());
    }

    @Test
    void testMultipleFieldUpdates() {
        checkoutDTO.setIdCart(100L);
        checkoutDTO.setDescuento(20.0);
        checkoutDTO.setEmail("test@example.com");
        
        assertEquals(100L, checkoutDTO.getIdCart());
        assertEquals(20.0, checkoutDTO.getDescuento());
        assertEquals("test@example.com", checkoutDTO.getEmail());
        
        // Update all fields again
        checkoutDTO.setIdCart(200L);
        checkoutDTO.setDescuento(30.0);
        checkoutDTO.setEmail("updated@example.com");
        
        assertEquals(200L, checkoutDTO.getIdCart());
        assertEquals(30.0, checkoutDTO.getDescuento());
        assertEquals("updated@example.com", checkoutDTO.getEmail());
    }

    @Test
    void testNullToValueToNull() {
        // Start with null values
        assertNull(checkoutDTO.getIdCart());
        assertNull(checkoutDTO.getDescuento());
        assertNull(checkoutDTO.getEmail());
        
        // Set values
        checkoutDTO.setIdCart(100L);
        checkoutDTO.setDescuento(15.0);
        checkoutDTO.setEmail("test@example.com");
        
        assertEquals(100L, checkoutDTO.getIdCart());
        assertEquals(15.0, checkoutDTO.getDescuento());
        assertEquals("test@example.com", checkoutDTO.getEmail());
        
        // Set back to null
        checkoutDTO.setIdCart(null);
        checkoutDTO.setDescuento(null);
        checkoutDTO.setEmail(null);
        
        assertNull(checkoutDTO.getIdCart());
        assertNull(checkoutDTO.getDescuento());
        assertNull(checkoutDTO.getEmail());
    }
}


