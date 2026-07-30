package com.doctor.appointment.exception;

public class InvalidTimeRangeException extends RuntimeException {

    public InvalidTimeRangeException(String message) {
        super(message);
    }

    public InvalidTimeRangeException() {
        super("End date must be after start date");
    }
}
