package com.example.pharmacy.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BooleanConverterTest {

    private final BooleanConverter converter = new BooleanConverter();

    @Test
    void convert_deberiaRetornarTrueSiRecibeY() {
        assertTrue(converter.convert("Y"));
        assertTrue(converter.convert("y"));
    }

    @Test
    void convert_deberiaRetornarFalseSiRecibeOtroValor() {
        assertFalse(converter.convert("N"));
        assertFalse(converter.convert("No"));
        assertFalse(converter.convert("true"));
        assertFalse(converter.convert(""));
        assertFalse(converter.convert(" "));
    }

    @Test
    void convert_deberiaRetornarFalseSiRecibeNull() {
        assertFalse(converter.convert(null));
    }
}
