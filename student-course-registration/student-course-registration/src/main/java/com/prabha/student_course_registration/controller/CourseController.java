package com.prabha.student_course_registration.controller;

import com.prabha.student_course_registration.dto.CourseRequestDTO;
import com.prabha.student_course_registration.dto.CourseResponseDTO;
import com.prabha.student_course_registration.service.CourseService;
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
 * REST endpoints for Course creation and lookup.
 */
@RestController
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    /**
     * POST /courses - creates a new course.
     */
    @PostMapping
    public ResponseEntity<CourseResponseDTO> createCourse(@Valid @RequestBody CourseRequestDTO request) {
        CourseResponseDTO response = courseService.createCourse(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * GET /courses/{courseId} - retrieves a course together with the
     * students registered in it.
     */
    @GetMapping("/{courseId}")
    public ResponseEntity<CourseResponseDTO> getCourse(@PathVariable Long courseId) {
        CourseResponseDTO response = courseService.getCourseWithStudents(courseId);
        return ResponseEntity.ok(response);
    }
}
