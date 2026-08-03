package com.prabha.student_course_registration.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Lightweight Course projection, embedded inside {@link StudentResponseDTO}.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseSummaryDTO {

    private Long courseId;
    private String courseName;
    private String courseCode;
    private Integer credits;
}
