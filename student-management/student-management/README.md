# Student Management System

A RESTful CRUD application built with Spring Boot 4 (Java 17, Spring Data JPA, MySQL,
Bean Validation, Lombok) that manages Student records.

## Tech Stack
- Java 17
- Spring Boot 4.1.0
- Maven
- Spring Web MVC (`spring-boot-starter-webmvc`)
- Spring Data JPA
- MySQL
- Bean Validation (`spring-boot-starter-validation`)
- Lombok

## Project Structure
```
com.prabha.student_management
├── controller       REST controllers
├── service           business logic (interface)
│   └── impl           business logic (implementation)
├── repository        Spring Data JPA repositories
├── entity             JPA entities
├── dto                request/response payloads + ApiResponse wrapper
└── exception          custom exceptions + GlobalExceptionHandler
```

## Getting Started

### 1. Create the database
```sql
CREATE DATABASE studentdb;
```
(Optional — `createDatabaseIfNotExist=true` is already set in the datasource URL,
so the app will create it automatically if your MySQL user has the `CREATE` privilege.)

### 2. Configure credentials
Edit `src/main/resources/application.properties` and set your own MySQL username/password:
```properties
spring.datasource.username=root
spring.datasource.password=your_mysql_password
```

### 3. Run the application
```bash
./mvnw spring-boot:run
```
The API will be available at `http://localhost:8080`.

### 4. (Optional) Load sample data
Once the app has run once (so the `students` table exists), you can load sample rows:
```bash
mysql -u root -p studentdb < sample-data.sql
```

## API Endpoints

All responses follow this consistent JSON shape:
```json
{
  "success": true,
  "message": "Student added successfully",
  "data": { }
}
```

| Method | Endpoint                          | Description                              |
|--------|------------------------------------|-------------------------------------------|
| POST   | `/api/students`                   | Add a new student                         |
| GET    | `/api/students`                   | Get all students                          |
| GET    | `/api/students/{studentId}`       | Get a student by id                       |
| PUT    | `/api/students/{studentId}`       | Update an existing student                |
| DELETE | `/api/students/{studentId}`       | Delete a student                          |
| GET    | `/api/students/department/{dept}` | Get students in a given department        |
| GET    | `/api/students/cgpa/{minCgpa}`    | Get students with CGPA greater than value  |

### Sample request body (POST / PUT)
```json
{
  "name": "Arun Kumar",
  "email": "arun.kumar@example.com",
  "phoneNumber": "9876543210",
  "department": "Computer Science",
  "yearOfStudy": 2,
  "cgpa": 8.75
}
```

### Validation rules
- `name` — required
- `email` — required, must be a valid email, must be unique
- `phoneNumber` — required
- `department` — required
- `yearOfStudy` — required
- `cgpa` — required, between 0 and 10

### HTTP status codes
| Status | Meaning                                            |
|--------|-----------------------------------------------------|
| 200    | Successful GET / PUT / DELETE                      |
| 201    | Student created (POST)                             |
| 400    | Validation error (missing/invalid fields)          |
| 404    | Student not found                                  |
| 409    | Duplicate email                                    |
| 500    | Unexpected server error                             |

## Running the Tests
```bash
./mvnw test
```
Tests run against an in-memory H2 database (see `src/test/resources/application.properties`)
so they do **not** require MySQL to be running. They cover:
- Adding a valid student
- Rejecting a duplicate email
- Rejecting missing/invalid fields
- Updating an existing / non-existing student
- Deleting an existing / non-existing student
- Searching by an existing department / a department with no results
- Fetching a student with an invalid id
