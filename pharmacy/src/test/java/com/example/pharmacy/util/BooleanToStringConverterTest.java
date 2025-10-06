package com.example.pharmacy.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BooleanToStringConverterTest {

    private final BooleanToStringConverter converter = new BooleanToStringConverter();

    @Test
    void convert_deberiaRetornarYCuandoEsTrue() {
        assertEquals("Y", converter.convert(true));
    }

    @Test
    void convert_deberiaRetornarNCuandoEsFalse() {
        assertEquals("N", converter.convert(false));
    }

    @Test
    void convert_deberiaLanzarNullPointerSiEsNull() {
        assertThrows(NullPointerException.class, () -> converter.convert(null));
    }
}
