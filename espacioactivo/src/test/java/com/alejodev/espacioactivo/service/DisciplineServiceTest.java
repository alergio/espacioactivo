package com.alejodev.espacioactivo.service;

import com.alejodev.espacioactivo.dto.DisciplineDTO;
import com.alejodev.espacioactivo.dto.ResponseDTO;
import com.alejodev.espacioactivo.exception.ResourceNotFoundException;
import com.alejodev.espacioactivo.service.impl.DisciplineService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DisciplineServiceTest {


    @Autowired
    private DisciplineService disciplineService;

    private ResponseDTO disciplineDTOResponseCreate;
    private ResponseDTO disciplineDTOResponseReadById;
    private ResponseDTO disciplineDTOResponseUpdate;
    private ResponseDTO disciplineDTOResponseDelete;

    private Long disciplineID;
    private DisciplineDTO disciplineDTOSaved;


    @BeforeAll
    void setUpData() {

        DisciplineDTO disciplineDTOForCreate = new DisciplineDTO();
        DisciplineDTO disciplineDTOForUpdate = new DisciplineDTO();

        disciplineDTOForCreate.setName("Futbol");
        disciplineDTOForCreate.setType("SPACE_RENTAL");

        disciplineDTOResponseCreate = disciplineService.create(disciplineDTOForCreate);

        disciplineDTOSaved = (DisciplineDTO) disciplineDTOResponseCreate.getData().get("Discipline");
        disciplineID = disciplineDTOSaved.getId();

        disciplineDTOForUpdate.setId(disciplineID);
        disciplineDTOForUpdate.setName("Tennis");
        disciplineDTOForUpdate.setType("GROUP_CLASS");

        disciplineDTOResponseUpdate = disciplineService.update(disciplineDTOForUpdate);
        disciplineDTOResponseReadById = disciplineService.readById(disciplineID);
        disciplineDTOResponseDelete = disciplineService.delete(disciplineID);

    }

    @Test
    @Order(1)
    void create() {

        String expected = "ResponseDTO(statusCode=200, message=Discipline saved successfully., " +
                "data={Discipline=DisciplineDTO(id=" + disciplineID + ", name=Futbol, type=SPACE_RENTAL)})";
        assertEquals(expected, disciplineDTOResponseCreate.toString());

    }

    @Test
    @Order(2)
    void update() {

        String expected = "ResponseDTO(statusCode=200, message=Discipline updated successfully." +
                ", data={Discipline=DisciplineDTO(id=" + disciplineID + ", name=Tennis, type=GROUP_CLASS)})";
        assertEquals(expected, disciplineDTOResponseUpdate.toString());

    }

    @Test
    @Order(3)
    void readById() {

        String expected = "ResponseDTO(statusCode=200, message=Discipline successfully found.," +
                " data={Discipline=DisciplineDTO(id=" + disciplineID + ", name=Tennis, type=GROUP_CLASS)})";
        assertEquals(expected, disciplineDTOResponseReadById.toString());

    }

    @Test
    @Order(4)
    void delete() {

        String expected = "ResponseDTO(statusCode=200, message=Discipline deleted succesfully.," +
                " data={Discipline=DisciplineDTO(id=" + disciplineID + ", name=Tennis, type=GROUP_CLASS)})";
        assertEquals(expected, disciplineDTOResponseDelete.toString());

    }

    @Test
    @Order(5)
    void readByIdWithException() {
        assertThrows(ResourceNotFoundException.class, () -> disciplineService.readById(disciplineID));
    }

}
