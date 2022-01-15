import {Component, OnInit} from '@angular/core';
import {UserService} from '../../services/user.service';
import {User} from '../../model/user';
import {RepresentativeService} from '../../services/representative.service';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {HouseService} from '../../services/house.service';
import {StreetService} from '../../services/street.service';
import {Street} from '../../model/street';
import {HouseByUser} from '../../model/house-by-user';

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.css']
})
export class UsersComponent implements OnInit {

  users: User[];
  userId: string;
  selectedStreetId: string;
  streets: Street[];
  houses;
  housesByUser: HouseByUser[];
  selectedHouseId: string;

  constructor(private userService: UserService,
              private representativeService: RepresentativeService,
              private modalService: NgbModal,
              private houseService: HouseService,
              private streetService: StreetService) {
  }

  ngOnInit(): void {
    this.userService.getAllUsers().subscribe(users => {
      console.log(users);
      this.users = users;
    });


    this.streetService.getStreets().subscribe(streets => this.streets = streets);
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

  getHousesByUser(housesDialog, user: User): void {
    this.userId = user.id;
    this.houseService.getHousesByUser(user.id).subscribe(housesByUser => this.housesByUser = housesByUser);
    this.modalService.open(housesDialog, {ariaLabelledBy: 'modal-basic-title'}).result.then(() => {
    });
  }

  deleteHouse(house: HouseByUser): void {
    this.houseService.deleteHouseByUser(house.id).subscribe(() => {
      this.houseService.getHousesByUser(this.userId).subscribe(housesByUser => this.housesByUser = housesByUser);
    });
  }

  addRow(row): void {
    row.classList.remove('d-none');
  }

  removeRow(row): void {
    row.classList.add('d-none');
  }

  getHouses(): void {
    console.log(this.selectedStreetId);
    this.houseService.getHouseNumbersByStreet(this.selectedStreetId).subscribe(houses => {
      this.houses = houses;
    });
  }

  saveHouseByUser(): void {
    const houseByUser: HouseByUser = {} as HouseByUser;

    houseByUser.houseId = this.selectedHouseId;
    houseByUser.userId = this.userId;

    this.houseService.saveHouseByUser(houseByUser).subscribe(() => {
      this.houseService.getHousesByUser(this.userId).subscribe(housesByUser => this.housesByUser = housesByUser);
    });
  }
}
