package com.doctor.appointment.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "appointments"
       , indexes = {
                @Index(name = "idx_appointment_date", columnList = "start_time"),
                @Index(name = "idx_appointment_status", columnList = "status")
        }
)
@Getter
@Setter
@Builder
public class AppointmentEntity {
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "appointment_seq"
    )
    @SequenceGenerator(
            name = "appointment_seq",
            sequenceName = "appointment_sequence",
            allocationSize = 1,
            initialValue = 1
    )
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 10)
    private AppointmentStatus status;

    @Column(name = "patient_name", length = 100)
    private String patientName;

    @Column(name = "patient_phone", length = 20)
    private String patientPhone;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
    private DoctorEntity doctor;

    @Version
    @Column(name = "version", nullable = false)
    @Builder.Default
    private Long version = 0L;




}
