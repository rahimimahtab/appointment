package com.doctor.appointment.repository;


import com.doctor.appointment.model.AppointmentEntity;
import com.doctor.appointment.model.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.persistence.LockModeType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<AppointmentEntity, Long> {

    // ========================================
    // Find by Status
    // ========================================

    List<AppointmentEntity> findByStatus(AppointmentStatus status);

    // ========================================
    // Find by Date (Day)
    // ========================================

    @Query("SELECT a FROM AppointmentEntity a WHERE DATE(a.startTime) = :date")
    List<AppointmentEntity> findByDate(@Param("date") LocalDate date);

    @Query("SELECT a FROM AppointmentEntity a WHERE DATE(a.startTime) = :date AND a.status = :status")
    List<AppointmentEntity> findByDateAndStatus(@Param("date") LocalDate date,
                                          @Param("status") AppointmentStatus status);

    // ========================================
    // Find Open Appointments for a Day
    // ========================================

    @Query("SELECT a FROM AppointmentEntity a WHERE DATE(a.startTime) = :date AND a.status = 'OPEN' ORDER BY a.startTime")
    List<AppointmentEntity> findOpenAppointmentsByDate(@Param("date") LocalDate date);

    // ========================================
    // Find by Patient Phone
    // ========================================

    @Query("SELECT a FROM AppointmentEntity a WHERE a.patientPhone = :phone AND a.status = 'TAKEN' ORDER BY a.startTime")
    List<AppointmentEntity> findTakenByPatientPhone(@Param("phone") String phone);

    // ========================================
    // Find by Time Range (for overlapping check)
    // ========================================

    @Query("SELECT a FROM AppointmentEntity a WHERE " +
            "(a.startTime < :endTime AND a.endTime > :startTime)")
    List<AppointmentEntity> findOverlappingAppointments(@Param("startTime") LocalDateTime startTime,
                                                  @Param("endTime") LocalDateTime endTime);

    // ========================================
    // Find with Lock (for Concurrency)
    // ========================================

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM AppointmentEntity a WHERE a.id = :id")
    Optional<AppointmentEntity> findByIdWithLock(@Param("id") Long id);

    // ========================================
    // Delete by Status (for Doctor)
    // ========================================

    @Query("SELECT a FROM AppointmentEntity a WHERE a.id = :id AND a.status = 'OPEN'")
    Optional<AppointmentEntity> findOpenAppointmentById(@Param("id") Long id);

    // ========================================
    // Check Existence
    // ========================================

    boolean existsByStartTimeAndEndTime(LocalDateTime startTime, LocalDateTime endTime);

    @Query("SELECT COUNT(a) > 0 FROM AppointmentEntity a WHERE a.startTime = :startTime AND a.endTime = :endTime")
    boolean existsByTimeRange(@Param("startTime") LocalDateTime startTime,
                              @Param("endTime") LocalDateTime endTime);
}
