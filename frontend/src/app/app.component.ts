import {Component, OnInit} from '@angular/core';
import {SessionService} from './services/session.service';
import {User} from './model/user';


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  user: User;
  isAdmin: boolean;
  failedLogin = false;
  isCollapsed = true;

  constructor(private sessionService: SessionService) {
  }

  ngOnInit(): void {
    this.login();
  }

  logout(): void {
    this.sessionService.logout();
  }

  userHasRoles(roles: string[]): boolean {
    return this.user.roles.some(r => roles.includes(r));
  }

  apiLogin(user: string, password: string): void {
    this.sessionService.setCredentials(user, password);
    this.login();
  }

  login(): void {
    this.failedLogin = false;
    this.sessionService.getUser().subscribe(user => {
      this.user = user;
      this.isAdmin = user.roles.filter(role => role === 'ROLE_ADMIN').length > 0;
    }, error => {
      this.failedLogin = true;
      console.log(error);
    });
  }

  toggleMenu(): void {
    this.isCollapsed = !this.isCollapsed;
  }
}
