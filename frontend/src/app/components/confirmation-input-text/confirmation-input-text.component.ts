import {Component, EventEmitter, OnInit, Output} from '@angular/core';

@Component({
  selector: 'app-confirmation-input-text',
  templateUrl: './confirmation-input-text.component.html',
  styleUrls: ['./confirmation-input-text.component.css']
})
export class ConfirmationInputTextComponent implements OnInit {

  @Output() cancel: EventEmitter<void> = new EventEmitter<void>();
  @Output() textAccepted: EventEmitter<string> = new EventEmitter<string>();

  constructor() {
  }

  ngOnInit(): void {
  }

}
