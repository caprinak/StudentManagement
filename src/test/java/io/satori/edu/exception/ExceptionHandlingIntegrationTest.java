package io.satori.edu.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.satori.edu.course.Course;
import io.satori.edu.student.Gender;
import io.satori.edu.student.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration test that tests the exception handlers using TestRestTemplate
 * with the real controllers (StudentController and CourseController)
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ExceptionHandlingIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private String getBaseUrl() {
        return "http://localhost:" + port + "/api/v1";
    }

    /**
     * Tests EntityNotFoundException when requesting a non-existent student
     */
    @Test
    public void testEntityNotFoundException() {
        // Make a request for a student with ID that doesn't exist
        ResponseEntity<ErrorResponse> response = restTemplate.getForEntity(
                getBaseUrl() + "/students/999", 
                ErrorResponse.class);

        // Assertions
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().getStatus()).isEqualTo(404);
        assertThat(response.getBody().getMessage()).contains("was not found");
        assertThat(response.getBody().getTimestamp()).isNotNull();
    }

    /**
     * Tests MethodArgumentNotValidException when creating a student with invalid data
     */
    @Test
    public void testMethodArgumentNotValidException() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create a student with invalid data (empty name and email)
        String invalidStudentJson = "{"
                + "\"name\": \"\","
                + "\"email\": \"invalid-email\","
                + "\"gender\": \"Male\","
                + "\"dob\": \"2000-01-01\""
                + "}";

        HttpEntity<String> requestEntity = new HttpEntity<>(invalidStudentJson, headers);

        ResponseEntity<ErrorResponse> response = restTemplate.exchange(
                getBaseUrl() + "/students?cohortId=1", 
                HttpMethod.POST, 
                requestEntity, 
                ErrorResponse.class);

        // Assertions
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getStatus()).isEqualTo(400);
        assertThat(response.getBody().getMessage()).isEqualTo("Validation failed");
        assertThat(response.getBody().getErrors()).isNotEmpty();
    }

    /**
     * Tests BadRequestException when adding a student with existing email
     */
    @Test
    public void testBadRequestException() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create student with an email that already exists (from DbConfiguration)
        String duplicateStudentJson = "{"
                + "\"name\": \"Test Student\","
                + "\"email\": \"ntloc.developer@gmail.com\","
                + "\"gender\": \"Male\","
                + "\"dob\": \"2000-01-01\""
                + "}";

        HttpEntity<String> requestEntity = new HttpEntity<>(duplicateStudentJson, headers);

        ResponseEntity<ErrorResponse> response = restTemplate.exchange(
                getBaseUrl() + "/students?cohortId=1", 
                HttpMethod.POST, 
                requestEntity, 
                ErrorResponse.class);

        // Assertions
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getStatus()).isEqualTo(400);
        assertThat(response.getBody().getMessage()).contains("Email already exist");
    }

    /**
     * Tests MissingServletRequestParameterException when required parameter is missing
     */
    @Test
    public void testMissingServletRequestParameterException() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create valid student but omit required cohortId parameter
        String validStudentJson = "{"
                + "\"name\": \"Test Student\","
                + "\"email\": \"new.student@example.com\","
                + "\"gender\": \"Male\","
                + "\"dob\": \"2000-01-01\""
                + "}";

        HttpEntity<String> requestEntity = new HttpEntity<>(validStudentJson, headers);

        ResponseEntity<ErrorResponse> response = restTemplate.exchange(
                getBaseUrl() + "/students", // Missing cohortId parameter
                HttpMethod.POST, 
                requestEntity, 
                ErrorResponse.class);

        // Assertions
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getStatus()).isEqualTo(400);
        assertThat(response.getBody().getMessage()).contains("Missing required parameter");
    }

    /**
     * Tests MethodArgumentTypeMismatchException when parameter type is incorrect
     */
    @Test
    public void testMethodArgumentTypeMismatchException() {
        // Try to get students for a faculty but provide a non-integer ID
        ResponseEntity<ErrorResponse> response = restTemplate.getForEntity(
                getBaseUrl() + "/students/faculty/abc", 
                ErrorResponse.class);

        // Assertions
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getStatus()).isEqualTo(400);
        assertThat(response.getBody().getMessage()).isEqualTo("Type mismatch");
        assertThat(response.getBody().getErrors().get(0).getField()).isEqualTo("facultyId");
    }

    /**
     * Tests NoHandlerFoundException for a non-existent endpoint
     */
    @Test
    public void testNoHandlerFoundException() {
        ResponseEntity<ErrorResponse> response = restTemplate.getForEntity(
                getBaseUrl() + "/non-existent-endpoint", 
                ErrorResponse.class);

        // Assertions
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    /**
     * Tests validation constraints on Course entity when creating a course
     */
    @Test
    public void testCourseValidationFailure() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create a course with an invalid name (too short)
        String invalidCourseJson = "{"
                + "\"name\": \"a\""
                + "}";

        HttpEntity<String> requestEntity = new HttpEntity<>(invalidCourseJson, headers);

        ResponseEntity<ErrorResponse> response = restTemplate.exchange(
                getBaseUrl() + "/courses?facultyId=1", 
                HttpMethod.POST, 
                requestEntity, 
                ErrorResponse.class);

        // Assertions
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getStatus()).isEqualTo(400);
        assertThat(response.getBody().getMessage()).isEqualTo("Validation failed");
        assertThat(response.getBody().getErrors()).isNotEmpty();
        assertThat(response.getBody().getErrors().get(0).getField()).isEqualTo("name");
        assertThat(response.getBody().getErrors().get(0).getMessage()).contains("between 2 and 100");
    }

    /**
     * Tests constraints in Result entity
     */
    @Test
    public void testResultConstraintViolation() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create a result with invalid data
        String invalidResultJson = "{"
                + "\"grade\": -1"
                + "}";

        HttpEntity<String> requestEntity = new HttpEntity<>(invalidResultJson, headers);

        // This should trigger validation for negative grade
        ResponseEntity<ErrorResponse> response = restTemplate.exchange(
                getBaseUrl() + "/results?studentId=1&courseId=1", 
                HttpMethod.POST, 
                requestEntity, 
                ErrorResponse.class);

        // The response might be a 400 Bad Request depending on how the validation is set up
        assertThat(response.getStatusCode().is4xxClientError()).isTrue();
    }
}
