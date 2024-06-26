package com.alejodev.espacioactivo.service.impl;

import com.alejodev.espacioactivo.dto.*;
import com.alejodev.espacioactivo.entity.InformalRoleType;
import com.alejodev.espacioactivo.entity.Reservation;
import com.alejodev.espacioactivo.exception.MethodNotAllowedException;
import com.alejodev.espacioactivo.repository.impl.IReservationRepository;
import com.alejodev.espacioactivo.service.ICRUDService;
import com.alejodev.espacioactivo.service.mapper.CRUDMapper;
import com.alejodev.espacioactivo.service.mapper.ReadAllCondition;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import static com.alejodev.espacioactivo.service.mapper.CRUDMapperProvider.getReservationCRUDMapper;

@Service
public class ReservationService implements ICRUDService<ReservationDTO> {

    @Autowired
    private IReservationRepository reservationRepository;
    @Autowired
    AppointmentService appointmentService;
    private CRUDMapper<ReservationDTO, Reservation> crudMapper;

    private final Logger LOGGER = Logger.getLogger(ReservationService.class);

    @PostConstruct
    private void setUpCrudMapper(){
        crudMapper = getReservationCRUDMapper(reservationRepository);
    }

    @Override
    public ResponseDTO create(EntityIdentificatorDTO reservationDTO) {

        ReservationDTO reservationDTORequest = (ReservationDTO) reservationDTO;

        Long appointmentId = reservationDTORequest.getAppointmentDTO().getId();

        AppointmentDTO appointmentDTO = appointmentService.checkIfIsFullToCreateReservation(appointmentId);

        reservationDTORequest.setAppointmentDTO(appointmentDTO);
        return crudMapper.create(reservationDTORequest);

    }

    @Override
    public ResponseDTO readById(Long id) {
        return crudMapper.readById(id);
    }

    @Override
    public ResponseDTO readAll() {
        return crudMapper.readAll();
    }

    @Override
    public ResponseDTO update(EntityIdentificatorDTO reservationDTO) {
        return crudMapper.update(reservationDTO);
    }

    @Override
    public ResponseDTO delete(Long id) {
        return crudMapper.delete(id);
    }

    @Transactional
    public ResponseDTO cancelReservation(Long reservationID) {

        ResponseDTO response = new ResponseDTO();
        ReservationDTO reservationDTO =
                (ReservationDTO) crudMapper.readById(reservationID).getData().get("Reservation");

        if (reservationDTO.isCancelled()) {
            throw new MethodNotAllowedException("This reservation is already cancelled.");
        }

        reservationDTO.setCancelled(true);
        reservationDTO.setCancelledBy(String.valueOf(InformalRoleType.CUSTOMER));

        crudMapper.update(reservationDTO);
        ReservationDTO reservationDTOUpdated =
                appointmentService.setAppointmentToAvailableFromCancelledReservation(reservationDTO);

        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("Reservation cancelled successfully.");
        response.setData(Collections.singletonMap("Reservation", reservationDTOUpdated));

        LOGGER.info("Reservation cancelled successfully.");

        return response;

    }

    public void cancelReservationsByDisabledAppointment(Long appointmentId) {

        List<ReservationDTO> enabledReservationsFromAppointment =
                (List<ReservationDTO>) crudMapper.readAllWithCondition(
                        ReadAllCondition.RESERVATIONS_BY_APPOINTMENTID, appointmentId).getData().get("Reservations");

        for (ReservationDTO reservation : enabledReservationsFromAppointment) {
            reservation.setCancelled(true);
            reservation.setCancelledBy(String.valueOf(InformalRoleType.SERVICE_PROVIDER));
            crudMapper.update(reservation);
        }

    }


}
