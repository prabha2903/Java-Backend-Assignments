package com.prabha.student_management.controller;

import com.prabha.student_management.exception.ResourceNotFoundException;
import com.prabha.student_management.service.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Slice tests for {@link StudentController} focused on request validation
 * and HTTP status codes (missing fields -> 400, invalid id -> 404).
 *
 * <p>Request bodies are written as plain JSON strings (rather than via an
 * autowired Jackson ObjectMapper) to stay agnostic of whichever Jackson
 * major version is wired into the application context.</p>
 */
@WebMvcTest(StudentController.class)
class StudentControllerValidationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StudentService studentService;

    @Test
    void addStudent_missingFields_returnsBadRequest() throws Exception {
        // name, email, phoneNumber, department, yearOfStudy and cgpa are all missing.
        String emptyBody = "{}";

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(emptyBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void addStudent_invalidCgpa_returnsBadRequest() throws Exception {
        String body = """
                {
                  "name": "Test Student",
                  "email": "test@example.com",
                  "phoneNumber": "9876543210",
                  "department": "IT",
                  "yearOfStudy": 1,
                  "cgpa": 11.0
                }
                """;

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void getStudentById_invalidId_returnsNotFound() throws Exception {
        when(studentService.getStudentById(eq(999L)))
                .thenThrow(new ResourceNotFoundException("Student not found with id: 999"));

        mockMvc.perform(get("/api/students/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false));
    }
}
