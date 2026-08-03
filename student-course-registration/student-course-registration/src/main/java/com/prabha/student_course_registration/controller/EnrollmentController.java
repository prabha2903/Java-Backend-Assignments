package com.prabha.student_course_registration.controller;

import com.prabha.student_course_registration.dto.MessageResponseDTO;
import com.prabha.student_course_registration.service.EnrollmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST endpoints for enrolling/removing a Student's enrollment in a Course.
 */
@RestController
@RequestMapping("/students/{studentId}/courses/{courseId}")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    /**
     * POST /students/{studentId}/courses/{courseId} - enrolls the student
     * into the course.
     */
    @PostMapping
    public ResponseEntity<MessageResponseDTO> enroll(@PathVariable Long studentId, @PathVariable Long courseId) {
        enrollmentService.enrollStudentInCourse(studentId, courseId);
        MessageResponseDTO response = new MessageResponseDTO(
                "Student " + studentId + " successfully enrolled in course " + courseId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * DELETE /students/{studentId}/courses/{courseId} - removes the
     * student's enrollment from the course.
     */
    @DeleteMapping
    public ResponseEntity<MessageResponseDTO> unenroll(@PathVariable Long studentId, @PathVariable Long courseId) {
        enrollmentService.removeEnrollment(studentId, courseId);
        MessageResponseDTO response = new MessageResponseDTO(
                "Enrollment removed for student " + studentId + " from course " + courseId);
        return ResponseEntity.ok(response);
    }
}
