package com.prabha.hospital_management.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientResponse {

    private Long patientId;

    private String patientName;

    private Integer age;

    private String gender;

    private String phoneNumber;
}
