package com.alejodev.espacioactivo.repository.impl;

import com.alejodev.espacioactivo.entity.Appointment;
import com.alejodev.espacioactivo.repository.IGenericRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IAppointmentRepository extends IGenericRepository<Appointment, Long> {

    @Query("SELECT a FROM Appointment a " +
            "WHERE a.appointmentState.name != 'EXPIRED' " +
            "AND (a.date <= CURRENT_DATE() AND a.time <= CURRENT_TIME())")
    List<Appointment> findCandidateAppointmentsToBeExpired();

    @Query("SELECT a FROM Appointment a " +
            "WHERE a.appointmentState.name != 'EXPIRED'")
    List<Appointment> findUnexpiredAppointments();

}

