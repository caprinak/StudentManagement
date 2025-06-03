package io.satori.edu.exception;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test for ConstraintViolationException using a specialized REST controller
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ConstraintViolationExceptionTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    // A special controller just for this test
    @RestController
    @RequestMapping("/api/test/constraints")
    @Validated
    public static class ConstraintTestController {

        @GetMapping("/min/{id}")
        public ResponseEntity<String> testMinConstraint(
                @PathVariable @Min(value = 1, message = "ID must be at least 1") Integer id) {
            return ResponseEntity.ok("Valid ID: " + id);
        }

        @GetMapping("/size")
        public ResponseEntity<String> testSizeConstraint(
                @RequestParam @Size(min = 5, max = 50, message = "Name must be between 5 and 50 characters") String name) {
            return ResponseEntity.ok("Valid name: " + name);
        }

        @GetMapping("/notblank")
        public ResponseEntity<String> testNotBlankConstraint(
                @RequestParam @NotBlank(message = "Value cannot be blank") String value) {
            return ResponseEntity.ok("Valid value: " + value);
        }
    }

    private String getBaseUrl() {
        return "http://localhost:" + port + "/api/test/constraints";
    }

    /**
     * Tests @Min constraint on path variable
     */
    @Test
    public void testMinConstraintViolation() {
        // Call endpoint with invalid ID (less than 1)
        ResponseEntity<ErrorResponse> response = restTemplate.getForEntity(
                getBaseUrl() + "/min/0",
                ErrorResponse.class);

        // Assertions
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getStatus()).isEqualTo(400);
        assertThat(response.getBody().getMessage()).isEqualTo("Validation failed");
        assertThat(response.getBody().getErrors()).hasSize(1);
        assertThat(response.getBody().getErrors().get(0).getField()).contains("testMinConstraint.id");
        assertThat(response.getBody().getErrors().get(0).getMessage()).isEqualTo("ID must be at least 1");
    }

    /**
     * Tests @Size constraint on request parameter
     */
    @Test
    public void testSizeConstraintViolation() {
        // Call endpoint with invalid name (too short)
        ResponseEntity<ErrorResponse> response = restTemplate.getForEntity(
                getBaseUrl() + "/size?name=abc",
                ErrorResponse.class);

        // Assertions
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getStatus()).isEqualTo(400);
        assertThat(response.getBody().getMessage()).isEqualTo("Validation failed");
        assertThat(response.getBody().getErrors()).hasSize(1);
        assertThat(response.getBody().getErrors().get(0).getField()).contains("testSizeConstraint.name");
        assertThat(response.getBody().getErrors().get(0).getMessage()).contains("between 5 and 50");
    }

    /**
     * Tests @NotBlank constraint on request parameter
     */
    @Test
    public void testNotBlankConstraintViolation() {
        // Call endpoint with blank value
        ResponseEntity<ErrorResponse> response = restTemplate.getForEntity(
                getBaseUrl() + "/notblank?value=",
                ErrorResponse.class);

        // Assertions
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getStatus()).isEqualTo(400);
        assertThat(response.getBody().getMessage()).isEqualTo("Validation failed");
        assertThat(response.getBody().getErrors()).hasSize(1);
        assertThat(response.getBody().getErrors().get(0).getField()).contains("testNotBlankConstraint.value");
        assertThat(response.getBody().getErrors().get(0).getMessage()).isEqualTo("Value cannot be blank");
    }

    /**
     * Tests valid request for comparison
     */
    @Test
    public void testValidRequest() {
        // Call endpoint with valid value
        ResponseEntity<String> response = restTemplate.getForEntity(
                getBaseUrl() + "/size?name=ValidName",
                String.class);

        // Assertions
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Valid name: ValidName");
    }
}
