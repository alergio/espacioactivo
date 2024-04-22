package com.alejodev.espacioactivo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Domicilios, por ahora solo asociados a las actividades.
 *
 * @author alejo
 * @version 1.0 21-4-2024
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String street;
    private String number;
    private String state;

    @JoinColumn(name = "activity_id")
    @OneToOne()
    private Activity activity;

}
