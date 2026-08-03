package com.prabha.employee_passport_mapping.service;

import com.prabha.employee_passport_mapping.dto.EmployeeCreateRequest;
import com.prabha.employee_passport_mapping.dto.EmployeeResponse;
import com.prabha.employee_passport_mapping.dto.PassportCreateRequest;

public interface EmployeeService {

    EmployeeResponse createEmployee(EmployeeCreateRequest request);

    EmployeeResponse assignPassport(Long employeeId, PassportCreateRequest request);

    EmployeeResponse getEmployee(Long employeeId);

    void deleteEmployee(Long employeeId);
}
