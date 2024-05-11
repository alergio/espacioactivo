package com.alejodev.espacioactivo.controller.coach;

import com.alejodev.espacioactivo.dto.ActivityDTO;
import com.alejodev.espacioactivo.service.impl.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/api/v1/service-provider/activity")
public class ActivityServiceProviderController {

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

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteActivity(@PathVariable Long id) {
        return ResponseEntity.ok(activityService.deleteByServiceProvider(id));
    }


}
