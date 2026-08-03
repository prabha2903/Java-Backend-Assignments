package com.prabha.student_management.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Payload returned to clients representing a Student.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentResponseDTO {

    private Long studentId;
    private String name;
    private String email;
    private String phoneNumber;
    private String department;
    private Integer yearOfStudy;
    private BigDecimal cgpa;
}
