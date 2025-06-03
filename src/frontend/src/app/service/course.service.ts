import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Course } from '../interface/course';


@Injectable({
  providedIn: 'root'
})
export class CourseService {

  private backendURL = 'http://localhost:8080/api/v1/courses'; //URL to connect to backend;

  constructor(private http:HttpClient) { }

  public getAllCourse(): Observable<Course[]>
  {
    return this.http.get<Course[]>(`${this.backendURL}`);
  }

  public addCourse(course: Course, facultyId: number): Observable<Course>
  {
    const params = new HttpParams().set('facultyId', facultyId);
    return this.http.post<Course>(`${this.backendURL}/${facultyId}`,course, {params: params});
  }

  public deleteCourse(courseId: number): Observable<void>
  {
    return this.http.delete<void>(`${this.backendURL}/${courseId}`);
  }

  public updateCourse(courseId: number, name: string, facultyId: number): Observable<Course>
  {
    const params= new HttpParams().set('name',name).set('facultyId',facultyId);
    return this.http.put<Course>(`${this.backendURL}/${courseId}`,params);
  }
}
