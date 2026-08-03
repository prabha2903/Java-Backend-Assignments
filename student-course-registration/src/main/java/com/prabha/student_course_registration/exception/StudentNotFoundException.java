package com.prabha.student_course_registration.exception;

/**
 * Thrown when a requested Student does not exist. Mapped to HTTP 404.
 */
public class StudentNotFoundException extends RuntimeException {

    public StudentNotFoundException(String message) {
        super(message);
    }
}
