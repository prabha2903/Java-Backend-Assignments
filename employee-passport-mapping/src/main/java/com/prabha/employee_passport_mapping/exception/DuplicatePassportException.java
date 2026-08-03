package com.prabha.employee_passport_mapping.exception;

public class DuplicatePassportException extends RuntimeException {

    public DuplicatePassportException(String passportNumber) {
        super("Passport number already exists: " + passportNumber);
    }
}
