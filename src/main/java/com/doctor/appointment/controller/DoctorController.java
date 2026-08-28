package com.doctor.appointment.controller;

import com.doctor.appointment.dto.DoctorRequest;
import com.doctor.appointment.dto.DoctorResponse;
import com.doctor.appointment.service.DoctorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
@Slf4j
public class DoctorController {

    private final DoctorService doctorService;

    // ========================================
    // 1. ثبت پزشک جدید
    // POST /api/doctors
    // ========================================

    @PostMapping
    public ResponseEntity<DoctorResponse> createDoctor(
            @Valid @RequestBody DoctorRequest request) {

        log.info("REST request to create doctor");
        DoctorResponse response = doctorService.saveDoctor(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ========================================
    // 2. دریافت همه پزشکان
    // GET /api/doctors
    // ========================================

    @GetMapping
    public ResponseEntity<List<DoctorResponse>> getAllDoctors() {

        log.info("REST request to get all doctors");
        List<DoctorResponse> doctors = doctorService.getAllDoctors();
        return ResponseEntity.ok(doctors);
    }

    // ========================================
    // 3. دریافت پزشک با ID
    // GET /api/doctors/{id}
    // ========================================

    @GetMapping("/{id}")
    public ResponseEntity<DoctorResponse> getDoctorById(@PathVariable Long id) {

        log.info("REST request to get doctor by id: {}", id);
        DoctorResponse doctor = doctorService.getDoctorById(id);
        return ResponseEntity.ok(doctor);
    }

    // ========================================
    // 4. دریافت پزشک با کد ملی
    // GET /api/doctors/national-code/{nationalCode}
    // ========================================

    @GetMapping("/national-code/{nationalCode}")
    public ResponseEntity<DoctorResponse> getDoctorByNationalCode(
            @PathVariable String nationalCode) {

        log.info("REST request to get doctor by national code: {}", nationalCode);
        DoctorResponse doctor = doctorService.getDoctorByNationalCode(nationalCode);
        return ResponseEntity.ok(doctor);
    }

    // ========================================
    // 5. دریافت پزشک با ایمیل
    // GET /api/doctors/email/{email}
    // ========================================

    @GetMapping("/email/{email}")
    public ResponseEntity<DoctorResponse> getDoctorByEmail(
            @PathVariable String email) {

        log.info("REST request to get doctor by email: {}", email);
        DoctorResponse doctor = doctorService.getDoctorByEmail(email);
        return ResponseEntity.ok(doctor);
    }

    // ========================================
    // 6. جستجوی پزشکان با نام
    // GET /api/doctors/search?keyword=علی
    // ========================================

    @GetMapping("/search")
    public ResponseEntity<List<DoctorResponse>> searchDoctors(
            @RequestParam String keyword) {

        log.info("REST request to search doctors by keyword: {}", keyword);
        List<DoctorResponse> doctors = doctorService.searchDoctorsByName(keyword);
        return ResponseEntity.ok(doctors);
    }

    // ========================================
    // 7. دریافت پزشکان بر اساس تخصص
    // GET /api/doctors/specialty/{specialty}
    // ========================================

    @GetMapping("/specialty/{specialty}")
    public ResponseEntity<List<DoctorResponse>> getDoctorsBySpecialty(
            @PathVariable String specialty) {

        log.info("REST request to get doctors by specialty: {}", specialty);
        List<DoctorResponse> doctors = doctorService.getDoctorsBySpecialty(specialty);
        return ResponseEntity.ok(doctors);
    }

    // ========================================
    // 8. به‌روزرسانی پزشک
    // PUT /api/doctors/{id}
    // ========================================

    @PutMapping("/{id}")
    public ResponseEntity<DoctorResponse> updateDoctor(
            @PathVariable Long id,
            @Valid @RequestBody DoctorRequest request) {

        log.info("REST request to update doctor with id: {}", id);
        DoctorResponse doctor = doctorService.updateDoctor(id, request);
        return ResponseEntity.ok(doctor);
    }

    // ========================================
    // 9. حذف پزشک
    // DELETE /api/doctors/{id}
    // ========================================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDoctor(@PathVariable Long id) {

        log.info("REST request to delete doctor with id: {}", id);
        doctorService.deleteDoctor(id);
        return ResponseEntity.noContent().build();
    }
}
