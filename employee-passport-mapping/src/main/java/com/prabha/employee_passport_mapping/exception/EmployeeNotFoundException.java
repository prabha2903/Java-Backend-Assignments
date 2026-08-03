package com.prabha.employee_passport_mapping.exception;

public class EmployeeNotFoundException extends RuntimeException {

    public EmployeeNotFoundException(Long employeeId) {
        super("Employee not found with id: " + employeeId);
    }
}
