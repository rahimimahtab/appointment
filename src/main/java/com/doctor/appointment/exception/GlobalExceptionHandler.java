package com.doctor.appointment.exception;



import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // ========================================
    // 404 - Appointment Not Found
    // ========================================

    @ExceptionHandler(AppointmentNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleAppointmentNotFound(
            AppointmentNotFoundException ex, WebRequest request) {

        log.error("Appointment not found: {}", ex.getMessage());
        ErrorResponse error = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                ex.getMessage(),
                request.getDescription(false)
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    // ========================================
    // 406 - Appointment Not Open (Already Taken)
    // ========================================

    @ExceptionHandler(AppointmentNotOpenException.class)
    public ResponseEntity<ErrorResponse> handleAppointmentNotOpen(
            AppointmentNotOpenException ex, WebRequest request) {

        log.error("Appointment not open: {}", ex.getMessage());
        ErrorResponse error = new ErrorResponse(
                HttpStatus.NOT_ACCEPTABLE.value(),
                "Not Acceptable",
                ex.getMessage(),
                request.getDescription(false)
        );
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(error);
    }

    // ========================================
    // 400 - Invalid Time Range
    // ========================================

    @ExceptionHandler(InvalidTimeRangeException.class)
    public ResponseEntity<ErrorResponse> handleInvalidTimeRange(
            InvalidTimeRangeException ex, WebRequest request) {

        log.error("Invalid time range: {}", ex.getMessage());
        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                ex.getMessage(),
                request.getDescription(false)
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // ========================================
    // 409 - Concurrency Conflict
    // ========================================

    @ExceptionHandler(OptimisticLockingFailureException.class)
    public ResponseEntity<ErrorResponse> handleConcurrencyConflict(
            OptimisticLockingFailureException ex, WebRequest request) {

        log.error("Concurrency conflict: {}", ex.getMessage());
        ErrorResponse error = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                "Conflict",
                "Appointment is being modified by another user. Please try again.",
                request.getDescription(false)
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    // ========================================
    // 400 - Validation Errors
    // ========================================

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(
            MethodArgumentNotValidException ex, WebRequest request) {

        List<String> details = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());

        log.error("Validation errors: {}", details);
        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Validation Failed",
                "Invalid input data",
                request.getDescription(false),
                details
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // ========================================
    // 500 - Internal Server Error (Fallback)
    // ========================================

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllExceptions(
            Exception ex, WebRequest request) {

        log.error("Unexpected error: {}", ex.getMessage(), ex);
        ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "An unexpected error occurred",
                request.getDescription(false)
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}