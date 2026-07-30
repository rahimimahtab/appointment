package com.doctor.appointment.dto;

import com.doctor.appointment.model.AppointmentEntity;
import com.doctor.appointment.model.AppointmentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentResponse {

    private Long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private AppointmentStatus status;
    private String patientName;
    private String patientPhone;
    private boolean open;
    private boolean taken;

    public static AppointmentResponse fromEntity(AppointmentEntity appointment) {
        return AppointmentResponse.builder()
                .id(appointment.getId())
                .startTime(appointment.getStartTime())
                .endTime(appointment.getEndTime())
                .status(appointment.getStatus())
                .patientName(appointment.getPatientName())
                .patientPhone(appointment.getPatientPhone())
                .build();
    }
}
