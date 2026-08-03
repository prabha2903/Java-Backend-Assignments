package com.prabha.jwt_authentication.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Since spring-boot-starter-security is on the classpath, Spring Security would
 * otherwise secure every endpoint by default (HTTP Basic login). This project's
 * scope is the Product Management API only, so all /api/products/** endpoints
 * are opened up and CSRF is disabled for the stateless REST API.
 */
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
        return http.build();
    }
}
