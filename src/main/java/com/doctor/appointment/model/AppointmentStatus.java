package com.doctor.appointment.model;

import lombok.Getter;


@Getter
public enum AppointmentStatus {
    OPEN(0),
    TAKEN(1),
    CANCEL(2);

   private final Integer statusCode;


    private AppointmentStatus(Integer statusCode) {
        this.statusCode = statusCode;
    }

}
