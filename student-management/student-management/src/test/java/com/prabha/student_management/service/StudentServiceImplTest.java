package com.prabha.student_management.service;

import com.prabha.student_management.dto.StudentRequestDTO;
import com.prabha.student_management.dto.StudentResponseDTO;
import com.prabha.student_management.entity.Student;
import com.prabha.student_management.exception.DuplicateEmailException;
import com.prabha.student_management.exception.ResourceNotFoundException;
import com.prabha.student_management.repository.StudentRepository;
import com.prabha.student_management.service.impl.StudentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link StudentServiceImpl} covering the assignment's
 * required test cases at the service (business-logic) layer.
 */
@ExtendWith(MockitoExtension.class)
class StudentServiceImplTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentServiceImpl studentService;

    private Student existingStudent;
    private StudentRequestDTO validRequest;

    @BeforeEach
    void setUp() {
        existingStudent = Student.builder()
                .studentId(1L)
                .name("Arun Kumar")
                .email("arun.kumar@example.com")
                .phoneNumber("9876543210")
                .department("Computer Science")
                .yearOfStudy(2)
                .cgpa(new BigDecimal("8.50"))
                .build();

        validRequest = new StudentRequestDTO(
                "Arun Kumar",
                "arun.kumar@example.com",
                "9876543210",
                "Computer Science",
                2,
                new BigDecimal("8.50"));
    }

    // ---------- Add Student ----------

    @Test
    void addStudent_validStudent_savesAndReturnsStudent() {
        when(studentRepository.existsByEmail(validRequest.getEmail())).thenReturn(false);
        when(studentRepository.save(any(Student.class))).thenReturn(existingStudent);

        StudentResponseDTO result = studentService.addStudent(validRequest);

        assertThat(result).isNotNull();
        assertThat(result.getStudentId()).isEqualTo(1L);
        assertThat(result.getEmail()).isEqualTo("arun.kumar@example.com");
        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    void addStudent_duplicateEmail_throwsDuplicateEmailException() {
        when(studentRepository.existsByEmail(validRequest.getEmail())).thenReturn(true);

        assertThatThrownBy(() -> studentService.addStudent(validRequest))
                .isInstanceOf(DuplicateEmailException.class)
                .hasMessageContaining(validRequest.getEmail());

        verify(studentRepository, never()).save(any(Student.class));
    }

    // ---------- Get By Id ----------

    @Test
    void getStudentById_invalidId_throwsResourceNotFoundException() {
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> studentService.getStudentById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void getStudentById_validId_returnsStudent() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(existingStudent));

        StudentResponseDTO result = studentService.getStudentById(1L);

        assertThat(result.getStudentId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Arun Kumar");
    }

    // ---------- Update ----------

    @Test
    void updateStudent_existingId_updatesAndReturnsStudent() {
        StudentRequestDTO updateRequest = new StudentRequestDTO(
                "Arun K.", "arun.k@example.com", "9000000000", "Computer Science", 3, new BigDecimal("9.10"));

        when(studentRepository.findById(1L)).thenReturn(Optional.of(existingStudent));
        when(studentRepository.findByEmail(updateRequest.getEmail())).thenReturn(Optional.empty());
        when(studentRepository.save(any(Student.class))).thenAnswer(invocation -> invocation.getArgument(0));

        StudentResponseDTO result = studentService.updateStudent(1L, updateRequest);

        assertThat(result.getName()).isEqualTo("Arun K.");
        assertThat(result.getEmail()).isEqualTo("arun.k@example.com");
        assertThat(result.getYearOfStudy()).isEqualTo(3);
        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    void updateStudent_nonExistingId_throwsResourceNotFoundException() {
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> studentService.updateStudent(99L, validRequest))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(studentRepository, never()).save(any(Student.class));
    }

    // ---------- Delete ----------

    @Test
    void deleteStudent_existingId_deletesStudent() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(existingStudent));

        studentService.deleteStudent(1L);

        verify(studentRepository, times(1)).delete(existingStudent);
    }

    @Test
    void deleteStudent_nonExistingId_throwsResourceNotFoundException() {
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> studentService.deleteStudent(99L))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(studentRepository, never()).delete(any(Student.class));
    }

    // ---------- Search by Department ----------

    @Test
    void getStudentsByDepartment_existingDepartment_returnsStudents() {
        when(studentRepository.findByDepartment("Computer Science"))
                .thenReturn(List.of(existingStudent));

        List<StudentResponseDTO> result = studentService.getStudentsByDepartment("Computer Science");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getDepartment()).isEqualTo("Computer Science");
    }

    @Test
    void getStudentsByDepartment_emptyResult_returnsEmptyList() {
        when(studentRepository.findByDepartment("Mechanical")).thenReturn(List.of());

        List<StudentResponseDTO> result = studentService.getStudentsByDepartment("Mechanical");

        assertThat(result).isEmpty();
    }

    // ---------- CGPA Filter ----------

    @Test
    void getStudentsWithCgpaGreaterThan_returnsMatchingStudents() {
        when(studentRepository.findByCgpaGreaterThan(new BigDecimal("8.0")))
                .thenReturn(List.of(existingStudent));

        List<StudentResponseDTO> result = studentService.getStudentsWithCgpaGreaterThan(new BigDecimal("8.0"));

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCgpa()).isEqualByComparingTo("8.50");
    }
}
