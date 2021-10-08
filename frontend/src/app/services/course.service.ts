import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable, of} from 'rxjs';
import {environment} from '../../environments/environment';
import {Course} from '../model/course';
import {tap} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class CourseService {

  private courses: Course[];

  constructor(private http: HttpClient) {
  }

  getAllCourses(): Observable<Course[]> {
    if(this.courses){
      return of(this.courses);
    }
    return this.http.get<Course[]>(`${environment.baseUrl}/courses`)
      .pipe(tap(courses => this.courses = courses));
  }
}
