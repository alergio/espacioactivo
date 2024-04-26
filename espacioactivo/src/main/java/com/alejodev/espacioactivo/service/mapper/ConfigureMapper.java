package com.alejodev.espacioactivo.service.mapper;

import com.alejodev.espacioactivo.dto.*;
import com.alejodev.espacioactivo.entity.*;
import org.apache.log4j.Logger;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Configuration;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Configuracion del Mapper para usar en el CRUDMapper.
 *
 * @author alejo
 * @version 1.0 21-04-2024
 */
@Configuration
public class ConfigureMapper {

    private static final Logger LOGGER = Logger.getLogger(ConfigureMapper.class);


    public static ModelMapper configureMapper() {
        ModelMapper modelMapper = new ModelMapper();


        /**
         * Mapear User a UserDTO
         */
        TypeMap<User, UserDTO> userToUserDTOMapper = modelMapper.createTypeMap(User.class, UserDTO.class);

        userToUserDTOMapper.addMappings(mapper -> {

            mapper.map(source -> Optional.ofNullable(source.getId()), UserDTO::setId);
            mapper.map(source -> Optional.ofNullable(source.getEmail()), UserDTO::setEmail);
            mapper.map(source -> Optional.ofNullable(source.getFirstname()), UserDTO::setFirstname);
            mapper.map(source -> Optional.ofNullable(source.getLastname()), UserDTO::setLastname);
            mapper.map(User::isEnabled, UserDTO::setEnabled);

            // Mapeo de RegistrationDate con manejo de valores nulos y conversión a String
            mapper.map(source -> Optional.ofNullable(source.getRegistrationDate())
                    .map(Date::toString)
                    .orElse(null), UserDTO::setRegistrationDate);

            // Mapeo de Roles
            mapper.map(source -> Optional.ofNullable(source.getRoles())
                    .map(roles -> roles.stream()
                            .map(role -> modelMapper.map(role, RoleDTO.class))
                            .collect(Collectors.toSet()))
                    .orElse(null), UserDTO::setRoles);
        });



        // ---------------------------------------------------------------------------- //
        // ---------------------------------------------------------------------------- //


        /**
         * Mapear UserDTO a User
         */
        TypeMap<UserDTO, User> userDTOToUserMapper = modelMapper.createTypeMap(UserDTO.class, User.class);

        userDTOToUserMapper.addMappings(mapper -> {

            mapper.map(source -> Optional.ofNullable(source.getId()), User::setId);
            mapper.map(source -> Optional.ofNullable(source.getEmail()), User::setEmail);
            mapper.map(source -> Optional.ofNullable(source.getFirstname()), User::setFirstname);
            mapper.map(source -> Optional.ofNullable(source.getLastname()), User::setLastname);
            mapper.map(UserDTO::isEnabled, User::setEnabled);

            // Mapeo de RegistrationDate con manejo de valores nulos y conversión a String
            mapper.map(source -> Optional.ofNullable(source.getRegistrationDate())
                    .map(dateString -> Date.valueOf(LocalDate.parse(dateString)))
                    .orElse(null), User::setRegistrationDate);

            // Mapeo de Roles
            mapper.map(source -> Optional.ofNullable(source.getRoles())
                    .map(roles -> roles.stream()
                            .map(role -> modelMapper.map(role, Role.class))
                            .collect(Collectors.toSet()))
                    .orElse(null), User::setRoles);
        });





        // ---------------------------------------------------------------------------- //
        // ---------------------------------------------------------------------------- //


        /**
         * Mapear Activity a ActivityDTO
         */
        TypeMap<Activity, ActivityDTO> activityToActivityDTOMapper
                = modelMapper.createTypeMap(Activity.class, ActivityDTO.class);

        Converter<User, UserDTO> userToUserDTOConverter = context -> {
            User source = context.getSource();
            UserDTO destination = new UserDTO();
            userToUserDTOMapper.map(source, destination);
            return destination;
        };

        activityToActivityDTOMapper.addMappings(
                mapper -> {
                    mapper.using(userToUserDTOConverter)
                        .map(Activity::getUser, ActivityDTO::setUserDTO);

                    mapper.map(Activity::getDiscipline, ActivityDTO::setDisciplineDTO);

                    mapper.map(Activity::getAddress, ActivityDTO::setAddressDTO);

                }
        );


        // ---------------------------------------------------------------------------- //
        // ---------------------------------------------------------------------------- //



        /**
         * Mapear ActivityDTO a Activity
         */
        TypeMap<ActivityDTO, Activity> activityDTOToActivityMapper
                = modelMapper.createTypeMap(ActivityDTO.class, Activity.class);

        Converter<UserDTO, User> userDTOToUserConverter = context -> {
            UserDTO source = context.getSource();
            User destination = new User();
            userDTOToUserMapper.map(source, destination);
            return destination;
        };

        Converter<DisciplineDTO, Discipline> disciplineDTOToDisciplineConverter = context -> {
            DisciplineDTO source = context.getSource();
            Discipline destination = new Discipline();

            Optional.ofNullable(source.getId()).ifPresent(destination::setId);
            Optional.ofNullable(source.getName()).ifPresent(destination::setName);
            Optional.ofNullable(source.getType())
                    .map(DisciplineType::valueOf)
                    .ifPresent(destination::setType);

            return destination;
        };

        activityDTOToActivityMapper.addMappings(
                mapper -> {
                    mapper.using(userDTOToUserConverter)
                        .map(ActivityDTO::getUserDTO, Activity::setUser);

                    mapper.using(disciplineDTOToDisciplineConverter)
                            .map(ActivityDTO::getDisciplineDTO, Activity::setDiscipline);

                    mapper.map(ActivityDTO::getAddressDTO, Activity::setAddress);
                }

        );



        // ---------------------------------------------------------------------------- //
        // ---------------------------------------------------------------------------- //


        /**
         * Mapear Appointment a AppointmentDTO
         */
        TypeMap<Appointment, AppointmentDTO> appointmentToAppointmentDTOMapper
                = modelMapper.createTypeMap(Appointment.class, AppointmentDTO.class);

            Converter<Activity, ActivityDTO> activityToActivityDTOConverter = context -> {
                Activity source = context.getSource();
                ActivityDTO destination = new ActivityDTO();
                activityToActivityDTOMapper.map(source, destination);
                return destination;
            };

        Converter<AppointmentState, AppointmentStateDTO> appointmentStateToApoointmentStateDTOConverter
                = context -> {
            AppointmentState source = context.getSource();
            AppointmentStateDTO destination = new AppointmentStateDTO();

            Optional.ofNullable(source.getId()).ifPresent(destination::setId);
            Optional.ofNullable(source.getName())
                    .map(AppointmentStateType::name)
                    .ifPresent(destination::setName);

            return destination;
        };


        appointmentToAppointmentDTOMapper.addMappings(
                mapper -> {

                    // mapeo la actividad
                    mapper.using(activityToActivityDTOConverter)
                        .map(Appointment::getActivity, AppointmentDTO::setActivityDTO);

                    // mapeo la fecha
                    mapper.map(source -> Optional.ofNullable(source.getDate())
                            .map(Date::toString)
                            .orElse(null), AppointmentDTO::setDate);

                    // mapeo la hora
                    mapper.map(source -> Optional.ofNullable(source.getTime())
                            .map(Time::toString)
                            .orElse(null), AppointmentDTO::setTime);

                    // mapeo el estado
                    mapper.using(appointmentStateToApoointmentStateDTOConverter)
                            .map(Appointment::getAppointmentState, AppointmentDTO::setAppointmentStateDTO);

                }
        );



        // ---------------------------------------------------------------------------- //
        // ---------------------------------------------------------------------------- //



        /**
         * Mapear AppointmentDTO a Appointment
         */
        TypeMap<AppointmentDTO, Appointment> appointmentDTOToAppointmentMapper
                = modelMapper.createTypeMap(AppointmentDTO.class, Appointment.class);


        Converter<ActivityDTO, Activity> activityDTOToActivityConverter = context -> {
            ActivityDTO source = context.getSource();
            Activity destination = new Activity();
            activityDTOToActivityMapper.map(source, destination);
            return destination;
        };

        Converter<AppointmentStateDTO, AppointmentState> appointmentStateDTOToApoointmentStateConverter
                = context -> {
            AppointmentStateDTO source = context.getSource();
            AppointmentState destination = new AppointmentState();

            Optional.ofNullable(source.getId()).ifPresent(destination::setId);
            Optional.ofNullable(source.getName())
                    .map(AppointmentStateType::valueOf)
                    .ifPresent(destination::setName);

            return destination;
        };


        appointmentDTOToAppointmentMapper.addMappings(
                mapper -> {

                    // mapeo al actividad
                    mapper.using(activityDTOToActivityConverter)
                            .map(AppointmentDTO::getActivityDTO, Appointment::setActivity);

                    // mapeo la fecha
                    mapper.map(source -> Optional.ofNullable(source.getDate())
                            .map(dateString -> Date.valueOf(LocalDate.parse(dateString)))
                            .orElse(null), Appointment::setDate);

                    // mapeo la hora
                    mapper.map(source -> Optional.ofNullable(source.getTime())
                            .map(timeString -> Time.valueOf(LocalTime.parse(timeString)))
                            .orElse(null), Appointment::setTime);

                    // mapeo el estado
                    mapper.using(appointmentStateDTOToApoointmentStateConverter)
                            .map(AppointmentDTO::getAppointmentStateDTO, Appointment::setAppointmentState);

                }
        );





        // ---------------------------------------------------------------------------- //
        // ---------------------------------------------------------------------------- //



        /**
         * Mapear Reservation a ReservationDTO
         */
        TypeMap<Reservation, ReservationDTO> reservationToReservationDTOMapper
                = modelMapper.createTypeMap(Reservation.class, ReservationDTO.class);

        Converter<Appointment, AppointmentDTO> appointmentToActivityDTOConverter = context -> {
            Appointment source = context.getSource();
            AppointmentDTO destination = new AppointmentDTO();
            appointmentToAppointmentDTOMapper.map(source, destination);
            return destination;
        };


        reservationToReservationDTOMapper.addMappings(
                mapper -> {

                    // mapeo el usuario
                    mapper.using(userToUserDTOConverter)
                        .map(Reservation::getUser, ReservationDTO::setUserDTO);

                    // mapeo el turno
                    mapper.using(appointmentToActivityDTOConverter)
                            .map(Reservation::getAppointment, ReservationDTO::setAppointmentDTO);

                }
        );



        // ---------------------------------------------------------------------------- //
        // ---------------------------------------------------------------------------- //



        /**
         * Mapear ReservationDTO a Reservation
         */
        TypeMap<ReservationDTO, Reservation> reservationDTOToReservationMapper
                = modelMapper.createTypeMap(ReservationDTO.class, Reservation.class);

        Converter<AppointmentDTO, Appointment> appointmentDTOToActivityConverter = context -> {
            AppointmentDTO source = context.getSource();
            Appointment destination = new Appointment();
            appointmentDTOToAppointmentMapper.map(source, destination);
            return destination;
        };


        reservationDTOToReservationMapper.addMappings(
                mapper -> {

                    // mapeo el usuario DTO a usuario entidad
                    mapper.using(userDTOToUserConverter)
                            .map(ReservationDTO::getUserDTO, Reservation::setUser);

//                  mapeo appointment DTO a appointment entidad
                    mapper.using(appointmentDTOToActivityConverter)
                            .map(ReservationDTO::getAppointmentDTO, Reservation::setAppointment);


                }
        );



        // ---------------------------------------------------------------------------- //
        // ---------------------------------------------------------------------------- //



        return modelMapper;

    }

}
