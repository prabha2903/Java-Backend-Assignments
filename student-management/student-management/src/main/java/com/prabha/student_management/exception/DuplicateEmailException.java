package com.prabha.student_management.exception;

/**
 * Thrown when a Student is created or updated with an email address
 * that already exists in the system.
 * Results in an HTTP 409 (Conflict) response.
 */
public class DuplicateEmailException extends RuntimeException {

    public DuplicateEmailException(String message) {
        super(message);
    }
}
