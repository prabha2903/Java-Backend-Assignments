package com.prabha.hospital_management.dao;

import com.prabha.hospital_management.entity.Patient;

import java.util.Optional;

public interface PatientDao {

    Patient save(Patient patient);

    Optional<Patient> findById(Long patientId);
}
