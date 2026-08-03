package com.prabha.jwt_authentication.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prabha.jwt_authentication.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Entry point invoked whenever an unauthenticated request tries to access a protected
 * endpoint, or when a supplied JWT is missing/invalid/expired. Returns a clean JSON
 * error response instead of the default Spring Security HTML/plain-text response.
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());

    @Override
    public void commence(HttpServletRequest request,
                          HttpServletResponse response,
                          AuthenticationException authException) throws IOException {

        Object attributeMessage = request.getAttribute("jwt_exception");
        String message = attributeMessage != null
                ? attributeMessage.toString()
                : "Full authentication is required to access this resource. Missing or invalid Authorization header.";

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        ApiResponse<Object> apiResponse = ApiResponse.error(message);
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
    }
}
