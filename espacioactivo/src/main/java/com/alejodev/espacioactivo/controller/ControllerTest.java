package com.alejodev.espacioactivo.controller;

import com.alejodev.espacioactivo.dto.DisciplineDTO;
import com.alejodev.espacioactivo.service.impl.DisciplineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ControllerTest {

    @Autowired
    DisciplineService disciplineService;

    @PostMapping("/discipline/create")
    public ResponseEntity<?> createDiscipline(@RequestBody DisciplineDTO disciplineDTO) {
        return ResponseEntity.ok(disciplineService.create(disciplineDTO));
    }

    @GetMapping("/discipline/{id}")
    public ResponseEntity<?> getDiscipline(@PathVariable Long id){
        return ResponseEntity.ok(disciplineService.readById(id));
    }

    @GetMapping("/discipline/all")
    public ResponseEntity<?> getAllDisciplines(){
        return ResponseEntity.ok(disciplineService.readAll());
    }

    @PutMapping("/discipline/update")
    public ResponseEntity<?> updateDiscipline(@RequestBody DisciplineDTO disciplineDTO){
        return ResponseEntity.ok(disciplineService.update(disciplineDTO));
    }

    @DeleteMapping("/discipline/delete/{id}")
    public ResponseEntity<?> deleteDiscipline(@PathVariable Long id){
        return ResponseEntity.ok(disciplineService.delete(id));
    }

}
