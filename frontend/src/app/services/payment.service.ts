import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {PaymentFilter} from '../model/payment-filter';
import {Observable, of} from 'rxjs';
import {environment} from '../../environments/environment';
import {PaymentConcept} from '../model/payment-concept';
import {Payment} from '../model/payment';
import {PaymentSubConcept} from '../model/payment-sub-concept';
import {tap} from 'rxjs/operators';
import {PaymentViewPagination} from '../model/payment-view-pagination';

@Injectable({
  providedIn: 'root'
})
export class PaymentService {

  private paymentConcepts: PaymentConcept[];
  private paymentSubConcepts: Map<string, PaymentSubConcept[]> = new Map<string, PaymentSubConcept[]>();

  constructor(private http: HttpClient) {
  }

  public getPayments(paymentFilter: PaymentFilter, page: number): Observable<PaymentViewPagination> {
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

    if (paymentFilter.status) {
      queryParams += `&status=${paymentFilter.status}`;
    }

    return this.http.get<PaymentViewPagination>(`${environment.baseUrl}/payments?${queryParams}&page=${page}`);
  }

  getPaymentConcepts(): Observable<PaymentConcept[]> {
    if (this.paymentConcepts) {
      return of(this.paymentConcepts);
    }

    return this.http.get<PaymentConcept[]>(`${environment.baseUrl}/paymentConcepts`)
      .pipe(tap(paymentConcepts => this.paymentConcepts = paymentConcepts.sort((a, b) => a.label < b.label ? -1 : 1)));
  }

  getPaymentSubConcepts(paymentConceptId: string): Observable<PaymentSubConcept[]> {

    if (this.paymentSubConcepts.has(paymentConceptId)) {
      return of(this.paymentSubConcepts.get(paymentConceptId));
    }

    return this.http.get<PaymentSubConcept[]>(`${environment.baseUrl}/paymentSubConcepts/concept/${paymentConceptId}`)
      .pipe(tap(paymentSubConcepts => this.paymentSubConcepts.set(paymentConceptId, paymentSubConcepts)));
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

  validatePayment(id: string): Observable<Payment> {
    return this.http.patch<Payment>(`${environment.baseUrl}/payments/${id}`, {});
  }

  conflictPayment(id: string): Observable<Payment> {
    return this.http.put<Payment>(`${environment.baseUrl}/payments/${id}`, {});
  }
}
