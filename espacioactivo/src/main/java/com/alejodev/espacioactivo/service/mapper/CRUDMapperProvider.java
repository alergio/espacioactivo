package com.alejodev.espacioactivo.service.mapper;

import com.alejodev.espacioactivo.dto.ActivityDTO;
import com.alejodev.espacioactivo.dto.DisciplineDTO;
import com.alejodev.espacioactivo.dto.ReservationDTO;
import com.alejodev.espacioactivo.entity.Activity;
import com.alejodev.espacioactivo.entity.Discipline;
import com.alejodev.espacioactivo.entity.Reservation;
import com.alejodev.espacioactivo.repository.impl.IActivityRepository;
import com.alejodev.espacioactivo.repository.impl.IDisciplineRepository;
import com.alejodev.espacioactivo.repository.impl.IReservationRepository;
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


}
