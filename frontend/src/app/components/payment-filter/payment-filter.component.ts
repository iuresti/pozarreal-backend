import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {PaymentFilter} from '../../model/payment-filter';
import {NgbDateStruct} from '@ng-bootstrap/ng-bootstrap';

import '@angular/localize/init';
import {FormBuilder} from '@angular/forms';
import {PaymentConcept} from '../../model/payment-concept';
import {PaymentService} from '../../services/payment.service';
import {IDropdownSettings} from 'ng-multiselect-dropdown';
import {HouseService} from '../../services/house.service';
import {HouseNumber} from '../../model/house-number';


@Component({
  selector: 'app-payment-filter',
  templateUrl: './payment-filter.component.html',
  styleUrls: ['./payment-filter.component.css']
})
export class PaymentFilterComponent implements OnInit {

  @Output() selectionDone: EventEmitter<PaymentFilter> = new EventEmitter<PaymentFilter>();
  @Output() addPaymentClicked: EventEmitter<void> = new EventEmitter<void>();
  @Input() havePermission: boolean;
  modelStartDate: NgbDateStruct;
  modelEndDate: NgbDateStruct;
  paymentConcepts: PaymentConcept[] = [];
  selectedPaymentConcepts: PaymentConcept[];
  dropdownSettings: IDropdownSettings;
  selectedStreet: string;
  selectedHouse: string;
  selectedPaymentMode: string;
  selectedStatus: string;
  houses: HouseNumber[];


  constructor(private fb: FormBuilder,
              private houseService: HouseService,
              private paymentService: PaymentService) {
  }

  ngOnInit(): void {
    this.paymentService.getPaymentConcepts().subscribe(paymentConcepts => {
      this.paymentConcepts = paymentConcepts.sort((a, b) => a.label < b.label ? -1 : 1);
    });

    this.selectedPaymentConcepts = [];

    this.dropdownSettings = {
      singleSelection: false,
      idField: 'id',
      textField: 'label',
      itemsShowLimit: 4,
      enableCheckAll: false,
      unSelectAllText: 'Todos'
    };

    const now = new Date();

    this.modelStartDate = {
      year: now.getFullYear(),
      month: now.getMonth() % 2 === 0 ? now.getMonth() + 1 : now.getMonth(),
      day: 1
    };

    this.modelEndDate = {
      year: now.getFullYear(),
      month: now.getMonth() + 1,
      day: now.getDate()
    };
  }

  searchPayments(): void {
    const paymentFilter: PaymentFilter = {
      street: this.selectedStreet,
      house: this.selectedHouse,
      paymentMode: this.selectedPaymentMode,
      concepts: this.selectedPaymentConcepts.map(paymentConcept => paymentConcept.id),
      startDate: `${this.pad(this.modelStartDate.year)}-${this.pad(this.modelStartDate.month)}-${this.pad(this.modelStartDate.day)}`,
      endDate: `${this.pad(this.modelEndDate.year)}-${this.pad(this.modelEndDate.month)}-${this.pad(this.modelEndDate.day)}`,
      status: this.selectedStatus
    };

    this.selectionDone.emit(paymentFilter);
  }

  streetSelected(selectedSteet: string): void {
    this.selectedStreet = selectedSteet;
    if (this.selectedStreet) {
      this.houseService.getHouseNumbersByStreet(this.selectedStreet).subscribe(houses => {
        this.houses = houses;
      });
    } else {
      this.selectedHouse = '';
      this.houses = [];
    }
  }

  pad(value: number): string {
    return value < 10 ? '0' + value : '' + value;
  }

  addPayment(): void {
    this.addPaymentClicked.emit();
  }

}
