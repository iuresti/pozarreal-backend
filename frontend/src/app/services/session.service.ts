import {Injectable} from '@angular/core';
import {User} from '../model/user';
import {HttpClient} from '@angular/common/http';
import {Observable, of} from 'rxjs';
import {environment} from '../../environments/environment';
import {map} from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class SessionService {
  private user: User;
  private _authorizationHeader: string;

  constructor(private http: HttpClient) {
  }

  getUser(): Observable<User> {
    if (this.user) {
      return of(this.user);
    }

    return this.http.get<User>(`${environment.baseUrl}/loggedUser`)
      .pipe(map(user => {
        this.user = user;
        return this.user;
      }));
  }


  get authorizationHeader(): string {
    return this._authorizationHeader;
  }


  setCredentials(user: string, password: string): void {
    this._authorizationHeader = btoa(user + ':' + password);
  }

  logout(): void {
    this.user = null;
    this._authorizationHeader = null;
    window.location.href = '/logout';
  }

  updateUser(): void {
    this.user = null;
    this.getUser().subscribe();
  }
}
