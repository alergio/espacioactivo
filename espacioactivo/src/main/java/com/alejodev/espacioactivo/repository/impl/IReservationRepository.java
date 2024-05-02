package com.alejodev.espacioactivo.repository.impl;

import com.alejodev.espacioactivo.entity.Reservation;
import com.alejodev.espacioactivo.repository.IGenericRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IReservationRepository extends IGenericRepository<Reservation, Long> {

    @Query("""
        SELECT COUNT(r) FROM Reservation r
        WHERE r.appointment.id = :appointmentId AND r.cancelled = false
        """)
    Long findAllReservationsByAppointment(Long appointmentId);


}
