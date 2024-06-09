package com.alejodev.espacioactivo.service.impl;

import com.alejodev.espacioactivo.dto.*;
import com.alejodev.espacioactivo.entity.DisciplineType;
import com.alejodev.espacioactivo.entity.RequestStatus;
import com.alejodev.espacioactivo.entity.RequestToCreateDiscipline;
import com.alejodev.espacioactivo.exception.DataIntegrityVExceptionWithMsg;
import com.alejodev.espacioactivo.exception.MethodNotAllowedException;
import com.alejodev.espacioactivo.exception.ResourceAlreadyExistsException;
import com.alejodev.espacioactivo.repository.impl.IRequestToCreateDisciplineRepository;
import com.alejodev.espacioactivo.service.ICRUDService;
import com.alejodev.espacioactivo.service.mapper.CRUDMapper;
import com.alejodev.espacioactivo.service.mapper.ReadAllCondition;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

import static com.alejodev.espacioactivo.exception.DataIntegrityVExceptionWithMsg.emptyFieldMessage;
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

        // todas las validaciones para la request
        allValidationsForRequestToCreateDiscipline(requestToCreateDisciplineDTO);

        // fuerzo el valor del status
        requestToCreateDisciplineDTO.setStatus(String.valueOf(RequestStatus.ON_HOLD));

        return create(requestToCreateDisciplineDTO);

    }


    public ResponseDTO readAllByServiceProvider() {
        Long userId = getAuthenticatedUserId();
        return crudMapper.readAllWithCondition(ReadAllCondition.DISCIPLINE_REQUESTS_BY_USERID, userId);
    }


    public ResponseDTO updateByServiceProvider(RequestToCreateDisciplineDTO requestToCreateDisciplineDTO) {
        RequestToCreateDisciplineDTO requestDTOForUpdate = dataValidatorForUpdate(requestToCreateDisciplineDTO);
        return update(requestDTOForUpdate);
    }

    public ResponseDTO deleteByServiceProvider(Long requestId) {

        RequestToCreateDisciplineDTO requestDTOForDelete = getUserRequestById(requestId);

        // si no esta en ON_HOLD no se puede eliminar.
        if (!Objects.equals(requestDTOForDelete.getStatus(), String.valueOf(RequestStatus.ON_HOLD))) {
            throw new MethodNotAllowedException("Your request has already been reviewed by an administrator" +
                    " and can no longer be deleted. The status of the request is: " + requestDTOForDelete.getStatus());
        }

        return delete(requestId);
    }

    private RequestToCreateDisciplineDTO dataValidatorForUpdate(RequestToCreateDisciplineDTO requestToCreateDisciplineDTO) {
        // valido que no venga el id vacio
        if (requestToCreateDisciplineDTO.getId() == null) {
            throw new DataIntegrityVExceptionWithMsg(emptyFieldMessage("requestToCreateDisciplineDTO.id"));
        }

        Long requestId = requestToCreateDisciplineDTO.getId();
        RequestToCreateDisciplineDTO requestDTOForUpdate = getUserRequestById(requestId);

        boolean differencesFlag = false;

        // si no esta en ON_HOLD no se puede actualizar.
        if (!Objects.equals(requestDTOForUpdate.getStatus(), String.valueOf(RequestStatus.ON_HOLD))) {
            throw new MethodNotAllowedException("Your request has already been reviewed by an administrator" +
                    " and can no longer be edited. The status of the request is: " + requestDTOForUpdate.getStatus());
        }

        // si envio un nombre, y no es igual al que ya tiene
        if (requestToCreateDisciplineDTO.getDisciplineName() != null
                && !requestToCreateDisciplineDTO.getDisciplineName().equals(requestDTOForUpdate.getDisciplineName())) {
            differencesFlag = true;
            requestDTOForUpdate.setDisciplineName(requestToCreateDisciplineDTO.getDisciplineName());
        }

        // si envio un tipo, y no es igual al que ya tiene
        if (requestToCreateDisciplineDTO.getDisciplineType() != null
                && !requestToCreateDisciplineDTO.getDisciplineType().equals(requestDTOForUpdate.getDisciplineType())) {
            differencesFlag = true;
            requestDTOForUpdate.setDisciplineType(requestToCreateDisciplineDTO.getDisciplineType());
        }

        if (!differencesFlag) {
            throw new DataIntegrityVExceptionWithMsg("No changes found to update.");
        }

        // todas las validaciones para la request
        allValidationsForRequestToCreateDiscipline(requestDTOForUpdate);

        return requestDTOForUpdate;
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

    private void allValidationsForRequestToCreateDiscipline(RequestToCreateDisciplineDTO requestToCreateDisciplineDTO) {
        // validar que la disciplina enviada sea valida y no exista
        disciplineValidator(requestToCreateDisciplineDTO);

        // hacer una validacion que se fije si ya existe una solicitud con ese nombre y tipo
        requestDisciplineDataValidator(requestToCreateDisciplineDTO);
    }

    private RequestToCreateDisciplineDTO getUserRequestById(Long requestId) {
        Long userId = getAuthenticatedUserId();
        return (RequestToCreateDisciplineDTO) crudMapper.getUserEntityDTOById(requestId, userId, ReadAllCondition.DISCIPLINE_REQUESTS_BY_USERID);
    }

}
