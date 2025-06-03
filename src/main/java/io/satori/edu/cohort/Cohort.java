package io.satori.edu.cohort;

import io.satori.edu.faculty.Faculty;
import io.satori.edu.student.Student;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Entity(name = "Cohort")
@Table(name = "cohort", uniqueConstraints = {
                                                @UniqueConstraint(name = "cohort_name_unique",
                                                columnNames = "name")
                                                }
)
public class Cohort  {

    @Id
    @SequenceGenerator(name = "cohort_sequence",
                        sequenceName = "cohort_sequence",
                        allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
                    generator = "cohort_sequence")
    @Column(name = "id",
            updatable = false,
            nullable = false)
    private int id;

    @NotBlank(message = "Cohort name is required")
    @Size(min = 2, max = 50, message = "Cohort name must be between 2 and 50 characters")
    @Column(name = "name",nullable = false)
    private String name;

    @OneToMany(mappedBy = "cohort", cascade = CascadeType.ALL)
    private List<Student> student;

    @ManyToOne
    @JoinColumn(name = "faculty_id", //column foreign key
                referencedColumnName = "id",
                foreignKey = @ForeignKey(name = "fk_cohort_faculty")) //name of foreign key
    private Faculty faculty;

    public Cohort() {
    }


    public Cohort(String name, Faculty faculty) {
        this.name = name;
        this.faculty = faculty;
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

    public Faculty getFaculty() {
        return faculty;
    }

    public void setFaculty(Faculty faculty) {
        this.faculty = faculty;
    }


    @Override
    public String toString() {
        return "Cohort{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", faculty=" + faculty +
                '}';
    }
}
