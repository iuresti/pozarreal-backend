import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {User} from '../model/user';
import {environment} from '../../environments/environment';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient) {
  }

  getAllUsers(): Observable<User[]> {
    return this.http.get<User[]>(`${environment.baseUrl}/users`);
  }

  getAllRoles(): Observable<string[]> {
    return this.http.get<string[]>(`${environment.baseUrl}/users/roles`);
  }

  removeRole(user: User, role: string): Observable<string[]> {
    return this.http.delete<string[]>(`${environment.baseUrl}/users/${user.id}/roles/${role}`);
  }

  addRole(user: User, role: string): Observable<string[]> {
    return this.http.post<string[]>(`${environment.baseUrl}/users/${user.id}/roles/${role}`, {});
  }

  saveUser(user: User): Observable<User> {
    return this.http.put<User>(`${environment.baseUrl}/users/${user.id}`, user);
  }
}
