import {Injectable} from '@angular/core';
import {Representative} from '../model/representative';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class RepresentativeService {

  constructor(private http: HttpClient) {
  }

  saveStreet(userId: string, street: string): Observable<Representative> {
    return this.http.patch<Representative>(
      `${environment.baseUrl}/representatives/${userId}/street/${street}`, null);
  }

  removeRepresentative(userId: string): Observable<void> {
    return this.http.delete<void>(`${environment.baseUrl}/representatives/${userId}`);
  }
}
