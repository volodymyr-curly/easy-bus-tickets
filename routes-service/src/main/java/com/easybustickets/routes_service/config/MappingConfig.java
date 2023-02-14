package com.easybustickets.routes_service.config;

import com.easybustickets.routes_service.util.converters.LocalDateTimeToStringConverter;
import com.easybustickets.routes_service.util.converters.StringToLocalDateTimeConverter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MappingConfig {

    public static final String PATTERN = "dd-MM-yyyy HH:mm";

    @Bean
    public ModelMapper getModelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addConverter(new LocalDateTimeToStringConverter(PATTERN));
        modelMapper.addConverter(new StringToLocalDateTimeConverter(PATTERN));
        return modelMapper;
    }
}
