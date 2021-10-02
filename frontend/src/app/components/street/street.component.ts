import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Street} from '../../model/street';
import {StreetService} from '../../services/street.service';

@Component({
  selector: 'app-street',
  templateUrl: './street.component.html',
  styleUrls: ['./street.component.css']
})
export class StreetComponent implements OnInit {

  streets: Street[] = [];
  @Input() selectedStreet: string;
  @Output() streetSelected: EventEmitter<string> = new EventEmitter<string>();

  constructor(private streetService: StreetService) {
  }

  ngOnInit(): void {
    this.streetService.getStreets().subscribe(streets => {
      this.streets = streets.sort((a, b) => a.name < b.name ? -1 : 1);
      if (this.streets.length === 1) {
        this.selectedStreet = this.streets[0].id;
        this.streetSelected.emit(this.streets[0].id);
      }
    });
  }

}
