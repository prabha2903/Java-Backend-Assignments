package com.prabha.employee_passport_mapping.exception;

/**
 * Wraps low-level Hibernate / JDBC failures so the controller layer never
 * has to deal with org.hibernate.* exceptions directly.
 */
public class DatabaseException extends RuntimeException {

    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
