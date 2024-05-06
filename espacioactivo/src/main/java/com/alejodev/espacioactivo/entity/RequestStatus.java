package com.alejodev.espacioactivo.entity;

import jakarta.persistence.Table;
import lombok.Getter;
@Table(name = "RequestStatus")
@Getter
public enum RequestStatus {
    ON_HOLD,
    APPROVED,
    REJECTED;
}