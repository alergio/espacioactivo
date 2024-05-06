package com.alejodev.espacioactivo.service.mapper;

// condicion para hacer un ReadAll, por ejemplo, traeme todos los que no esten expirados seria una condicion
public enum ReadAllCondition {
    APPOINTMENTS_UNEXPIRED,
    REQUESTS_BY_USER_ID,
    ACTIVITIES_BY_USERNAME;
}
