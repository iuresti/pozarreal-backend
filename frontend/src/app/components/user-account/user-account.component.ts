import {Component, OnInit} from '@angular/core';
import {User} from '../../model/user';
import {StreetService} from '../../services/street.service';
import {HouseService} from '../../services/house.service';
import {UserService} from '../../services/user.service';
import {SessionService} from '../../services/session.service';
import {HouseByUser} from '../../model/house-by-user';
import {ChipService} from "../../services/chip.service";

@Component({
  selector: 'app-user-account',
  templateUrl: './user-account.component.html',
  styleUrls: ['./user-account.component.css']
})
export class UserAccountComponent implements OnInit {

  user: User;
  userName: string;
  housesByUser: HouseByUser[];

  constructor(private streetService: StreetService,
              private housesService: HouseService,
              private userService: UserService,
              private chipService: ChipService,
              private sessionService: SessionService) {
  }

  ngOnInit(): void {
    this.sessionService.getUser().subscribe(loggedUser => {
      this.user = loggedUser;
      this.userName = this.user.name;
    });

    this.housesService.getHousesByUser().subscribe(housesByUser => {
      this.housesByUser = housesByUser;
      this.housesByUser.forEach(houseByUser => {
        this.housesService.getHousePayments(houseByUser.houseId).subscribe(paymentByConcept => {
          houseByUser.twoMonthsPayments = paymentByConcept;
        });
        this.chipService.getChipsByHouse(houseByUser.houseId).subscribe(chips => {
          houseByUser.chips = chips;
        })
      });
    });
  }

  saveUserInfo(): void {
    this.userService.saveUser(this.user).subscribe(() => {
      this.sessionService.updateUser();
    });
  }

  saveName(): void {
    this.userService.updateName(this.userName).subscribe(() => {
      this.sessionService.updateUser();
    })
  }
}
