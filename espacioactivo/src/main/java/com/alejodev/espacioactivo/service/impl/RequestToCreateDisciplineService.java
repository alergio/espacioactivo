package com.alejodev.espacioactivo.service.impl;

import com.alejodev.espacioactivo.dto.DisciplineDTO;
import com.alejodev.espacioactivo.dto.EntityIdentificatorDTO;
import com.alejodev.espacioactivo.dto.RequestToCreateDisciplineDTO;
import com.alejodev.espacioactivo.dto.ResponseDTO;
import com.alejodev.espacioactivo.entity.RequestStatus;
import com.alejodev.espacioactivo.entity.RequestToCreateDiscipline;
import com.alejodev.espacioactivo.exception.WrongTypeException;
import com.alejodev.espacioactivo.repository.impl.IRequestToCreateDisciplineRepository;
import com.alejodev.espacioactivo.service.ICRUDService;
import com.alejodev.espacioactivo.service.mapper.CRUDMapper;
import com.alejodev.espacioactivo.service.mapper.ReadAllCondition;
import jakarta.annotation.PostConstruct;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.alejodev.espacioactivo.security.auth.AuthenticationService.getUserName;
import static com.alejodev.espacioactivo.service.mapper.CRUDMapperProvider.getRequestToCreateDisciplineCrudMapper;

/**
 *
 *
 * @author alejo
 * @version 1.0 05-05-2024
 */
@Service
public class RequestToCreateDisciplineService implements ICRUDService<RequestToCreateDisciplineDTO> {

    @Autowired
    DisciplineService disciplineService;
    @Autowired
    private IRequestToCreateDisciplineRepository requestToCreateDisciplineRepository;
    private CRUDMapper<RequestToCreateDisciplineDTO, RequestToCreateDiscipline> crudMapper;


    @PostConstruct
    private void setUpCrudMapper(){
        crudMapper = getRequestToCreateDisciplineCrudMapper(requestToCreateDisciplineRepository);
    }

    // este metodo es para usuarios con el rol correspondiente, el resto del crud va a ser solo para admins
    @Override
    public ResponseDTO create(EntityIdentificatorDTO requestToCreateDisciplineDTO) {

        RequestToCreateDisciplineDTO requestToCreateDisciplineDTOCasted
                = (RequestToCreateDisciplineDTO) requestToCreateDisciplineDTO;

        DisciplineDTO disciplineDTO = new DisciplineDTO();
        disciplineDTO.setName(requestToCreateDisciplineDTOCasted.getDisciplineName());
        disciplineDTO.setType(requestToCreateDisciplineDTOCasted.getDisciplineType());

        disciplineService.validateDiscipline(disciplineDTO);

        if (!EnumUtils.isValidEnum(RequestStatus.class, requestToCreateDisciplineDTOCasted.getStatus())) {
            throw new WrongTypeException("Status");
        }

        return crudMapper.create(requestToCreateDisciplineDTO);

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
    public ResponseDTO update(EntityIdentificatorDTO entityDTO) {
        return crudMapper.update(entityDTO);
    }

    @Override
    public ResponseDTO delete(Long id) {
        return crudMapper.delete(id);
    }

    public ResponseDTO readAllByUser() {
        String userName = getUserName();
        return crudMapper.readAllWithCondition(ReadAllCondition.DISCIPLINE_REQUESTS_BY_USERNAME, userName);
    }



}
