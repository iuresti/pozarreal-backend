import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {environment} from '../../environments/environment';
import {House} from '../model/house';
import {HouseInfo} from "../model/house-info";

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

  getHousesByStreet(selectedStreet: string): Observable<House[]> {
    return this.http.get<House[]>(`${environment.baseUrl}/streets/${selectedStreet}/houses`);
  }

  getHouseInfo(houseId: string): Observable<HouseInfo> {
    return this.http.get<HouseInfo>(`${environment.baseUrl}/house/info/${houseId}`);
  }

  saveHouseNotes(houseId: string, notes: string): Observable<void> {
    return this.http.patch<void>(`${environment.baseUrl}/house/${houseId}/notes`, notes);
  }
}
