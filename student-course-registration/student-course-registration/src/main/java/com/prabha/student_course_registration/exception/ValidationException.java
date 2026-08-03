package com.prabha.student_course_registration.exception;

/**
 * Thrown for business-level validation failures that Bean Validation
 * annotations cannot express (e.g. uniqueness checks such as duplicate
 * email or duplicate course code). Mapped to HTTP 400 (Bad Request).
 */
public class ValidationException extends RuntimeException {

    public ValidationException(String message) {
        super(message);
    }
}
