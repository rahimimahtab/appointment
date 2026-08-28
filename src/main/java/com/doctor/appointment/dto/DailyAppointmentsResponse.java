package com.doctor.appointment.dto;


import com.doctor.appointment.model.AppointmentEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyAppointmentsResponse {

    private LocalDate date;
    private List<AppointmentResponse> openAppointments;
    private List<AppointmentResponse> takenAppointments;
    private int totalOpen;
    private int totalTaken;

    public static DailyAppointmentsResponse fromEntities(
            LocalDate date,
            List<AppointmentEntity> openAppointments,
            List<AppointmentEntity> takenAppointments) {

        List<AppointmentResponse> openResponses = openAppointments.stream()
                .map(AppointmentResponse::fromEntity)
                .toList();

        List<AppointmentResponse> takenResponses = takenAppointments.stream()
                .map(AppointmentResponse::fromEntity)
                .toList();

        return DailyAppointmentsResponse.builder()
                .date(date)
                .openAppointments(openResponses)
                .takenAppointments(takenResponses)
                .totalOpen(openResponses.size())
                .totalTaken(takenResponses.size())
                .build();
    }
}