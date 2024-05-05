package com.alejodev.espacioactivo.service.impl;

import com.alejodev.espacioactivo.dto.ActivityDTO;
import com.alejodev.espacioactivo.dto.EntityIdentificatorDTO;
import com.alejodev.espacioactivo.dto.ResponseDTO;
import com.alejodev.espacioactivo.entity.Activity;
import com.alejodev.espacioactivo.entity.Discipline;
import com.alejodev.espacioactivo.exception.DataIntegrityVExceptionWithMsg;
import com.alejodev.espacioactivo.repository.impl.IActivityRepository;
import com.alejodev.espacioactivo.repository.impl.IDisciplineRepository;
import com.alejodev.espacioactivo.service.ICRUDService;
import com.alejodev.espacioactivo.service.mapper.CRUDMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.alejodev.espacioactivo.service.mapper.CRUDMapperProvider.getActivityCRUDMapper;

@Service
public class ActivityService implements ICRUDService<ActivityDTO> {

    @Autowired
    private IActivityRepository activityRepository;
    @Autowired
    private IDisciplineRepository disciplineRepository;
    private CRUDMapper<ActivityDTO, Activity> crudMapper;

    @PostConstruct
    private void setUpCrudMapper(){
        crudMapper = getActivityCRUDMapper(activityRepository);
    }

    @Override
    public ResponseDTO create(EntityIdentificatorDTO activityDTO) {

        ActivityDTO activityDTORequest = (ActivityDTO) activityDTO;
        Optional<Discipline> discipline =
                disciplineRepository.findById(activityDTORequest.getDisciplineDTO().getId());

        if (discipline.isPresent()) {
            return crudMapper.create(activityDTO);
        } else {
            throw new DataIntegrityVExceptionWithMsg("Discipline");
        }

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
    public ResponseDTO update(EntityIdentificatorDTO activityDTO) {
        return crudMapper.update(activityDTO);
    }

    @Override
    public ResponseDTO delete(Long id) {
        return crudMapper.delete(id);
    }
}
