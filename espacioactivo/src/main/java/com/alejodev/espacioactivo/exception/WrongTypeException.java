package com.alejodev.espacioactivo.exception;

public class WrongTypeException extends RuntimeException{
    public WrongTypeException(String entity){
        super("The " + entity + " type provided is not valid.");
    }
}
