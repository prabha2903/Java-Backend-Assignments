package com.prabha.employee_passport_mapping.dao;

import java.util.Optional;

import com.prabha.employee_passport_mapping.entity.Employee;
import com.prabha.employee_passport_mapping.entity.Passport;

public interface EmployeeDao {

    Employee save(Employee employee);

    /**
     * Attaches (persists) a Passport to an already existing Employee.
     * Throws EmployeeNotFoundException (from the service layer) if the
     * employee does not exist.
     */
    Employee assignPassport(Long employeeId, Passport passport);

    /**
     * Loads an Employee WITHOUT initializing its (LAZY) passport association.
     */
    Optional<Employee> findById(Long employeeId);

    /**
     * Loads an Employee together with its Passport in a single query using
     * HQL "join fetch" - demonstrates explicit eager loading regardless of
     * the LAZY fetch type declared on the entity mapping.
     */
    Optional<Employee> findByIdWithPassport(Long employeeId);

    boolean existsByPassportNumber(String passportNumber);

    void delete(Employee employee);
}
