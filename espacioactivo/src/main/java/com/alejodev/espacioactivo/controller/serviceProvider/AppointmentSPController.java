package com.alejodev.espacioactivo.controller.serviceProvider;

import com.alejodev.espacioactivo.dto.AppointmentDTO;
import com.alejodev.espacioactivo.service.impl.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/api/v1/service-provider/appointment")
public class AppointmentSPController {

    @Autowired
    AppointmentService appointmentService;


    @PostMapping("/create")
    public ResponseEntity<?> createAppointment(@RequestBody AppointmentDTO appointmentDTO) {
        return ResponseEntity.ok(appointmentService.createByServiceProvider(appointmentDTO));
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllAppointments(){
        return ResponseEntity.ok(appointmentService.readAllByServiceProvider());
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateAppointment(@RequestBody AppointmentDTO appointmentDTO) {
        return ResponseEntity.ok(appointmentService.updateByServiceProvider(appointmentDTO));
    }

}
