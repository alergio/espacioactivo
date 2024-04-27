package com.alejodev.espacioactivo.repository.impl;

import com.alejodev.espacioactivo.entity.AppointmentState;
import com.alejodev.espacioactivo.entity.AppointmentStateType;
import com.alejodev.espacioactivo.repository.IGenericRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IAppointmentStateRepository extends IGenericRepository<AppointmentState, Long> {
    AppointmentState findByName(AppointmentStateType name);
}
