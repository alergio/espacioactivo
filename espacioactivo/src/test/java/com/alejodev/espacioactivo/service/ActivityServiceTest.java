package com.alejodev.espacioactivo.service;

import com.alejodev.espacioactivo.dto.*;
import com.alejodev.espacioactivo.exception.ResourceNotFoundException;
import com.alejodev.espacioactivo.service.impl.ActivityService;
import org.junit.jupiter.api.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.alejodev.espacioactivo.service.mapper.ConfigureMapper.configureMapper;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ActivityServiceTest {

    ModelMapper modelMapper = configureMapper();

    @Autowired
    private ActivityService activityService;

    private ResponseDTO activityDTOResponseCreate;
    private ResponseDTO activityDTOResponseReadById;
    private ResponseDTO activityDTOResponseUpdate;
    private ResponseDTO activityDTOResponseDelete;

    private Long activityID;
    private Long addressID;
    private ActivityDTO activityDTOSaved;


    @BeforeAll
    void setUpData() {

        // voy a usar todos los datos precargados que estan en el script de datos
        // los asocio solamente con el id

        ActivityDTO activityDTOForCreate = new ActivityDTO();

        // customer raul
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);

        // clases grupales de yoga
        DisciplineDTO disciplineDTO = new DisciplineDTO();
        disciplineDTO.setId(2L);

        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setStreet("Av Italia");
        addressDTO.setNumber("1377");
        addressDTO.setState("Montevideo");

        activityDTOForCreate.setPrice(200);
        activityDTOForCreate.setMaxPeople(5);
        activityDTOForCreate.setUserDTO(userDTO);
        activityDTOForCreate.setAddressDTO(addressDTO);
        activityDTOForCreate.setDisciplineDTO(disciplineDTO);

        activityDTOResponseCreate = activityService.create(activityDTOForCreate);

        activityDTOSaved = (ActivityDTO) activityDTOResponseCreate.getData().get("Activity");
        activityID = activityDTOSaved.getId();
        addressID = activityDTOSaved.getAddressDTO().getId();

        AddressDTO addressDTOForUpdate = new AddressDTO();
        addressDTOForUpdate.setId(addressID);
        addressDTOForUpdate.setStreet("Dionisio Lopez");
        addressDTOForUpdate.setNumber("2291");
        addressDTOForUpdate.setState("Montevideo");

        ActivityDTO activityDTOForUpdate = modelMapper.map(activityDTOForCreate, ActivityDTO.class);
        activityDTOForUpdate.setId(activityID);
        activityDTOForUpdate.setPrice(120);
        activityDTOForUpdate.setMaxPeople(25);
        activityDTOForUpdate.setAddressDTO(addressDTOForUpdate);

        activityDTOResponseUpdate = activityService.update(activityDTOForUpdate);
        activityDTOResponseReadById = activityService.readById(activityID);
        activityDTOResponseDelete = activityService.delete(activityID);

    }


    @Test
    @Order(1)
    void create() {

        String expected = "ResponseDTO(statusCode=200, message=Activity saved successfully., " +
                "data={Activity=ActivityDTO(id=" + activityID + ", price=200, maxPeople=5, " +
                "disciplineDTO=DisciplineDTO(id=2, name=null, type=null), " +
                "userDTO=UserDTO(id=1, firstname=null, lastname=null, email=null, " +
                "registrationDate=null, isEnabled=false, roles=[]), " +
                "addressDTO=AddressDTO(id=" + addressID + ", street=Av Italia, " +
                "number=1377, state=Montevideo))})";
        assertEquals(expected, activityDTOResponseCreate.toString());

    }

    @Test
    @Order(2)
    void update() {

        String expected = "ResponseDTO(statusCode=200, message=Activity updated successfully., " +
                "data={Activity=ActivityDTO(id=" + activityID + ", price=120, maxPeople=25, " +
                "disciplineDTO=DisciplineDTO(id=2, name=null, type=null), " +
                "userDTO=UserDTO(id=1, firstname=null, lastname=null, email=null, " +
                "registrationDate=null, isEnabled=false, roles=[]), addressDTO=AddressDTO" +
                "(id=" + addressID + ", street=Dionisio Lopez, number=2291, state=Montevideo))})";
        assertEquals(expected, activityDTOResponseUpdate.toString());

    }

    @Test
    @Order(3)
    void readById() {

        String expected = "ResponseDTO(statusCode=200, message=Activity successfully found." +
                ", data={Activity=ActivityDTO(id=" + activityID + ", price=120, maxPeople=25, " +
                "disciplineDTO=DisciplineDTO(id=2, name=Yoga, type=GROUP_CLASS), " +
                "userDTO=UserDTO(id=1, firstname=Raul, lastname=Pereira, email=customer_raul@test.com, " +
                "registrationDate=2024-04-20, isEnabled=true, roles=[RoleDTO(id=1, name=ROLE_CUSTOMER)]), " +
                "addressDTO=AddressDTO(id=" + addressID + ", street=Dionisio Lopez, number=2291, state=Montevideo))})";
        assertEquals(expected, activityDTOResponseReadById.toString());

    }

    @Test
    @Order(4)
    void delete() {

        String expected = "ResponseDTO(statusCode=200, message=Activity deleted succesfully." +
                ", data={Activity=ActivityDTO(id=" + activityID + ", price=120, maxPeople=25, " +
                "disciplineDTO=DisciplineDTO(id=2, name=Yoga, type=GROUP_CLASS), " +
                "userDTO=UserDTO(id=1, firstname=Raul, lastname=Pereira, email=customer_raul@test.com, " +
                "registrationDate=2024-04-20, isEnabled=true, roles=[RoleDTO(id=1, name=ROLE_CUSTOMER)]), " +
                "addressDTO=AddressDTO(id=" + addressID + ", street=Dionisio Lopez, number=2291, state=Montevideo))})";
        assertEquals(expected, activityDTOResponseDelete.toString());

    }

    @Test
    @Order(5)
    void readByIdWithException() {
        assertThrows(ResourceNotFoundException.class, () -> activityService.readById(activityID));
    }


}
