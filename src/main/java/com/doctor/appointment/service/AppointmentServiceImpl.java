package com.doctor.appointment.service;

import com.doctor.appointment.dto.AppointmentRequest;
import com.doctor.appointment.dto.AppointmentResponse;
import com.doctor.appointment.dto.DailyAppointmentsResponse;
import com.doctor.appointment.exception.AppointmentNotFoundException;
import com.doctor.appointment.exception.AppointmentNotOpenException;
import com.doctor.appointment.exception.DoctorNotFoundException;
import com.doctor.appointment.exception.InvalidTimeRangeException;
import com.doctor.appointment.model.AppointmentEntity;
import com.doctor.appointment.model.AppointmentStatus;
import com.doctor.appointment.model.DoctorEntity;
import com.doctor.appointment.repository.AppointmentRepository;
import com.doctor.appointment.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppointmentServiceImpl implements AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;

    // ========================================
    // 1. اضافه کردن نوبت‌ها (با تقسیم به بازه‌های ۳۰ دقیقه‌ای)
    // ========================================

    @Override
    public List<AppointmentResponse> addAppointments(AppointmentRequest request) {
        log.info("Adding appointments: start={}, end={}", request.getStartTime(), request.getEndTime());

//         Test Case 1: بررسی اینکه تاریخ شروع از پایان زودتر باشد
        if (request.getStartTime().isAfter(request.getEndTime())) {
            throw new InvalidTimeRangeException("End date must be after start date");
        }
        // Test Case 2: اگر بازه کمتر از ۳۰ دقیقه باشد، هیچ نوبتی اضافه نشود
        if (request.getStartTime().plusMinutes(30).isAfter(request.getEndTime())) {
            log.warn("Time period is less than 30 minutes, no appointments added");
            return new ArrayList<>();
        }

        Optional<DoctorEntity> doctorEntity = doctorRepository.findByNationalCode(request.getNationalCode());
        if (doctorEntity.isEmpty()){
            throw new DoctorNotFoundException("national code is invalid");
        }




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
                        .doctor(doctorEntity.get())
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


    // ========================================
    // 2. مشاهده نوبت‌های روزانه (باز و رزرو شده)
    // ========================================

    @Override
    @Transactional(readOnly = true)
    public DailyAppointmentsResponse getDailyAppointments(LocalDate date) {
        log.info("Getting daily appointments for date: {}", date);

        List<AppointmentEntity> openAppointments = appointmentRepository.findOpenAppointmentsByDate(date);
        List<AppointmentEntity> takenAppointments = appointmentRepository.findByDateAndStatus(date, AppointmentStatus.TAKEN);

        return DailyAppointmentsResponse.fromEntities(date, openAppointments, takenAppointments);
    }

    // ========================================
    // 3. مشاهده نوبت‌های باز
    // ========================================

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentResponse> getOpenAppointments(LocalDate date) {
        log.info("Getting open appointments for date: {}", date);

        List<AppointmentEntity> openAppointments = appointmentRepository.findOpenAppointmentsByDate(date);
        return openAppointments.stream()
                .map(AppointmentResponse::fromEntity)
                .toList();
    }

    // ========================================
    // 4. مشاهده نوبت‌های رزرو شده
    // ========================================

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentResponse> getTakenAppointments(LocalDate date) {
        log.info("Getting taken appointments for date: {}", date);

        List<AppointmentEntity> takenAppointments = appointmentRepository.findByDateAndStatus(date, AppointmentStatus.TAKEN);
        return takenAppointments.stream()
                .map(AppointmentResponse::fromEntity)
                .toList();
    }


    // ========================================
    // 6. مشاهده همه نوبت‌ها
    // ========================================

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentResponse> getAllAppointments() {
        log.info("Getting all appointments");

        List<AppointmentEntity> appointments = appointmentRepository.findAll();
        return appointments.stream()
                .map(AppointmentResponse::fromEntity)
                .sorted((a1, a2) -> a1.getStartTime().compareTo(a2.getStartTime()))
                .toList();
    }

    // ========================================
    // 5. حذف نوبت باز (با مدیریت هم‌زمانی)
    // ========================================

    @Override
    public void deleteOpenAppointment(Long appointmentId) {
        log.info("Deleting open appointment with id: {}", appointmentId);

        try {
            // Test Case 1: اگر نوبت باز وجود نداشته باشد، خطای ۴۰۴
            AppointmentEntity appointment = appointmentRepository.findOpenAppointmentById(appointmentId)
                    .orElseThrow(() -> new AppointmentNotFoundException(appointmentId));

            // Test Case 2: اگر نوبت توسط بیمار رزرو شده باشد، خطای ۴۰۶
            if (AppointmentStatus.TAKEN.getStatusCode().equals(appointment.getStatus().getStatusCode())) {
                throw new AppointmentNotOpenException("Appointment with id " + appointmentId + " is already taken");
            }

            // حذف نوبت
            appointmentRepository.delete(appointment);
            log.info("Successfully deleted appointment with id: {}", appointmentId);

        } catch (OptimisticLockingFailureException e) {
            // Test Case 3: بررسی هم‌زمانی
            log.error("Concurrency conflict while deleting appointment with id: {}", appointmentId);
            throw new RuntimeException("Appointment is being modified by another user. Please try again.");
        }
    }
}
