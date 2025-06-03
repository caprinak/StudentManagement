import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Cohort } from '../interface/cohort';

@Injectable({
  providedIn: 'root'
})
export class CohortService {

  private backendURL = 'http://localhost:8080/api/v1/cohorts'; //URL to connect to backend;

  constructor(private http:HttpClient) { }

  public getAllCohort(): Observable<Cohort[]>
  {
    return this.http.get<Cohort[]>(`${this.backendURL}`);
  }

  public addCohort(cohort: Cohort, facultyId: number): Observable<Cohort>
  {
    const params = new HttpParams().set('facultyId', facultyId);
    return this.http.post<Cohort>(`${this.backendURL}`,cohort, {params: params});
  }

  public deleteCohort(cohortId: number): Observable<void>
  {
    return this.http.delete<void>(`${this.backendURL}/${cohortId}`);
  }

  public updateCohort(cohortId: number, name: string, facultyId: number): Observable<Cohort>
  {
    const params= new HttpParams().set('name',name).set('facultyId',facultyId);
    return this.http.put<Cohort>(`${this.backendURL}/${cohortId}`,params);
  }

}
