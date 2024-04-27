package com.alejodev.espacioactivo.service;

import com.alejodev.espacioactivo.service.impl.AppointmentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestForTest {
//    @Autowired
//    private IAppointmentRepository appointmentRepository;
//
    @Autowired
    private AppointmentService appointmentService;
    @Test
    void test(){
//
//        List<Appointment> appointmentList = appointmentRepository.findNonExpiredAppointmentsToMarkThemExpired();
//
//        boolean isEmpty = appointmentList.isEmpty();
//

        appointmentService.checkAppointmentsToMarkAsExpired();

    }

}
