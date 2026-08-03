package com.prabha.hospital_management.controller;

import com.prabha.hospital_management.dto.PatientRequest;
import com.prabha.hospital_management.dto.PatientResponse;
import com.prabha.hospital_management.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @PostMapping
    public ResponseEntity<PatientResponse> addPatient(@RequestBody PatientRequest request) {
        PatientResponse response = patientService.addPatient(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
