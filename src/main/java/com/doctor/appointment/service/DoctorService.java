package com.doctor.appointment.service;

import com.doctor.appointment.dto.AppointmentRequest;
import com.doctor.appointment.dto.AppointmentResponse;

import java.util.List;

public interface DoctorService {

    List<AppointmentResponse> addAppointments(AppointmentRequest request);
}
