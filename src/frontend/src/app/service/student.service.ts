import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Gender } from '../enum/gender.enum';
import { Student } from '../interface/student';

@Injectable({
  providedIn: 'root'
})
export class StudentService {

  private backendURL = 'http://localhost:8080/api/v1/students'; //URL to connect to backend;

  constructor(private http: HttpClient) { }

  public getAllStudent(): Observable<Student[]> //get All Student from backend
  {
    return this.http.get<Student[]>(`${this.backendURL}`); //localhost:8080/spring-boot/student
  }

  public addStudent(student: Student, cohortId: number): Observable<Student>
  {
    const params = new HttpParams()
      .set('cohortId', cohortId);
    return this.http.post<Student>(`${this.backendURL}`,student,{params: params});
  }

  public deleteStudent(studentId: number): Observable<void>
  {
    return this.http.delete<void>(`${this.backendURL}/${studentId}`);
  }

  public updateStudent(studentId: number, name: string, email: string,gender: Gender, dob: Date, cohortId: number): Observable<Student>
  {
    const params= new HttpParams().set('name',name).set('email',email).set('gender',gender).set('dob',dob.toString()).set('cohortId',cohortId);
    return this.http.put<Student>(`${this.backendURL}/${studentId}`,params);
  }
}
