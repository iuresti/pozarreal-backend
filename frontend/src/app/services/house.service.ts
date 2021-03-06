import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable, of} from 'rxjs';
import {environment} from '../../environments/environment';
import {House} from '../model/house';
import {HouseNumber} from '../model/house-number';
import {map} from 'rxjs/operators';
import {HouseInfo} from '../model/house-info';
import {HouseByUser} from '../model/house-by-user';
import {PaymentByConcept} from "../model/payment-by-concept";

@Injectable({
  providedIn: 'root'
})
export class HouseService {

  private housesByStreet: Map<string, HouseNumber[]> = new Map<string, HouseNumber[]>();

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

  getHouseNumbersByStreet(selectedStreet: string): Observable<HouseNumber[]> {
    if (this.housesByStreet.has(selectedStreet)) {
      return of(this.housesByStreet.get(selectedStreet));
    }

    return this.getHousesByStreet(selectedStreet)
      .pipe(map(houses => {
        houses = houses.sort((a, b) => a.number < b.number ? -1 : 1);
        this.housesByStreet.set(selectedStreet, houses.map(house => {
          return {
            id: house.id,
            number: house.number
          };
        }));
        return this.housesByStreet.get(selectedStreet);
      }));
  }

  getHouseInfo(houseId: string): Observable<HouseInfo> {
    return this.http.get<HouseInfo>(`${environment.baseUrl}/house/info/${houseId}`);
  }

  saveHouseNotes(houseId: string, notes: string): Observable<void> {
    return this.http.patch<void>(`${environment.baseUrl}/house/${houseId}/notes`, notes);
  }

  getHousesByUser(userId?: string): Observable<HouseByUser[]> {
    return this.http.get<HouseByUser[]>(`${environment.baseUrl}/house/${userId ? userId : ''}`).pipe(map(housesByUser => {
      housesByUser.map(houseByUser => {
        this.getHouseInfo(houseByUser.houseId).subscribe(houseInfo => {
          houseByUser.number = houseInfo.number;
          houseByUser.streetName = houseInfo.streetName;
        });
      });
      return housesByUser;
    }));
  }

  deleteHouseByUser(id: string): Observable<void> {
    return this.http.delete<void>(`${environment.baseUrl}/house/${id}`);
  }

  saveHouseByUser(housesByUser: HouseByUser): Observable<HouseByUser> {
    return this.http.post<HouseByUser>(`${environment.baseUrl}/house`, housesByUser);
  }

  getHousePayments(houseId: string): Observable<PaymentByConcept[]> {
    return this.http.get<PaymentByConcept[]>(`${environment.baseUrl}/house/payments/${houseId}`);
  }

}
