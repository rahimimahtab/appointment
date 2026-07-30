package com.doctor.appointment.exception;


public class DoctorAlreadyExistsException extends RuntimeException {

    public DoctorAlreadyExistsException(String message) {
        super(message);
    }

    public DoctorAlreadyExistsException(String field, String value) {
        super("Doctor with " + field + " '" + value + "' already exists");
    }
}
