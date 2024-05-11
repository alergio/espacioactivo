package com.alejodev.espacioactivo.service.impl;

import com.alejodev.espacioactivo.dto.*;
import com.alejodev.espacioactivo.entity.Activity;
import com.alejodev.espacioactivo.entity.Appointment;
import com.alejodev.espacioactivo.entity.AppointmentState;
import com.alejodev.espacioactivo.entity.AppointmentStateType;
import com.alejodev.espacioactivo.exception.AppointmentIsFullException;
import com.alejodev.espacioactivo.exception.AppointmentStateException;
import com.alejodev.espacioactivo.exception.DataIntegrityVExceptionWithMsg;
import com.alejodev.espacioactivo.exception.DataIntegrityVExceptionWithNotFoundEx;
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

import java.sql.Time;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.alejodev.espacioactivo.exception.DataIntegrityVExceptionWithMsg.emptyFieldMessage;
import static com.alejodev.espacioactivo.security.auth.AuthenticationService.getAuthenticatedUserId;
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

    @Autowired
    private ActivityService activityService;

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
            throw new DataIntegrityVExceptionWithNotFoundEx("Activity");
        }

        if (appointmentState.isEmpty()) {
            throw new DataIntegrityVExceptionWithNotFoundEx("Appointment State");
        }

        appointmentDTORequest.setTotalReserves(0);

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
        return crudMapper.readAllWithCondition(ReadAllCondition.APPOINTMENTS_UNEXPIRED, null);
    }


    public AppointmentDTO updateAppointmentByReservation(Long appointmentId,
                                                         boolean setUnavailableFlag,
                                                         boolean addTotalReservesFlag){

        AppointmentDTO appointmentDTO = (AppointmentDTO) crudMapper.readById(appointmentId).getData().get("Appointment");
        String appointmentStateDTOName
                = appointmentDTO.getAppointmentStateDTO().getName();

        switch (AppointmentStateType.valueOf(appointmentStateDTOName)) {

            case AVAILABLE -> {

                if (setUnavailableFlag) {
                    AppointmentState unavailableState
                            = appointmentStateRepository.findByName(AppointmentStateType.UNAVAILABLE);
                    AppointmentStateDTO unavaiableStateDTO
                            = modelMapper.map(unavailableState, AppointmentStateDTO.class);
                    appointmentDTO.setAppointmentStateDTO(unavaiableStateDTO);
                    appointmentDTO.setFull(true);
                }

                if (addTotalReservesFlag) {
                    appointmentDTO.setTotalReserves(appointmentDTO.getTotalReserves() + 1);
                }

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

        appointmentDTO.setTotalReserves(appointmentDTO.getTotalReserves() - 1);

        if (AppointmentStateType.UNAVAILABLE == AppointmentStateType.valueOf(appointmentState)) {
            AppointmentState availableState
                    = appointmentStateRepository.findByName(AppointmentStateType.AVAILABLE);
            AppointmentStateDTO availableStateDTO
                    = modelMapper.map(availableState, AppointmentStateDTO.class);

            appointmentDTO.setAppointmentStateDTO(availableStateDTO);
            appointmentDTO.setFull(false);
        }

        AppointmentDTO appointmentDTOUpdated =
                (AppointmentDTO) crudMapper.update(appointmentDTO).getData().get("Appointment");

        reservationDTO.setAppointmentDTO(appointmentDTOUpdated);

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


    public AppointmentDTO checkIfIsFullToCreateReservation(Long appointmentId) {

        AppointmentDTO appointmentDTO =
                (AppointmentDTO) readById(appointmentId).getData().get("Appointment");

        Integer maxPeople = appointmentDTO.getMaxPeople();
        Integer totalReserves = appointmentDTO.getTotalReserves();

        String appointmentStateDTOName
                = appointmentDTO.getAppointmentStateDTO().getName();

        switch (AppointmentStateType.valueOf(appointmentStateDTOName)) {

            case AVAILABLE -> {
                if (totalReserves < maxPeople) {
                    if (maxPeople - totalReserves == 1) {
                        // se llena el ultimo cupo
                        appointmentDTO = updateAppointmentByReservation(appointmentId, true, true);
                    } else {
                        appointmentDTO = updateAppointmentByReservation(appointmentId, false, true);
                    }
                    return appointmentDTO;
                } else {
                    updateAppointmentByReservation(appointmentId, true, false);
                    throw new AppointmentIsFullException();
                }
            }

            case UNAVAILABLE, EXPIRED ->
                    throw new AppointmentStateException(AppointmentStateType.valueOf(appointmentStateDTOName));

        }

        return null;

    }


    public ResponseDTO createByServiceProvider(AppointmentDTO appointmentDTO) {
        appointmentDataValidator(appointmentDTO);
        return create(appointmentDTO);
    }

    public ResponseDTO readAllByServiceProvider(){
        // este solo trae las que el creo
        // esto ya valida que el usuario este logueado
        Long userId = getAuthenticatedUserId();
        return crudMapper.readAllWithCondition(ReadAllCondition.APPOINTMENTS_BY_USERID, userId);
    }

    private void appointmentDataValidator(AppointmentDTO appointmentDTO) {

        if (appointmentDTO.getActivityDTO() == null || appointmentDTO.getActivityDTO().getId() == null){
            throw new DataIntegrityVExceptionWithMsg(emptyFieldMessage("ActivityDTO or ActivityDTO.id"));
        }

        // ahora vamos a verificar que la actividad que se esta enviando
        // fue creada por el usuario que envia la solicitud
        activityService.getUserActivityDTOById(appointmentDTO.getActivityDTO().getId());

        if (appointmentDTO.getDate() == null) {
            throw new DataIntegrityVExceptionWithMsg(emptyFieldMessage("Date"));
        }

        LocalDate dateInRequest = LocalDate.parse(appointmentDTO.getDate());
        LocalDate tomorrow = LocalDate.now().plusDays(1);

        if (dateInRequest.isBefore(tomorrow)){
            throw new DataIntegrityVExceptionWithMsg("The application does not allow you " +
                    "to create shifts before tomorrow, you can enter shifts for tomorrow onwards.");
        }

        if (appointmentDTO.getTime() == null) {
            throw new DataIntegrityVExceptionWithMsg(emptyFieldMessage("Time"));
        }

        Time startTimeAllowed = Time.valueOf("06:00:00");
        Time endTimeAllowed = Time.valueOf("23:00:00");
        Time timeInRequest = Time.valueOf(appointmentDTO.getTime());

        // si la hora ingresada no esta dentro de la franja horaria permitida
        if ( !( timeInRequest.before(endTimeAllowed) && timeInRequest.after(startTimeAllowed) ) ) {
            throw new DataIntegrityVExceptionWithMsg("The application does not allow you " +
                    "to create shifts with a schedule outside the permitted range. " +
                    "This period runs from " + startTimeAllowed.toString() + " to " + endTimeAllowed.toString() + ".");
        }

        if (appointmentDTO.getMaxPeople() == null ) {
            throw new DataIntegrityVExceptionWithMsg(emptyFieldMessage("MaxPeople"));
        }

        if (appointmentDTO.getMaxPeople() < 1 || appointmentDTO.getMaxPeople() > 25) {
            throw new DataIntegrityVExceptionWithMsg("The value for the maxPeople field is invalid, " +
                    "it must be greater than 0 and less than 25.");
        }

        if (appointmentDTO.getAppointmentStateDTO() == null || appointmentDTO.getAppointmentStateDTO().getId() == null) {
            throw new DataIntegrityVExceptionWithMsg(emptyFieldMessage("AppointmentStateDTO or AppointmentStateDTO.id"));
        }

    }




}
