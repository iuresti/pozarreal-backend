import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {User} from '../../model/user';
import {UserService} from '../../services/user.service';

@Component({
  selector: 'app-roles-by-user',
  templateUrl: './roles-by-user.component.html',
  styleUrls: ['./roles-by-user.component.css']
})
export class RolesByUserComponent implements OnInit {

  @Input() user: User;

  allRoles = [];
  availableRoles = [];
  @Output() roleRemoved: EventEmitter<string> = new EventEmitter<string>();

  constructor(private userService: UserService) {
  }

  ngOnInit(): void {

    this.userService.getAllRoles().subscribe(roles => {
      this.allRoles = roles;
      this.updateAvailableRoles();
    });
  }

  removeRole(user: User, role: string): void {
    this.userService.removeRole(user, role).subscribe(newRoles => {
      this.user.roles = newRoles;
      this.updateAvailableRoles();
      this.roleRemoved.emit(role);
    });
  }

  private updateAvailableRoles(): void {
    this.availableRoles = this.allRoles.filter(value => this.user.roles.indexOf(value) === -1);
    this.user.roles.sort();
  }

  addRole(user: User, role: string): void {
    this.userService.addRole(user, role).subscribe(newRoles => {
      this.user.roles = newRoles;
      this.updateAvailableRoles();
    });
  }
}
