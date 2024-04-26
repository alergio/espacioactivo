package com.alejodev.espacioactivo.entity;

import jakarta.persistence.Table;
import lombok.Getter;

/**
 * Enum de posibles estados para la entidad Appointment.
 *
 * @author alejo
 * @version 1.0 24-04-2024
 */
@Table(name = "AppointmentStateType")
@Getter
public enum AppointmentStateType {
    AVAILABLE,
    UNAVAILABLE,
    EXPIRED;
}
