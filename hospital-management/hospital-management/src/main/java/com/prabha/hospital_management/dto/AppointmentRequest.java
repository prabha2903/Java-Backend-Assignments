package com.prabha.hospital_management.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Accepts ISO-8601 formatted date/time in JSON, e.g.
 * "appointmentDate": "2026-08-10", "appointmentTime": "14:30:00" (seconds optional).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentRequest {

    private Long doctorId;

    private Long patientId;

    private LocalDate appointmentDate;

    private LocalTime appointmentTime;

    private String reason;
}
