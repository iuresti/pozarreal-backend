import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {PaymentFilter} from '../../model/payment-filter';
import {NgbDateStruct} from '@ng-bootstrap/ng-bootstrap';

import '@angular/localize/init';
import {FormBuilder} from '@angular/forms';
import {PaymentConcept} from '../../model/payment-concept';
import {PaymentService} from '../../services/payment.service';
import {IDropdownSettings} from 'ng-multiselect-dropdown';
import {HouseService} from '../../services/house.service';
import {House} from '../../model/house';


@Component({
  selector: 'app-payment-filter',
  templateUrl: './payment-filter.component.html',
  styleUrls: ['./payment-filter.component.css']
})
export class PaymentFilterComponent implements OnInit {

  @Output() selectionDone: EventEmitter<PaymentFilter> = new EventEmitter<PaymentFilter>();
  @Output() addPaymentClicked: EventEmitter<void> = new EventEmitter<void>();
  @Input() isAdmin: boolean;
  modelStartDate: NgbDateStruct;
  modelEndDate: NgbDateStruct;
  paymentConcepts: PaymentConcept[] = [];
  selectedPaymentConcepts: PaymentConcept[];
  dropdownSettings: IDropdownSettings;
  selectedStreet: string;
  selectedHouse: string;
  selectedPaymentMode: string;
  houses: House[];


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
      endDate: `${this.pad(this.modelEndDate.year)}-${this.pad(this.modelEndDate.month)}-${this.pad(this.modelEndDate.day)}`
    };

    this.selectionDone.emit(paymentFilter);
  }

  streetSelected(selectedSteet: string): void {
    console.log(selectedSteet);
    this.selectedStreet = selectedSteet;
    if (this.selectedStreet) {
      this.houseService.getHousesByStreet(this.selectedStreet).subscribe(houses => {
        this.houses = houses.sort((a, b) => a.number < b.number ? -1 : 1);
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
