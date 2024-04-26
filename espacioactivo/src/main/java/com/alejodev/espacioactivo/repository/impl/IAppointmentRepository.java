package com.alejodev.espacioactivo.repository.impl;

import com.alejodev.espacioactivo.entity.Appointment;
import com.alejodev.espacioactivo.repository.IGenericRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IAppointmentRepository extends IGenericRepository<Appointment, Long> {
}
