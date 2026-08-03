package com.prabha.student_management.exception;

/**
 * Thrown when a requested resource (e.g. a Student) cannot be found.
 * Results in an HTTP 404 response.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
