package com.alejodev.espacioactivo.controller;

import com.alejodev.espacioactivo.dto.ActivityDTO;
import com.alejodev.espacioactivo.dto.AppointmentDTO;
import com.alejodev.espacioactivo.dto.DisciplineDTO;
import com.alejodev.espacioactivo.dto.ReservationDTO;
import com.alejodev.espacioactivo.service.impl.ActivityService;
import com.alejodev.espacioactivo.service.impl.AppointmentService;
import com.alejodev.espacioactivo.service.impl.DisciplineService;
import com.alejodev.espacioactivo.service.impl.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ControllerTest {

    @Autowired
    DisciplineService disciplineService;
    @Autowired
    ActivityService activityService;
    @Autowired
    ReservationService reservationService;
    @Autowired
    AppointmentService appointmentService;



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





    @PostMapping("/activity/create")
    public ResponseEntity<?> createActivity(@RequestBody ActivityDTO activityDTO) {
        return ResponseEntity.ok(activityService.create(activityDTO));
    }
    @GetMapping("/activity/{id}")
    public ResponseEntity<?> getActivity(@PathVariable Long id){
        return ResponseEntity.ok(activityService.readById(id));
    }
    @GetMapping("/activity/all")
    public ResponseEntity<?> getAllActivities(){
        return ResponseEntity.ok(activityService.readAll());
    }
    @PutMapping("/activity/update")
    public ResponseEntity<?> updateActivity(@RequestBody ActivityDTO activityDTO){
        return ResponseEntity.ok(activityService.update(activityDTO));
    }
    @DeleteMapping("/activity/delete/{id}")
    public ResponseEntity<?> deleteActivity(@PathVariable Long id){
        return ResponseEntity.ok(activityService.delete(id));
    }




    @PostMapping("/reservation/create")
    public ResponseEntity<?> createReservation(@RequestBody ReservationDTO reservationDTO) {
        return ResponseEntity.ok(reservationService.create(reservationDTO));
    }
    @GetMapping("/reservation/{id}")
    public ResponseEntity<?> getReservation(@PathVariable Long id){
        return ResponseEntity.ok(reservationService.readById(id));
    }
    @GetMapping("/reservation/all")
    public ResponseEntity<?> getAllReservations(){
        return ResponseEntity.ok(reservationService.readAll());
    }
    @PutMapping("/reservation/update")
    public ResponseEntity<?> updateReservation(@RequestBody ReservationDTO reservationDTO){
        return ResponseEntity.ok(reservationService.update(reservationDTO));
    }
    @DeleteMapping("/reservation/delete/{id}")
    public ResponseEntity<?> deleteReservation(@PathVariable Long id){
        return ResponseEntity.ok(reservationService.delete(id));
    }



    @PostMapping("/appointment/create")
    public ResponseEntity<?> createAppointment(@RequestBody AppointmentDTO appointmentDTO) {
        return ResponseEntity.ok(appointmentService.create(appointmentDTO));
    }
    @GetMapping("/appointment/{id}")
    public ResponseEntity<?> getAppointment(@PathVariable Long id){
        return ResponseEntity.ok(appointmentService.readById(id));
    }
    @GetMapping("/appointment/all")
    public ResponseEntity<?> getAllAppointments(){
        return ResponseEntity.ok(appointmentService.readAll());
    }
    @PutMapping("/appointment/update")
    public ResponseEntity<?> updateAppointment(@RequestBody AppointmentDTO appointmentDTO){
        return ResponseEntity.ok(appointmentService.update(appointmentDTO));
    }
    @DeleteMapping("/appointment/delete/{id}")
    public ResponseEntity<?> deleteAppointment(@PathVariable Long id){
        return ResponseEntity.ok(appointmentService.delete(id));
    }




}
