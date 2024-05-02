package com.alejodev.espacioactivo.exception;

public class AppointmentIsFullException extends RuntimeException {

    public AppointmentIsFullException(){
        super("The appointment is already full.");
    }

}
