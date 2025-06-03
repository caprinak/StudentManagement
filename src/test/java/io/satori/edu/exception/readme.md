# Exception Handling Tests

## Overview

This directory contains tests for the global exception handling functionality in the application. The tests verify that all exception handlers in the `GlobalExceptionHandler` class properly catch and process exceptions, returning standardized error responses.

## Test Classes

1. **ControllerExceptionHandlingTest.java**
   - Integration tests that use TestRestTemplate to test real controllers
   - Tests StudentController, CourseController and ResultController endpoints
   - Verifies that exceptions are properly handled and formatted

2. **ConstraintViolationExceptionTest.java**
   - Specifically tests Bean Validation constraint violations
   - Uses a custom test controller with various constraints
   - Tests @Min, @Size, and @NotBlank constraints

## How to Run

The tests can be run as part of the standard test suite:

```bash
./mvnw test
```

Or individually using your IDE's test runner.

## Using Postman for Manual Testing

To test the exception handling using Postman, you can use the following requests:

### Entity Not Found Exception
```
GET http://localhost:8080/api/v1/students/999
```

### Validation Exception
```
POST http://localhost:8080/api/v1/students?cohortId=1
Content-Type: application/json

{
  "name": "",
  "email": "invalid-email",
  "gender": "Male",
  "dob": "2000-01-01"
}
```

### Bad Request Exception (Email Already Exists)
```
POST http://localhost:8080/api/v1/students?cohortId=1
Content-Type: application/json

{
  "name": "Test Student",
  "email": "ntloc.developer@gmail.com",
  "gender": "Male",
  "dob": "2000-01-01"
}
```

### Missing Parameter Exception
```
POST http://localhost:8080/api/v1/students
Content-Type: application/json

{
  "name": "Test Student",
  "email": "new.student@example.com",
  "gender": "Male",
  "dob": "2000-01-01"
}
```

### Type Mismatch Exception
```
GET http://localhost:8080/api/v1/students/faculty/abc
```

### Constraint Violation (Path Variable)
```
GET http://localhost:8080/api/test/constraints/min/0
```

### Constraint Violation (Request Parameter)
```
GET http://localhost:8080/api/test/constraints/size?name=ab
```

### Generic Exception
```
GET http://localhost:8080/api/test/exceptions/generic-exception
```

## Expected Response Format

```json
{
  "status": 400,
  "message": "Error message here",
  "errors": [
    {
      "field": "fieldName",
      "message": "Field-specific error message"
    }
  ],
  "timestamp": "2023-06-01 12:34:56"
}
```

## Note

When running these tests, make sure the application is properly initialized with test data from `DbConfiguration`.
