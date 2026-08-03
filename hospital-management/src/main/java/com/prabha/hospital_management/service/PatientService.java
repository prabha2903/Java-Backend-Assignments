package com.prabha.hospital_management.service;

import com.prabha.hospital_management.dto.PatientRequest;
import com.prabha.hospital_management.dto.PatientResponse;

public interface PatientService {

    PatientResponse addPatient(PatientRequest request);
}
