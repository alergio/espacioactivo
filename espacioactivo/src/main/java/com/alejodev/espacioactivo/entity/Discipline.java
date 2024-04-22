package com.alejodev.espacioactivo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Todas las disciplinas disponibles de la aplicacion, son su nombre y su tipo.
 *
 * @author alejo
 * @version 1.0 21-04-2024
 */
@Entity
@Table(name = "Discipline")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Discipline {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private DisciplineType type;

    @OneToMany(mappedBy = "discipline", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Activity> activities;

}
