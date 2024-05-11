package com.alejodev.espacioactivo.entity;

import jakarta.persistence.Table;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

/**
 * Distintos roles que tiene la aplicacion.
 * Administrador, Proveedor de lugares para alquilar, Profesor/Entrenador y Cliente.
 *
 * @author alejo
 * @version 1.0 04-21-2024
 */
@Table(name = "RoleType")
@Getter
public enum RoleType implements GrantedAuthority {
    ROLE_ADMIN,
    ROLE_SERVICE_PROVIDER,
    ROLE_CUSTOMER;

    @Override
    public String getAuthority() {
        return name();
    }
}
