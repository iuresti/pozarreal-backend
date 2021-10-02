import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {PaymentFilter} from '../model/payment-filter';
import {Observable} from 'rxjs';
import {PaymentView} from '../model/payment-view';
import {environment} from '../../environments/environment';
import {PaymentConcept} from '../model/payment-concept';
import {Payment} from '../model/payment';
import {PaymentSubConcept} from '../model/payment-sub-concept';

@Injectable({
  providedIn: 'root'
})
export class PaymentService {

  constructor(private http: HttpClient) {
  }

  public getPayments(paymentFilter: PaymentFilter): Observable<PaymentView[]> {
    let queryParams = `rand=${Math.random()}`;

    if (paymentFilter.street) {
      queryParams += `&street=${paymentFilter.street}`;
    }

    if (paymentFilter.house) {
      queryParams += `&house=${paymentFilter.house}`;
    }

    if (paymentFilter.paymentMode) {
      queryParams += `&paymentMode=${paymentFilter.paymentMode}`;
    }

    if (paymentFilter.startDate) {
      queryParams += `&startDate=${paymentFilter.startDate}`;
    }

    if (paymentFilter.endDate) {
      queryParams += `&endDate=${paymentFilter.endDate}`;
    }

    if (paymentFilter.concepts && paymentFilter.concepts.length) {
      queryParams += `&concepts=${paymentFilter.concepts.join(',')}`;
    }

    return this.http.get<PaymentView[]>(`${environment.baseUrl}/payments?${queryParams}`);
  }

  getPaymentConcepts(): Observable<PaymentConcept[]> {
    return this.http.get<PaymentConcept[]>(`${environment.baseUrl}/paymentConcepts`);
  }

  getPaymentSubConcepts(paymentConceptId: string): Observable<PaymentSubConcept[]> {
    return this.http.get<PaymentSubConcept[]>(`${environment.baseUrl}/paymentSubConcepts/concept/${paymentConceptId}`);
  }

  save(newPayment: Payment): Observable<Payment> {
    return this.http.post<Payment>(`${environment.baseUrl}/payments`, newPayment);
  }

  delete(id: string): Observable<void> {
    return this.http.delete<void>(`${environment.baseUrl}/payments/${id}`);
  }

  getPayment(id: string): Observable<Payment> {
    return this.http.get<Payment>(`${environment.baseUrl}/payments/${id}`);
  }
}
