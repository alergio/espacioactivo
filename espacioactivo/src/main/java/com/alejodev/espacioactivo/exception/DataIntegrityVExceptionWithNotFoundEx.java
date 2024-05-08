package com.alejodev.espacioactivo.exception;
public class DataIntegrityVExceptionWithNotFoundEx extends RuntimeException {
    public DataIntegrityVExceptionWithNotFoundEx(String entity){
        super("The " + entity + " you are sending is not found (404).");
    }
}