# API Endpoints List

## Base URL: http://localhost:8080/api/v1

## Students

```
GET    /students                   # Get all students
GET    /students/{id}              # Get student by ID
GET    /students/faculty/{facultyId} # Get students by faculty ID
POST   /students?cohortId={id}  # Create student
PUT    /students/{id}              # Update student
DELETE /students/{id}              # Delete student
```

## Courses

```
GET    /courses                    # Get all courses
GET    /courses/{id}               # Get course by ID
POST   /courses?facultyId={id}     # Create course
PUT    /courses/{id}               # Update course
DELETE /courses/{id}               # Delete course
```

## Cohorts

```
GET    /cohorts                 # Get all cohorts
GET    /cohorts/{id}            # Get cohort by ID
POST   /cohorts?facultyId={id}  # Create cohort
PUT    /cohorts/{id}            # Update cohort
DELETE /cohorts/{id}            # Delete cohort
```

## Library Cards

```
GET    /library-cards              # Get all library cards
GET    /library-cards/{id}         # Get library card by ID
POST   /library-cards              # Create library card
PUT    /library-cards/{id}         # Update library card
DELETE /library-cards/{id}         # Delete library card
```

## Results

```
GET    /results                    # Get all results
GET    /results/student/{studentId}/course/{courseId} # Get result by student and course
GET    /results/grade/{minGrade}   # Get results by minimum grade
POST   /results?studentId={id}&courseId={id} # Create result
PUT    /results/student/{studentId}/course/{courseId} # Update result
DELETE /results/student/{studentId}/course/{courseId} # Delete result
```

## Faculties

```
GET    /faculties                  # Get all faculties
GET    /faculties/{id}             # Get faculty by ID
POST   /faculties                  # Create faculty
PUT    /faculties/{id}             # Update faculty
DELETE /faculties/{id}             # Delete faculty
```
