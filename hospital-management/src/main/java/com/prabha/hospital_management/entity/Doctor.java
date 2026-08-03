package com.prabha.hospital_management.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * Doctor entity.
 * One Doctor can have many Appointments (OneToMany).
 */
@Entity
@Table(name = "doctors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doctor_id")
    private Long doctorId;

    @Column(name = "doctor_name", nullable = false, length = 150)
    private String doctorName;

    @Column(name = "specialization", nullable = false, length = 150)
    private String specialization;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    /**
     * Cascade PERSIST/MERGE only - deleting an Appointment must never cascade
     * back and delete the Doctor. Doctor deletion is out of scope for this
     * assignment, so no REMOVE cascade is configured here.
     */
    @OneToMany(mappedBy = "doctor", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Appointment> appointments = new ArrayList<>();
}
