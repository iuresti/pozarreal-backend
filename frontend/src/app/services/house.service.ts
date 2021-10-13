import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable, of} from 'rxjs';
import {StreetInfo} from '../model/street-info';
import {environment} from '../../environments/environment';
import {Street} from '../model/street';
import {House} from '../model/house';
import {HouseNumber} from "../model/house-number";
import {map} from "rxjs/operators";

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
            "id": house.id,
            "number": house.number
          }
        }));
        return this.housesByStreet.get(selectedStreet);
      }));
  }
}
