package com.prabha.jwt_authentication.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Safe, public-facing representation of a User. Never exposes the password.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

    private Long id;
    private String name;
    private String username;
    private String email;
    private String role;
    private LocalDateTime createdAt;
}
