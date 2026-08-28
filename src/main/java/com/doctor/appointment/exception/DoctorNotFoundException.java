package com.doctor.appointment.exception;

public class DoctorNotFoundException extends RuntimeException {

    public DoctorNotFoundException(String message) {
        super(message);
    }

    public DoctorNotFoundException(Long id) {
        super("Doctor with id " + id + " not found");
    }

    public DoctorNotFoundException(String field, String value) {
        super("Doctor with " + field + " '" + value + "' not found");
    }
}
