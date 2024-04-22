package com.alejodev.espacioactivo.exception;

/**
 * Excepcion para enviar al cliente cuando no se encuentra un recurso.
 * @author alejo
 * @version 1.0 22-04-2024
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String entityClassName){
        super("Error: " + entityClassName + " not found.");
    }
}