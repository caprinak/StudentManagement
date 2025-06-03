import { HttpErrorResponse } from '@angular/common/http';
import { Component, Input, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Gender } from 'src/app/enum/gender.enum';
import { Cohort } from 'src/app/interface/cohort';
import { Student } from 'src/app/interface/student';
import { CohortService } from 'src/app/service/cohort.service';
import { StudentService } from 'src/app/service/student.service';

@Component({
  selector: 'app-student',
  templateUrl: './student.component.html',
  styleUrls: ['./student.component.css']
})
export class StudentComponent implements OnInit {

  public listCohort: Cohort[];
  public listStudent: Student[];
  public student: Student;
   selectedCohortId: number | null = null;


  //convert enum like JS object
  keys = Object.keys;
  genders = Gender;;


  constructor(private studentService: StudentService, private calssroomService: CohortService) { }

  ngOnInit(): void {
    this.getAllStudent();
    this.getAllCohort();
    //  // If editing existing student
    //     if (this.student && this.student.cohort) {
    //         this.selectedCohortId = this.student.cohort.id;
    //     }

  }
  //  // When loading student data
  //   loadStudent(id: number) {
  //       this.studentService.getStudentById(id).subscribe(
  //           (student) => {
  //               this.student = student;
  //               this.selectedCohortId = student.cohort?.id || null;
  //           }
  //       );
  //   }


  public getStudent(student: Student)
  {
    this.student = student;
  }
  public getAllCohort():void
  {
      this.calssroomService.getAllCohort().subscribe(
        (response: Cohort[]) => {
          this.listCohort = response;
          console.log(this.listCohort);
        },
        (error: HttpErrorResponse) => {
          alert(error.error.message);
        }
      )
  }

  public getAllStudent():void
  {
      this.studentService.getAllStudent().subscribe(
        (response: Student[]) => {
          this.listStudent = response;
          console.log(this.listStudent);
        },
        (error: HttpErrorResponse) => {
          alert(error.error.message);
        }
      );
  }

  public addStudent(addForm: NgForm):void
  {
    document.getElementById('add-student-close').click();
    this.studentService.addStudent(addForm.value,addForm.value.cohortId ).subscribe(
      (response: Student) => {
        console.log(response);
        alert('Added student successfully')
        this.getAllStudent(); //reload list faculty
        addForm.reset();
      },
      (error: HttpErrorResponse) => {
          alert(error.error.message);
      }
    );
  }

  public deleteStudent(studentId: number): void
  {
    document.getElementById('btn-no-delete').click(); //when delete susscess, click close button to close the form
    this.studentService.deleteStudent(studentId).subscribe(
      (response: void) => {
        console.log(response);
        this.getAllStudent(); //reload list faculty
      },
      (error: HttpErrorResponse) =>
      {
        alert(error.error.message);
      }
    )
  }
  public updateStudent(updateForm: NgForm):void
  {
      document.getElementById('btn-edit-close').click();
      this.studentService.updateStudent(updateForm.value.id,updateForm.value.name,
                                      updateForm.value.email,updateForm.value.gender,
                                      updateForm.value.dob,updateForm.value.cohortId).subscribe(
        (response: Student) => {
          console.log(response);
          alert('Update successfully');
          this.getAllStudent();
        },
        (error: HttpErrorResponse) =>
        {
         alert(error.error.message);
        }
      )
  }

  public searchStudent(input: string):void
  {
    console.log(input);
    const result: Student[] = [];
    console.log(result);
    for(const student of this.listStudent) //loop of js
    {
      if(student.name.toLowerCase().indexOf(input.toLowerCase()) !== -1  //if student.name include input
      || student.email.toLowerCase().indexOf(input.toLowerCase()) !== -1  //if student.email include input
      || student?.cohort?.name.toLowerCase().indexOf(input.toLowerCase()) !== -1)  //if student?.cohort?.name include input
      {
        result.push(student); //push student in result array
      }

    }
    this.listStudent = result; //filter new list student when input match student.name or student.email
    if(result.length === 0 || !input) //if result empty or input empty
    {
      this.getAllStudent(); //reload list student
    }
  }

}
