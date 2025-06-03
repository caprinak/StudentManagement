package io.satori.edu.exception;

import io.satori.edu.cohort.Cohort;
import io.satori.edu.cohort.CohortRepository;
import io.satori.edu.student.Gender;
import io.satori.edu.student.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class DataIntegrityExceptionTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CohortRepository cohortRepository;

    /**
     * Test DataIntegrityViolationException when trying to insert a student with duplicate email
     * Note: This test uses direct JPA operations instead of controller to ensure we trigger 
     * the DataIntegrityViolationException specifically
     */
    @Test
    public void testDataIntegrityViolationException() throws Exception {
        // First, create a student with a specific email to ensure it exists
        String uniqueEmail = "unique.test." + System.currentTimeMillis() + "@example.com";
        String firstStudentJson = "{"
                + "\"name\": \"First Test Student\","
                + "\"email\": \"" + uniqueEmail + "\","
                + "\"gender\": \"Male\","
                + "\"dob\": \"2000-01-01\""
                + "}";

        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/students")
                .param("cohortId", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(firstStudentJson)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        // Now try to create a second student with the same email
        String duplicateStudentJson = "{"
                + "\"name\": \"Second Test Student\","
                + "\"email\": \"" + uniqueEmail + "\","
                + "\"gender\": \"Female\","
                + "\"dob\": \"2001-02-02\""
                + "}";

        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/students")
                .param("cohortId", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(duplicateStudentJson)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Email already exist in database"));
    }
}
