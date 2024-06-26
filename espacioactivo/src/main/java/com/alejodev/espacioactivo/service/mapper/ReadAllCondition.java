package com.alejodev.espacioactivo.service.mapper;

// condicion para hacer un ReadAll, por ejemplo, traeme todos los que no esten expirados seria una condicion
public enum ReadAllCondition {
    APPOINTMENTS_UNEXPIRED,
    DISCIPLINE_REQUESTS_BY_USERID,
    ACTIVITIES_BY_USERID,
    APPOINTMENTS_BY_USERID,
    RESERVATIONS_BY_APPOINTMENTID;
}
