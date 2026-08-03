package com.prabha.student_course_registration.exception;

/**
 * Thrown when attempting to enroll a student into a course they are already
 * enrolled in. Mapped to HTTP 409 (Conflict).
 */
public class DuplicateEnrollmentException extends RuntimeException {

    public DuplicateEnrollmentException(String message) {
        super(message);
    }
}
