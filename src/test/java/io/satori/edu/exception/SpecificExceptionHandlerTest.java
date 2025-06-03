package io.satori.edu.exception;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class SpecificExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * Tests APIEntityNotFoundException handler
     */
    @Test
    public void testAPIEntityNotFoundException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/test/exceptions/entity-not-found")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Test entity not found exception"));
    }

    /**
     * Tests BadRequestException handler
     */
    @Test
    public void testBadRequestException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/test/exceptions/bad-request")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Test bad request exception"));
    }

    /**
     * Tests ConstraintViolationException handler with path variable
     */
    @Test
    public void testConstraintViolationExceptionWithPathVariable() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/test/exceptions/constraint/0")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors[0].field").value("testConstraintViolation.id"))
                .andExpect(jsonPath("$.errors[0].message").value("ID must be greater than or equal to 1"));
    }

    /**
     * Tests ConstraintViolationException handler with request parameter
     */
    @Test
    public void testConstraintViolationExceptionWithRequestParam() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/test/exceptions/constraint-param?name=ab")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors[0].field").value("testConstraintViolationParam.name"))
                .andExpect(jsonPath("$.errors[0].message").value("Name must be between 3 and 20 characters"));
    }

    /**
     * Tests MethodArgumentNotValidException handler
     */
    @Test
    public void testMethodArgumentNotValidException() throws Exception {
        String invalidDtoJson = "{"
                + "\"name\": \"\","
                + "\"email\": \"test@example.com\""
                + "}";

        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/test/exceptions/validation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidDtoJson)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors[0].field").value("name"))
                .andExpect(jsonPath("$.errors[0].message").value("Name cannot be blank"));
    }

    /**
     * Tests generic exception handler (catch-all)
     */
    @Test
    public void testGenericExceptionHandler() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/test/exceptions/generic-exception")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.message").value("An unexpected error occurred"));
    }
}
