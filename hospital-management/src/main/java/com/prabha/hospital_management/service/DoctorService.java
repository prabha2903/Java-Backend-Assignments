package com.prabha.hospital_management.service;

import com.prabha.hospital_management.dto.DoctorRequest;
import com.prabha.hospital_management.dto.DoctorResponse;

public interface DoctorService {

    DoctorResponse addDoctor(DoctorRequest request);
}
