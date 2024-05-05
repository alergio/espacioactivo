package com.alejodev.espacioactivo.exception;
public class DataIntegrityVExceptionWithMsg extends RuntimeException {
    public DataIntegrityVExceptionWithMsg(String entity){
        super("The " + entity + " you are sending is not found (404).");
    }
}