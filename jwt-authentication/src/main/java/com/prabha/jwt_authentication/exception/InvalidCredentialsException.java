package com.prabha.jwt_authentication.exception;

/**
 * Thrown when a login attempt is made with an incorrect username or password.
 */
public class InvalidCredentialsException extends RuntimeException {

    public InvalidCredentialsException(String message) {
        super(message);
    }
}
