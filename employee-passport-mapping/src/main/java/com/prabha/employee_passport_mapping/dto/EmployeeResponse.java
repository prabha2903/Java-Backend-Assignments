package com.prabha.employee_passport_mapping.dto;

import org.hibernate.Hibernate;

import com.prabha.employee_passport_mapping.entity.Employee;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeResponse {
    private Long employeeId;
    private String employeeName;
    private String department;
    private Double salary;
    private PassportResponse passport;

    /**
     * IMPORTANT: must be called while the Hibernate Session that loaded
     * {@code employee} is still open, otherwise accessing a LAZY
     * employee.getPassport() proxy will throw LazyInitializationException.
     * Callers should either have used a "join fetch" query (see
     * EmployeeDaoImpl#findByIdWithPassport) or must build this DTO inside
     * the DAO/service transaction boundary.
     */
    public static EmployeeResponse fromEntity(Employee employee) {
        if (employee == null) {
            return null;
        }
        PassportResponse passportResponse = Hibernate.isInitialized(employee.getPassport())
                ? PassportResponse.fromEntity(employee.getPassport())
                : null;

        return EmployeeResponse.builder()
                .employeeId(employee.getEmployeeId())
                .employeeName(employee.getEmployeeName())
                .department(employee.getDepartment())
                .salary(employee.getSalary())
                .passport(passportResponse)
                .build();
    }
}
