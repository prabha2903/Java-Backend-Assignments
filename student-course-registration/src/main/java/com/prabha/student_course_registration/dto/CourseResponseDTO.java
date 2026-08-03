package com.prabha.student_course_registration.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

/**
 * Response payload representing a Course, optionally including the set of
 * students registered in it.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseResponseDTO {

    private Long courseId;
    private String courseName;
    private String courseCode;
    private Integer credits;
    private Set<StudentSummaryDTO> students;
}
