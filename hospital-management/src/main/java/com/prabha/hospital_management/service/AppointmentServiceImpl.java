package com.prabha.hospital_management.service;

import com.prabha.hospital_management.dao.AppointmentDao;
import com.prabha.hospital_management.dao.DoctorDao;
import com.prabha.hospital_management.dao.PatientDao;
import com.prabha.hospital_management.dto.AppointmentRequest;
import com.prabha.hospital_management.dto.AppointmentResponse;
import com.prabha.hospital_management.entity.Appointment;
import com.prabha.hospital_management.entity.Doctor;
import com.prabha.hospital_management.entity.Patient;
import com.prabha.hospital_management.exception.AppointmentNotFoundException;
import com.prabha.hospital_management.exception.DoctorNotFoundException;
import com.prabha.hospital_management.exception.DuplicateAppointmentException;
import com.prabha.hospital_management.exception.PatientNotFoundException;
import com.prabha.hospital_management.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentDao appointmentDao;
    private final DoctorDao doctorDao;
    private final PatientDao patientDao;

    @Override
    public AppointmentResponse scheduleAppointment(AppointmentRequest request) {
        validate(request);

        Doctor doctor = doctorDao.findById(request.getDoctorId())
                .orElseThrow(() -> new DoctorNotFoundException(
                        "Doctor not found with id: " + request.getDoctorId()));

        Patient patient = patientDao.findById(request.getPatientId())
                .orElseThrow(() -> new PatientNotFoundException(
                        "Patient not found with id: " + request.getPatientId()));

        boolean duplicate = appointmentDao.exists(
                request.getDoctorId(),
                request.getPatientId(),
                request.getAppointmentDate(),
                request.getAppointmentTime());

        if (duplicate) {
            throw new DuplicateAppointmentException(
                    "An appointment already exists for this Doctor, Patient, Date and Time combination");
        }

        Appointment appointment = new Appointment();
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setAppointmentDate(request.getAppointmentDate());
        appointment.setAppointmentTime(request.getAppointmentTime());
        appointment.setReason(request.getReason().trim());

        Appointment saved = appointmentDao.save(appointment);
        return toResponse(saved);
    }

    @Override
    public List<AppointmentResponse> getAllAppointments() {
        return appointmentDao.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public AppointmentResponse getAppointmentById(Long appointmentId) {
        Appointment appointment = appointmentDao.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException(
                        "Appointment not found with id: " + appointmentId));
        return toResponse(appointment);
    }

    @Override
    public void deleteAppointment(Long appointmentId) {
        appointmentDao.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException(
                        "Appointment not found with id: " + appointmentId));
        appointmentDao.deleteById(appointmentId);
    }

    private void validate(AppointmentRequest request) {
        if (request == null) {
            throw new ValidationException("Request body is required");
        }
        if (request.getDoctorId() == null) {
            throw new ValidationException("Doctor id is required");
        }
        if (request.getPatientId() == null) {
            throw new ValidationException("Patient id is required");
        }
        if (request.getAppointmentDate() == null) {
            throw new ValidationException("Appointment date is required");
        }
        if (request.getAppointmentTime() == null) {
            throw new ValidationException("Appointment time is required");
        }
        if (request.getReason() == null || request.getReason().trim().isEmpty()) {
            throw new ValidationException("Reason is required");
        }
    }

    private AppointmentResponse toResponse(Appointment appointment) {
        return AppointmentResponse.builder()
                .appointmentId(appointment.getAppointmentId())
                .doctorId(appointment.getDoctor().getDoctorId())
                .doctorName(appointment.getDoctor().getDoctorName())
                .patientId(appointment.getPatient().getPatientId())
                .patientName(appointment.getPatient().getPatientName())
                .appointmentDate(appointment.getAppointmentDate())
                .appointmentTime(appointment.getAppointmentTime())
                .reason(appointment.getReason())
                .build();
    }
}
