package com.alejodev.espacioactivo.service.mapper;

import com.alejodev.espacioactivo.dto.EntityIdentificatorDTO;
import com.alejodev.espacioactivo.dto.ResponseDTO;
import com.alejodev.espacioactivo.exception.ResourceNotFoundException;
import com.alejodev.espacioactivo.repository.IGenericRepository;
import com.alejodev.espacioactivo.service.ICRUDService;
import lombok.*;
import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Servicio encargado de hacer el CRUD para ser reutilizado desde otros servicios.
 *
 * @author alejo
 * @version 1.0 21-04-2024
 */
@Service
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CRUDMapper <T, E> implements ICRUDService {

    private Class<T> dtoClass;
    private Class<E> entityClass;
    private IGenericRepository<E, Long> repository;

    private String entityClassName;
    private String entityClassNamePlural;

    private Logger LOGGER = Logger.getLogger(CRUDMapper.class);
    private ModelMapper modelMapper = new ModelMapper();


    /**
     * Metodo generico para crear una nueva entidad en la base de datos.
     * Mapeamos el DTO que nos llega a la entidad y despues se mapea esa entidad guardada
     * a un nuevo DTO para enviarlo al cliente con los datos obtenidos de la BD.
     * @param entityDTO -> DTO de la entidad que queremos crear
     * @return -> ResponseDTO que llega al cliente
     */
    @Override
    public ResponseDTO create(EntityIdentificatorDTO entityDTO){

        ResponseDTO responseDTO = new ResponseDTO();

        E entity = modelMapper.map(entityDTO, entityClass);
        repository.save(entity);
        T entityDTOMapped = modelMapper.map(entity, dtoClass);

        responseDTO.setStatusCode(HttpStatus.OK.value());
        responseDTO.setMessage(entityClassName + " saved successfully.");
        responseDTO.setData(Collections.singletonMap(entityClassName, entityDTOMapped));

        LOGGER.info(entityClassName + " saved successfully.");

        return responseDTO;

    }

    /**
     * Metodo generico para buscar por id una entidad en la base de datos.
     * Si encontramos la entidad la mapeamos a un DTO para enviarla al cliente.
     * @param id de la entidad
     * @return -> ResponseDTO que llega al cliente
     */
    @Override
    public ResponseDTO readById(Long id) {

        ResponseDTO responseDTO = new ResponseDTO();

        Optional<E> optionalEntity = repository.findById(id);

        if (optionalEntity.isPresent()) {

            E entity = optionalEntity.get();
            T entityDTO = modelMapper.map(entity, dtoClass);

            responseDTO.setStatusCode(HttpStatus.OK.value());
            responseDTO.setMessage(entityClassName + " successfully found.");
            responseDTO.setData(Collections.singletonMap(entityClassName, entityDTO));

            LOGGER.info(entityClassName + " with id " + id + " successfully found.");

        } else {
            throw new ResourceNotFoundException(entityClassName);
        }

        return responseDTO;

    }


    /**
     * Metodo generico para buscar todos los datos de una entidad que haya en la BD.
     * Si hay, mapeamos la lista de entidades a una lista de DTO para mandarsela al cliente.
     * @return -> ResponseDTO que llega al cliente
     */
    @Override
    public ResponseDTO readAll(){

        ResponseDTO responseDTO = new ResponseDTO();
        List<E> entityList = repository.findAll();

        if (!entityList.isEmpty()){

            List<T> entityDTOList = entityList.stream()
                    .map(entity -> modelMapper.map(entity, dtoClass))
                    .toList();

            responseDTO.setStatusCode(HttpStatus.OK.value());
            responseDTO.setMessage("Request to see all " + entityClassNamePlural + " completed successfully.");
            responseDTO.setData(Collections.singletonMap(entityClassNamePlural, entityDTOList));

            LOGGER.info("Request to see all " + entityClassNamePlural + " completed successfully.");

        } else {
            throw new ResourceNotFoundException(entityClassNamePlural);
        }

        return responseDTO;

    }


    /**
     * Metodo generico para actualizar una entidad en la BD.
     * Si se encuentra, la actualizamos y despues la mapeamos a un DTO para mostrarla al cliente.
     * @return -> ResponseDTO que llega al cliente
     * @param -> DTO de la entidad a actualizar.
     */
    @Override
    public ResponseDTO update(EntityIdentificatorDTO entityDTO) {

        ResponseDTO responseDTO = new ResponseDTO();
        Optional<E> optionalEntity = repository.findById(entityDTO.getId());

        if (optionalEntity.isPresent()) {

            E entity = optionalEntity.get();

            modelMapper.map(entityDTO, entity);
            repository.save(entity);

            T entityDTOMapped = modelMapper.map(entity, dtoClass);

            responseDTO.setStatusCode(HttpStatus.OK.value());
            responseDTO.setMessage(entityClassName + " updated successfully.");
            responseDTO.setData(Collections.singletonMap(entityClassName, entityDTOMapped));

            LOGGER.info(entityClassName + " updated successfully.");

        } else {
            throw new ResourceNotFoundException(entityClassName);
        }

        return responseDTO;

    }


    /**
     * Metodo generico para eliminar una entidad de la BD.
     * Espera un id, si encuentra la entidad la mapea para enviarsela al cliente y despues la elimina.
     * @return -> ResponseDTO que llega al cliente
     */
    @Override
    public ResponseDTO delete(Long id) {

        ResponseDTO responseDTO = new ResponseDTO();
        Optional<E> deletedEntity = repository.findById(id);

        if (deletedEntity.isPresent()) {

        T entityDTOMapped = modelMapper.map(deletedEntity.get(), dtoClass);

        repository.deleteById(id);

        responseDTO.setStatusCode(HttpStatus.OK.value());
        responseDTO.setMessage(entityClassName + " deleted succesfully.");
        responseDTO.setData(Collections.singletonMap(entityClassName, entityDTOMapped));

        LOGGER.info(entityClassName + " with id " + id + " deleted succesfully.");

    } else {
        throw new ResourceNotFoundException(entityClassName);
    }

        return responseDTO;

}





}
