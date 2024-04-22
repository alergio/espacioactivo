package com.alejodev.espacioactivo.entity;

import jakarta.persistence.Table;
import lombok.Getter;

/**
 * Enumerado con los distintos tipos de disciplinas.
 * Clases grupales, clases personalizadas o un espacio para alquilar.
 *
 * @author alejo
 * @version 1.0 21-04-2024
 */
@Table(name = "DisciplineType")
@Getter
public enum DisciplineType {
    GROUP_CLASS,
    PERSONALIZED_CLASS,
    SPACE_RENTAL;
}

