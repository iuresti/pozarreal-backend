import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {CourseAssistantPayment} from '../model/course-assistant-payment';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CoursePaymentService {

  constructor(private http: HttpClient) {
  }

  getAllPayments(courseAssistantId: string): Observable<CourseAssistantPayment[]> {
    return this.http.get<CourseAssistantPayment[]>(`${environment.baseUrl}/course-assistants-payment/assistant/${courseAssistantId}`);
  }

  save(payment: CourseAssistantPayment): Observable<CourseAssistantPayment> {
    if (payment.id) {
      return this.http.put<CourseAssistantPayment>(`${environment.baseUrl}/course-assistants-payment`, payment);
    }
    return this.http.post<CourseAssistantPayment>(`${environment.baseUrl}/course-assistants-payment`, payment);
  }

  deletePayment(courseAssistantPaymentId: string): Observable<void> {
    return this.http.delete<void>(`${environment.baseUrl}/course-assistants-payment/${courseAssistantPaymentId}`);
  }
}
