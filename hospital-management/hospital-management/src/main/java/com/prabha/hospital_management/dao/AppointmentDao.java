package com.prabha.hospital_management.dao;

import com.prabha.hospital_management.entity.Appointment;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface AppointmentDao {

    Appointment save(Appointment appointment);

    List<Appointment> findAll();

    Optional<Appointment> findById(Long appointmentId);

    void deleteById(Long appointmentId);

    boolean exists(Long doctorId, Long patientId, LocalDate appointmentDate, LocalTime appointmentTime);
}
