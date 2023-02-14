package com.easybustickets.routes_service.util.converters;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeToStringConverter implements Converter<LocalDateTime, String> {

    private final DateTimeFormatter formatter;

    public LocalDateTimeToStringConverter(String pattern) {
        this.formatter = DateTimeFormatter.ofPattern(pattern);
    }

    @Override
    public String convert(MappingContext<LocalDateTime, String> context) {
        LocalDateTime source = context.getSource();
        return source.format(formatter);
    }
}
