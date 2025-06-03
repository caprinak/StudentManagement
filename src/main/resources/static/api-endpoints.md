# Student Management System API Endpoints

## Base URL

```
http://localhost:8080/api/v1
```

## Authentication

All endpoints are currently open and do not require authentication.

## Response Formats

- Successful responses return HTTP status codes 200, 201, or 204
- Error responses return appropriate status codes (400, 404, 500) with error messages
- Data is returned in JSON format

## Students

### Get All Students

```
GET /students
```

**Response:**
```json
[
  {
    "id": 1,
    "name": "John Doe",
    "email": "john.doe@example.com",
    "gender": "Male",
    "dob": "1999-09-28",
    "Cohort": {
      "id": 1,
      "name": "DCT1171"
    }
  },
  {...}
]
```

### Get Student by ID

```
GET /students/{id}
```

**Response:**
```json
{
  "id": 1,
  "name": "John Doe",
  "email": "john.doe@example.com",
  "gender": "Male",
  "dob": "1999-09-28",
  "Cohort": {
    "id": 1,
    "name": "DCT1171"
  }
}
```

### Get Students by Faculty

```
GET /students/faculty/{facultyId}
```

**Response:**
```json
[
  {
    "id": 1,
    "name": "John Doe",
    "email": "john.doe@example.com",
    "gender": "Male",
    "dob": "1999-09-28",
    "Cohort": {
      "id": 1,
      "name": "DCT1171"
    }
  },
  {...}
]
```

### Create Student

```
POST /students?CohortId={CohortId}
```

**Request Body:**
```json
{
  "name": "Jane Doe",
  "email": "jane.doe@example.com",
  "gender": "Female",
  "dob": "2000-05-15"
}
```

**Response Status:** 201 Created

### Update Student

```
PUT /students/{id}?name={name}&email={email}&gender={gender}&dob={YYYY-MM-DD}&CohortId={CohortId}
```

All query parameters are optional. Only include the ones you want to update.

**Response Status:** 200 OK

### Delete Student

```
DELETE /students/{id}
```

**Response Status:** 204 No Content

## Courses

### Get All Courses

```
GET /courses
```

**Response:**
```json
[
  {
    "id": 1,
    "name": "Programming techniques",
    "faculty": {
      "id": 1,
      "name": "Information Technology"
    }
  },
  {...}
]
```

### Get Course by ID

```
GET /courses/{id}
```

**Response:**
```json
{
  "id": 1,
  "name": "Programming techniques",
  "faculty": {
    "id": 1,
    "name": "Information Technology"
  }
}
```

### Create Course

```
POST /courses?facultyId={facultyId}
```

**Request Body:**
```json
{
  "name": "Advanced Java Programming"
}
```

**Response Status:** 201 Created

### Update Course

```
PUT /courses/{id}?name={name}&facultyId={facultyId}
```

All query parameters are optional. Only include the ones you want to update.

**Response Status:** 200 OK

### Delete Course

```
DELETE /courses/{id}
```

**Response Status:** 204 No Content

## Cohorts

### Get All Cohorts

```
GET /Cohorts
```

**Response:**
```json
[
  {
    "id": 1,
    "name": "DCT1171",
    "faculty": {
      "id": 1,
      "name": "Information Technology"
    }
  },
  {...}
]
```

### Get Cohort by ID

```
GET /Cohorts/{id}
```

**Response:**
```json
{
  "id": 1,
  "name": "DCT1171",
  "faculty": {
    "id": 1,
    "name": "Information Technology"
  }
}
```

### Create Cohort

```
POST /Cohorts?facultyId={facultyId}
```

**Request Body:**
```json
{
  "name": "DCT1175"
}
```

**Response Status:** 201 Created

### Update Cohort

```
PUT /Cohorts/{id}?name={name}&facultyId={facultyId}
```

All query parameters are optional. Only include the ones you want to update.

**Response Status:** 200 OK

### Delete Cohort

```
DELETE /Cohorts/{id}
```

**Response Status:** 204 No Content

## Library Cards

### Get All Library Cards

```
GET /library-cards
```

**Response:**
```json
[
  {
    "id": 1,
    "card_number": "1001",
    "student": {
      "id": 1,
      "name": "John Doe"
    }
  },
  {...}
]
```

### Get Library Card by ID

```
GET /library-cards/{id}
```

**Response:**
```json
{
  "id": 1,
  "card_number": "1001",
  "student": {
    "id": 1,
    "name": "John Doe"
  }
}
```

### Create Library Card

```
POST /library-cards
```

**Request Body:**
```json
{
  "cardNumber": "1050",
  "studentId": 5
}
```

**Response:**
```json
{
  "id": 9,
  "card_number": "1050",
  "student": {
    "id": 5,
    "name": "Jane Smith"
  }
}
```

### Update Library Card

```
PUT /library-cards/{id}
```

**Request Body:**
```json
{
  "cardNumber": "1051",
  "studentId": 5
}
```

**Response:**
```json
{
  "id": 9,
  "card_number": "1051",
  "student": {
    "id": 5,
    "name": "Jane Smith"
  }
}
```

### Delete Library Card

```
DELETE /library-cards/{id}
```

**Response Status:** 204 No Content

## Results

### Get All Results

```
GET /results
```

**Response:**
```json
[
  {
    "id": {
      "studentId": 1,
      "courseId": 1
    },
    "student": {
      "id": 1,
      "name": "John Doe"
    },
    "course": {
      "id": 1,
      "name": "Programming techniques"
    },
    "grade": 8
  },
  {...}
]
```

### Get Result by Student and Course

```
GET /results/student/{studentId}/course/{courseId}
```

**Response:**
```json
{
  "id": {
    "studentId": 1,
    "courseId": 1
  },
  "student": {
    "id": 1,
    "name": "John Doe"
  },
  "course": {
    "id": 1,
    "name": "Programming techniques"
  },
  "grade": 8
}
```

### Get Results by Grade Threshold

```
GET /results/grade/{minGrade}
```

**Response:**
```json
[
  {
    "id": {
      "studentId": 1,
      "courseId": 1
    },
    "student": {
      "id": 1,
      "name": "John Doe"
    },
    "course": {
      "id": 1,
      "name": "Programming techniques"
    },
    "grade": 8
  },
  {...}
]
```

### Create Result

```
POST /results?studentId={studentId}&courseId={courseId}
```

**Request Body:**
```json
{
  "grade": 7
}
```

**Response Status:** 201 Created

### Update Result

```
PUT /results/student/{studentId}/course/{courseId}?grade={grade}
```

**Response Status:** 200 OK

### Delete Result

```
DELETE /results/student/{studentId}/course/{courseId}
```

**Response Status:** 204 No Content

## Faculties

### Get All Faculties

```
GET /faculties
```

**Response:**
```json
[
  {
    "id": 1,
    "name": "Information Technology"
  },
  {
    "id": 2,
    "name": "Business Administration"
  }
]
```

### Get Faculty by ID

```
GET /faculties/{id}
```

**Response:**
```json
{
  "id": 1,
  "name": "Information Technology"
}
```

### Create Faculty

```
POST /faculties
```

**Request Body:**
```json
{
  "name": "Engineering"
}
```

**Response Status:** 201 Created

### Update Faculty

```
PUT /faculties/{id}?name={name}
```

**Response Status:** 200 OK

### Delete Faculty

```
DELETE /faculties/{id}
```

**Response Status:** 204 No Content

## Error Responses

All endpoints may return the following error responses:

### 400 Bad Request

```json
{
  "field1": "Error message for field1",
  "field2": "Error message for field2"
}
```

### 404 Not Found

```json
{
  "message": "Resource with id X was not found"
}
```

### 500 Internal Server Error

```json
{
  "message": "An unexpected error occurred"
}
```

## Notes for Frontend Developers

1. All endpoints support CORS for local development.
2. Date fields should be formatted as ISO-8601: `YYYY-MM-DD`.
3. For endpoints with multiple query parameters, you can omit any parameters that aren't needed for your update.
4. When displaying data, handle null values gracefully in the UI.
5. Implement proper error handling for all API requests.

## Example Usage (Angular Service)

```typescript
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class StudentService {
  private baseUrl = 'http://localhost:8080/api/v1/students';

  constructor(private http: HttpClient) { }

  getStudents(): Observable<any> {
    return this.http.get(this.baseUrl);
  }

  getStudentById(id: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/${id}`);
  }

  createStudent(student: Object, CohortId: number): Observable<any> {
    return this.http.post(`${this.baseUrl}?CohortId=${CohortId}`, student);
  }

  updateStudent(id: number, value: any): Observable<any> {
    let url = `${this.baseUrl}/${id}?`;

    if (value.name) url += `name=${value.name}&`;
    if (value.email) url += `email=${value.email}&`;
    if (value.gender) url += `gender=${value.gender}&`;
    if (value.dob) url += `dob=${value.dob}&`;
    if (value.CohortId) url += `CohortId=${value.CohortId}`;

    return this.http.put(url, {});
  }

  deleteStudent(id: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${id}`);
  }
}
```
