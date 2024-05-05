package com.alejodev.espacioactivo.config;

import com.alejodev.espacioactivo.service.impl.AppointmentService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class SchedulerConfig {

    @Autowired
    AppointmentService appointmentService;

    @Scheduled(cron = "0 0 * * * *")
    public void updateAppointments() throws InterruptedException {
        appointmentService.checkAppointmentsToMarkAsExpired();
    }

}
