package com.alejodev.espacioactivo.entity;

import com.alejodev.espacioactivo.dto.DisciplineDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@Table(name = "RequestToCreateDiscipline")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestToCreateDiscipline {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String disciplineName;

    @Enumerated(EnumType.STRING)
    private DisciplineType disciplineType;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    // Implementación de equals() y hashCode()
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RequestToCreateDiscipline requestToCreateDiscipline = (RequestToCreateDiscipline) o;
        return Objects.equals(id, requestToCreateDiscipline.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}

