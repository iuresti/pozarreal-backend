import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Chip} from '../model/chip';
import {environment} from '../../environments/environment';
import {Course} from '../model/course';

@Injectable({
  providedIn: 'root'
})
export class CourseService {

  constructor(private http: HttpClient) {
  }

  getAllCourses(): Observable<Course[]> {
    return this.http.get<Course[]>(`${environment.baseUrl}/courses`);
  }
}
