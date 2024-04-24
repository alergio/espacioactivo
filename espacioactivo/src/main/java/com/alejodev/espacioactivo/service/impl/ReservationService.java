package com.alejodev.espacioactivo.service.impl;

import com.alejodev.espacioactivo.dto.EntityIdentificatorDTO;
import com.alejodev.espacioactivo.dto.ReservationDTO;
import com.alejodev.espacioactivo.dto.ResponseDTO;
import com.alejodev.espacioactivo.entity.Reservation;
import com.alejodev.espacioactivo.repository.impl.IReservationRepository;
import com.alejodev.espacioactivo.service.ICRUDService;
import com.alejodev.espacioactivo.service.mapper.CRUDMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.alejodev.espacioactivo.service.mapper.CRUDMapperProvider.getReservationCRUDMapper;

@Service
public class ReservationService implements ICRUDService<ReservationDTO> {

    @Autowired
    private IReservationRepository reservationRepository;
    private CRUDMapper<ReservationDTO, Reservation> crudMapper;

    @PostConstruct
    private void setUpCrudMapper(){
        crudMapper = getReservationCRUDMapper(reservationRepository);
    }

    @Override
    public ResponseDTO create(EntityIdentificatorDTO reservationDTO) {
        return crudMapper.create(reservationDTO);
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
    public ResponseDTO update(EntityIdentificatorDTO reservationDTO) {
        return crudMapper.update(reservationDTO);
    }

    @Override
    public ResponseDTO delete(Long id) {
        return crudMapper.delete(id);
    }
}
