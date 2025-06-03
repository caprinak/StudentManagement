# RESTful API Design Guidelines

## Introduction

This document outlines the RESTful API design principles used in our student management system. Following these guidelines ensures our APIs are intuitive, consistent, and follow industry best practices.

## Key Principles

### 1. Resource-Oriented URLs

URLs should identify resources (nouns), not actions or methods (verbs).

#### Good Examples:

```
/api/v1/students
/api/v1/students/42
/api/v1/courses
/api/v1/courses/123
```

#### Bad Examples:

```
/api/v1/get-students
/api/v1/add-course
/api/v1/students/delete/42
```

### 2. HTTP Methods

Use HTTP methods appropriately:

- `GET` - Retrieve resources
- `POST` - Create resources
- `PUT` - Update resources (full update)
- `PATCH` - Partial update of resources
- `DELETE` - Delete resources

### 3. Response Status Codes

Use appropriate HTTP status codes:

- `200 OK` - Request succeeded
- `201 Created` - Resource created successfully
- `204 No Content` - Success but no response body (e.g., after DELETE)
- `400 Bad Request` - Invalid input
- `404 Not Found` - Resource not found
- `500 Internal Server Error` - Server-side error

### 4. Resource Naming

- Use plural nouns for collections: `/students`, not `/student`
- Use kebab-case for multi-word resources: `/library-cards`, not `/libraryCards` or `/library_cards`
- Keep URLs as short as possible

### 5. API Versioning

Include API version in the URL path:

```
/api/v1/students
```

### 6. Query Parameters

Use query parameters for:

- Filtering: `/api/v1/students?grade=A`
- Sorting: `/api/v1/students?sort=lastName`
- Pagination: `/api/v1/students?page=2&size=10`

### 7. Relationships

Express relationships through URLs:

```
/api/v1/students/42/courses       # Courses taken by student 42
/api/v1/Cohorts/5/students     # Students in Cohort 5
```

### 8. Request & Response Format

- Use JSON for both requests and responses
- Use camelCase for JSON property names
- Include proper content type headers: `Content-Type: application/json`

### 9. Error Handling

Provide clear error messages with appropriate status codes:

```json
{
  "status": 400,
  "message": "Invalid input",
  "errors": [
    {
      "field": "email",
      "message": "Must be a valid email address"
    }
  ]
}
```

### 10. Documentation

Documentation should be:

- Clear and concise
- Include examples
- Describe all parameters and response formats
- Specify possible error codes and messages

## Examples

### Student Resource

#### Get all students

```
GET /api/v1/students
```

#### Get a specific student

```
GET /api/v1/students/42
```

#### Create a new student

```
POST /api/v1/students
Content-Type: application/json

{
  "name": "Jane Doe",
  "email": "jane.doe@example.com",
  "gender": "Female",
  "dob": "2000-05-15"
}
```

#### Update a student

```
PUT /api/v1/students/42
Content-Type: application/json

{
  "name": "Jane Smith",
  "email": "jane.smith@example.com"
}
```

#### Delete a student

```
DELETE /api/v1/students/42
```

## Conclusion

Following these RESTful API design principles ensures our APIs are intuitive, consistent, and maintainable. These guidelines should be applied to all new API development and used when refactoring existing APIs.
