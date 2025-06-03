package io.satori.edu.student;


import io.satori.edu.cohort.CohortRepository;
import io.satori.edu.exception.APIEntityNotFoundException;
import io.satori.edu.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service //this annotation marks a Java class that performs some service, such as execute business logic
public class StudentService {

    private final StudentRepository studentRepository;
    private final CohortRepository cohortRepository;

    @Autowired //studentRepository autowired inject into StudentService
    public StudentService(StudentRepository repository, CohortRepository cohortRepository) {
        this.studentRepository = repository;
        this.cohortRepository = cohortRepository;
    }

    public List<Student> getStudent()
    {
        return studentRepository.findAll();
    }

    public List<Student> getStudentInWhichFaculty(Integer facultyId)
    {
        return studentRepository.findStudentInWhichFaculty(facultyId);
    }

    public Student getStudentById(Integer studentId)
    {
        return studentRepository.findById(studentId).orElseThrow(() //if(studentRepository.findById(studentId) == true) return Student else Error Exception
                -> new APIEntityNotFoundException("student with id "+studentId+ " was not found"));

    }

//    public Student addStudent(Student student, Integer CohortId) {
//        Optional<Student> studentByEmail = studentRepository.findStudentByEmail(student.getEmail());
//        if(studentByEmail.isPresent()) //if email of student is present in database
//        {
//            throw new IllegalStateException("Email already exist in database");
//        }
//        boolean exists = CohortRepository.existsById(CohortId);
//        if(!exists)
//        {
//            throw new APIEntityNotFoundException("Cohort with id "+CohortId+ " was not found");
//        }
//        student.setCohort(CohortRepository.getById(CohortId));
//        studentRepository.save(student);
//        System.out.println(student);
//        return student;
//    }

    public void addStudent(Student student,Integer CohortId) {
        Optional<Student> studentByEmail = studentRepository.findStudentByEmail(student.getEmail());
        if(studentByEmail.isPresent()) //if email of student is present in database
        {
            throw new BadRequestException("Email already exist in database");
        }
        boolean exists = cohortRepository.existsById(CohortId);
        if(!exists)
        {
            throw new APIEntityNotFoundException("Cohort with id "+CohortId+ " was not found");
        }
        student.setCohort(cohortRepository.getById(CohortId));
        studentRepository.save(student);
        System.out.println(student);
    }

    public void deleteStudent(Integer studentId)
    {
        boolean exists = studentRepository.existsById(studentId);
        if(!exists)
        {
            throw new APIEntityNotFoundException("student with id = "+studentId+ " does not exist in database");
        }
        System.out.println("Student id = "+studentId+" has been delete");
        studentRepository.deleteById(studentId);
    }

    public void updateStudent(Integer studentId, String name, String email, Gender gender, LocalDate dob, Integer CohortId)
    {
        //Check studentId in database
        Student student = studentRepository.findById(studentId).orElseThrow(()
                -> new APIEntityNotFoundException("student with id "+studentId+ " does not exist"));

        //update name
        if(name != null && name.length() > 0 && !Objects.equals(student.getName(),name)) //if the new name has been provided is not the same name in database
        {
            student.setName(name);
        }
        //update email (Objects.equals(student.getEmail(),email) == false)
        if(email != null && email.length() > 0 && !Objects.equals(student.getEmail(),email)) //if the new email has been provided is not the same email current one in database
        {
            Optional<Student> studentByEmail = studentRepository.findStudentByEmail(email);
            if(studentByEmail.isPresent()) //if new email of student is present in database
            {
                throw new BadRequestException("Email already exist in database");
               //throw new IllegalStateException("Email already exist in database");
            }
            student.setEmail(email);
        }
        //update gender
        if(gender != null && !Objects.equals(student.getGender(),gender)) //if the new CohortId has been provided is not the same CohortId in database
        {
            student.setGender(gender);
        }
        //update dob
        if(dob != null && !Objects.equals(student.getDob(),dob)) //if the new name has been provided is not the same name in database
        {
            student.setDob(dob);
        }
        //update Cohort
        if(CohortId != null && !Objects.equals(student.getCohort().getId(),CohortId)) //if the new CohortId has been provided is not the same CohortId in database
        {
            student.setCohort(cohortRepository.getById(CohortId));
        }
        studentRepository.save(student);
    }


}
