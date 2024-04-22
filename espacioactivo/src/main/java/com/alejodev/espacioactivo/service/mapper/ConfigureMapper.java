package com.alejodev.espacioactivo.service.mapper;

import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * Configuracion del Mapper para usar en el CRUDMapper.
 *
 * @author alejo
 * @version 1.0 21-04-2024
 */
@Component
public class ConfigureMapper {

    private static final Logger LOGGER = Logger.getLogger(ConfigureMapper.class);

    public ModelMapper configureMapper() {

        ModelMapper modelMapper = new ModelMapper();

        return modelMapper;

    }

}
