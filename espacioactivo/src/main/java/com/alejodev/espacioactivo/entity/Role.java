package com.alejodev.espacioactivo.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

/**
 * Roles y vinculacion con los usuarios.
 *
 * @author alejo
 * @version 1.0 21-4-2024
 */
@Entity
@Table(name = "Role")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private RoleType name;

    @Override
    public String getAuthority() {
        return name.name();
    }
}
