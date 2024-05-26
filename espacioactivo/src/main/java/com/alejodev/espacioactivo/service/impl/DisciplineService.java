package com.alejodev.espacioactivo.service.impl;

import com.alejodev.espacioactivo.dto.DisciplineDTO;
import com.alejodev.espacioactivo.dto.EntityIdentificatorDTO;
import com.alejodev.espacioactivo.dto.ResponseDTO;
import com.alejodev.espacioactivo.entity.Discipline;
import com.alejodev.espacioactivo.entity.DisciplineType;
import com.alejodev.espacioactivo.exception.ResourceAlreadyExistsException;
import com.alejodev.espacioactivo.exception.WrongTypeException;
import com.alejodev.espacioactivo.repository.impl.IDisciplineRepository;
import com.alejodev.espacioactivo.service.ICRUDService;
import com.alejodev.espacioactivo.service.mapper.CRUDMapper;
import jakarta.annotation.PostConstruct;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.alejodev.espacioactivo.service.mapper.CRUDMapperProvider.getDisciplineCRUDMapper;


/**
 *
 *
 * @author alejo
 * @version 1.0 21-04-2024
 */
@Service
public class DisciplineService implements ICRUDService<DisciplineDTO> {

    @Autowired
    private IDisciplineRepository disciplineRepository;
    private CRUDMapper<DisciplineDTO, Discipline> crudMapper;

    @PostConstruct
    private void setUpCrudMapper(){
        crudMapper = getDisciplineCRUDMapper(disciplineRepository);
    }


    @Override
    public ResponseDTO create(EntityIdentificatorDTO disciplineDTO) {

        DisciplineDTO disciplineDTORequest = (DisciplineDTO) disciplineDTO;

        validateDiscipline(disciplineDTORequest);

        return crudMapper.create(disciplineDTO);
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
    public ResponseDTO update(EntityIdentificatorDTO disciplineDTO) {
        return crudMapper.update(disciplineDTO);
    }

    @Override
    public ResponseDTO delete(Long id) {
        return crudMapper.delete(id);
    }

    public void validateDiscipline(DisciplineDTO disciplineDTO) {

        if (!EnumUtils.isValidEnum(DisciplineType.class, disciplineDTO.getType())) {
            throw new WrongTypeException("Discipline");
        }

        Optional<Discipline> discipline =
                disciplineRepository.findIfExistsDiscipline(
                        disciplineDTO.getName(), DisciplineType.valueOf(disciplineDTO.getType())
                );

        if (discipline.isPresent()) {
            throw new ResourceAlreadyExistsException("Discipline");
        }

    }



}
