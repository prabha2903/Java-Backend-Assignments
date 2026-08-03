package com.prabha.employee_passport_mapping.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PassportCreateRequest {
    private String passportNumber;
    private String country;
    private LocalDate issueDate;
    private LocalDate expiryDate;
}
