import {Injectable} from '@angular/core';
import {CanLoad, Route, UrlSegment, UrlTree} from '@angular/router';
import {Observable} from 'rxjs';
import {SessionService} from '../services/session.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanLoad {

  constructor(private sessionService: SessionService) {
  }

  canLoad(
    route: Route,
    segments: UrlSegment[]): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    this.sessionService.getUser().subscribe(user => {
      if (user) {
        return true;
      }
    });
    return false;
  }
}
