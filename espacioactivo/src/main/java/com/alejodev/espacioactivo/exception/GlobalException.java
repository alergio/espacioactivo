package com.alejodev.espacioactivo.exception;

import com.alejodev.espacioactivo.dto.ResponseDTO;
import org.apache.log4j.Logger;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Handler global de todas las excepciones para enviar al cliente y al servidor.
 *
 * @author alejo
 * @version 1.0 22-04-2024
 */
@ControllerAdvice
public class GlobalException {

    Logger LOGGER = Logger.getLogger(GlobalException.class);


    /**
     * Generic Exception Handler para que el cliente reciba un mensaje si algo inesperado falla.
     * @param ex
     * @return
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception ex) {

        ResponseDTO response = new ResponseDTO();

        response.setMessage("An unexpected error occurred on the server. Exception: " + ex.getClass().getSimpleName());
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());

        LOGGER.error("ERROR 500: An unexpected error (" + ex.getClass().getSimpleName() + ") occurred on the server. Trace:", ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);

    }


    /**
     * Handler para excepcion por enviar datos invalidos.
     * @param ex
     * @return
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolationException(Exception ex) {

        ResponseDTO response = new ResponseDTO();

        response.setMessage("An error has occurred on the server, there are conflicts with the integrity of the data you sent.");
        response.setStatusCode(HttpStatus.BAD_REQUEST.value());

        LOGGER.error("ERROR 400: BAD REQUEST (" + ex.getClass().getSimpleName() + ") occurred on the server. Trace:", ex);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

    }


    /**
     * Handler para recurso no encontrado.
     * @param ex
     * @return
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> resourceNotFound(ResourceNotFoundException ex) {

        ResponseDTO response = new ResponseDTO();

        response.setStatusCode(HttpStatus.NOT_FOUND.value());
        response.setMessage(ex.getMessage());

        LOGGER.error("ERROR 404: Resource Not Found Exception. Exception Msg: " + ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

    }

}
