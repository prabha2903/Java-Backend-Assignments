# JWT Authentication System

## Overview

This project is a secure RESTful API developed using Spring Boot and Spring Security with JWT (JSON Web Token) Authentication. It provides user registration, login, JWT token generation, profile management, and secure API access using token-based authentication.

## Technologies Used

- Java 17
- Spring Boot
- Spring Security
- JWT (JSON Web Token)
- Spring Data JPA
- Hibernate
- MySQL
- Maven
- Lombok
- Postman

## Features

- User Registration
- User Login
- JWT Token Generation
- Secure REST APIs
- Password Encryption (BCrypt)
- User Profile Update
- Authentication & Authorization
- Input Validation
- Global Exception Handling

## Database

**Database Name:** `jwt_auth_db`

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /auth/register | Register a new user |
| POST | /auth/login | Authenticate user and generate JWT |
| GET | /users/profile | View authenticated user profile |
| PUT | /users/profile | Update authenticated user profile |

## Security Features

- JWT Authentication
- Password Encryption using BCrypt
- Protected APIs
- Stateless Authentication
- Authorization Header Validation

## Validation

- Duplicate Username Validation
- Password Validation
- Mandatory Field Validation
- Invalid Credentials Handling
- Missing Authorization Header Handling
- Expired JWT Handling

## Exception Handling

- User Not Found
- Duplicate Username
- Invalid Credentials
- Unauthorized Access
- Expired JWT Token
- Validation Errors

## API Testing

Postman

## Build Tool

Maven

## Author

**Prabha**