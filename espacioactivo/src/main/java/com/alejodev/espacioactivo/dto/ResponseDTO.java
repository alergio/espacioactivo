package com.alejodev.espacioactivo.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * El objeto que usaremos como interfaz para enviar las respuestas del servidor al cliente.
 *
 * @author alejo
 * @version 1.0 21-04-2024
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ResponseDTO {
    private int statusCode;
    private String message;
    private Map<String, Object> data;
}
