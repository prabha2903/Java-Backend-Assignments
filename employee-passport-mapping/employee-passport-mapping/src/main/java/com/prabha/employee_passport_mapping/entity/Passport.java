package com.prabha.employee_passport_mapping.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Passport entity - the OWNING side of the Employee <-> Passport one-to-one
 * association (it holds the "employee_id" foreign key column).
 *
 * fetch = FetchType.EAGER on purpose: whenever a Passport is loaded directly,
 * its owning Employee is also loaded immediately, demonstrating eager
 * loading in contrast to the LAZY fetch declared on Employee#passport.
 */
@Entity
@Table(name = "passports")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "employee")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Passport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "passport_id")
    @EqualsAndHashCode.Include
    private Long passportId;

    @Column(name = "passport_number", nullable = false, unique = true, length = 50)
    private String passportNumber;

    @Column(name = "country", nullable = false, length = 100)
    private String country;

    @Column(name = "issue_date", nullable = false)
    private LocalDate issueDate;

    @Column(name = "expiry_date", nullable = false)
    private LocalDate expiryDate;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "employee_id", referencedColumnName = "employee_id", unique = true, nullable = false)
    private Employee employee;
}
