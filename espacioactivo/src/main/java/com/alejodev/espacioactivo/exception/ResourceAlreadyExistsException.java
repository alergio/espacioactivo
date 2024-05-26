package com.alejodev.espacioactivo.exception;

public class ResourceAlreadyExistsException extends RuntimeException {
    public ResourceAlreadyExistsException(String resource){
        super("There is already a " + resource + " with that name and that type.");
    }
}
