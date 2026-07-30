package com.doctor.appointment.controller;


import com.doctor.appointment.service.DoctorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/doctor")
@RequiredArgsConstructor
@Slf4j
public class DoctorController {

    private final DoctorService doctorService;

}
