import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {environment} from '../../environments/environment';
import {CourseAssistant} from '../model/course-assistant';

@Injectable({
  providedIn: 'root'
})
export class CourseAssistantService {

  constructor(private http: HttpClient) {
  }

  getCourseAssistants(courseId: string): Observable<CourseAssistant[]> {
    return this.http.get<CourseAssistant[]>(`${environment.baseUrl}/course-assistants/course/${courseId}`);
  }

  save(newStudent: CourseAssistant): Observable<CourseAssistant> {
    if (newStudent.id) {
      return this.http.put<CourseAssistant>(`${environment.baseUrl}/course-assistants`, newStudent);
    }
    return this.http.post<CourseAssistant>(`${environment.baseUrl}/course-assistants`, newStudent);
  }

  delete(id: string): Observable<void> {
    return this.http.delete<void>(`${environment.baseUrl}/course-assistants/${id}`);
  }
}
