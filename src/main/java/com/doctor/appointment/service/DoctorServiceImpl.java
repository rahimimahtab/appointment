package com.doctor.appointment.service;


import com.doctor.appointment.dto.DoctorRequest;
import com.doctor.appointment.dto.DoctorResponse;
import com.doctor.appointment.exception.DoctorAlreadyExistsException;
import com.doctor.appointment.exception.DoctorNotFoundException;
import com.doctor.appointment.model.DoctorEntity;
import com.doctor.appointment.repository.AppointmentRepository;
import com.doctor.appointment.repository.DoctorRepository;
import com.doctor.appointment.service.DoctorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@Transactional
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;

    public DoctorServiceImpl(DoctorRepository doctorRepository, AppointmentRepository appointmentRepository) {
        this.doctorRepository = doctorRepository;
        this.appointmentRepository = appointmentRepository;
    }

    // ========================================
    // 1. ذخیره پزشک جدید
    // ========================================

    @Override
    public DoctorResponse saveDoctor(DoctorRequest request) {
        log.info("Saving new doctor: {} {}", request.getFirstName(), request.getLastName());

        // بررسی یکتایی کد ملی
        if (doctorRepository.existsByNationalCode(request.getNationalCode())) {
            throw new DoctorAlreadyExistsException("nationalCode", request.getNationalCode());
        }

        // بررسی یکتایی ایمیل (اگر وارد شده باشد)
        if (request.getEmail() != null && !request.getEmail().isBlank()) {
            if (doctorRepository.existsByEmail(request.getEmail())) {
                throw new DoctorAlreadyExistsException("email", request.getEmail());
            }
        }

        // ساخت Entity جدید
        DoctorEntity doctor = DoctorEntity.builder()
                .nationalCode(request.getNationalCode())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .specialty(request.getSpecialty())
                .email(request.getEmail())
                .phone(request.getPhone())
                .build();

        // ذخیره در دیتابیس
        DoctorEntity savedDoctor = doctorRepository.save(doctor);
        log.info("Doctor saved with id: {}", savedDoctor.getId());

        return DoctorResponse.fromEntity(savedDoctor);
    }

    // ========================================
    // 2. دریافت همه پزشکان
    // ========================================

    @Override
    @Transactional(readOnly = true)
    public List<DoctorResponse> getAllDoctors() {
        log.info("Getting all doctors");

        List<DoctorEntity> doctors = doctorRepository.findAllSortedByName();
        return doctors.stream()
                .map(DoctorResponse::fromEntity)
                .toList();
    }

    // ========================================
    // 3. دریافت پزشک با ID
    // ========================================

    @Override
    @Transactional(readOnly = true)
    public DoctorResponse getDoctorById(Long id) {
        log.info("Getting doctor by id: {}", id);

        DoctorEntity doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new DoctorNotFoundException(id));

        return DoctorResponse.fromEntity(doctor);
    }

    // ========================================
    // 4. دریافت پزشک با کد ملی
    // ========================================

    @Override
    @Transactional(readOnly = true)
    public DoctorResponse getDoctorByNationalCode(String nationalCode) {
        log.info("Getting doctor by national code: {}", nationalCode);

        DoctorEntity doctor = doctorRepository.findByNationalCode(nationalCode)
                .orElseThrow(() -> new DoctorNotFoundException("nationalCode", nationalCode));

        return DoctorResponse.fromEntity(doctor);
    }

    // ========================================
    // 5. دریافت پزشک با ایمیل
    // ========================================

    @Override
    @Transactional(readOnly = true)
    public DoctorResponse getDoctorByEmail(String email) {
        log.info("Getting doctor by email: {}", email);

        DoctorEntity doctor = doctorRepository.findByEmail(email)
                .orElseThrow(() -> new DoctorNotFoundException("email", email));

        return DoctorResponse.fromEntity(doctor);
    }

    // ========================================
    // 6. جستجوی پزشکان با نام
    // ========================================

    @Override
    @Transactional(readOnly = true)
    public List<DoctorResponse> searchDoctorsByName(String keyword) {
        log.info("Searching doctors by keyword: {}", keyword);

        List<DoctorEntity> doctors = doctorRepository.searchByName(keyword);
        return doctors.stream()
                .map(DoctorResponse::fromEntity)
                .toList();
    }

    // ========================================
    // 7. دریافت پزشکان بر اساس تخصص
    // ========================================

    @Override
    @Transactional(readOnly = true)
    public List<DoctorResponse> getDoctorsBySpecialty(String specialty) {
        log.info("Getting doctors by specialty: {}", specialty);

        List<DoctorEntity> doctors = doctorRepository.findBySpecialty(specialty);
        return doctors.stream()
                .map(DoctorResponse::fromEntity)
                .toList();
    }

    // ========================================
    // 8. به‌روزرسانی پزشک
    // ========================================

    @Override
    public DoctorResponse updateDoctor(Long id, DoctorRequest request) {
        log.info("Updating doctor with id: {}", id);

        // پیدا کردن پزشک
        DoctorEntity doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new DoctorNotFoundException(id));

        // بررسی کد ملی (اگر تغییر کرده باشد)
        if (!doctor.getNationalCode().equals(request.getNationalCode()) &&
                doctorRepository.existsByNationalCode(request.getNationalCode())) {
            throw new DoctorAlreadyExistsException("nationalCode", request.getNationalCode());
        }

        // بررسی ایمیل (اگر تغییر کرده باشد)
        if (request.getEmail() != null && !request.getEmail().isBlank() &&
                !doctor.getEmail().equals(request.getEmail()) &&
                doctorRepository.existsByEmail(request.getEmail())) {
            throw new DoctorAlreadyExistsException("email", request.getEmail());
        }

        // به‌روزرسانی فیلدها
        doctor.setNationalCode(request.getNationalCode());
        doctor.setFirstName(request.getFirstName());
        doctor.setLastName(request.getLastName());
        doctor.setSpecialty(request.getSpecialty());
        doctor.setEmail(request.getEmail());
        doctor.setPhone(request.getPhone());

        DoctorEntity updatedDoctor = doctorRepository.save(doctor);
        log.info("Doctor updated with id: {}", updatedDoctor.getId());

        return DoctorResponse.fromEntity(updatedDoctor);
    }

    // ========================================
    // 9. حذف پزشک
    // ========================================

    @Override
    public void deleteDoctor(Long id) {
        log.info("Deleting doctor with id: {}", id);

        DoctorEntity doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new DoctorNotFoundException(id));

        doctorRepository.delete(doctor);
        log.info("Doctor deleted with id: {}", id);
    }
}
