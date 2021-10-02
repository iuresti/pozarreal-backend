import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {Chip} from '../model/chip';
import {environment} from '../../environments/environment';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class ChipService {

  constructor(private http: HttpClient) {
  }

  getChipsByHouse(houseId: string): Observable<Chip[]> {
    return this.http.get<Chip[]>(`${environment.baseUrl}/chips/house/${houseId}`);
  }

  deactivateChip(chipDeactivated: Chip): Observable<Chip> {
    return this.http.patch<Chip>(`${environment.baseUrl}/chips/deactivate/${chipDeactivated.id}`, {});
  }

  activateChip(chipDeactivated: Chip): Observable<Chip> {
    return this.http.patch<Chip>(`${environment.baseUrl}/chips/activate/${chipDeactivated.id}`, {});
  }

  addChip(newChip: Chip): Observable<Chip> {
    return this.http.post<Chip>(`${environment.baseUrl}/chips`, newChip);
  }

  removeChip(chip: Chip): Observable<Chip> {
    return this.http.delete<Chip>(`${environment.baseUrl}/chips/${chip.id}`);
  }
}
