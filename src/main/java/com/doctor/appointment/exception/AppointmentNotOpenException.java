package com.doctor.appointment.exception;

public class AppointmentNotOpenException extends RuntimeException {

    public AppointmentNotOpenException(String message) {
        super(message);
    }

    public AppointmentNotOpenException(Long id) {
        super("Appointment with id " + id + " is not open and cannot be deleted");
    }
}