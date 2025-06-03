package io.satori.edu.exception;

import io.satori.edu.student.Gender;
import io.satori.edu.student.Student;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller specifically designed to test different exception scenarios
 */
@RestController
@RequestMapping("/api/test/exceptions")
@Validated
public class TestControllerForExceptions {

    /**
     * Endpoint that throws EntityNotFoundException
     */
    @GetMapping("/entity-not-found")
    public ResponseEntity<String> testEntityNotFoundException() {
        throw new APIEntityNotFoundException("Test entity not found exception");
    }

    /**
     * Endpoint that throws BadRequestException
     */
    @GetMapping("/bad-request")
    public ResponseEntity<String> testBadRequestException() {
        throw new BadRequestException("Test bad request exception");
    }

    /**
     * Endpoint that triggers ConstraintViolationException with path variable
     */
    @GetMapping("/constraint/{id}")
    public ResponseEntity<String> testConstraintViolation(
            @PathVariable @Min(value = 1, message = "ID must be greater than or equal to 1") Integer id) {
        return ResponseEntity.ok("ID: " + id);
    }

    /**
     * Endpoint that triggers ConstraintViolationException with request parameter
     */
    @GetMapping("/constraint-param")
    public ResponseEntity<String> testConstraintViolationParam(
            @RequestParam @Size(min = 3, max = 20, message = "Name must be between 3 and 20 characters") String name) {
        return ResponseEntity.ok("Name: " + name);
    }

    /**
     * Endpoint that triggers MethodArgumentNotValidException
     */
    @PostMapping("/validation")
    public ResponseEntity<String> testValidation(@Valid @RequestBody ValidationTestDTO dto) {
        return ResponseEntity.ok("Valid DTO: " + dto.getName());
    }

    /**
     * Endpoint that throws a generic exception to test the fallback handler
     */
    @GetMapping("/generic-exception")
    public ResponseEntity<String> testGenericException() {
        throw new RuntimeException("This is a test exception");
    }

    /**
     * DTO for validation testing
     */
    public static class ValidationTestDTO {
        @NotBlank(message = "Name cannot be blank")
        @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
        private String name;

        private String email;

        public ValidationTestDTO() {
        }

        public ValidationTestDTO(String name, String email) {
            this.name = name;
            this.email = email;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
}
