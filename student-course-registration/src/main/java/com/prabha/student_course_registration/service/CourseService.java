package com.prabha.student_course_registration.service;

import com.prabha.student_course_registration.dao.CourseDao;
import com.prabha.student_course_registration.dto.CourseRequestDTO;
import com.prabha.student_course_registration.dto.CourseResponseDTO;
import com.prabha.student_course_registration.dto.StudentSummaryDTO;
import com.prabha.student_course_registration.entity.Course;
import com.prabha.student_course_registration.entity.Student;
import com.prabha.student_course_registration.exception.CourseNotFoundException;
import com.prabha.student_course_registration.exception.ValidationException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Business logic for Course creation and retrieval.
 */
@Service
public class CourseService {

    private final CourseDao courseDao;

    public CourseService(CourseDao courseDao) {
        this.courseDao = courseDao;
    }

    /**
     * Creates a new course after enforcing the unique-course-code business rule.
     */
    public CourseResponseDTO createCourse(CourseRequestDTO request) {
        courseDao.findByCourseCode(request.getCourseCode()).ifPresent(existing -> {
            throw new ValidationException("A course with code '" + request.getCourseCode() + "' already exists");
        });

        Course course = new Course(request.getCourseName(), request.getCourseCode(), request.getCredits());
        Course saved = courseDao.save(course);
        return toResponseDTO(saved, Collections.emptySet());
    }

    /**
     * Retrieves a course along with the students registered in it. The
     * "students" collection is fetched/initialized inside the DAO's Session
     * and converted to DTOs here - safe even though the mapping is
     * FetchType.LAZY.
     */
    public CourseResponseDTO getCourseWithStudents(Long courseId) {
        Course course = courseDao.findByIdWithStudents(courseId)
                .orElseThrow(() -> new CourseNotFoundException("Course not found with id: " + courseId));
        return toResponseDTO(course, course.getStudents());
    }

    private CourseResponseDTO toResponseDTO(Course course, Set<Student> students) {
        Set<StudentSummaryDTO> studentDTOs = students.stream()
                .map(student -> new StudentSummaryDTO(
                        student.getStudentId(),
                        student.getStudentName(),
                        student.getEmail(),
                        student.getDepartment()))
                .collect(Collectors.toSet());

        return new CourseResponseDTO(
                course.getCourseId(),
                course.getCourseName(),
                course.getCourseCode(),
                course.getCredits(),
                studentDTOs
        );
    }
}
