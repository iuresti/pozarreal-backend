import {Component, OnInit} from '@angular/core';
import {UserService} from '../../services/user.service';
import {User} from '../../model/user';
import {RepresentativeService} from '../../services/representative.service';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {HouseService} from '../../services/house.service';
import {StreetService} from '../../services/street.service';
import {Street} from '../../model/street';
import {HouseByUser} from '../../model/house-by-user';
import {House} from '../../model/house';
import Swal from 'sweetalert2';

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
  houses: House[];
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

  getHousesByUser(housesDialog, user: User): void {
    this.userId = user.id;
    this.selectedHouseId = null;
    this.selectedStreetId = null;
    this.houseService.getHousesByUser(user.id).subscribe(housesByUser => {
      this.housesByUser = housesByUser;
    });
    this.modalService.open(housesDialog, {ariaLabelledBy: 'modal-basic-title'}).result.then();
  }


  deleteHouse(house: HouseByUser): void {
    Swal.fire({
      title: `¿Confirmas la eliminación de la casa ${house.streetName} ${house.number} de este usuario?`,
      showDenyButton: true,
      showCancelButton: false,
      confirmButtonText: `Sí`,
      denyButtonText: `No`,
    }).then((result) => {
      if (result.isConfirmed) {
        this.houseService.deleteHouseByUser(house.id).subscribe(() => {
          this.houseService.getHousesByUser(this.userId).subscribe(housesByUser => {
            this.housesByUser = housesByUser;
            Swal.fire('Eliminado!', '', 'success').then(() => this.getHouses());
          });
        });
      }
    });
  }

  addRow(row: HTMLDivElement): void {
    row.classList.remove('d-none');
    this.streetService.getStreets().subscribe(streets => this.streets = streets);
  }

  removeRow(row: HTMLDivElement): void {
    row.classList.add('d-none');
  }

  getHouses(): void {
    this.houseService.getHousesByStreet(this.selectedStreetId).subscribe(houses => {
      this.houseService.getHousesByUser(this.userId).subscribe(housesByUser => {
        this.houses = houses.filter(house => !housesByUser.find(houseByUser => houseByUser.houseId === house.id));
      });
    });
  }

  saveHouseByUser(): void {
    const houseByUser: HouseByUser = {} as HouseByUser;

    houseByUser.houseId = this.selectedHouseId;
    houseByUser.userId = this.userId;

    this.houseService.saveHouseByUser(houseByUser).subscribe(() => {
      this.houseService.getHousesByUser(this.userId).subscribe(housesByUser => {
        this.selectedHouseId = null;
        this.housesByUser = housesByUser;
        this.getHouses();
      });
    });
  }

}
