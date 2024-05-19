package com.alejodev.espacioactivo.service.impl;

import com.alejodev.espacioactivo.dto.ActivityDTO;
import com.alejodev.espacioactivo.dto.AddressDTO;
import com.alejodev.espacioactivo.dto.EntityIdentificatorDTO;
import com.alejodev.espacioactivo.dto.ResponseDTO;
import com.alejodev.espacioactivo.entity.Activity;
import com.alejodev.espacioactivo.entity.Discipline;
import com.alejodev.espacioactivo.exception.DataIntegrityVExceptionWithMsg;
import com.alejodev.espacioactivo.exception.DataIntegrityVExceptionWithNotFoundEx;
import com.alejodev.espacioactivo.exception.InvalidUserException;
import com.alejodev.espacioactivo.exception.ResourceNotFoundException;
import com.alejodev.espacioactivo.repository.impl.IActivityRepository;
import com.alejodev.espacioactivo.repository.impl.IDisciplineRepository;
import com.alejodev.espacioactivo.service.ICRUDService;
import com.alejodev.espacioactivo.service.mapper.CRUDMapper;
import com.alejodev.espacioactivo.service.mapper.ReadAllCondition;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.alejodev.espacioactivo.exception.DataIntegrityVExceptionWithMsg.emptyFieldMessage;
import static com.alejodev.espacioactivo.security.auth.AuthenticationService.getAuthenticatedUserId;
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

        if (activityDTORequest.getDisciplineDTO() == null) {
            throw new DataIntegrityVExceptionWithMsg(emptyFieldMessage("disciplineDTO"));
        }

        Optional<Discipline> discipline =
                disciplineRepository.findById(activityDTORequest.getDisciplineDTO().getId());

        if (discipline.isEmpty()) {
            throw new DataIntegrityVExceptionWithNotFoundEx("Discipline");
        }

        return crudMapper.create(activityDTO);

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

    public ResponseDTO readAllByServiceProvider(){
        // este solo trae las que el creo

        // esto ya valida que el usuario este logueado
        Long userId = getAuthenticatedUserId();
        return crudMapper.readAllWithCondition(ReadAllCondition.ACTIVITIES_BY_USERID, userId);
    }

    public ResponseDTO createByServiceProvider(ActivityDTO activityDTO) {
        activityDataValidator(activityDTO);
        return create(activityDTO);
    }



    public ResponseDTO updateByServiceProvider(ActivityDTO activityDTORequest) {

        // aca estoy condicionando para que solo pueda modificar los que son de el

        if (activityDTORequest.getId() == null) {
            throw new DataIntegrityVExceptionWithMsg(emptyFieldMessage("activityDTO.id"));
        }

        AddressDTO addressDTO = activityDTORequest.getAddressDTO();

        if (addressDTO == null && activityDTORequest.getPrice() == null) {
            throw new DataIntegrityVExceptionWithMsg(emptyFieldMessage("Address and price"));
        }

        Long activityId = activityDTORequest.getId();
        ActivityDTO activityDTOForUpdate = getUserActivityDTOById(activityId);

        if(addressDTO != null){
            activityDTOForUpdate.setAddressDTO(addressDTO);
        }

        if(activityDTORequest.getPrice() != null) {
            activityDTOForUpdate.setPrice(activityDTORequest.getPrice());
        }

        return update(activityDTOForUpdate);

    }


    public ResponseDTO deleteByServiceProvider(Long activityId) {

        getUserActivityDTOById(activityId);
        return delete(activityId);

    }



    public ActivityDTO getUserActivityDTOById(Long activityId) {
        ActivityDTO activityDTO = null;

        ResponseDTO responseForAllActivities = readAllByServiceProvider();
        List<ActivityDTO> activitiesDTO = (List<ActivityDTO>) responseForAllActivities.getData().get("Activities");

        for(ActivityDTO activity : activitiesDTO) {
            if(activity.getId().equals(activityId)){
                activityDTO = activity;
                break;
            }
        }

        if (activityDTO != null) {
            return activityDTO;
        } else {
            throw new ResourceNotFoundException("Activity");
        }
    }


    private static void activityDataValidator(ActivityDTO activityDTO) {

        Long userId = getAuthenticatedUserId();

        if (activityDTO.getUserDTO() == null || activityDTO.getUserDTO().getId() == null) {
            throw new DataIntegrityVExceptionWithMsg(emptyFieldMessage("userDTO or userDTO.id"));
        }

        if (!activityDTO.getUserDTO().getId().equals(userId)) {
            throw new InvalidUserException();
        }

        if (activityDTO.getAddressDTO() == null) {
            throw new DataIntegrityVExceptionWithMsg(emptyFieldMessage("addressDTO"));
        }

        if (activityDTO.getAddressDTO().getState() == null) {
            throw new DataIntegrityVExceptionWithMsg(emptyFieldMessage("addressDTO.state"));
        }

        if (activityDTO.getPrice() == null) {
            throw new DataIntegrityVExceptionWithMsg(emptyFieldMessage("price"));
        }
    }




}
