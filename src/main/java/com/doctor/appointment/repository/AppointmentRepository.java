package com.doctor.appointment.repository;


import com.doctor.appointment.model.AppointmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface AppointmentRepository extends JpaRepository<AppointmentEntity,Long> {

    // ========================================
    // Check Existence
    // ========================================

    boolean existsByStartTimeAndEndTime(LocalDateTime startTime, LocalDateTime endTime);

    @Query("SELECT COUNT(a) > 0 FROM AppointmentEntity a WHERE a.startTime = :startTime AND a.endTime = :endTime")
    boolean existsByTimeRange(@Param("startTime") LocalDateTime startTime,
                              @Param("endTime") LocalDateTime endTime);
}
