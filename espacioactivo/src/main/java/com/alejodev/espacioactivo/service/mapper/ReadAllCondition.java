package com.alejodev.espacioactivo.service.mapper;

// condicion para hacer un ReadAll, por ejemplo, traeme todos los que no esten expirados seria una condicion
public enum ReadAllCondition {
    APPOINTMENTS_UNEXPIRED,
    DISCIPLINE_REQUESTS_BY_USERNAME,
    ACTIVITIES_BY_USERNAME;
}
