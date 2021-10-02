import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {Street} from '../model/street';
import {StreetInfo} from '../model/street-info';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class StreetService {

  constructor(private http: HttpClient) {
  }

  getStreets(): Observable<Street[]> {
    return this.http.get<Street[]>(`${environment.baseUrl}/streets`);
  }

  getStreetInfo(streetId: string): Observable<StreetInfo> {
    return this.http.get<StreetInfo>(`${environment.baseUrl}/streetInfo/${streetId}`);
  }
}
