package com.prabha.jwt_authentication.exception;

/**
 * Thrown when attempting to register a user with a username that already exists.
 */
public class DuplicateUsernameException extends RuntimeException {

    public DuplicateUsernameException(String message) {
        super(message);
    }
}
