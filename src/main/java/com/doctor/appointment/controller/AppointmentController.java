package com.doctor.appointment.controller;


import com.doctor.appointment.dto.AppointmentRequest;
import com.doctor.appointment.dto.AppointmentResponse;
import com.doctor.appointment.dto.DailyAppointmentsResponse;
import com.doctor.appointment.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
@Slf4j
public class AppointmentController {
    private final AppointmentService appointmentService;


    // ========================================
    // 1. اضافه کردن نوبت‌های جدید
    // POST /api/doctor/appointments
    // ========================================

    @PostMapping("/register")
    public ResponseEntity<List<AppointmentResponse>> addAppointments(
            @Valid @RequestBody AppointmentRequest request) {

        log.info("REST request to add appointments: {}", request);
        List<AppointmentResponse> appointments = appointmentService.addAppointments(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(appointments);
    }

    // ========================================
    // 2. مشاهده نوبت‌های روزانه (باز و رزرو شده)
    // GET /api/doctor/appointments/daily?date=2026-07-30
    // ========================================

    @GetMapping("/daily")
    public ResponseEntity<DailyAppointmentsResponse> getDailyAppointments(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        log.info("REST request to get daily appointments for date: {}", date);
        DailyAppointmentsResponse response = appointmentService.getDailyAppointments(date);
        return ResponseEntity.ok(response);
    }

    // ========================================
    // 3. مشاهده نوبت‌های باز
    // GET /api/doctor/appointments/open?date=2026-07-30
    // ========================================

    @GetMapping("/open")
    public ResponseEntity<List<AppointmentResponse>> getOpenAppointments(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        log.info("REST request to get open appointments for date: {}", date);
        List<AppointmentResponse> appointments = appointmentService.getOpenAppointments(date);
        return ResponseEntity.ok(appointments);
    }


    // ========================================
    // 4. مشاهده نوبت‌های رزرو شده
    // GET /api/doctor/appointments/taken?date=2026-07-30
    // ========================================

    @GetMapping("/taken")
    public ResponseEntity<List<AppointmentResponse>> getTakenAppointments(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        log.info("REST request to get taken appointments for date: {}", date);
        List<AppointmentResponse> appointments = appointmentService.getTakenAppointments(date);
        return ResponseEntity.ok(appointments);
    }

    // ========================================
    // 5. حذف نوبت باز
    // DELETE /api/doctor/appointments/{id}
    // ========================================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOpenAppointment(@PathVariable Long id) {

        log.info("REST request to delete open appointment with id: {}", id);
        appointmentService.deleteOpenAppointment(id);
        return ResponseEntity.noContent().build();
    }

    // ========================================
    // 6. مشاهده همه نوبت‌ها (بدون فیلتر)
    // GET /api/doctor/appointments/all
    // ========================================

    @GetMapping("/all")
    public ResponseEntity<List<AppointmentResponse>> getAllAppointments() {

        log.info("REST request to get all appointments");
        List<AppointmentResponse> appointments = appointmentService.getAllAppointments();
        return ResponseEntity.ok(appointments);
    }
}
