package com.prabha.hospital_management.service;

import com.prabha.hospital_management.dto.AppointmentRequest;
import com.prabha.hospital_management.dto.AppointmentResponse;

import java.util.List;

public interface AppointmentService {

    AppointmentResponse scheduleAppointment(AppointmentRequest request);

    List<AppointmentResponse> getAllAppointments();

    AppointmentResponse getAppointmentById(Long appointmentId);

    void deleteAppointment(Long appointmentId);
}
