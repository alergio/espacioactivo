package com.alejodev.espacioactivo.service;

import com.alejodev.espacioactivo.dto.DisciplineDTO;
import com.alejodev.espacioactivo.dto.EntityIdentificatorDTO;
import com.alejodev.espacioactivo.dto.ResponseDTO;

/**
 * Interfaz para reutilizar en los servicios que hacen CRUDs.
 *
 * @author alejo
 * @version 1.0 21-04-2024
 * @param <T> algun DTO.
 */
public interface ICRUDService<T> {
    ResponseDTO create (EntityIdentificatorDTO entityDTO) throws Exception;

    ResponseDTO readById(Long id);

    ResponseDTO readAll();

    ResponseDTO update(EntityIdentificatorDTO entityDTO);

    ResponseDTO delete(Long id);
}