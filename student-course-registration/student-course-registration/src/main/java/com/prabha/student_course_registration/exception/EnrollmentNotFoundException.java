package com.prabha.student_course_registration.exception;

/**
 * Thrown when attempting to remove an enrollment that does not exist.
 * Mapped to HTTP 404.
 */
public class EnrollmentNotFoundException extends RuntimeException {

    public EnrollmentNotFoundException(String message) {
        super(message);
    }
}
