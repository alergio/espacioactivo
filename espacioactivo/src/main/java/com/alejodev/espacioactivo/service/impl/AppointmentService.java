package com.alejodev.espacioactivo.service.impl;

import com.alejodev.espacioactivo.dto.*;
import com.alejodev.espacioactivo.entity.Activity;
import com.alejodev.espacioactivo.entity.Appointment;
import com.alejodev.espacioactivo.entity.AppointmentState;
import com.alejodev.espacioactivo.entity.AppointmentStateType;
import com.alejodev.espacioactivo.exception.*;
import com.alejodev.espacioactivo.repository.impl.IActivityRepository;
import com.alejodev.espacioactivo.repository.impl.IAppointmentRepository;
import com.alejodev.espacioactivo.service.ICRUDService;
import com.alejodev.espacioactivo.service.mapper.CRUDMapper;
import com.alejodev.espacioactivo.service.mapper.ReadAllCondition;
import jakarta.annotation.PostConstruct;
import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.sql.Time;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
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
    private AppointmentStateService appointmentStateService;
    @Autowired
    private IActivityRepository activityRepository;

    @Autowired
    private ActivityService activityService;
    @Autowired
    private ReservationService reservationService;

    private CRUDMapper<AppointmentDTO, Appointment> crudMapper;

    private final Logger LOGGER = Logger.getLogger(AppointmentService.class);

    @PostConstruct
    private void setUpCrudMapper(){
        crudMapper = getAppointmentCRUDMapper(appointmentRepository);
    }

    @Override
    public ResponseDTO create(EntityIdentificatorDTO appointmentDTO) {

        AppointmentDTO appointmentDTORequest = (AppointmentDTO) appointmentDTO;
        Optional<Activity> activity =
                activityRepository.findById(appointmentDTORequest.getActivityDTO().getId());

        appointmentStateService.getDTOById(appointmentDTORequest.getAppointmentStateDTO().getId());

        if (activity.isEmpty()) {
            throw new DataIntegrityVExceptionWithNotFoundEx("Activity");
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
                    AppointmentStateDTO unavaiableStateDTO
                            = appointmentStateService.getDTOByName(AppointmentStateType.UNAVAILABLE);
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
            AppointmentStateDTO availableStateDTO
                    = appointmentStateService.getDTOByName(AppointmentStateType.AVAILABLE);

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
            AppointmentState expiredState = appointmentStateService.getByName(AppointmentStateType.EXPIRED);
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


    public ResponseDTO createByServiceProvider(AppointmentDTO appointmentDTORequest) {
        appointmentDataValidatorForCreate(appointmentDTORequest);
        return create(appointmentDTORequest);
    }


    public ResponseDTO updateByServiceProvider(AppointmentDTO appointmentDTORequest)  {
        AppointmentDTO appointmentDTOForUpdate = appointmentDataValidatorForUpdate(appointmentDTORequest);
        return update(appointmentDTOForUpdate);
    }


    public ResponseDTO readAllByServiceProvider(){
        // este solo trae las que el creo
        // esto ya valida que el usuario este logueado
        Long userId = getAuthenticatedUserId();
        return crudMapper.readAllWithCondition(ReadAllCondition.APPOINTMENTS_BY_USERID, userId);
    }


    private void appointmentDataValidatorForCreate(AppointmentDTO appointmentDTO) {

        if (appointmentDTO.getActivityDTO() == null || appointmentDTO.getActivityDTO().getId() == null){
            throw new DataIntegrityVExceptionWithMsg(emptyFieldMessage("ActivityDTO or ActivityDTO.id"));
        }

        // ahora vamos a verificar que la actividad que se esta enviando
        // fue creada por el usuario que envia la solicitud
        activityService.getUserActivityDTOById(appointmentDTO.getActivityDTO().getId());

        if (appointmentDTO.getDate() == null) {
            throw new DataIntegrityVExceptionWithMsg(emptyFieldMessage("Date"));
        }

        dateValidator(appointmentDTO.getDate());

        if (appointmentDTO.getTime() == null) {
            throw new DataIntegrityVExceptionWithMsg(emptyFieldMessage("Time"));
        }

        timeValidator(appointmentDTO.getTime());

        if (appointmentDTO.getMaxPeople() == null ) {
            throw new DataIntegrityVExceptionWithMsg(emptyFieldMessage("MaxPeople"));
        }

        maxPeopleValidator(appointmentDTO.getMaxPeople());

        if (appointmentDTO.getAppointmentStateDTO() == null || appointmentDTO.getAppointmentStateDTO().getId() == null) {
            throw new DataIntegrityVExceptionWithMsg(emptyFieldMessage("AppointmentStateDTO or AppointmentStateDTO.id"));
        }

    }

    private static void timeValidator(String appointmentDTOTime) {
        Time startTimeAllowed = Time.valueOf("06:00:00");
        Time endTimeAllowed = Time.valueOf("23:00:00");
        Time timeInRequest = Time.valueOf(appointmentDTOTime);

        // si la hora ingresada no esta dentro de la franja horaria permitida
        if ( !( timeInRequest.before(endTimeAllowed) && timeInRequest.after(startTimeAllowed) ) ) {
            throw new DataIntegrityVExceptionWithMsg("The application does not allow you " +
                    "to create Appointments with a schedule outside the permitted range. " +
                    "This period runs from " + startTimeAllowed.toString() + " to " + endTimeAllowed.toString() + ".");
        }
    }

    private static void maxPeopleValidator(Integer maxPeople) {
        if (maxPeople < 1 || maxPeople > 25) {
            throw new DataIntegrityVExceptionWithMsg("The value for the maxPeople field is invalid, " +
                    "it must be greater than 0 and less than 25.");
        }
    }

    private static void dateValidator(String appointmentDTODate) {
        LocalDate dateInRequest = LocalDate.parse(appointmentDTODate);
        LocalDate tomorrow = LocalDate.now().plusDays(1);

        if (dateInRequest.isBefore(tomorrow)){
            throw new DataIntegrityVExceptionWithMsg("The application does not allow you " +
                    "to create Appointments before tomorrow, you can enter Appointments for tomorrow onwards.");
        }
    }

    public AppointmentDTO getUserAppointmentDTOById(Long appointmentId) {
        AppointmentDTO appointmentDTO = null;

        // validacion para que si el appointment no existe tire un 404
        AppointmentDTO appointmentDTOFounded = (AppointmentDTO) readById(appointmentId).getData().get("Appointment");

        ResponseDTO responseForAllAppointments = readAllByServiceProvider();
        List<AppointmentDTO> appointmentDTOList = (List<AppointmentDTO>) responseForAllAppointments.getData().get("Appointments");

        for (AppointmentDTO appointment : appointmentDTOList) {
            if (appointment.getId().equals(appointmentId)){
                appointmentDTO = appointment;
                break;
            }
        }

        if (appointmentDTO != null) {
            return appointmentDTO;
        } else {
            throw new MethodNotAllowedException("The Appointment does not belong to the person who making the request");
        }
    }


    private AppointmentDTO appointmentDataValidatorForUpdate(AppointmentDTO appointmentDTORequest) {

        if (appointmentDTORequest.getId() == null) {
            throw new DataIntegrityVExceptionWithMsg(emptyFieldMessage("Appointment id"));
        }

        if (appointmentDTORequest.getTotalReserves() != null) {
            throw new DataIntegrityVExceptionWithMsg("You cannot modify the totalReserves field." +
                    " It must be null.");
        }

        // esto ya verifica que el appointment que se quiere modificar pertenecen a quien envia la solicitud
        AppointmentDTO appointmentDTOForUpdate = getUserAppointmentDTOById(appointmentDTORequest.getId());
        
        boolean differencesFlag = false;

        // si el turno esta expirado no lo puedo editar
        if (AppointmentStateType.valueOf(appointmentDTOForUpdate
                .getAppointmentStateDTO()
                .getName()) == AppointmentStateType.EXPIRED)
        {
            throw new DataIntegrityVExceptionWithMsg("You cannot modify an expired appointment.");
        }


        // si envio un estado para modificar diferente al que ya esta asociado a este turno
        // o son iguales pero envio en true la flag para cancelar las reservas
        // y el turno tiene reservas activas
        // o si no envio datos de estado pero envio la flag de cancelar reservas y hay reservas activas

        boolean cancelReservesFlag = (appointmentDTORequest.isCancelAllReserves()
                && appointmentDTOForUpdate.getTotalReserves() > 0);

        if (appointmentDTORequest.isCancelAllReserves()
            && appointmentDTOForUpdate.getTotalReserves() <= 0) {
            throw new DataIntegrityVExceptionWithMsg("You cannot send the cancelAllReserves with 'true' value " +
                    "because there are no reservations to cancel.");
        }

        if (
                (appointmentDTORequest.getAppointmentStateDTO() != null
                    && (
                            !(Objects.equals(
                            appointmentDTORequest.getAppointmentStateDTO().getId(),
                            appointmentDTOForUpdate.getAppointmentStateDTO().getId()))
                                    || cancelReservesFlag

                    )
                ) ||
                        (appointmentDTORequest.getAppointmentStateDTO() == null && cancelReservesFlag)
        ) {

            // si estoy mandando un estado
            if (appointmentDTORequest.getAppointmentStateDTO() != null) {

                // verifico que se esta enviando un estado valido
                // utilizo el appointmentStateType que encuentro y lo seteo en el DTO
                // por las dudas de que en el request solo hayan enviado el id
                AppointmentStateType appointmentStateTypeRequest
                        = AppointmentStateType.valueOf(
                            appointmentStateService.getDTOById(appointmentDTORequest.getAppointmentStateDTO().getId()).getName());
                appointmentDTORequest.getAppointmentStateDTO().setName(String.valueOf(appointmentStateTypeRequest));

                // si el estado que llega es UNAVAILABLE (cambiando un AVAILABLE)
                if (appointmentStateTypeRequest == AppointmentStateType.UNAVAILABLE) {
                    appointmentDTOForUpdate.setAppointmentStateDTO(appointmentDTORequest.getAppointmentStateDTO());
                    differencesFlag = true;
                }

                // si el estado que llega es AVAILABLE
                if (appointmentStateTypeRequest == AppointmentStateType.AVAILABLE) {

                    if (appointmentDTORequest.getDate() != null) {
                        // verifico que la fecha que estoy enviando sea igual o mayor a maniana
                        dateValidator(appointmentDTORequest.getDate());
                    } else {
                        // utilizo la fecha que ya estaba guardada en el turno
                        dateValidator(appointmentDTOForUpdate.getDate());
                    }

                    // reinicio las reservas y el campo isFull
                    appointmentDTOForUpdate.setAppointmentStateDTO(appointmentDTORequest.getAppointmentStateDTO());
                    appointmentDTOForUpdate.setTotalReserves(0);
                    appointmentDTOForUpdate.setFull(false);
                    differencesFlag = true;
                }
            }

            // aca lo que va es cancelar todas las reservas asociadas al turno
            // si vino la flag en true
            if (cancelReservesFlag) {

                reservationService.cancelReservationsByDisabledAppointment(appointmentDTOForUpdate.getId());
                appointmentDTOForUpdate.setTotalReserves(0);
                appointmentDTOForUpdate.setFull(false);

                differencesFlag = true;
            }

        }

        if (appointmentDTORequest.getDate() != null && !appointmentDTORequest.getDate().equals(appointmentDTOForUpdate.getDate())) {
            dateValidator(appointmentDTORequest.getDate());
            appointmentDTOForUpdate.setDate(appointmentDTORequest.getDate());
            differencesFlag = true;
        }

        // si quiero setear full a un turno que no estaba full
        if (appointmentDTORequest.isFull() && !appointmentDTOForUpdate.isFull()) {

            // si request state es Unavailable o null, pasa bien y se le setea el de Unavailable.
            // si viene request state Available es un bad request
            setAppointmentToUnavailableThrowingBadState(appointmentDTORequest, appointmentDTOForUpdate);

            differencesFlag = true;
        }


        // si quiero sacarle el full a un turno que esta full
        // tengo que mandarle un max-people mayor
        if (!appointmentDTORequest.isFull() && appointmentDTOForUpdate.isFull()) {

            // si no envio maxPeople y el que estaba es menor igual al total de reservas
            // o envio uno que es menor al total de reservas o igual
            // bad request
            if ((appointmentDTORequest.getMaxPeople() == null
                && appointmentDTOForUpdate.getMaxPeople() <= appointmentDTOForUpdate.getTotalReserves())
                || (appointmentDTORequest.getMaxPeople() != null
                    && appointmentDTORequest.getMaxPeople() <= appointmentDTOForUpdate.getTotalReserves())) {
                throw new DataIntegrityVExceptionWithMsg("You cannot send an empty maxPeople field " +
                        "or a value for this field less than the total reserves for the Appointment" +
                        " if you want set an full Appointment to available." +
                        "You can send an empty maxPeople value if the default value of the Appointment is " +
                        "less than the total reserves for the Appointment.");
            }

            // tengo que cambiar tambien el estado del turno
            // de unavailable a available
            setAppointmentToAvailableThrowingBadState(appointmentDTORequest, appointmentDTOForUpdate);

            if (appointmentDTORequest.getMaxPeople() != null) {
                appointmentDTOForUpdate.setMaxPeople(appointmentDTORequest.getMaxPeople());
            }

            differencesFlag = true;
        }


        if (appointmentDTORequest.getMaxPeople() != null
                && !appointmentDTORequest.getMaxPeople().equals(appointmentDTOForUpdate.getMaxPeople()))
        {

            if (appointmentDTORequest.getMaxPeople() < appointmentDTOForUpdate.getTotalReserves()) {
                throw new DataIntegrityVExceptionWithMsg("You cannot send a max people field that " +
                        "is less than the total reservations that the shift has. (" +
                        appointmentDTOForUpdate.getTotalReserves() + " reserves).");
            }

            maxPeopleValidator(appointmentDTORequest.getMaxPeople());

            if (appointmentDTORequest.getMaxPeople().equals(appointmentDTOForUpdate.getTotalReserves())) {
                // pasa a estar full y por ende cambia de estado
                setAppointmentToUnavailableThrowingBadState(appointmentDTORequest, appointmentDTOForUpdate);
            }

            if (appointmentDTORequest.getMaxPeople() > appointmentDTOForUpdate.getTotalReserves()
            ) {
                // si mandan isFull en true y estan aumentando la cantidad de gente, no tiene sentido el request
                if (appointmentDTORequest.isFull()) {
                    throw new DataIntegrityVExceptionWithMsg("It makes no sense to send " +
                            " an isFull true value if you are wanting increase the limit of people");
                }

                // si estaba full tiene que dejar de estarlo
                // si estaba unavailable pasa a available
                setAppointmentToAvailableThrowingBadState(appointmentDTORequest, appointmentDTOForUpdate);
            }
            appointmentDTOForUpdate.setMaxPeople(appointmentDTORequest.getMaxPeople());
            differencesFlag = true;
        }

        if (appointmentDTORequest.getTime() != null
                && !appointmentDTORequest.getTime().equals(appointmentDTOForUpdate.getTime()))
        {
            timeValidator(appointmentDTORequest.getTime());
            appointmentDTOForUpdate.setTime(appointmentDTORequest.getTime());
            differencesFlag = true;
        }

        if (appointmentDTORequest.getActivityDTO() != null
                && !Objects.equals(appointmentDTORequest.getActivityDTO().getId(),
                appointmentDTOForUpdate.getActivityDTO().getId()))
        {
            // esto ya valida que la actividad enviada para modificar pertenezca a quien envia la solicitud
            activityService.getUserActivityDTOById(appointmentDTORequest.getActivityDTO().getId());
            appointmentDTOForUpdate.setActivityDTO(appointmentDTORequest.getActivityDTO());
            differencesFlag = true;
        }

        if (!differencesFlag) {
            throw new DataIntegrityVExceptionWithMsg("No changes found to update.");
        }

        return appointmentDTOForUpdate;
    }

    private void setAppointmentToUnavailableThrowingBadState(AppointmentDTO appointmentDTORequest, AppointmentDTO appointmentDTOForUpdate) {
        if (appointmentDTORequest.getAppointmentStateDTO() == null) {

            // chequear el estado del turno
            // si estaba available hay que setearlo unavailable
            if (Objects.equals
                    (appointmentDTOForUpdate.getAppointmentStateDTO().getName(), AppointmentStateType.AVAILABLE.name())
            ) {
                AppointmentStateDTO unavailableAppointmentStateDTO
                        = appointmentStateService.getDTOByName(AppointmentStateType.UNAVAILABLE);
                appointmentDTOForUpdate.setAppointmentStateDTO(unavailableAppointmentStateDTO);
            }
        // aca solo hay dos opciones, si viene unavailable se queda asi, si viene available Bad Request
        } else {
            // esto ya valida que sea un estado valido
            AppointmentStateDTO appointmentStateDTO
                    = appointmentStateService.getDTOById(appointmentDTORequest.getAppointmentStateDTO().getId());

            // si quiero mandar de estado Available
            if (Objects.equals(appointmentStateDTO.getName(), String.valueOf(AppointmentStateType.AVAILABLE))) {
                throw new DataIntegrityVExceptionWithMsg("You cannot send an Available value for " +
                        "AppointmentState field if you want to set your appointment to full. " +
                        "It must be set to Unavailable if it would be full. Also you can " +
                        "send AppointmentState field null and the application will be in charge of managing the state.");
            }

            appointmentDTOForUpdate.setAppointmentStateDTO(appointmentStateDTO);
        }

        appointmentDTOForUpdate.setFull(true);
    }

    private void setAppointmentToAvailableThrowingBadState(AppointmentDTO appointmentDTORequest, AppointmentDTO appointmentDTOForUpdate) {
        if (appointmentDTORequest.getAppointmentStateDTO() == null) {
            if (Objects.equals
                    (appointmentDTOForUpdate.getAppointmentStateDTO().getName(), AppointmentStateType.UNAVAILABLE.name())
            ) {
                AppointmentStateDTO availableAppointmentStateDTO
                        = appointmentStateService.getDTOByName(AppointmentStateType.AVAILABLE);
                appointmentDTOForUpdate.setAppointmentStateDTO(availableAppointmentStateDTO);
            }
        } else {
            // esto ya valida que sea un estado valido
            AppointmentStateDTO appointmentStateDTO
                    = appointmentStateService.getDTOById(appointmentDTORequest.getAppointmentStateDTO().getId());

            // si quiero mandar de estado Unavailable
            // si no es unavaialble significa que es available y en ese caso no hacemos nada
            if (Objects.equals(appointmentStateDTO.getName(), String.valueOf(AppointmentStateType.UNAVAILABLE))) {
                throw new DataIntegrityVExceptionWithMsg("It makes no sense to send " +
                        "an Unavailable value if you are wanting it to stop being full and adding the limit of people");
            }

            appointmentDTOForUpdate.setAppointmentStateDTO(appointmentStateDTO);
        }
        appointmentDTOForUpdate.setFull(false);
    }


}
