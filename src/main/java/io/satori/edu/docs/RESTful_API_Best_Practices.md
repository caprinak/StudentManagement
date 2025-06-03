# RESTful API Best Practices

## Table of Contents
1. [Introduction](#introduction)
2. [URL Structure](#url-structure)
3. [HTTP Methods](#http-methods)
4. [Status Codes](#status-codes)
5. [Request and Response Format](#request-and-response-format)
6. [Error Handling](#error-handling)
7. [Validation](#validation)
8. [Security](#security)
9. [Versioning](#versioning)
10. [Documentation](#documentation)
11. [Examples](#examples)

## Introduction

This document outlines the best practices for designing and implementing RESTful APIs in Spring Boot applications. Following these guidelines ensures consistency, maintainability, and ease of use for API consumers.

## URL Structure

### Best Practices

- **Use Nouns, Not Verbs**: Resources should be named with plural nouns, not verbs
  - ✅ `/api/v1/students`
  - ❌ `/api/v1/getStudents`

- **Use Hierarchical Structure**: Represent resource relationships in the URL path
  - ✅ `/api/v1/faculties/{facultyId}/students`
  - ❌ `/api/v1/students?facultyId={facultyId}`

- **Maintain Consistency**: Use consistent naming conventions across all endpoints
  - Always use lowercase, kebab-case for multi-word resources
  - Use plural nouns for collection resources

- **Keep URLs Simple**: Avoid deep nesting beyond 2-3 levels
  - ✅ `/api/v1/faculties/{facultyId}/students`
  - ❌ `/api/v1/faculties/{facultyId}/departments/{departmentId}/students/{studentId}/courses`

## HTTP Methods

| Method   | Purpose                                      | Idempotent | Safe |
|----------|----------------------------------------------|------------|------|
| GET      | Retrieve resource(s)                         | Yes        | Yes  |
| POST     | Create a new resource                        | No         | No   |
| PUT      | Update a resource completely                 | Yes        | No   |
| PATCH    | Update a resource partially                  | No         | No   |
| DELETE   | Remove a resource                            | Yes        | No   |

### Usage Guidelines

- **GET**: Use for retrieving resources without side effects
  ```
  GET /api/v1/students       # Get all students
  GET /api/v1/students/{id}  # Get a specific student
  ```

- **POST**: Use for creating new resources
  ```
  POST /api/v1/students      # Create a new student
  ```

- **PUT**: Use for complete updates of existing resources
  ```
  PUT /api/v1/students/{id}  # Replace entire student resource
  ```

- **PATCH**: Use for partial updates of existing resources
  ```
  PATCH /api/v1/students/{id}  # Update specific fields of student
  ```

- **DELETE**: Use for removing resources
  ```
  DELETE /api/v1/students/{id}  # Remove a student
  ```

## Status Codes

### Common Status Codes

| Code | Description                | Usage                                          |
|------|----------------------------|------------------------------------------------|
| 200  | OK                         | Successful GET, PUT, PATCH                     |
| 201  | Created                    | Successful POST that created a new resource    |
| 204  | No Content                 | Successful DELETE or PUT with no response body |
| 400  | Bad Request                | Invalid request format or parameters           |
| 401  | Unauthorized               | Authentication required                        |
| 403  | Forbidden                  | Authentication succeeded but no permission     |
| 404  | Not Found                  | Resource not found                             |
| 409  | Conflict                   | Request conflicts with current state           |
| 422  | Unprocessable Entity       | Validation error                               |
| 500  | Internal Server Error      | Server-side error                              |

## Request and Response Format

### Request Body

- Use JSON for request bodies
- Use camelCase for property names
- Include only necessary fields
- For complex objects, consider using DTOs

### Response Format

```json
// Success response (200 OK)
{
  "id": 1,
  "name": "John Doe",
  "email": "john.doe@example.com",
  "createdAt": "2023-06-01T10:15:30Z"
}

// Collection response (200 OK)
[
  {
    "id": 1,
    "name": "John Doe"
  },
  {
    "id": 2,
    "name": "Jane Smith"
  }
]

// Error response (400, 404, 500, etc.)
{
  "status": 400,
  "message": "Validation failed",
  "errors": [
    {
      "field": "email",
      "message": "Invalid email format"
    }
  ],
  "timestamp": "2023-06-01T10:15:30Z"
}
```

## Error Handling

### Principles

1. **Be Specific**: Provide detailed error messages
2. **Be Consistent**: Use a consistent error format
3. **Include Context**: Add helpful context about what went wrong
4. **Don't Expose Internals**: Avoid exposing internal implementation details

### Implementation in Spring Boot

```java
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            ex.getMessage(),
            null,
            LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<Map<String, String>> errors = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(error -> {
                Map<String, String> errorDetails = new HashMap<>();
                errorDetails.put("field", error.getField());
                errorDetails.put("message", error.getDefaultMessage());
                return errorDetails;
            })
            .collect(Collectors.toList());

        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Validation failed",
            errors,
            LocalDateTime.now()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
```

## Validation

### Bean Validation

Use Jakarta Bean Validation annotations to validate request bodies:

```java
public class StudentDTO {
    @NotNull(message = "Name cannot be null")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @NotNull(message = "Email cannot be null")
    @Email(message = "Email should be valid")
    private String email;

    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    // getters and setters
}
```

### Controller Validation

Activate validation in controllers using `@Valid` annotation:

```java
@PostMapping
public ResponseEntity<Student> createStudent(@Valid @RequestBody StudentDTO studentDTO) {
    Student student = studentService.createStudent(studentDTO);
    return new ResponseEntity<>(student, HttpStatus.CREATED);
}
```

## Security

### Best Practices

1. **Use HTTPS**: Always use HTTPS in production
2. **Implement Authentication**: Use OAuth2, JWT, or API Keys
3. **Apply Authorization**: Restrict access based on roles/permissions
4. **Rate Limiting**: Protect against abuse
5. **Input Validation**: Validate all inputs to prevent injection attacks
6. **CORS Configuration**: Configure proper CORS settings

### Spring Security Example

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeRequests()
            .antMatchers("/api/v1/public/**").permitAll()
            .antMatchers("/api/v1/admin/**").hasRole("ADMIN")
            .anyRequest().authenticated()
            .and()
            .httpBasic();
    }
}
```

## Versioning

### API Versioning Strategies

1. **URI Path Versioning**: Include version in the URI path
   ```
   /api/v1/students
   /api/v2/students
   ```

2. **Query Parameter Versioning**: Include version as a query parameter
   ```
   /api/students?version=1
   /api/students?version=2
   ```

3. **Header Versioning**: Include version in a custom header
   ```
   X-API-Version: 1
   ```

4. **Accept Header Versioning**: Include version in the Accept header
   ```
   Accept: application/vnd.company.v1+json
   ```

### Recommended Approach

URI Path Versioning is recommended for its simplicity and explicitness:

```java
@RestController
@RequestMapping("/api/v1/students")
public class StudentControllerV1 {
    // V1 implementation
}

@RestController
@RequestMapping("/api/v2/students")
public class StudentControllerV2 {
    // V2 implementation
}
```

## Documentation

### Swagger/OpenAPI

Use Springdoc or Springfox to generate API documentation:

```java
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Student Management API")
                        .version("1.0")
                        .description("API for managing students and faculties")
                        .contact(new Contact()
                                .name("API Support")
                                .email("support@example.com")))
                .externalDocs(new ExternalDocumentation()
                        .description("Student Management Documentation")
                        .url("https://example.com/docs"));
    }
}
```

Add pom.xml dependency:

```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-ui</artifactId>
    <version>1.6.9</version>
</dependency>
```

## Examples

### Before Refactoring

```java
// Inconsistent URL structure
@RestController
@RequestMapping(path = "/api/spring-boot/faculty")
public class FacultyController {

    @GetMapping
    public List<Faculty> getFacultyList() {
        return facultyService.getFaculty();
    }

    @GetMapping(path = "/get/{facultyId}")
    public Faculty getOneFaculty(@PathVariable("facultyId") Integer facultyId) {
        return facultyService.getOneFaculty(facultyId);
    }

    @PostMapping(path = "/add")
    public void addFaculty(@RequestBody Faculty faculty) {
        facultyService.addFaculty(faculty);
    }

    @DeleteMapping(path = "/delete/{facultyId}")
    public void deleteFaculty(@PathVariable("facultyId") Integer facultyId) {
        facultyService.deleteFaculty(facultyId);
    }

    @PutMapping(path = "/update")
    public void updateFaculty(@RequestBody Faculty faculty) {
        facultyService.updateFaculty(faculty);
    }
}
```

### After Refactoring

```java
// Consistent RESTful URL structure
@RestController
@RequestMapping("/api/v1/faculties")
@CrossOrigin(origins = "*")
public class FacultyController {

    @GetMapping
    public ResponseEntity<List<Faculty>> getAllFaculties() {
        List<Faculty> faculties = facultyService.getFaculty();
        return ResponseEntity.ok(faculties);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Faculty> getFacultyById(@PathVariable("id") Integer id) {
        Faculty faculty = facultyService.getOneFaculty(id);
        return ResponseEntity.ok(faculty);
    }

    @PostMapping
    public ResponseEntity<Faculty> createFaculty(@Valid @RequestBody Faculty faculty) {
        facultyService.addFaculty(faculty);
        return new ResponseEntity<>(faculty, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Faculty> updateFaculty(
            @PathVariable("id") Integer id,
            @Valid @RequestBody Faculty faculty) {
        faculty.setId(id); // Ensure the ID matches the path variable
        facultyService.updateFaculty(faculty);
        return ResponseEntity.ok(faculty);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFaculty(@PathVariable("id") Integer id) {
        facultyService.deleteFaculty(id);
        return ResponseEntity.noContent().build();
    }
}
```

### Response Bodies Example

#### Success Response (200 OK)

```json
{
  "id": 1,
  "name": "Computer Science",
  "description": "Faculty of Computer Science and Engineering"
}
```

#### Created Response (201 Created)

```json
{
  "id": 2,
  "name": "Mathematics",
  "description": "Faculty of Mathematics and Statistics"
}
```

#### Error Response (404 Not Found)

```json
{
  "status": 404,
  "message": "Faculty with ID 999 not found",
  "errors": null,
  "timestamp": "2023-06-01T10:15:30Z"
}
```

---

This document provides comprehensive guidelines for implementing RESTful APIs in Spring Boot applications. Following these practices ensures that your APIs are consistent, maintainable, and easy to consume.
To make your API more RESTful and consistent, you might want to standardize your approach across all endpoints. Most modern RESTful APIs:
1. Use JSON for request bodies
2. Use query parameters sparingly, mainly for filtering, sorting, and pagination
3. For updates, send the full or partial resource in the request body

    - The Spring controller expects a JSON object in the request body, which it deserializes into your Student object
    - Query parameters need to be sent separately in the URL
    - That's why I suggested using the `{ params: params }` syntax for the addStudent method

But since your backend is already implemented this way, the frontend implementation needs to match what the backend expects.

