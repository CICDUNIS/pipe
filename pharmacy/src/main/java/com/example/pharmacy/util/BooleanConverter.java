package com.example.pharmacy.util;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

@ReadingConverter
public class BooleanConverter implements Converter<String, Boolean> {
    @Override
    public Boolean convert(String source) {
        return "Y".equalsIgnoreCase(source);
    }
}



