import {Component, Input, OnInit} from '@angular/core';
import {ChipService} from '../../services/chip.service';
import {Chip} from '../../model/chip';


import {House} from '../../model/house';
import {Street} from '../../model/street';
import {DropEvent} from 'ng-drag-drop';

import Swal from 'sweetalert2';

@Component({
  selector: 'app-chips-by-house',
  templateUrl: './chips-by-house.component.html',
  styleUrls: ['./chips-by-house.component.css']
})
export class ChipsByHouseComponent implements OnInit {

  @Input() house: House;
  @Input() street: Street;
  activeChips: Chip[];
  inactiveChips: Chip[];
  addingChip = false;
  removeIconVisible = false;

  constructor(private chipService: ChipService) {
  }

  ngOnInit(): void {
    this.showChips();
  }

  showChips(): void {
    this.chipService.getChipsByHouse(this.house.id).subscribe(chips => {
      this.activeChips = chips.filter(chip => chip.valid);
      this.inactiveChips = chips.filter(chip => !chip.valid);
    });
  }

  deactivateChip($event: DropEvent): void {
    const chipDeactivated: Chip = $event.dragData;
    this.chipService.deactivateChip(chipDeactivated).subscribe(chip => {
      this.activeChips = this.activeChips.filter(value => value.code !== chip.code);
      this.inactiveChips.push(chipDeactivated);
    });
  }

  activateChip($event: DropEvent): void {
    const chipActivated: Chip = $event.dragData;
    this.chipService.activateChip(chipActivated).subscribe(chip => {
      this.inactiveChips = this.inactiveChips.filter(value => value.code !== chip.code);
      this.activeChips.push(chipActivated);
    });

  }

  addChip(chipCodeAdded: string): void {
    const newChip = {} as Chip;
    newChip.code = chipCodeAdded;
    newChip.house = this.house.id;
    newChip.valid = true;
    this.chipService.addChip(newChip).subscribe(chip => {
      this.activeChips.push(chip);
      this.addingChip = false;
    });
  }

  removeChip(chip: Chip): void {
    Swal.fire({
      title: `¿Confirma que desea eliminar el chip '${chip.code}'? Esta acción no podrá deshacerse`,
      showDenyButton: true,
      showCancelButton: false,
      confirmButtonText: `Sí`,
      denyButtonText: `No`,
    }).then((result) => {
      if (result.isConfirmed) {
        this.chipService.removeChip(chip).subscribe(() => {
          this.showChips();
        });
      }
    });
  }
}
