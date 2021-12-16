import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {HouseService} from '../../services/house.service';
import {HouseInfo} from '../../model/house-info';

@Component({
  selector: 'app-house-info',
  templateUrl: './house-info.component.html',
  styleUrls: ['./house-info.component.css']
})
export class HouseInfoComponent implements OnInit {

  houseInfo: HouseInfo;

  constructor(private router: Router,
              private activatedRoute: ActivatedRoute,
              private houseService: HouseService) {
    this.houseInfo = new HouseInfo();
  }

  ngOnInit(): void {
    this.activatedRoute.params.subscribe(params => {
      if (params.houseId) {
        this.houseService.getHouseInfo(params.houseId).subscribe(houseInfo => {
          this.houseInfo = houseInfo;
        });
      }
    });
  }

  saveNotes(): void {
    this.houseService.saveHouseNotes(this.houseInfo.id, this.houseInfo.notes).subscribe(() => console.log('notes saved'));
    this.back();
  }

  back(): void{
    this.router.navigate(['streets', this.houseInfo.street]).then(() => {});
  }
}
