package com.alejodev.espacioactivo.exception;

import com.alejodev.espacioactivo.entity.AppointmentStateType;

public class AppointmentStateException extends RuntimeException {
    public AppointmentStateException(AppointmentStateType stateType){
        super(generateErrorMessage(stateType));
    }

    private static String generateErrorMessage(AppointmentStateType stateType) {

        String errorMessage = "Error: It is not possible to create the reservation " +
                "because the state of the Appointment does not allow it. " +
                "Appointment state: ";

        switch (stateType) {
            case UNAVAILABLE, EXPIRED -> errorMessage += String.valueOf(stateType);
            default -> errorMessage = "Unknown error.";
        }

        return errorMessage;

    }
}
