package com.prabha.student_course_registration.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

/**
 * Response payload representing a Student, optionally including the set of
 * courses they are enrolled in.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentResponseDTO {

    private Long studentId;
    private String studentName;
    private String email;
    private String department;
    private Set<CourseSummaryDTO> courses;
}
