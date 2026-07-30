package com.doctor.appointment.dto;


import com.doctor.appointment.model.DoctorEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorResponse {

    private Long id;
    private String nationalCode;
    private String firstName;
    private String lastName;
    private String fullName;
    private String specialty;
    private String email;
    private String phone;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int appointmentCount;

    public static DoctorResponse fromEntity(DoctorEntity doctor) {
        return DoctorResponse.builder()
                .id(doctor.getId())
                .nationalCode(doctor.getNationalCode())
                .firstName(doctor.getFirstName())
                .lastName(doctor.getLastName())
                .fullName(doctor.getFirstName() + " " + doctor.getLastName())
                .specialty(doctor.getSpecialty())
                .email(doctor.getEmail())
                .phone(doctor.getPhone())
                .createdAt(doctor.getCreatedAt())
                .updatedAt(doctor.getUpdatedAt())
                .appointmentCount(doctor.getAppointments() != null ? doctor.getAppointments().size() : 0)
                .build();
    }
}
