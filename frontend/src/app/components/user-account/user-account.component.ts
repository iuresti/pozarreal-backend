import {Component, Input, OnInit} from '@angular/core';
import {User} from '../../model/user';
import {StreetService} from '../../services/street.service';
import {HouseService} from '../../services/house.service';
import {UserService} from '../../services/user.service';
import {SessionService} from '../../services/session.service';
import {logger} from 'codelyzer/util/logger';

@Component({
  selector: 'app-user-account',
  templateUrl: './user-account.component.html',
  styleUrls: ['./user-account.component.css']
})
export class UserAccountComponent implements OnInit {

  user: User;

  userName: string;

  constructor(private streetService: StreetService,
              private housesService: HouseService,
              private userService: UserService,
              private sessionService: SessionService) {
  }

  ngOnInit(): void {
    this.sessionService.getUser().subscribe(loggedUser => {
      this.user = loggedUser;
      this.userName = this.user.name;
    });
  }

  saveUserInfo(): void {
    this.userService.saveUser(this.user).subscribe(user => {
      this.sessionService.updateUser();
    });
  }
}
