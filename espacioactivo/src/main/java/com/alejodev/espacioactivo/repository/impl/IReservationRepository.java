package com.alejodev.espacioactivo.repository.impl;

import com.alejodev.espacioactivo.entity.Reservation;
import com.alejodev.espacioactivo.repository.IGenericRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IReservationRepository extends IGenericRepository<Reservation, Long> {
}
