import {Injectable} from '@angular/core';
import {Observable, of} from 'rxjs';
import {Street} from '../model/street';
import {StreetInfo} from '../model/street-info';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../environments/environment';
import {tap} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class StreetService {

  private streets: Street[];

  constructor(private http: HttpClient) {
  }

  getStreets(): Observable<Street[]> {
    if(this.streets){
      return of(this.streets);
    }

    return this.http.get<Street[]>(`${environment.baseUrl}/streets`)
      .pipe(tap(streets => this.streets = streets));
  }

  getStreetInfo(streetId: string): Observable<StreetInfo> {
    return this.http.get<StreetInfo>(`${environment.baseUrl}/streetInfo/${streetId}`);
  }
}
