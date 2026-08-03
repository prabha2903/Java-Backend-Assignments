package com.prabha.student_management.service.impl;

import com.prabha.student_management.dto.StudentRequestDTO;
import com.prabha.student_management.dto.StudentResponseDTO;
import com.prabha.student_management.entity.Student;
import com.prabha.student_management.exception.DuplicateEmailException;
import com.prabha.student_management.exception.ResourceNotFoundException;
import com.prabha.student_management.repository.StudentRepository;
import com.prabha.student_management.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    @Override
    public StudentResponseDTO addStudent(StudentRequestDTO requestDTO) {
        if (studentRepository.existsByEmail(requestDTO.getEmail())) {
            throw new DuplicateEmailException(
                    "A student with email '" + requestDTO.getEmail() + "' already exists");
        }

        Student student = toEntity(requestDTO);
        Student saved = studentRepository.save(student);
        return toResponseDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentResponseDTO> getAllStudents() {
        return studentRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public StudentResponseDTO getStudentById(Long studentId) {
        Student student = findStudentOrThrow(studentId);
        return toResponseDTO(student);
    }

    @Override
    public StudentResponseDTO updateStudent(Long studentId, StudentRequestDTO requestDTO) {
        Student existingStudent = findStudentOrThrow(studentId);

        studentRepository.findByEmail(requestDTO.getEmail())
                .filter(other -> !other.getStudentId().equals(studentId))
                .ifPresent(other -> {
                    throw new DuplicateEmailException(
                            "A student with email '" + requestDTO.getEmail() + "' already exists");
                });

        existingStudent.setName(requestDTO.getName());
        existingStudent.setEmail(requestDTO.getEmail());
        existingStudent.setPhoneNumber(requestDTO.getPhoneNumber());
        existingStudent.setDepartment(requestDTO.getDepartment());
        existingStudent.setYearOfStudy(requestDTO.getYearOfStudy());
        existingStudent.setCgpa(requestDTO.getCgpa());

        Student updated = studentRepository.save(existingStudent);
        return toResponseDTO(updated);
    }

    @Override
    public void deleteStudent(Long studentId) {
        Student student = findStudentOrThrow(studentId);
        studentRepository.delete(student);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentResponseDTO> getStudentsByDepartment(String department) {
        return studentRepository.findByDepartment(department)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentResponseDTO> getStudentsWithCgpaGreaterThan(BigDecimal cgpa) {
        return studentRepository.findByCgpaGreaterThan(cgpa)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    private Student findStudentOrThrow(Long studentId) {
        return studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Student not found with id: " + studentId));
    }

    private Student toEntity(StudentRequestDTO dto) {
        return Student.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .phoneNumber(dto.getPhoneNumber())
                .department(dto.getDepartment())
                .yearOfStudy(dto.getYearOfStudy())
                .cgpa(dto.getCgpa())
                .build();
    }

    private StudentResponseDTO toResponseDTO(Student student) {
        return StudentResponseDTO.builder()
                .studentId(student.getStudentId())
                .name(student.getName())
                .email(student.getEmail())
                .phoneNumber(student.getPhoneNumber())
                .department(student.getDepartment())
                .yearOfStudy(student.getYearOfStudy())
                .cgpa(student.getCgpa())
                .build();
    }
}
