package com.prabha.hospital_management.service;

import com.prabha.hospital_management.dao.DoctorDao;
import com.prabha.hospital_management.dto.DoctorRequest;
import com.prabha.hospital_management.dto.DoctorResponse;
import com.prabha.hospital_management.entity.Doctor;
import com.prabha.hospital_management.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private final DoctorDao doctorDao;

    @Override
    public DoctorResponse addDoctor(DoctorRequest request) {
        validate(request);

        Doctor doctor = new Doctor();
        doctor.setDoctorName(request.getDoctorName().trim());
        doctor.setSpecialization(request.getSpecialization().trim());
        doctor.setPhoneNumber(request.getPhoneNumber() != null ? request.getPhoneNumber().trim() : null);

        Doctor saved = doctorDao.save(doctor);
        return toResponse(saved);
    }

    private void validate(DoctorRequest request) {
        if (request == null) {
            throw new ValidationException("Request body is required");
        }
        if (request.getDoctorName() == null || request.getDoctorName().trim().isEmpty()) {
            throw new ValidationException("Doctor name is required");
        }
        if (request.getSpecialization() == null || request.getSpecialization().trim().isEmpty()) {
            throw new ValidationException("Specialization is required");
        }
    }

    private DoctorResponse toResponse(Doctor doctor) {
        return DoctorResponse.builder()
                .doctorId(doctor.getDoctorId())
                .doctorName(doctor.getDoctorName())
                .specialization(doctor.getSpecialization())
                .phoneNumber(doctor.getPhoneNumber())
                .build();
    }
}
