package com.alejodev.espacioactivo.service.impl;

import com.alejodev.espacioactivo.dto.AppointmentStateDTO;
import com.alejodev.espacioactivo.entity.AppointmentState;
import com.alejodev.espacioactivo.entity.AppointmentStateType;
import com.alejodev.espacioactivo.exception.DataIntegrityVExceptionWithNotFoundEx;
import com.alejodev.espacioactivo.repository.impl.IAppointmentStateRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.alejodev.espacioactivo.service.mapper.ConfigureMapper.configureMapper;

@Service
public class AppointmentStateService {

    @Autowired
    IAppointmentStateRepository appointmentStateRepository;
    private final ModelMapper modelMapper = configureMapper();

    public AppointmentStateDTO getDTOById(Long id) {
        Optional<AppointmentState> appointmentState =
                appointmentStateRepository.findById(id);

        if (appointmentState.isEmpty()) {
            throw new DataIntegrityVExceptionWithNotFoundEx("Appointment State");
        }

        return modelMapper.map(appointmentState.get(), AppointmentStateDTO.class);
    }

    public AppointmentState getByName(AppointmentStateType name) {
        Optional<AppointmentState> appointmentState = Optional.ofNullable(appointmentStateRepository.findByName(name));

        if (appointmentState.isEmpty()) {
            throw new DataIntegrityVExceptionWithNotFoundEx("Appointment State");
        }

        return appointmentState.get();
    }


    public AppointmentStateDTO getDTOByName(AppointmentStateType name) {
        Optional<AppointmentState> appointmentState = Optional.ofNullable(appointmentStateRepository.findByName(name));

        if (appointmentState.isEmpty()) {
            throw new DataIntegrityVExceptionWithNotFoundEx("Appointment State");
        }

        return modelMapper.map(appointmentState.get(), AppointmentStateDTO.class);
    }

}
