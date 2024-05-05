package com.alejodev.espacioactivo.exception;
public class DataIntegrityVExceptionWithMsg extends RuntimeException {
    public DataIntegrityVExceptionWithMsg(String msg){
        super(msg);
    }
}