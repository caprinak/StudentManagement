import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { stringify } from 'querystring';
import { Cohort } from 'src/app/interface/cohort';
import { Faculty } from 'src/app/interface/faculty';
import { CohortService } from 'src/app/service/cohort.service';
import { FacultyService } from 'src/app/service/faculty.service';

@Component({
  selector: 'app-cohort',
  templateUrl: './cohort.component.html',
  styleUrls: ['./cohort.component.css']
})
export class CohortComponent implements OnInit {

  public listCohort: Cohort[];
  public listFaculty: Faculty[];
  public cohort: Cohort;


  constructor(private cohortService: CohortService, private facultyService: FacultyService) { }

  ngOnInit(): void {
    this.getAllCohort();
    this.getAllFaculty();
  }

  public getCohort(cohort: Cohort)
  {
    this.cohort = cohort;
  }
  public getAllFaculty():void
  {
    this.facultyService.getAllFaculty().subscribe(
      (response: Faculty[]) => {
        this.listFaculty = response;
        console.log(this.listFaculty);
      },
      (error: HttpErrorResponse) => {
        alert(error.error.message);
      }
    )
  }

  public getAllCohort():void
  {
    this.cohortService.getAllCohort().subscribe(
      (response: Cohort[]) => {
        this.listCohort = response;
        console.log(this.listCohort);
      },
      (error: HttpErrorResponse) => {
        alert(error.error.message);
      }
    )
  }

  public addCohort(addForm: NgForm): void
  {
    document.getElementById('add-cohort-close').click();
    this.cohortService.addCohort(addForm.value,addForm.value.facultyId).subscribe(
      (response: Cohort) => {
        console.log(response);
        alert('Added cohort successfully')
        this.getAllCohort(); //reload list faculty
        addForm.reset();
      },
      (error: HttpErrorResponse) =>
      {
        alert(error.error.message);
      }
    );
  }

  public deleteCohort(cohortId: number): void
  {
    document.getElementById('btn-no-delete').click(); //when delete susscess, click close button to close the form
    this.cohortService.deleteCohort(cohortId).subscribe(
      (response: void) => {
        console.log(response);
        this.getAllCohort(); //reload list faculty
      },
      (error: HttpErrorResponse) =>
      {
       alert(error.error.message);
      }
    )
  }
  public updateCohort(updateForm: NgForm):void
  {
      document.getElementById('btn-edit-close').click();
      this.cohortService.updateCohort(updateForm.value.id,updateForm.value.name,updateForm.value.facultyId).subscribe(
        (response: Cohort) => {
          console.log(response);
          alert('Update successfully');
          this.getAllCohort();
        },
        (error: HttpErrorResponse) =>
        {
          alert(error.error.message);
        }
      )
  }

  public searchCohort(input: string):void
  {
    console.log(input);
    const result: Cohort[] = [];
    console.log(result);
    for(const cohort of this.listCohort) //loop of js
    {
      if(cohort.name.toLowerCase().indexOf(input.toLowerCase()) !== -1  //if cohort.name include input
      || cohort?.faculty?.name.toLowerCase().indexOf(input.toLowerCase()) !== -1)  //cohort?.faculty?.name
      {
        result.push(cohort); //push student in result array
      }

    }
    this.listCohort = result; //filter new list student when input match  cohort.name or cohort?.faculty?.name
    if(result.length === 0 || !input) //if result empty or input empty
    {
      this.getAllCohort(); //reload list student
    }
  }

}
