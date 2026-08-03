package com.prabha.student_course_registration.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Simple message response used for enroll/unenroll confirmations.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponseDTO {

    private String message;
}
