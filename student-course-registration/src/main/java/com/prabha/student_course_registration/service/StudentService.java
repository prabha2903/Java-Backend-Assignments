package com.prabha.student_course_registration.service;

import com.prabha.student_course_registration.dao.StudentDao;
import com.prabha.student_course_registration.dto.CourseSummaryDTO;
import com.prabha.student_course_registration.dto.StudentRequestDTO;
import com.prabha.student_course_registration.dto.StudentResponseDTO;
import com.prabha.student_course_registration.entity.Course;
import com.prabha.student_course_registration.entity.Student;
import com.prabha.student_course_registration.exception.StudentNotFoundException;
import com.prabha.student_course_registration.exception.ValidationException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Business logic for Student registration and retrieval.
 */
@Service
public class StudentService {

    private final StudentDao studentDao;

    public StudentService(StudentDao studentDao) {
        this.studentDao = studentDao;
    }

    /**
     * Registers a new student after enforcing the unique-email business rule.
     */
    public StudentResponseDTO registerStudent(StudentRequestDTO request) {
        studentDao.findByEmail(request.getEmail()).ifPresent(existing -> {
            throw new ValidationException("A student with email '" + request.getEmail() + "' already exists");
        });

        Student student = new Student(request.getStudentName(), request.getEmail(), request.getDepartment());
        Student saved = studentDao.save(student);
        return toResponseDTO(saved, Collections.emptySet());
    }

    /**
     * Retrieves a student along with the courses they are enrolled in.
     * The "courses" collection is fetched/initialized inside the DAO's
     * Session and converted to DTOs here - safe even though the mapping is
     * FetchType.LAZY.
     */
    public StudentResponseDTO getStudentWithCourses(Long studentId) {
        Student student = studentDao.findByIdWithCourses(studentId)
                .orElseThrow(() -> new StudentNotFoundException("Student not found with id: " + studentId));
        return toResponseDTO(student, student.getCourses());
    }

    private StudentResponseDTO toResponseDTO(Student student, Set<Course> courses) {
        Set<CourseSummaryDTO> courseDTOs = courses.stream()
                .map(course -> new CourseSummaryDTO(
                        course.getCourseId(),
                        course.getCourseName(),
                        course.getCourseCode(),
                        course.getCredits()))
                .collect(Collectors.toSet());

        return new StudentResponseDTO(
                student.getStudentId(),
                student.getStudentName(),
                student.getEmail(),
                student.getDepartment(),
                courseDTOs
        );
    }
}
