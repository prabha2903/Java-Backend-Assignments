package com.prabha.employee_passport_mapping.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.prabha.employee_passport_mapping.dao.EmployeeDao;
import com.prabha.employee_passport_mapping.dto.EmployeeCreateRequest;
import com.prabha.employee_passport_mapping.dto.EmployeeResponse;
import com.prabha.employee_passport_mapping.dto.PassportCreateRequest;
import com.prabha.employee_passport_mapping.entity.Employee;
import com.prabha.employee_passport_mapping.entity.Passport;
import com.prabha.employee_passport_mapping.exception.DuplicatePassportException;
import com.prabha.employee_passport_mapping.exception.EmployeeNotFoundException;
import com.prabha.employee_passport_mapping.exception.ValidationException;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeDao employeeDao;

    public EmployeeServiceImpl(EmployeeDao employeeDao) {
        this.employeeDao = employeeDao;
    }

    @Override
    public EmployeeResponse createEmployee(EmployeeCreateRequest request) {
        validateEmployee(request);

        Employee employee = Employee.builder()
                .employeeName(request.getEmployeeName().trim())
                .department(request.getDepartment().trim())
                .salary(request.getSalary())
                .build();

        Employee saved = employeeDao.save(employee);
        return EmployeeResponse.fromEntity(saved);
    }

    @Override
    public EmployeeResponse assignPassport(Long employeeId, PassportCreateRequest request) {
        validatePassport(request);

        Employee existingEmployee = employeeDao.findByIdWithPassport(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException(employeeId));

        if (existingEmployee.getPassport() != null) {
            throw new ValidationException(
                    "Employee with id " + employeeId + " already has a passport assigned (one-to-one only)");
        }

        if (employeeDao.existsByPassportNumber(request.getPassportNumber())) {
            throw new DuplicatePassportException(request.getPassportNumber());
        }

        Passport passport = Passport.builder()
                .passportNumber(request.getPassportNumber().trim())
                .country(request.getCountry().trim())
                .issueDate(request.getIssueDate())
                .expiryDate(request.getExpiryDate())
                .build();

        Employee employee = employeeDao.assignPassport(employeeId, passport);
        return EmployeeResponse.fromEntity(employee);
    }

    @Override
    public EmployeeResponse getEmployee(Long employeeId) {
        Employee employee = employeeDao.findByIdWithPassport(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException(employeeId));
        return EmployeeResponse.fromEntity(employee);
    }

    @Override
    public void deleteEmployee(Long employeeId) {
        Employee employee = employeeDao.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException(employeeId));
        employeeDao.delete(employee);
    }

    // ---------------------------------------------------------------
    // Manual validation (no Spring Data JPA / bean-validation auto-config
    // dependency required - kept explicit and simple per requirements).
    // ---------------------------------------------------------------

    private void validateEmployee(EmployeeCreateRequest request) {
        if (request == null) {
            throw new ValidationException("Request body is required");
        }
        if (isBlank(request.getEmployeeName())) {
            throw new ValidationException("Employee name is required");
        }
        if (isBlank(request.getDepartment())) {
            throw new ValidationException("Department is required");
        }
        if (request.getSalary() == null || request.getSalary() <= 0) {
            throw new ValidationException("Salary must be a positive number");
        }
    }

    private void validatePassport(PassportCreateRequest request) {
        if (request == null) {
            throw new ValidationException("Request body is required");
        }
        if (isBlank(request.getPassportNumber())) {
            throw new ValidationException("Passport number is required");
        }
        if (isBlank(request.getCountry())) {
            throw new ValidationException("Country is required");
        }
        if (request.getIssueDate() == null) {
            throw new ValidationException("Issue date is required");
        }
        if (request.getExpiryDate() == null) {
            throw new ValidationException("Expiry date is required");
        }
        if (request.getExpiryDate().isBefore(request.getIssueDate())
                || request.getExpiryDate().isEqual(request.getIssueDate())) {
            throw new ValidationException("Expiry date must be after issue date");
        }
        if (request.getExpiryDate().isBefore(LocalDate.now())) {
            throw new ValidationException("Expiry date must be in the future");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
