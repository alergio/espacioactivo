package com.alejodev.espacioactivo.controller.serviceProvider;

import com.alejodev.espacioactivo.dto.ActivityDTO;
import com.alejodev.espacioactivo.service.impl.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/api/v1/service-provider/activity")
public class ActivitySPController {

    @Autowired
    ActivityService activityService;


    @PostMapping("/create")
    public ResponseEntity<?> createActivity(@RequestBody ActivityDTO activityDTO) {
        return ResponseEntity.ok(activityService.createByServiceProvider(activityDTO));
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllActivities(){
        return ResponseEntity.ok(activityService.readAllByServiceProvider());
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateActivity(@RequestBody ActivityDTO activityDTO) {
        return ResponseEntity.ok(activityService.updateByServiceProvider(activityDTO));
    }
    

}
