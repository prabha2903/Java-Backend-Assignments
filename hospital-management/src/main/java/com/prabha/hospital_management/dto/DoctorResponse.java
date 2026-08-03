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
public class DoctorResponse {

    private Long doctorId;

    private String doctorName;

    private String specialization;

    private String phoneNumber;
}
