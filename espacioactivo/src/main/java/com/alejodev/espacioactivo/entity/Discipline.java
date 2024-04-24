package com.alejodev.espacioactivo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

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

    @OneToMany(mappedBy = "discipline", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private Set<Activity> activities = new HashSet<>();


    // Implementación de equals() y hashCode()
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Discipline discipline = (Discipline) o;
        return Objects.equals(id, discipline.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
