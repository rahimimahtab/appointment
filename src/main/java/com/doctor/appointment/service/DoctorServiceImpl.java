package com.doctor.appointment.service;

import com.doctor.appointment.dto.AppointmentRequest;
import com.doctor.appointment.dto.AppointmentResponse;
import com.doctor.appointment.model.DoctorEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorServiceImpl implements DoctorService {
    @Override
    public List<AppointmentResponse> addAppointments(AppointmentRequest request) {
        return List.of();
    }
}
