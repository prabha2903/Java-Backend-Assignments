package com.prabha.jwt_authentication.exception;

/**
 * Thrown when attempting to register or update a user with an email that already exists.
 */
public class DuplicateEmailException extends RuntimeException {

    public DuplicateEmailException(String message) {
        super(message);
    }
}
