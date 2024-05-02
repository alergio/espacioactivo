package com.alejodev.espacioactivo.exception;

public class DisciplineAlreadyExistsException extends RuntimeException {
    public DisciplineAlreadyExistsException(){
        super("There is already a discipline with that name and that type.");
    }
}
