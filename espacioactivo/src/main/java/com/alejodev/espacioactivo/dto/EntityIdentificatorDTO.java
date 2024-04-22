package com.alejodev.espacioactivo.dto;

/**
 * Interfaz para poder mapear DTOs que no estan definidos todavia.
 * Especifico para el CRUDMapper.
 *
 * @author alejo
 * @version 1.0 21-04-2024
 */
public interface EntityIdentificatorDTO {
    Long getId();
    void setId(Long id);
}
