package com.alejodev.espacioactivo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Time;
import java.util.Objects;


/**
 * Reservas asociadas a un cliente y un turno.
 *
 * @author alejo
 * @version 1.0 21-04-2024
 */
@Entity
@Table(name = "Reservation")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // aca tengo que ver como lo condiciono xq es solo para customers

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "appointment_id", nullable = false)
    private Appointment appointment;

    @Column(name = "is_cancelled")
    private boolean cancelled;


    // Implementación de equals() y hashCode()
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation reservation = (Reservation) o;
        return Objects.equals(id, reservation.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
