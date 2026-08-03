package com.prabha.jwt_authentication.service;

import com.prabha.jwt_authentication.dto.JwtResponse;
import com.prabha.jwt_authentication.dto.LoginRequest;
import com.prabha.jwt_authentication.dto.RegisterRequest;
import com.prabha.jwt_authentication.dto.UpdateProfileRequest;
import com.prabha.jwt_authentication.dto.UserResponse;

/**
 * Application-level authentication and user-profile operations.
 */
public interface AuthService {

    UserResponse register(RegisterRequest request);

    JwtResponse login(LoginRequest request);

    UserResponse updateProfile(String username, UpdateProfileRequest request);

    UserResponse getCurrentUser(String username);
}
