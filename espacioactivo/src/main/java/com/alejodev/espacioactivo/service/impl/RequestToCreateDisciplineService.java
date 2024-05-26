package com.alejodev.espacioactivo.service.impl;

import com.alejodev.espacioactivo.dto.DisciplineDTO;
import com.alejodev.espacioactivo.dto.EntityIdentificatorDTO;
import com.alejodev.espacioactivo.dto.RequestToCreateDisciplineDTO;
import com.alejodev.espacioactivo.dto.ResponseDTO;
import com.alejodev.espacioactivo.entity.DisciplineType;
import com.alejodev.espacioactivo.entity.RequestStatus;
import com.alejodev.espacioactivo.entity.RequestToCreateDiscipline;
import com.alejodev.espacioactivo.exception.ResourceAlreadyExistsException;
import com.alejodev.espacioactivo.repository.impl.IRequestToCreateDisciplineRepository;
import com.alejodev.espacioactivo.service.ICRUDService;
import com.alejodev.espacioactivo.service.mapper.CRUDMapper;
import com.alejodev.espacioactivo.service.mapper.ReadAllCondition;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.alejodev.espacioactivo.security.auth.AuthenticationService.getAuthenticatedUserId;
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

    @Override
    public ResponseDTO create(EntityIdentificatorDTO requestToCreateDisciplineDTO) {
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

    public ResponseDTO createByServiceProvider(RequestToCreateDisciplineDTO requestToCreateDisciplineDTO) {

        // hacer una validacion que se fije si ya existe una solicitud con ese nombre y tipo
        requestDisciplineDataValidator(requestToCreateDisciplineDTO);

        // validar que la disciplina enviada sea valida y no exista
        disciplineValidator(requestToCreateDisciplineDTO);

        // fuerzo el valor del status
        requestToCreateDisciplineDTO.setStatus(String.valueOf(RequestStatus.ON_HOLD));

        return create(requestToCreateDisciplineDTO);

    }


    public ResponseDTO readAllByServiceProvider() {
        Long userId = getAuthenticatedUserId();
        return crudMapper.readAllWithCondition(ReadAllCondition.DISCIPLINE_REQUESTS_BY_USERNAME, userId);
    }


    public ResponseDTO updateByServiceProvider(RequestToCreateDisciplineDTO requestToCreateDisciplineDTO) {

        return null;

    }





    private void disciplineValidator(RequestToCreateDisciplineDTO requestToCreateDisciplineDTO) {
        DisciplineDTO disciplineDTO = new DisciplineDTO();
        disciplineDTO.setName(requestToCreateDisciplineDTO.getDisciplineName());
        disciplineDTO.setType(requestToCreateDisciplineDTO.getDisciplineType());

        disciplineService.validateDiscipline(disciplineDTO);
    }

    private void requestDisciplineDataValidator(RequestToCreateDisciplineDTO requestToCreateDisciplineDTO) {
        Optional<RequestToCreateDiscipline> requestToCreateDiscipline =
                requestToCreateDisciplineRepository.findRequestByNameAndType(
                        requestToCreateDisciplineDTO.getDisciplineName(),
                        DisciplineType.valueOf(requestToCreateDisciplineDTO.getDisciplineType())
                );

        if (requestToCreateDiscipline.isPresent()) {
            throw new ResourceAlreadyExistsException("Request To Create Discipline");
        }
    }

}
