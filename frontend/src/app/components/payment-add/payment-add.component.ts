import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {StreetService} from '../../services/street.service';
import {HouseService} from '../../services/house.service';
import {House} from '../../model/house';
import {NgbDateStruct} from '@ng-bootstrap/ng-bootstrap';
import {PaymentConcept} from '../../model/payment-concept';
import {PaymentService} from '../../services/payment.service';
import {Payment} from '../../model/payment';
import {Street} from '../../model/street';
import {PaymentSubConcept} from '../../model/payment-sub-concept';
import {DateFormatterService} from '../../services/date-formatter.service';
import {HouseNumber} from "../../model/house-number";

@Component({
  selector: 'app-payment-add',
  templateUrl: './payment-add.component.html',
  styleUrls: ['./payment-add.component.css']
})
export class PaymentAddComponent implements OnInit {
  streets: Street[];
  houses: HouseNumber[];
  modelPaymentDate: NgbDateStruct;
  maxDate: NgbDateStruct;
  paymentConcepts: PaymentConcept[] = [];
  paymentSubConcepts: PaymentSubConcept[] = [];

  @Input() paymentData: Payment;
  @Input() house: House;
  @Input() street: Street;
  isNew: boolean;
  labelConcept: string;
  labelSubConcept: string;

  @Output() dataReady: EventEmitter<void> = new EventEmitter<void>();
  @Output() wrongData: EventEmitter<void> = new EventEmitter<void>();

  constructor(private streetService: StreetService,
              private houseService: HouseService,
              private paymentService: PaymentService) {
  }

  ngOnInit(): void {

    const now = new Date();

    this.maxDate = {
      year: now.getFullYear(),
      month: now.getMonth() + 1,
      day: now.getDate()
    };

    this.isNew = !this.house;

    this.paymentService.getPaymentConcepts().subscribe(paymentConcepts => {
      this.paymentConcepts = paymentConcepts.sort((a, b) => a.label < b.label ? -1 : 1);
      console.log(this.paymentData.paymentConceptId);
      this.paymentConcepts.forEach(conceptId => {
        if (this.paymentData.paymentConceptId == conceptId.id) this.labelConcept = conceptId.label;
      });
    });

    this.paymentService.getPaymentSubConcepts(this.paymentData.paymentConceptId).subscribe(paymentSubConcepts => {
      this.paymentSubConcepts = paymentSubConcepts;
      this.paymentSubConcepts.forEach(subConceptId => {
        if (this.paymentData.paymentSubConceptId == subConceptId.id) this.labelSubConcept = subConceptId.label;
      });

    });

    if (this.isNew) {
      this.streetService.getStreets().subscribe(streets => {
        this.streets = streets.sort((a, b) => a.name < b.name ? -1 : 1);
        console.log(this.paymentData.streetId);
      });

      this.houseService.getHouseNumbersByStreet(this.paymentData.streetId).subscribe(houses => {
        this.houses = houses;
      });

    }

    if (this.paymentData.paymentDate) {
      this.prepareStartDateFromExistentDate();
    } else {
      this.prepareStartDate();
    }

    if (!this.paymentData.id) {
      this.formChanged();
    }

  }

  streetSelected(): void {
    this.paymentData.houseId = null;
    if (this.paymentData.streetId) {
      this.houseService.getHousesByStreet(this.paymentData.streetId).subscribe(houses => {
        this.houses = houses;
        this.formChanged();
      });
    } else {
      this.houses = [];
      this.formChanged();
    }
  }

  conceptUpdated(): void {
    this.paymentSubConcepts = [];
    this.paymentData.paymentSubConceptId = null;
    if (this.paymentData.paymentConceptId) {
      this.paymentService.getPaymentSubConcepts(this.paymentData.paymentConceptId).subscribe(paymentSubConcepts => {
        this.paymentSubConcepts = paymentSubConcepts;
        this.formChanged();
      });
    } else {
      this.formChanged();
    }
  }

  updateDate(): void {
    this.paymentData.paymentDate = DateFormatterService.formatDate(this.modelPaymentDate);
    this.formChanged();
  }

  formChanged(): void {
    const validPayment = this.isHouseValid() &&
      this.isPaymentModeValid() &&
      this.isPaymentConceptValid() &&
      this.isPaymentAmountValid() &&
      this.isPaymentDateValid() &&
      this.isPaymentSubConceptValid();

    if (validPayment) {
      this.dataReady.emit();
    } else {
      this.wrongData.emit();
    }
  }

  isStreetValid(): boolean {
    return !!this.paymentData.streetId;
  }

  isHouseValid(): boolean {
    return !!this.paymentData.houseId;
  }

  isPaymentModeValid(): boolean {
    return !!this.paymentData.paymentMode;
  }

  isPaymentConceptValid(): boolean {
    return !!this.paymentData.paymentConceptId;
  }

  isPaymentAmountValid(): boolean {
    return this.paymentData.amount > 0;
  }

  isPaymentDateValid(): boolean {
    return this.modelPaymentDate.year >= new Date().getFullYear();
  }

  isPaymentSubConceptValid(): boolean {
    return this.paymentSubConcepts.length === 0 || !!this.paymentData.paymentSubConceptId;
  }

  prepareStartDate(): void {
    const now = new Date();
    this.modelPaymentDate = {
      year: now.getFullYear(),
      month: now.getMonth() + 1,
      day: now.getDate()
    };
    this.updateDate();
  }

  prepareStartDateFromExistentDate(): void {
    this.modelPaymentDate = {
      year: parseInt(this.paymentData.paymentDate.substr(0, 4), 10),
      month: parseInt(this.paymentData.paymentDate.substr(5, 2), 10),
      day: parseInt(this.paymentData.paymentDate.substr(8, 2), 10),
    };
    this.updateDate();
  }
}
