package com.prabha.student_course_registration.controller;

import com.prabha.student_course_registration.dto.StudentRequestDTO;
import com.prabha.student_course_registration.dto.StudentResponseDTO;
import com.prabha.student_course_registration.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST endpoints for Student registration and lookup.
 */
@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    /**
     * POST /students - registers a new student.
     */
    @PostMapping
    public ResponseEntity<StudentResponseDTO> registerStudent(@Valid @RequestBody StudentRequestDTO request) {
        StudentResponseDTO response = studentService.registerStudent(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * GET /students/{studentId} - retrieves a student together with the
     * courses they are enrolled in.
     */
    @GetMapping("/{studentId}")
    public ResponseEntity<StudentResponseDTO> getStudent(@PathVariable Long studentId) {
        StudentResponseDTO response = studentService.getStudentWithCourses(studentId);
        return ResponseEntity.ok(response);
    }
}
