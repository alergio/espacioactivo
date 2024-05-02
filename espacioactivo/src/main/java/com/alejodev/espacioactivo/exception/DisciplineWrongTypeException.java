package com.alejodev.espacioactivo.exception;

public class DisciplineWrongTypeException extends RuntimeException{
    public DisciplineWrongTypeException(){
        super("The discipline type provided is not valid.");
    }
}
