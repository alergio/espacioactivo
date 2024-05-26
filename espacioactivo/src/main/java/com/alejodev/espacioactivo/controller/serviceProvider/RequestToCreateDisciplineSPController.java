package com.alejodev.espacioactivo.controller.serviceProvider;

import com.alejodev.espacioactivo.dto.ActivityDTO;
import com.alejodev.espacioactivo.dto.RequestToCreateDisciplineDTO;
import com.alejodev.espacioactivo.service.impl.RequestToCreateDisciplineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/api/v1/service-provider/request-new-discipline")
public class RequestToCreateDisciplineSPController {

    @Autowired
    RequestToCreateDisciplineService requestToCreateDisciplineService;

    @PostMapping("/create")
    public ResponseEntity<?> createRequest(@RequestBody RequestToCreateDisciplineDTO requestToCreateDisciplineDTO) {
        return ResponseEntity.ok(requestToCreateDisciplineService.createByServiceProvider(requestToCreateDisciplineDTO));
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllRequests(){
        return ResponseEntity.ok(requestToCreateDisciplineService.readAllByServiceProvider());
    }

}
