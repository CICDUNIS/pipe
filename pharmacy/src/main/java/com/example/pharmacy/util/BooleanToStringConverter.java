package com.example.pharmacy.util;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

@WritingConverter
public class BooleanToStringConverter implements Converter<Boolean, String> {
    @Override
    public String convert(Boolean source) {
        return source ? "Y" : "N";
    }
}


