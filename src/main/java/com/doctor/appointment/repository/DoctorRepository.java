package com.doctor.appointment.repository;

import com.doctor.appointment.model.DoctorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<DoctorEntity,Long> {
    // ========================================
    // Find by National Code (کد ملی - یکتا)
    // ========================================
    Optional<DoctorEntity> findByNationalCode(String nationalCode);

    // ========================================
    // Check if National Code exists
    // ========================================
    boolean existsByNationalCode(String nationalCode);

    // ========================================
    // Find by Email
    // ========================================
    Optional<DoctorEntity> findByEmail(String email);

    // ========================================
    // Check if Email exists
    // ========================================
    boolean existsByEmail(String email);

    // ========================================
    // Find by Phone
    // ========================================
    Optional<DoctorEntity> findByPhone(String phone);

    // ========================================
    // Search by First Name or Last Name
    // ========================================
    @Query("SELECT d FROM DoctorEntity d WHERE " +
            "LOWER(d.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(d.lastName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<DoctorEntity> searchByName(@Param("keyword") String keyword);

    // ========================================
    // Find by Specialty
    // ========================================
    List<DoctorEntity> findBySpecialty(String specialty);

    // ========================================
    // Find all doctors sorted by last name
    // ========================================
    @Query("SELECT d FROM DoctorEntity d ORDER BY d.lastName ASC, d.firstName ASC")
    List<DoctorEntity> findAllSortedByName();
}
