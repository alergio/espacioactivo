package com.alejodev.espacioactivo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Actividades que proveen los profesores o space-renters.
 * Estan asociadas directamente a una disciplina, un usuario,
 * una direccion y una o varias reservas.
 *
 * @author alejo
 * @version 21-04-2024
 */
@Entity
@Table(name = "Activity")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "discipline_id", nullable = false)
    private Discipline discipline;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // --------> aca tengo que ver como lo condiciono por rol porque no puede ser un customer

    @OneToOne(mappedBy = "activity", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Address address;

    @OneToMany(mappedBy = "activity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reservation> reservations;

    private Integer price;
    private Integer maxPeople;

}
