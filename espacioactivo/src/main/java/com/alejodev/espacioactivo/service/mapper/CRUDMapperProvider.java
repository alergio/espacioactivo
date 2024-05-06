package com.alejodev.espacioactivo.service.mapper;

import com.alejodev.espacioactivo.dto.*;
import com.alejodev.espacioactivo.entity.*;
import com.alejodev.espacioactivo.repository.impl.*;
import org.springframework.stereotype.Component;


@Component
public class CRUDMapperProvider {

    public static CRUDMapper<DisciplineDTO, Discipline> getDisciplineCRUDMapper(IDisciplineRepository disciplineRepository) {

        CRUDMapper<DisciplineDTO, Discipline> disciplineCRUDMapper = new CRUDMapper<>();

        Class<Discipline> disciplineClass = Discipline.class;
        String disciplineClassName = disciplineClass.getSimpleName();

        disciplineCRUDMapper.setRepository(disciplineRepository);
        disciplineCRUDMapper.setDtoClass(DisciplineDTO.class);
        disciplineCRUDMapper.setEntityClass(disciplineClass);
        disciplineCRUDMapper.setEntityClassName(disciplineClassName);
        disciplineCRUDMapper.setEntityClassNamePlural(disciplineClassName + "s");

        return disciplineCRUDMapper;

    }


    public static CRUDMapper<ActivityDTO, Activity> getActivityCRUDMapper(IActivityRepository activityRepository) {

        CRUDMapper<ActivityDTO, Activity> activityCRUDMapper = new CRUDMapper<>();

        Class<Activity> activityClass = Activity.class;
        String activityClassName = activityClass.getSimpleName();

        activityCRUDMapper.setRepository(activityRepository);
        activityCRUDMapper.setDtoClass(ActivityDTO.class);
        activityCRUDMapper.setEntityClass(activityClass);
        activityCRUDMapper.setEntityClassName(activityClassName);
        activityCRUDMapper.setEntityClassNamePlural("Activities");

        return activityCRUDMapper;

    }


    public static CRUDMapper<ReservationDTO, Reservation> getReservationCRUDMapper(IReservationRepository reservationRepository) {

        CRUDMapper<ReservationDTO, Reservation> reservationCRUDMapper = new CRUDMapper<>();

        Class<Reservation> reservationClass = Reservation.class;
        String reservationClassName = reservationClass.getSimpleName();

        reservationCRUDMapper.setRepository(reservationRepository);
        reservationCRUDMapper.setDtoClass(ReservationDTO.class);
        reservationCRUDMapper.setEntityClass(reservationClass);
        reservationCRUDMapper.setEntityClassName(reservationClassName);
        reservationCRUDMapper.setEntityClassNamePlural(reservationClassName + "s");

        return reservationCRUDMapper;

    }


    public static CRUDMapper<AppointmentDTO, Appointment> getAppointmentCRUDMapper(IAppointmentRepository appointmentRepository) {

        CRUDMapper<AppointmentDTO, Appointment> appointmentCRUDMapper = new CRUDMapper<>();

        Class<Appointment> appointmentClass = Appointment.class;
        String appointmentClassName = appointmentClass.getSimpleName();

        appointmentCRUDMapper.setRepository(appointmentRepository);
        appointmentCRUDMapper.setDtoClass(AppointmentDTO.class);
        appointmentCRUDMapper.setEntityClass(appointmentClass);
        appointmentCRUDMapper.setEntityClassName(appointmentClassName);
        appointmentCRUDMapper.setEntityClassNamePlural(appointmentClassName + "s");

        return appointmentCRUDMapper;

    }


    public static CRUDMapper<RequestToCreateDisciplineDTO, RequestToCreateDiscipline> getRequestToCreateDisciplineCrudMapper
            (IRequestToCreateDisciplineRepository requestToCreateDisciplineRepository) {

        CRUDMapper<RequestToCreateDisciplineDTO, RequestToCreateDiscipline>
                RequestToCreateDisciplineCrudMapper = new CRUDMapper<>();

        Class<RequestToCreateDiscipline> RequestToCreateDisciplineClass
                = RequestToCreateDiscipline.class;

        String RequestToCreateDisciplineClassName = RequestToCreateDisciplineClass.getSimpleName();

        RequestToCreateDisciplineCrudMapper.setRepository(requestToCreateDisciplineRepository);
        RequestToCreateDisciplineCrudMapper.setDtoClass(RequestToCreateDisciplineDTO.class);
        RequestToCreateDisciplineCrudMapper.setEntityClass(RequestToCreateDisciplineClass);
        RequestToCreateDisciplineCrudMapper.setEntityClassName(RequestToCreateDisciplineClassName);
        RequestToCreateDisciplineCrudMapper.setEntityClassNamePlural(RequestToCreateDisciplineClassName + "s");

        return RequestToCreateDisciplineCrudMapper;

    }


}
