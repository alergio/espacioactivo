package com.alejodev.espacioactivo.controller.coach;

import com.alejodev.espacioactivo.dto.ActivityDTO;
import com.alejodev.espacioactivo.service.impl.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/api/v1/coach/activity")
public class ActivityCoachController {

    @Autowired
    ActivityService activityService;

    @PostMapping("/create")
    public ResponseEntity<?> createActivity(@RequestBody ActivityDTO activityDTO) {
        return ResponseEntity.ok(activityService.create(activityDTO));
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllActivities(){
        return ResponseEntity.ok(activityService.readAllByCoach());
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateActivity(@RequestBody ActivityDTO activityDTO) {
        return ResponseEntity.ok(activityService.updateByCoach(activityDTO));
    }


}
