package com.alejodev.espacioactivo.exception;

import com.alejodev.espacioactivo.dto.ResponseDTO;
import org.apache.log4j.Logger;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
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

    private final Logger LOGGER = Logger.getLogger(GlobalException.class);
    private final String dataIntegrityErrorMsg =
            "An error has occurred on the server, there are conflicts with the integrity of the data you sent.";


    /**
     * Generic Exception Handler para que el cliente reciba un mensaje si algo inesperado falla.
     * @param ex
     * @return
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception ex) {

        ResponseDTO response = new ResponseDTO();

        response.setMessage("An unexpected error occurred on the server. Exception: " + ex.getClass().getSimpleName() + " Ex Msg: " + ex.getMessage());
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());

        LOGGER.error("ERROR 500: An unexpected error (" + ex.getClass().getSimpleName() + ") occurred on the server. Msg: " + ex.getMessage() + " Trace:", ex);

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

        response.setStatusCode(HttpStatus.BAD_REQUEST.value());
        response.setMessage(dataIntegrityErrorMsg);

        LOGGER.error("ERROR 400: BAD REQUEST (" + ex.getClass().getSimpleName() + ") occurred on the server. Trace:", ex);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

    }

    @ExceptionHandler(DataIntegrityVExceptionWithNotFoundEx.class)
    public ResponseEntity<Object> handleDataIntegrityVExceptionWithNotFoundEx(Exception ex) {

        ResponseDTO response = new ResponseDTO();

        response.setStatusCode(HttpStatus.BAD_REQUEST.value());
        response.setMessage(dataIntegrityErrorMsg + " Msg: " + ex.getMessage());

        LOGGER.error("ERROR 400: BAD REQUEST (" + ex.getClass().getSimpleName() + ") occurred on the server. Trace:", ex);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

    }

    @ExceptionHandler(DataIntegrityVExceptionWithMsg.class)
    public ResponseEntity<Object> handleDataIntegrityVExceptionWithMsg(Exception ex) {

        ResponseDTO response = new ResponseDTO();

        response.setStatusCode(HttpStatus.BAD_REQUEST.value());
        response.setMessage(dataIntegrityErrorMsg + " Msg: " + ex.getMessage());

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

    private ResponseEntity<Object> appointmentNotAvailable(Exception ex, String entity) {

        ResponseDTO response = new ResponseDTO();

        response.setStatusCode(HttpStatus.CONFLICT.value());
        response.setMessage("ERROR 409: The " + entity + " is not available. Exception Msg: " + ex.getMessage());

        LOGGER.error("ERROR 409: The " + entity + " is not available. Exception Msg: " + ex.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);

    }

    @ExceptionHandler(AppointmentStateException.class)
    public ResponseEntity<Object> appointmentStateEx(AppointmentStateException ex) {
        return appointmentNotAvailable(ex, "Appointment");
    }

    @ExceptionHandler(AppointmentIsFullException.class)
    public ResponseEntity<Object> appointmentFullEx(AppointmentIsFullException ex) {
        return appointmentNotAvailable(ex, "Appointment");
    }

    @ExceptionHandler(DisciplineAlreadyExistsException.class)
    public ResponseEntity<Object> disciplineAlreadyExistsEx(DisciplineAlreadyExistsException ex) {
        return appointmentNotAvailable(ex, "Discipline");
    }

    @ExceptionHandler(WrongTypeException.class)
    public ResponseEntity<Object> WrongTypeEx(WrongTypeException ex) {

        ResponseDTO response = new ResponseDTO();

        response.setStatusCode(HttpStatus.BAD_REQUEST.value());
        response.setMessage("There is a problem with the data you are sending. Msg: " + ex.getMessage());

        LOGGER.error("ERROR 400: BAD REQUEST (" + ex.getClass().getSimpleName() + ") occurred on the server. Trace:", ex);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

    }

    @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
    public ResponseEntity<Object> AuthenticationNotFoundException(AuthenticationCredentialsNotFoundException ex) {

        ResponseDTO response = new ResponseDTO();

        response.setStatusCode(HttpStatus.UNAUTHORIZED.value());
        response.setMessage("No authenticated user found.");

        LOGGER.error("ERROR 401: UNAUTHORIZED (" + ex.getClass().getSimpleName() + ") occurred on the server. Trace:", ex);

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);

    }


}
