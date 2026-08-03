package com.prabha.student_management.controller;

import com.prabha.student_management.dto.ApiResponse;
import com.prabha.student_management.dto.StudentRequestDTO;
import com.prabha.student_management.dto.StudentResponseDTO;
import com.prabha.student_management.service.StudentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

/**
 * REST endpoints for managing students.
 */
@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
@Validated
public class StudentController {

    private final StudentService studentService;

    @PostMapping
    public ResponseEntity<ApiResponse<StudentResponseDTO>> addStudent(
            @Valid @RequestBody StudentRequestDTO requestDTO) {
        StudentResponseDTO created = studentService.addStudent(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Student added successfully", created));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<StudentResponseDTO>>> getAllStudents() {
        List<StudentResponseDTO> students = studentService.getAllStudents();
        return ResponseEntity.ok(ApiResponse.success("Students retrieved successfully", students));
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<ApiResponse<StudentResponseDTO>> getStudentById(@PathVariable Long studentId) {
        StudentResponseDTO student = studentService.getStudentById(studentId);
        return ResponseEntity.ok(ApiResponse.success("Student retrieved successfully", student));
    }

    @PutMapping("/{studentId}")
    public ResponseEntity<ApiResponse<StudentResponseDTO>> updateStudent(
            @PathVariable Long studentId,
            @Valid @RequestBody StudentRequestDTO requestDTO) {
        StudentResponseDTO updated = studentService.updateStudent(studentId, requestDTO);
        return ResponseEntity.ok(ApiResponse.success("Student updated successfully", updated));
    }

    @DeleteMapping("/{studentId}")
    public ResponseEntity<ApiResponse<Object>> deleteStudent(@PathVariable Long studentId) {
        studentService.deleteStudent(studentId);
        return ResponseEntity.ok(ApiResponse.success("Student deleted successfully", null));
    }

    @GetMapping("/department/{department}")
    public ResponseEntity<ApiResponse<List<StudentResponseDTO>>> getStudentsByDepartment(
            @PathVariable String department) {
        List<StudentResponseDTO> students = studentService.getStudentsByDepartment(department);
        String message = students.isEmpty()
                ? "No students found in department: " + department
                : "Students retrieved successfully";
        return ResponseEntity.ok(ApiResponse.success(message, students));
    }

    @GetMapping("/cgpa/{minCgpa}")
    public ResponseEntity<ApiResponse<List<StudentResponseDTO>>> getStudentsWithCgpaGreaterThan(
            @PathVariable @DecimalMin(value = "0.0", message = "minCgpa must not be negative") BigDecimal minCgpa) {
        List<StudentResponseDTO> students = studentService.getStudentsWithCgpaGreaterThan(minCgpa);
        return ResponseEntity.ok(ApiResponse.success("Students retrieved successfully", students));
    }
}
