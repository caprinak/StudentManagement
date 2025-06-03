package io.satori.edu.dbconfiguration;


import io.satori.edu.cohort.Cohort;
import io.satori.edu.cohort.CohortRepository;
import io.satori.edu.course.Course;
import io.satori.edu.course.CourseRepository;
import io.satori.edu.faculty.Faculty;
import io.satori.edu.faculty.FacultyRepository;
import io.satori.edu.librarycard.LibraryCard;
import io.satori.edu.librarycard.LibraryCardRepository;
import io.satori.edu.result.Result;
import io.satori.edu.result.ResultId;
import io.satori.edu.result.ResultRepository;
import io.satori.edu.student.Gender;
import io.satori.edu.student.Student;
import io.satori.edu.student.StudentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@Configuration
public class DbConfiguration {

    @Bean
    CommandLineRunner commandLineRunner(StudentRepository studentRepository,
                                        CohortRepository cohortRepository,
                                        FacultyRepository facultyRepository,
                                        CourseRepository courseRepository,
                                        ResultRepository resultRepository,
                                        LibraryCardRepository libraryCardRepository)
    {
        return args ->  {

            //INSERT Faculty
            Faculty faculty_1  = new Faculty("Computer Science") ;
            Faculty faculty_2  = new Faculty("Data Science") ;
            Faculty faculty_3  = new Faculty("Fine Arts") ;
            facultyRepository.saveAll(List.of(faculty_1, faculty_2, faculty_3));

            //INSERT Cohort
            Cohort classCS101  = new Cohort("CS101", faculty_1);
            Cohort classCS202  = new Cohort("CS202", faculty_1);
            Cohort classDS101  = new Cohort("DS101", faculty_2);
            Cohort classDS202  = new Cohort("DS202", faculty_2);
            Cohort classART101 = new Cohort("ART101", faculty_3);

            cohortRepository.saveAll(List.of(classCS101, classCS202, classDS101, classDS202, classART101));

            //INSERT Student
            Student alex = new Student(
                    "Alex Johnson",
                    "alex.johnson@example.com",
                    Gender.Male,
                    LocalDate.of(2000, Month.APRIL, 15),
                    classCS101);
            Student emma = new Student(
                    "Emma Rodriguez",
                    "emma.r@example.com",
                    Gender.Female,
                    LocalDate.of(2001, Month.JANUARY, 22),
                    classCS101);
            Student michael = new Student(
                    "Michael Chen",
                    "mchen@example.com",
                    Gender.Male,
                    LocalDate.of(1999, Month.OCTOBER, 8),
                    classCS202);
            Student olivia = new Student(
                    "Olivia Williams",
                    "owilliams@example.com",
                    Gender.Female,
                    LocalDate.of(2000, Month.AUGUST, 30),
                    classCS202);
            Student daniel = new Student(
                    "Daniel Kim",
                    "dkim@example.com",
                    Gender.Male,
                    LocalDate.of(1998, Month.JULY, 14),
                    classDS101);
            Student sophia = new Student(
                    "Sophia Patel",
                    "spatel@example.com",
                    Gender.Female,
                    LocalDate.of(2001, Month.MARCH, 5),
                    classDS101);
            Student noah = new Student(
                    "Noah Garcia",
                    "ngarcia@example.com",
                    Gender.Male,
                    LocalDate.of(1999, Month.DECEMBER, 18),
                    classDS202);
            Student isabella = new Student(
                    "Isabella Martinez",
                    "imartinez@example.com",
                    Gender.Female,
                    LocalDate.of(2000, Month.JUNE, 27),
                    classART101);
            Student ethan = new Student(
                    "Ethan Brown",
                    "ebrown@example.com",
                    Gender.Male,
                    LocalDate.of(1997, Month.FEBRUARY, 11),
                    classART101);
            studentRepository.saveAll(List.of(alex, emma, michael, olivia, daniel, sophia, noah, isabella, ethan));

            //INSERT Library_card
            LibraryCard card_1 = new LibraryCard("1001", alex);
            LibraryCard card_2 = new LibraryCard("1002", emma);
            LibraryCard card_3 = new LibraryCard("1003", michael);
            LibraryCard card_4 = new LibraryCard("1004", olivia);
            LibraryCard card_5 = new LibraryCard("1005", daniel);
            LibraryCard card_6 = new LibraryCard("1006", sophia);
            LibraryCard card_7 = new LibraryCard("1007", noah);
            LibraryCard card_8 = new LibraryCard("1008", isabella);
            LibraryCard card_9 = new LibraryCard("1009", ethan);
            libraryCardRepository.saveAll(List.of(card_1, card_2, card_3, card_4, card_5, card_6, card_7, card_8, card_9));

            //INSERT Course
            Course course_1 = new Course("Introduction to Programming", faculty_1);
            Course course_2 = new Course("Data Structures and Algorithms", faculty_1);
            Course course_3 = new Course("Web Development", faculty_1);
            Course course_4 = new Course("Machine Learning Fundamentals", faculty_2);
            Course course_5 = new Course("Big Data Analytics", faculty_2);
            Course course_6 = new Course("Statistical Methods", faculty_2);
            Course course_7 = new Course("Drawing Fundamentals", faculty_3);
            Course course_8 = new Course("Art History", faculty_3);
            courseRepository.saveAll(List.of(course_1, course_2, course_3, course_4, course_5, course_6, course_7, course_8));

            //INSERT Result
            Result result_1 = new Result(new ResultId(1, 1), alex, course_1, 9);
            Result result_2 = new Result(new ResultId(2, 2), emma, course_2, 8);
            Result result_3 = new Result(new ResultId(3, 1), michael, course_1, 7);
            Result result_4 = new Result(new ResultId(1, 3), alex, course_3, 8);
            Result result_5 = new Result(new ResultId(5, 4), daniel, course_4, 9);
            Result result_6 = new Result(new ResultId(6, 5), sophia, course_5, 7);
            Result result_7 = new Result(new ResultId(7, 6), noah, course_6, 6);
            Result result_8 = new Result(new ResultId(8, 7), isabella, course_7, 10);
            Result result_9 = new Result(new ResultId(9, 8), ethan, course_8, 8);
            Result result_10 = new Result(new ResultId(4, 2), olivia, course_2, 9);
            Result result_11 = new Result(new ResultId(4, 3), olivia, course_3, 7);
            Result result_12 = new Result(new ResultId(5, 6), daniel, course_6, 8);
            Result result_13 = new Result(new ResultId(2, 3), emma, course_3, 9);
            Result result_14 = new Result(new ResultId(8, 8), isabella, course_8, 9);
            resultRepository.saveAll(List.of(result_1, result_2, result_3, result_4, result_5, result_6, result_7, 
                                             result_8, result_9, result_10, result_11, result_12, result_13, result_14));

        };
    }
}
