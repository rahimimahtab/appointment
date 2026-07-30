package com.doctor.appointment.service;

import com.doctor.appointment.dto.AppointmentRequest;
import com.doctor.appointment.dto.AppointmentResponse;
import com.doctor.appointment.model.AppointmentEntity;
import com.doctor.appointment.model.AppointmentStatus;
import com.doctor.appointment.model.DoctorEntity;
import com.doctor.appointment.repository.AppointmentRepository;
import com.doctor.appointment.repository.DoctorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;

    public DoctorServiceImpl(DoctorRepository doctorRepository, AppointmentRepository appointmentRepository) {
        this.doctorRepository = doctorRepository;
        this.appointmentRepository = appointmentRepository;
    }

    // ========================================
    // 1. اضافه کردن نوبت‌ها (با تقسیم به بازه‌های ۳۰ دقیقه‌ای)
    // ========================================

    @Override
    public List<AppointmentResponse> addAppointments(AppointmentRequest request) {
        log.info("Adding appointments: start={}, end={}", request.getStartTime(), request.getEndTime());

        // Test Case 1: بررسی اینکه تاریخ شروع از پایان زودتر باشد
//        if (request.getStartTime().isAfter(request.getEndTime())) {
//            throw new InvalidTimeRangeException("End date must be after start date");
//        }

        // Test Case 2: اگر بازه کمتر از ۳۰ دقیقه باشد، هیچ نوبتی اضافه نشود
//        if (request.getStartTime().plusMinutes(30).isAfter(request.getEndTime())) {
//            log.warn("Time period is less than 30 minutes, no appointments added");
//            return new ArrayList<>();
//        }

        List<AppointmentEntity> createdAppointments = new ArrayList<>();
        LocalDateTime current = request.getStartTime();

        // تقسیم بازه به بازه‌های ۳۰ دقیقه‌ای
        while (current.isBefore(request.getEndTime())) {
            LocalDateTime slotEnd = current.plusMinutes(30);

            // اگر بازه باقی‌مانده کمتر از ۳۰ دقیقه است، نادیده بگیر
            if (slotEnd.isAfter(request.getEndTime())) {
                break;
            }

            // بررسی نکنه قبلاً نوبتی در این بازه وجود ندارد
            if (!appointmentRepository.existsByTimeRange(current, slotEnd)) {
                AppointmentEntity appointment = AppointmentEntity.builder()
                        .startTime(current)
                        .endTime(slotEnd)
                        .status(AppointmentStatus.OPEN)
                        .build();

                AppointmentEntity saved = appointmentRepository.save(appointment);
                createdAppointments.add(saved);
                log.debug("Created appointment: {} - {}", current, slotEnd);
            } else {
                log.warn("Appointment already exists: {} - {}", current, slotEnd);
            }

            current = slotEnd;
        }

        log.info("Created {} appointments", createdAppointments.size());
        return createdAppointments.stream()
                .map(AppointmentResponse::fromEntity)
                .toList();
    }
}
