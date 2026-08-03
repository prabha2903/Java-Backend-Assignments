package com.prabha.hospital_management.exception;

public class DuplicateAppointmentException extends RuntimeException {

    public DuplicateAppointmentException(String message) {
        super(message);
    }
}
