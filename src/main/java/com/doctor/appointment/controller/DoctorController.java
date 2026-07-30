package com.doctor.appointment.controller;


import com.doctor.appointment.dto.AppointmentRequest;
import com.doctor.appointment.dto.AppointmentResponse;
import com.doctor.appointment.service.DoctorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/doctor")
@RequiredArgsConstructor
@Slf4j
public class DoctorController {

    private final DoctorService doctorService;


    // ========================================
    // 1. اضافه کردن نوبت‌های جدید
    // POST /api/doctor/appointments
    // ========================================

    @PostMapping("/appointments")
    public ResponseEntity<List<AppointmentResponse>> addAppointments(
            @Valid @RequestBody AppointmentRequest request) {

        log.info("REST request to add appointments: {}", request);
        List<AppointmentResponse> appointments = doctorService.addAppointments(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(appointments);
    }

}
