package com.prabha.student_course_registration.exception;

/**
 * Thrown when a requested Course does not exist. Mapped to HTTP 404.
 */
public class CourseNotFoundException extends RuntimeException {

    public CourseNotFoundException(String message) {
        super(message);
    }
}
