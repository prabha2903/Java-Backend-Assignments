package com.prabha.hospital_management.dao;

import com.prabha.hospital_management.entity.Doctor;

import java.util.Optional;

public interface DoctorDao {

    Doctor save(Doctor doctor);

    Optional<Doctor> findById(Long doctorId);
}
