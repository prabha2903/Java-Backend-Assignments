package com.prabha.student_management.service;

import com.prabha.student_management.dto.StudentRequestDTO;
import com.prabha.student_management.dto.StudentResponseDTO;

import java.math.BigDecimal;
import java.util.List;

/**
 * Business operations available for managing students.
 */
public interface StudentService {

    StudentResponseDTO addStudent(StudentRequestDTO requestDTO);

    List<StudentResponseDTO> getAllStudents();

    StudentResponseDTO getStudentById(Long studentId);

    StudentResponseDTO updateStudent(Long studentId, StudentRequestDTO requestDTO);

    void deleteStudent(Long studentId);

    List<StudentResponseDTO> getStudentsByDepartment(String department);

    List<StudentResponseDTO> getStudentsWithCgpaGreaterThan(BigDecimal cgpa);
}
