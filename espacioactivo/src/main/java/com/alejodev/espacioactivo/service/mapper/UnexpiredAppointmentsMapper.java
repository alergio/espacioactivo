package com.alejodev.espacioactivo.service.mapper;

import com.alejodev.espacioactivo.dto.AppointmentDTO;
import com.alejodev.espacioactivo.dto.ResponseDTO;
import com.alejodev.espacioactivo.entity.Appointment;
import com.alejodev.espacioactivo.exception.ResourceNotFoundException;
import com.alejodev.espacioactivo.repository.impl.IAppointmentRepository;
import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import static com.alejodev.espacioactivo.service.mapper.ConfigureMapper.configureMapper;

@Service
public class UnexpiredAppointmentsMapper {

    @Autowired
    private IAppointmentRepository appointmentRepository;
    private final ModelMapper modelMapper = configureMapper();
    private final Logger LOGGER = Logger.getLogger(UnexpiredAppointmentsMapper.class);
    private final String appointmentClassNamePlural = Appointment.class.getSimpleName() + "s";

    public ResponseDTO readAllUnexpired(){

        ResponseDTO responseDTO = new ResponseDTO();
        List<Appointment> appointmentList = appointmentRepository.findUnexpiredAppointments();

        if (!appointmentList.isEmpty()){

            List<AppointmentDTO> appointmentDTOList = appointmentList.stream()
                    .map(appointment -> modelMapper.map(appointment, AppointmentDTO.class))
                    .toList();

            responseDTO.setStatusCode(HttpStatus.OK.value());
            responseDTO.setMessage("Request to see all " + appointmentClassNamePlural + " completed successfully.");
            responseDTO.setData(Collections.singletonMap(appointmentClassNamePlural, appointmentDTOList));

            LOGGER.info("Request to see all " + appointmentClassNamePlural + " completed successfully.");

        } else {
            throw new ResourceNotFoundException(appointmentClassNamePlural);
        }

        return responseDTO;

    }


}
