package com.alejodev.espacioactivo.repository;

import com.alejodev.espacioactivo.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IReservationRepository extends JpaRepository<Reservation, Long> {
}
