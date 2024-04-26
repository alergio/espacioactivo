package com.alejodev.espacioactivo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Tabla que va hacer referencia a los posibles estados de un turno.
 *
 * @author alejo
 * @version 1.0 24-04-2024
 */
@Entity
@Table(name = "AppointmentState")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentState {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private AppointmentStateType name;

}
