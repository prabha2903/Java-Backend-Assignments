package com.prabha.hospital_management.service;

import com.prabha.hospital_management.dao.PatientDao;
import com.prabha.hospital_management.dto.PatientRequest;
import com.prabha.hospital_management.dto.PatientResponse;
import com.prabha.hospital_management.entity.Patient;
import com.prabha.hospital_management.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientDao patientDao;

    @Override
    public PatientResponse addPatient(PatientRequest request) {
        validate(request);

        Patient patient = new Patient();
        patient.setPatientName(request.getPatientName().trim());
        patient.setAge(request.getAge());
        patient.setGender(request.getGender() != null ? request.getGender().trim() : null);
        patient.setPhoneNumber(request.getPhoneNumber() != null ? request.getPhoneNumber().trim() : null);

        Patient saved = patientDao.save(patient);
        return toResponse(saved);
    }

    private void validate(PatientRequest request) {
        if (request == null) {
            throw new ValidationException("Request body is required");
        }
        if (request.getPatientName() == null || request.getPatientName().trim().isEmpty()) {
            throw new ValidationException("Patient name is required");
        }
        if (request.getAge() == null || request.getAge() <= 0) {
            throw new ValidationException("Age must be a positive number");
        }
    }

    private PatientResponse toResponse(Patient patient) {
        return PatientResponse.builder()
                .patientId(patient.getPatientId())
                .patientName(patient.getPatientName())
                .age(patient.getAge())
                .gender(patient.getGender())
                .phoneNumber(patient.getPhoneNumber())
                .build();
    }
}
