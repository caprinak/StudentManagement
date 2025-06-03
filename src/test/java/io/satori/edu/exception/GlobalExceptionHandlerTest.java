package io.satori.edu.exception;

import io.satori.edu.cohort.Cohort;
import io.satori.edu.cohort.CohortRepository;
import io.satori.edu.student.Gender;
import io.satori.edu.student.Student;
import io.satori.edu.student.StudentController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CohortRepository cohortRepository;

    /**
     * Tests EntityNotFoundException when trying to get a non-existent student
     */
    @Test
    public void testEntityNotFoundException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/students/999")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value(containsString("was not found")));
    }

    /**
     * Tests MethodArgumentNotValidException with invalid student data
     */
    @Test
    public void testMethodArgumentNotValidException() throws Exception {
        String invalidStudentJson = "{"
                + "\"name\": \"\","
                + "\"email\": \"invalid-email\","
                + "\"gender\": \"Male\","
                + "\"dob\": \"2000-01-01\""
                + "}";

        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/students")
                .param("cohortId", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidStudentJson)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Validation failed"));
    }

    /**
     * Tests BadRequestException when trying to add a student with existing email
     */
    @Test
    public void testBadRequestException() throws Exception {
        // Use existing email from DbConfiguration (Nguyen Thanh Loc's email)
        String duplicateEmailStudentJson = "{"
                + "\"name\": \"Test Student\","
                + "\"email\": \"ntloc.developer@gmail.com\","
                + "\"gender\": \"Male\","
                + "\"dob\": \"2000-01-01\""
                + "}";

        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/students")
                .param("cohortId", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(duplicateEmailStudentJson)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value(containsString("Email already exist")));
    }

    /**
     * Tests MissingServletRequestParameterException when required parameter is missing
     */
    @Test
    public void testMissingServletRequestParameterException() throws Exception {
        String validStudentJson = "{"
                + "\"name\": \"Test Student\","
                + "\"email\": \"new.student@example.com\","
                + "\"gender\": \"Male\","
                + "\"dob\": \"2000-01-01\""
                + "}";

        // Missing required cohortId parameter
        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(validStudentJson)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Missing required parameter"));
    }

    /**
     * Tests MethodArgumentTypeMismatchException when parameter type is incorrect
     */
    @Test
    public void testMethodArgumentTypeMismatchException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/students/faculty/abc") // Faculty ID should be a number
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Type mismatch"));
    }

    /**
     * Tests NoHandlerFoundException for a non-existent endpoint
     */
    @Test
    public void testNoHandlerFoundException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/non-existent-endpoint")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    /**
     * Tests ConstraintViolationException by triggering a validation constraint
     * Note: This requires a controller method with @Validated annotation and constraints
     */
    @Test
    public void testConstraintViolationException() throws Exception {
        // Create a controller method with custom validation if needed
        // For this example, we'll use a path variable with validation
        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/students/0") // Assuming 0 is invalid ID
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
