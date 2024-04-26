package com.alejodev.espacioactivo.service.impl;

import com.alejodev.espacioactivo.dto.AppointmentDTO;
import com.alejodev.espacioactivo.dto.EntityIdentificatorDTO;
import com.alejodev.espacioactivo.dto.ReservationDTO;
import com.alejodev.espacioactivo.dto.ResponseDTO;
import com.alejodev.espacioactivo.entity.Appointment;
import com.alejodev.espacioactivo.entity.Reservation;
import com.alejodev.espacioactivo.repository.impl.IAppointmentRepository;
import com.alejodev.espacioactivo.repository.impl.IReservationRepository;
import com.alejodev.espacioactivo.service.ICRUDService;
import com.alejodev.espacioactivo.service.mapper.CRUDMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.alejodev.espacioactivo.service.mapper.CRUDMapperProvider.getAppointmentCRUDMapper;

@Service
public class AppointmentService implements ICRUDService<AppointmentDTO> {

    @Autowired
    private IAppointmentRepository appointmentRepository;
    private CRUDMapper<AppointmentDTO, Appointment> crudMapper;

    @PostConstruct
    private void setUpCrudMapper(){
        crudMapper = getAppointmentCRUDMapper(appointmentRepository);
    }

    @Override
    public ResponseDTO create(EntityIdentificatorDTO appointmentDTO) {
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
}
