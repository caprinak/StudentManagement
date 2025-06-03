package io.satori.edu.student;


import io.satori.edu.cohort.Cohort;
import io.satori.edu.result.Result;
import io.satori.edu.librarycard.LibraryCard;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;


@Entity(name = "Student") //Mapping Student class to table in database which have column (id, name, email, dob)
@Table(name = "student",uniqueConstraints = {
                                            @UniqueConstraint(
                                                name = "student_email_unique",
                                                columnNames = "email")
                                            }
)
public class Student {

    @Id //id will be primary key of table student
    @SequenceGenerator(name = "student_sequence",
                        sequenceName = "student_sequence",
                        allocationSize = 1) //generate sequence with id auto increment begin 1
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
                    generator = "student_sequence") //use sequence is just defined above
    @Column(name = "id",
            updatable = false,
            nullable = false)
    private int id;

    @Column(name = "name",nullable = false)
    private String name;

    @Column(name = "email",nullable = false)
    private String email;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "gender",nullable = false)
    private Gender gender;

    @Column(name = "dob",nullable = false)
    private LocalDate dob;

//    @Transient //This annotation mean is no need column age in database because age will calculate by dob
//    private int age;

    @OneToOne(mappedBy = "student",cascade = CascadeType.ALL,orphanRemoval = true)
    private LibraryCard libraryCard;

    @ManyToOne
    @JoinColumn(name = "Cohort_id", //Cohort_id  column
                referencedColumnName = "id",
                foreignKey = @ForeignKey(name = "fk_Cohort"))// Foreign Key in student table
    private Cohort cohort;

    @OneToMany(mappedBy = "student",cascade = CascadeType.ALL)
    private List<Result> result;



    public Student() {}

    public Student(String name, String email, Gender gender, LocalDate dob, Cohort Cohort) {
        this.name = name;
        this.email = email;
        this.gender = gender;
        this.dob = dob;
        this.cohort = Cohort;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

//    public int getAge() {
//        return Period.between(this.dob,LocalDate.now()).getYears();
//    } //count age
//
//
//    public void setAge(int age) {
//        this.age = age;
//    }

    public Cohort getCohort() {
        return this.cohort;
    }

    public void setCohort(Cohort cohort) {
        this.cohort = cohort;
    }


    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", gender=" + gender +
                ", dob=" + dob +
                ", Cohort=" + cohort +
                '}';
    }
}
