package com.doctor.appointment.service;

import com.doctor.appointment.dto.DoctorRequest;
import com.doctor.appointment.dto.DoctorResponse;

import java.util.List;

public interface DoctorService {
    // ========================================
    // ذخیره پزشک جدید
    // ========================================
    DoctorResponse saveDoctor(DoctorRequest request);

    // ========================================
    // دریافت همه پزشکان
    // ========================================
    List<DoctorResponse> getAllDoctors();

    // ========================================
    // دریافت پزشک با ID
    // ========================================
    DoctorResponse getDoctorById(Long id);

    // ========================================
    // دریافت پزشک با کد ملی
    // ========================================
    DoctorResponse getDoctorByNationalCode(String nationalCode);

    // ========================================
    // دریافت پزشک با ایمیل
    // ========================================
    DoctorResponse getDoctorByEmail(String email);

    // ========================================
    // جستجوی پزشکان با نام
    // ========================================
    List<DoctorResponse> searchDoctorsByName(String keyword);

    // ========================================
    // دریافت پزشکان بر اساس تخصص
    // ========================================
    List<DoctorResponse> getDoctorsBySpecialty(String specialty);

    // ========================================
    // به‌روزرسانی پزشک
    // ========================================
    DoctorResponse updateDoctor(Long id, DoctorRequest request);

    // ========================================
    // حذف پزشک
    // ========================================
    void deleteDoctor(Long id);

}
