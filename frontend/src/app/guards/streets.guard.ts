import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, RouterStateSnapshot, UrlTree} from '@angular/router';
import {Observable} from 'rxjs';
import {SessionService} from '../services/session.service';

@Injectable({
  providedIn: 'root'
})
export class StreetsGuard implements CanActivate {

  constructor(private sessionService: SessionService) {
  }

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    this.sessionService.getUser().subscribe(user => {
      return user.roles.some(r => ['ROLE_ADMIN', 'ROLE_REPRESENTATIVE'].includes(r));
    });

    return false;
  }

}
