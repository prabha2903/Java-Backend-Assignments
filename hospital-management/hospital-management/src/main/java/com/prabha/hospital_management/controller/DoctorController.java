package com.prabha.hospital_management.controller;

import com.prabha.hospital_management.dto.DoctorRequest;
import com.prabha.hospital_management.dto.DoctorResponse;
import com.prabha.hospital_management.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    @PostMapping
    public ResponseEntity<DoctorResponse> addDoctor(@RequestBody DoctorRequest request) {
        DoctorResponse response = doctorService.addDoctor(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
