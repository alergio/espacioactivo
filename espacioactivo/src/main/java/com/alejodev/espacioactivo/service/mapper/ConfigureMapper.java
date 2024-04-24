package com.alejodev.espacioactivo.service.mapper;

import com.alejodev.espacioactivo.dto.*;
import com.alejodev.espacioactivo.entity.*;
import jakarta.persistence.EnumType;
import org.apache.log4j.Logger;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import java.util.Set;
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
        TypeMap<User, UserDTO> userToUserDTOMapping = modelMapper.createTypeMap(User.class, UserDTO.class);

        userToUserDTOMapping.addMappings(mapper -> {

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
        TypeMap<UserDTO, User> userDTOToUserMapping = modelMapper.createTypeMap(UserDTO.class, User.class);

        userDTOToUserMapping.addMappings(mapper -> {

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
        TypeMap<Activity, ActivityDTO> activityToActivityDTOMapping
                = modelMapper.createTypeMap(Activity.class, ActivityDTO.class);

        Converter<User, UserDTO> userToUserDTOConverter = context -> {
            User source = context.getSource();
            UserDTO destination = new UserDTO();
            userToUserDTOMapping.map(source, destination);
            return destination;
        };

        activityToActivityDTOMapping.addMappings(
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
        TypeMap<ActivityDTO, Activity> activityDTOToActivityMapping
                = modelMapper.createTypeMap(ActivityDTO.class, Activity.class);

        Converter<UserDTO, User> userDTOToUserConverter = context -> {
            UserDTO source = context.getSource();
            User destination = new User();
            userDTOToUserMapping.map(source, destination);
            return destination;
        };

        Converter<DisciplineDTO, Discipline> disciplineDTOToDiscipline = context -> {
            DisciplineDTO source = context.getSource();
            Discipline destination = new Discipline();

            Optional.ofNullable(source.getId()).ifPresent(destination::setId);
            Optional.ofNullable(source.getName()).ifPresent(destination::setName);
            Optional.ofNullable(source.getType())
                    .map(DisciplineType::valueOf)
                    .ifPresent(destination::setType);

            return destination;
        };

        activityDTOToActivityMapping.addMappings(
                mapper -> {
                    mapper.using(userDTOToUserConverter)
                        .map(ActivityDTO::getUserDTO, Activity::setUser);

                    mapper.using(disciplineDTOToDiscipline)
                            .map(ActivityDTO::getDisciplineDTO, Activity::setDiscipline);

                    mapper.map(ActivityDTO::getAddressDTO, Activity::setAddress);
                }

        );



        // ---------------------------------------------------------------------------- //
        // ---------------------------------------------------------------------------- //



        /**
         * Mapear Reservation a ReservationDTO
         */
        TypeMap<Reservation, ReservationDTO> reservationToReservationDTOMapping
                = modelMapper.createTypeMap(Reservation.class, ReservationDTO.class);

        Converter<Activity, ActivityDTO> activityToActivityDTOConverter = context -> {
            Activity source = context.getSource();
            ActivityDTO destination = new ActivityDTO();
            activityToActivityDTOMapping.map(source, destination);
            return destination;
        };

        reservationToReservationDTOMapping.addMappings(
                mapper -> {
                    mapper.using(activityToActivityDTOConverter)
                        .map(Reservation::getActivity, ReservationDTO::setActivityDTO);

                    mapper.using(userToUserDTOConverter)
                        .map(Reservation::getUser, ReservationDTO::setUserDTO);

                    mapper.map(source -> Optional.ofNullable(source.getDate())
                            .map(Date::toString)
                            .orElse(null), ReservationDTO::setDate);

                    mapper.map(source -> Optional.ofNullable(source.getTime())
                            .map(Time::toString)
                            .orElse(null), ReservationDTO::setTime);
                }
        );



        // ---------------------------------------------------------------------------- //
        // ---------------------------------------------------------------------------- //



        /**
         * Mapear ReservationDTO a Reservation
         */
        TypeMap<ReservationDTO, Reservation> reservationDTOToReservationMapping
                = modelMapper.createTypeMap(ReservationDTO.class, Reservation.class);

        Converter<ActivityDTO, Activity> activityDTOToActivityConverter = context -> {
            ActivityDTO source = context.getSource();
            Activity destination = new Activity();
            activityDTOToActivityMapping.map(source, destination);
            return destination;
        };

        reservationDTOToReservationMapping.addMappings(
                mapper -> {
                    mapper.using(activityDTOToActivityConverter)
                            .map(ReservationDTO::getActivityDTO, Reservation::setActivity);

                    mapper.using(userDTOToUserConverter)
                            .map(ReservationDTO::getUserDTO, Reservation::setUser);

                    mapper.map(source -> Optional.ofNullable(source.getDate())
                            .map(dateString -> Date.valueOf(LocalDate.parse(dateString)))
                            .orElse(null), Reservation::setDate);

                    mapper.map(source -> Optional.ofNullable(source.getTime())
                            .map(timeString -> Time.valueOf(LocalTime.parse(timeString)))
                            .orElse(null), Reservation::setTime);

                }
        );



        // ---------------------------------------------------------------------------- //
        // ---------------------------------------------------------------------------- //



        return modelMapper;

    }

}
