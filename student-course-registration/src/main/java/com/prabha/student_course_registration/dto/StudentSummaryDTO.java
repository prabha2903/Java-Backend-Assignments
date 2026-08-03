package com.prabha.student_course_registration.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Lightweight Student projection, embedded inside {@link CourseResponseDTO}.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentSummaryDTO {

    private Long studentId;
    private String studentName;
    private String email;
    private String department;
}
