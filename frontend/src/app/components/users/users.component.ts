import {Component, OnInit} from '@angular/core';
import {UserService} from '../../services/user.service';
import {User} from '../../model/user';
import {RepresentativeService} from '../../services/representative.service';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.css']
})
export class UsersComponent implements OnInit {

  users: User[];

  constructor(private userService: UserService,
              private representativeService: RepresentativeService,
              private modalService: NgbModal) {
  }

  ngOnInit(): void {
    this.userService.getAllUsers().subscribe(users => {
      console.log(users);
      this.users = users;
    });
  }

  saveStreetForRepresentative(user: User, streetId: string): void {
    this.representativeService.saveStreet(user.id, streetId).subscribe(() => {
      console.log('representative saved');
    });
  }

  roleRemoved(user: User, roleName: string): void {
    if (roleName === 'ROLE_REPRESENTATIVE') {
      this.representativeService.removeRepresentative(user.id).subscribe(() => {
        console.log('representative removed');
      });
    }
  }

  hasRole(user: User, role: string): boolean {
    return user.roles.indexOf(role) >= 0;
  }

  showHouses(housesDialog): void {
    this.modalService.open(housesDialog, {ariaLabelledBy: 'modal-basic-title'}).result.then(() => {});
  }
}
