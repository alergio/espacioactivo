package com.alejodev.espacioactivo.service.impl;

import com.alejodev.espacioactivo.dto.*;
import com.alejodev.espacioactivo.entity.Activity;
import com.alejodev.espacioactivo.entity.Appointment;
import com.alejodev.espacioactivo.entity.AppointmentState;
import com.alejodev.espacioactivo.entity.AppointmentStateType;
import com.alejodev.espacioactivo.exception.AppointmentIsFullException;
import com.alejodev.espacioactivo.exception.AppointmentStateException;
import com.alejodev.espacioactivo.exception.DataIntegrityVExceptionWithMsg;
import com.alejodev.espacioactivo.repository.impl.IActivityRepository;
import com.alejodev.espacioactivo.repository.impl.IAppointmentRepository;
import com.alejodev.espacioactivo.repository.impl.IAppointmentStateRepository;
import com.alejodev.espacioactivo.service.ICRUDService;
import com.alejodev.espacioactivo.service.mapper.CRUDMapper;
import com.alejodev.espacioactivo.service.mapper.ReadAllCondition;
import jakarta.annotation.PostConstruct;
import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

import static com.alejodev.espacioactivo.service.mapper.CRUDMapperProvider.getAppointmentCRUDMapper;
import static com.alejodev.espacioactivo.service.mapper.ConfigureMapper.configureMapper;

@Service
public class AppointmentService implements ICRUDService<AppointmentDTO> {

    @Autowired
    private IAppointmentRepository appointmentRepository;
    @Autowired
    private IAppointmentStateRepository appointmentStateRepository;
    @Autowired
    private IActivityRepository activityRepository;
    private CRUDMapper<AppointmentDTO, Appointment> crudMapper;

    private final Logger LOGGER = Logger.getLogger(AppointmentService.class);
    private final ModelMapper modelMapper = configureMapper();

    @PostConstruct
    private void setUpCrudMapper(){
        crudMapper = getAppointmentCRUDMapper(appointmentRepository);
    }

    @Override
    public ResponseDTO create(EntityIdentificatorDTO appointmentDTO) {

        AppointmentDTO appointmentDTORequest = (AppointmentDTO) appointmentDTO;
        Optional<Activity> activity =
                activityRepository.findById(appointmentDTORequest.getActivityDTO().getId());
        Optional<AppointmentState> appointmentState =
                appointmentStateRepository.findById(appointmentDTORequest.getAppointmentStateDTO().getId());

        if (activity.isEmpty()) {
            throw new DataIntegrityVExceptionWithMsg("Activity");
        }

        if (appointmentState.isEmpty()) {
            throw new DataIntegrityVExceptionWithMsg("Appointment State");
        }

        return crudMapper.create(appointmentDTO);

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
    public ResponseDTO update(EntityIdentificatorDTO appointmentDTO) {
        return crudMapper.update(appointmentDTO);
    }

    @Override
    public ResponseDTO delete(Long id) {
        return crudMapper.delete(id);
    }


    public ResponseDTO readAllUnexpired() {
        return crudMapper.readAllWithCondition(ReadAllCondition.UNEXPIRED);
    }

    public AppointmentDTO setUnavailableAppointmentState(Long appointmentId, boolean isFull){

        AppointmentDTO appointmentDTO = (AppointmentDTO) crudMapper.readById(appointmentId).getData().get("Appointment");
        String appointmentStateDTOName
                = appointmentDTO.getAppointmentStateDTO().getName();

        switch (AppointmentStateType.valueOf(appointmentStateDTOName)) {

            case AVAILABLE -> {
                AppointmentState unavailableState
                        = appointmentStateRepository.findByName(AppointmentStateType.UNAVAILABLE);
                AppointmentStateDTO unavaiableStateDTO
                        = modelMapper.map(unavailableState, AppointmentStateDTO.class);
                appointmentDTO.setAppointmentStateDTO(unavaiableStateDTO);
                appointmentDTO.setFull(isFull);
                ResponseDTO appointmentDTOResponse = crudMapper.update(appointmentDTO);
                return (AppointmentDTO) appointmentDTOResponse.getData().get("Appointment");
            }

            case UNAVAILABLE, EXPIRED ->
                    throw new AppointmentStateException(AppointmentStateType.valueOf(appointmentStateDTOName));

        }

        return null;

    }



    public ReservationDTO setAppointmentToAvailableFromCancelledReservation(ReservationDTO reservationDTO) {

        AppointmentDTO appointmentDTO = reservationDTO.getAppointmentDTO();
        String appointmentState = appointmentDTO.getAppointmentStateDTO().getName();

        if (AppointmentStateType.UNAVAILABLE == AppointmentStateType.valueOf(appointmentState)) {

            AppointmentState availableState
                    = appointmentStateRepository.findByName(AppointmentStateType.AVAILABLE);
            AppointmentStateDTO availableStateDTO
                    = modelMapper.map(availableState, AppointmentStateDTO.class);

            appointmentDTO.setAppointmentStateDTO(availableStateDTO);
            appointmentDTO.setFull(false);

            AppointmentDTO appointmentDTOUpdated =
                    (AppointmentDTO) crudMapper.update(appointmentDTO).getData().get("Appointment");

            reservationDTO.setAppointmentDTO(appointmentDTOUpdated);

        }

            return reservationDTO;

    }


    public void checkAppointmentsToMarkAsExpired(){

        List <Appointment> appointmentsToBeExpired = appointmentRepository.findCandidateAppointmentsToBeExpired();
        long appointmentsCount = appointmentsToBeExpired.size();

        if (appointmentsCount > 0) {
            AppointmentState expiredState = appointmentStateRepository.findByName(AppointmentStateType.EXPIRED);
            LOGGER.info("Se encontraron " + appointmentsCount + " turnos para expirar.");

            appointmentsToBeExpired.forEach(appointment -> appointment.setAppointmentState(expiredState));

            appointmentRepository.saveAll(appointmentsToBeExpired);
        } else {
            LOGGER.info("No se encontraron turnos para expirar.");
        }

    }


    public AppointmentDTO checkIfIsFullToCreateReservation(Long appointmentId, Long totalReserves) {

        AppointmentDTO appointmentDTO =
                (AppointmentDTO) readById(appointmentId).getData().get("Appointment");

        int maxPeople = appointmentDTO.getMaxPeople();

        String appointmentStateDTOName
                = appointmentDTO.getAppointmentStateDTO().getName();

        switch (AppointmentStateType.valueOf(appointmentStateDTOName)) {

            case AVAILABLE -> {
                if (totalReserves < maxPeople) {
                    if (maxPeople - totalReserves == 1) {
                        // se llena el ultimo cupo
                        appointmentDTO = setUnavailableAppointmentState(appointmentId, true);
                    }
                    return appointmentDTO;
                } else {
                    setUnavailableAppointmentState(appointmentId, true);
                    throw new AppointmentIsFullException();
                }
            }

            case UNAVAILABLE, EXPIRED ->
                    throw new AppointmentStateException(AppointmentStateType.valueOf(appointmentStateDTOName));

        }

        return null;

    }




}
