import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {StreetInfo} from '../model/street-info';
import {environment} from '../../environments/environment';
import {Street} from '../model/street';
import {House} from '../model/house';

@Injectable({
  providedIn: 'root'
})
export class HouseService {

  constructor(private http: HttpClient) {
  }

  enableChips(houseId: string, enable: boolean): Observable<void> {
    return this.http.put<void>(`${environment.baseUrl}/house/${houseId}/chips`, {
      houseId,
      enable
    });
  }

  getHousesByStreet(selectedStreet: string): Observable<House[]>{
    return this.http.get<House[]>(`${environment.baseUrl}/streets/${selectedStreet}/houses`);
  }
}
