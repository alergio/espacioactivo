package com.alejodev.espacioactivo.exception;

public class DataIntegrityVExceptionWithMsg extends RuntimeException {
    public DataIntegrityVExceptionWithMsg(String msg){
        super(msg);
    }

    public static String emptyFieldMessage(String field) {
        return "You can't send empty " + field + " field.";
    }
}
