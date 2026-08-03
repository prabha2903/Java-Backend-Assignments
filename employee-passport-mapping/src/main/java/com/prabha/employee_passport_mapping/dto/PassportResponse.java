package com.prabha.employee_passport_mapping.dto;

import java.time.LocalDate;

import com.prabha.employee_passport_mapping.entity.Passport;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PassportResponse {
    private Long passportId;
    private String passportNumber;
    private String country;
    private LocalDate issueDate;
    private LocalDate expiryDate;

    public static PassportResponse fromEntity(Passport passport) {
        if (passport == null) {
            return null;
        }
        return PassportResponse.builder()
                .passportId(passport.getPassportId())
                .passportNumber(passport.getPassportNumber())
                .country(passport.getCountry())
                .issueDate(passport.getIssueDate())
                .expiryDate(passport.getExpiryDate())
                .build();
    }
}
