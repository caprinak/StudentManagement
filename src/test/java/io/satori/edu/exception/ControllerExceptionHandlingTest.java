package io.satori.edu.exception;

import io.satori.edu.course.Course;
import io.satori.edu.result.Result;
import io.satori.edu.result.ResultId;
import io.satori.edu.student.Gender;
import io.satori.edu.student.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ControllerExceptionHandlingTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String getBaseUrl() {
        return "http://localhost:" + port + "/api/v1";
    }

    /*
     * StudentController Exception Tests
     */

    /**
     * Test EntityNotFoundException - Student not found
     */
    @Test
    public void testStudentNotFound() {
        // Request a non-existent student
        ResponseEntity<ErrorResponse> response = restTemplate.getForEntity(
                getBaseUrl() + "/students/9999",
                ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().getStatus()).isEqualTo(404);
        assertThat(response.getBody().getMessage()).contains("was not found");
    }

    /**
     * Test BadRequestException - Email already exists
     */
    @Test
    public void testStudentEmailAlreadyExists() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Use an email from existing data
        String duplicateEmailJson = "{"
                + "\"name\": \"Test Student\","
                + "\"email\": \"ntloc.developer@gmail.com\","
                + "\"gender\": \"Male\","
                + "\"dob\": \"2000-01-01\""
                + "}";

        HttpEntity<String> entity = new HttpEntity<>(duplicateEmailJson, headers);

        URI uri = UriComponentsBuilder
                .fromUriString(getBaseUrl() + "/students")
                .queryParam("cohortId", 1)
                .build()
                .toUri();

        ResponseEntity<ErrorResponse> response = restTemplate.exchange(
                uri,
                HttpMethod.POST,
                entity,
                ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getStatus()).isEqualTo(400);
        assertThat(response.getBody().getMessage()).contains("Email already exist");
    }

    /**
     * Test MissingServletRequestParameterException - Missing required parameter
     */
    @Test
    public void testStudentMissingRequiredParameter() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String studentJson = "{"
                + "\"name\": \"Test Student\","
                + "\"email\": \"new.test@example.com\","
                + "\"gender\": \"Male\","
                + "\"dob\": \"2000-01-01\""
                + "}";

        HttpEntity<String> entity = new HttpEntity<>(studentJson, headers);

        // Omit required cohortId parameter
        ResponseEntity<ErrorResponse> response = restTemplate.exchange(
                getBaseUrl() + "/students",
                HttpMethod.POST,
                entity,
                ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getStatus()).isEqualTo(400);
        assertThat(response.getBody().getMessage()).contains("Missing required parameter");
        assertThat(response.getBody().getErrors().get(0).getField()).isEqualTo("cohortId");
    }

    /**
     * Test MethodArgumentTypeMismatchException - Wrong parameter type
     */
    @Test
    public void testStudentWrongParameterType() {
        ResponseEntity<ErrorResponse> response = restTemplate.getForEntity(
                getBaseUrl() + "/students/faculty/abc", // Faculty ID should be a number
                ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getStatus()).isEqualTo(400);
        assertThat(response.getBody().getMessage()).isEqualTo("Type mismatch");
        assertThat(response.getBody().getErrors().get(0).getField()).isEqualTo("facultyId");
    }

    /*
     * CourseController Exception Tests
     */

    /**
     * Test validation constraints on Course entity
     */
    @Test
    public void testCourseValidationFailure() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Course name too short (violates @Size constraint)
        String invalidCourseJson = "{"
                + "\"name\": \"a\""
                + "}";

        HttpEntity<String> entity = new HttpEntity<>(invalidCourseJson, headers);

        URI uri = UriComponentsBuilder
                .fromUriString(getBaseUrl() + "/courses")
                .queryParam("facultyId", 1)
                .build()
                .toUri();

        ResponseEntity<ErrorResponse> response = restTemplate.exchange(
                uri,
                HttpMethod.POST,
                entity,
                ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getStatus()).isEqualTo(400);
        assertThat(response.getBody().getMessage()).isEqualTo("Validation failed");
        assertThat(response.getBody().getErrors()).hasSize(1);
        assertThat(response.getBody().getErrors().get(0).getField()).isEqualTo("name");
        assertThat(response.getBody().getErrors().get(0).getMessage()).contains("between 2 and 100");
    }

    /**
     * Test NoHandlerFoundException - Non-existent endpoint
     */
    @Test
    public void testNoHandlerFoundException() {
        ResponseEntity<ErrorResponse> response = restTemplate.getForEntity(
                getBaseUrl() + "/non-existent-path",
                ErrorResponse.class);

        assertThat(response.getStatusCode().is4xxClientError()).isTrue();
        // The exact behavior depends on how your application is configured
        // It might return 404 Not Found
    }

    /*
     * ResultController Exception Tests
     */

    /**
     * Test with invalid result data
     */
    @Test
    public void testResultInvalidData() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create invalid result (no studentId/courseId and negative grade)
        String invalidResultJson = "{"
                + "\"grade\": -1"
                + "}";

        HttpEntity<String> entity = new HttpEntity<>(invalidResultJson, headers);

        // This should fail due to invalid data
        URI uri = UriComponentsBuilder
                .fromUriString(getBaseUrl() + "/results")
                .queryParam("studentId", 9999) // Non-existent student ID
                .queryParam("courseId", 1)
                .build()
                .toUri();

        ResponseEntity<ErrorResponse> response = restTemplate.exchange(
                uri,
                HttpMethod.POST,
                entity,
                ErrorResponse.class);

        assertThat(response.getStatusCode().is4xxClientError()).isTrue();
    }

    /**
     * Test exception handling when updating a non-existent result
     */
    @Test
    public void testUpdateNonExistentResult() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Build URI for updating a non-existent result combination
        URI uri = UriComponentsBuilder
                .fromUriString(getBaseUrl() + "/results/student/9999/course/9999")
                .queryParam("grade", 10)
                .build()
                .toUri();

        ResponseEntity<ErrorResponse> response = restTemplate.exchange(
                uri,
                HttpMethod.PUT,
                new HttpEntity<>(headers),
                ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().getStatus()).isEqualTo(404);
    }
}
