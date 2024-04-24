package com.alejodev.espacioactivo.service;

import com.alejodev.espacioactivo.dto.*;
import com.alejodev.espacioactivo.exception.ResourceNotFoundException;
import com.alejodev.espacioactivo.service.impl.ReservationService;
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
public class ReservationServiceTest {

    @Autowired
    ReservationService reservationService;

    ModelMapper modelMapper = configureMapper();

    private ResponseDTO reservationDTOResponseCreate;
    private ResponseDTO reservationDTOResponseReadById;
    private ResponseDTO reservationDTOResponseUpdate;
    private ResponseDTO reservationDTOResponseDelete;

    private Long reservationID;
    private ReservationDTO reservationDTOSaved;


    @BeforeAll
    void setUpData(){

        ReservationDTO reservationDTOForCreate = new ReservationDTO();

        ActivityDTO activityDTOForReservation = new ActivityDTO();
        DisciplineDTO disciplineDTOForActivity = new DisciplineDTO();
        UserDTO activityProviderUserDTO = new UserDTO();
        UserDTO userDTOForReservation = new UserDTO();

        // datos precargados que estan en la BD
        // cargamos la actividad asociada en la reserva
        activityProviderUserDTO.setId(3L);
        disciplineDTOForActivity.setId(3L);
        activityDTOForReservation.setId(7L);
        activityDTOForReservation.setUserDTO(activityProviderUserDTO);
        activityDTOForReservation.setDisciplineDTO(disciplineDTOForActivity);

        // cargamos la reserva
        userDTOForReservation.setId(4L);
        reservationDTOForCreate.setUserDTO(userDTOForReservation);
        reservationDTOForCreate.setActivityDTO(activityDTOForReservation);
        reservationDTOForCreate.setDate("2024-04-22");
        reservationDTOForCreate.setTime("10:30:00");

        reservationDTOResponseCreate = reservationService.create(reservationDTOForCreate);

        reservationDTOSaved = (ReservationDTO) reservationDTOResponseCreate.getData().get("Reservation");
        reservationID = reservationDTOSaved.getId();

        ReservationDTO reservationDTOForUpdate = modelMapper.map(reservationDTOForCreate, ReservationDTO.class);
        reservationDTOForUpdate.setId(reservationID);
        reservationDTOForUpdate.setDate("2024-04-25");
        reservationDTOForUpdate.setTime("14:30:00");

        reservationDTOResponseUpdate = reservationService.update(reservationDTOForUpdate);
        reservationDTOResponseReadById = reservationService.readById(reservationID);
        reservationDTOResponseDelete = reservationService.delete(reservationID);

    }


    @Test
    @Order(1)
    void create() {

        String expected = "ResponseDTO(statusCode=200, message=Reservation saved successfully." +
                ", data={Reservation=ReservationDTO(id=" + reservationID + ", date=2024-04-22, time=10:30:00, " +
                "activityDTO=ActivityDTO(id=7, price=null, maxPeople=null, " +
                "disciplineDTO=DisciplineDTO(id=3, name=null, type=null), " +
                "userDTO=UserDTO(id=3, firstname=null, lastname=null, email=null, r" +
                "egistrationDate=null, isEnabled=false, roles=[]), addressDTO=null), " +
                "userDTO=UserDTO(id=4, firstname=null, lastname=null, email=null, " +
                "registrationDate=null, isEnabled=false, roles=[]))})";
        assertEquals(expected, reservationDTOResponseCreate.toString());

    }

    @Test
    @Order(2)
    void update() {

        String expected = "ResponseDTO(statusCode=200, message=Reservation updated successfully., " +
                "data={Reservation=ReservationDTO(id=" + reservationID + ", date=2024-04-25, time=14:30:00, " +
                "activityDTO=ActivityDTO(id=7, price=null, maxPeople=null, " +
                "disciplineDTO=DisciplineDTO(id=3, name=null, type=null), " +
                "userDTO=UserDTO(id=3, firstname=null, lastname=null, email=null, " +
                "registrationDate=null, isEnabled=false, roles=[]), addressDTO=null), " +
                "userDTO=UserDTO(id=4, firstname=null, lastname=null, email=null, " +
                "registrationDate=null, isEnabled=false, roles=[]))})";
        assertEquals(expected, reservationDTOResponseUpdate.toString());

    }

    @Test
    @Order(3)
    void readById() {

        String expected = "ResponseDTO(statusCode=200, message=Reservation successfully found., " +
                "data={Reservation=ReservationDTO(id=" + reservationID + ", date=2024-04-25, time=14:30:00, " +
                "activityDTO=ActivityDTO(id=7, price=300, maxPeople=500, " +
                "disciplineDTO=DisciplineDTO(id=3, name=Futbol, type=SPACE_RENTAL), " +
                "userDTO=UserDTO(id=3, firstname=Alejo, lastname=Maya, email=alejo@mail.com, " +
                "registrationDate=2024-04-21, isEnabled=true, roles=[RoleDTO(id=1, name=ROLE_CUSTOMER)]), " +
                "addressDTO=AddressDTO(id=26, street=change, number=eee, state=change)), " +
                "userDTO=UserDTO(id=4, firstname=Alejo, lastname=Maya, email=alejo@mail.com, " +
                "registrationDate=2024-04-21, isEnabled=true, roles=[RoleDTO(id=1, name=ROLE_CUSTOMER)]))})";
        assertEquals(expected, reservationDTOResponseReadById.toString());

    }

    @Test
    @Order(4)
    void delete() {

        String expected = "ResponseDTO(statusCode=200, message=Reservation deleted succesfully., " +
                "data={Reservation=ReservationDTO(id=" + reservationID + ", date=2024-04-25, time=14:30:00, " +
                "activityDTO=ActivityDTO(id=7, price=300, maxPeople=500, " +
                "disciplineDTO=DisciplineDTO(id=3, name=Futbol, type=SPACE_RENTAL), " +
                "userDTO=UserDTO(id=3, firstname=Alejo, lastname=Maya, email=alejo@mail.com, " +
                "registrationDate=2024-04-21, isEnabled=true, roles=[RoleDTO(id=1, name=ROLE_CUSTOMER)]), " +
                "addressDTO=AddressDTO(id=26, street=change, number=eee, state=change)), " +
                "userDTO=UserDTO(id=4, firstname=Alejo, lastname=Maya, email=alejo@mail.com, " +
                "registrationDate=2024-04-21, isEnabled=true, roles=[RoleDTO(id=1, name=ROLE_CUSTOMER)]))})";
        assertEquals(expected, reservationDTOResponseDelete.toString());

    }

    @Test
    @Order(5)
    void readByIdWithException() {
        assertThrows(ResourceNotFoundException.class, () -> reservationService.readById(reservationID));
    }


}
