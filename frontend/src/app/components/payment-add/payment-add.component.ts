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
import {HouseNumber} from '../../model/house-number';
import {User} from '../../model/user';
import {SessionService} from '../../services/session.service';

@Component({
  selector: 'app-payment-add',
  templateUrl: './payment-add.component.html',
  styleUrls: ['./payment-add.component.css']
})
export class PaymentAddComponent implements OnInit {

  user: User;
  streets: Street[];
  houses: HouseNumber[];
  modelPaymentDate: NgbDateStruct;
  paymentConcepts: PaymentConcept[] = [];
  paymentSubConcepts: PaymentSubConcept[] = [];

  @Input() maxDate: NgbDateStruct;
  @Input() minDate: NgbDateStruct;
  @Input() paymentData: Payment;
  @Input() house: House;
  @Input() street: Street;
  @Input() paymentDateReadOnly: boolean;
  @Input() isEdit: boolean;
  isNew: boolean;
  labelConcept: string;
  labelSubConcept: string;

  @Output() date: EventEmitter<string> = new EventEmitter<string>();
  @Output() dataReady: EventEmitter<void> = new EventEmitter<void>();
  @Output() wrongData: EventEmitter<void> = new EventEmitter<void>();

  constructor(private streetService: StreetService,
              private sessionService: SessionService,
              private houseService: HouseService,
              private paymentService: PaymentService) {
  }

  ngOnInit(): void {

    this.sessionService.getUser().subscribe(user => this.user = user);

    this.isNew = !this.house;

    this.prepareConceptsAndSubConcepts();

    this.prepareStreetsAndHouses();

    if (this.paymentData.paymentDate) {
      this.prepareStartDateFromExistentDate();
    } else {
      this.prepareStartDate();
    }

    if (!this.paymentData.id) {
      this.formChanged();
    }

  }

  private prepareStreetsAndHouses(): void {
    if (this.isNew) {
      this.streetService.getStreets().subscribe(streets => {
        this.streets = streets;
      });

      if (this.paymentData.streetId) {
        this.houseService.getHouseNumbersByStreet(this.paymentData.streetId).subscribe(houses => {
          this.houses = houses;
        });
      }
    }
  }

  private prepareConceptsAndSubConcepts(): void {
    this.paymentService.getPaymentConcepts().subscribe(paymentConcepts => {
      this.paymentConcepts = paymentConcepts;
      if (this.paymentData.paymentConceptId) {
        this.labelConcept = this.paymentConcepts.find(paymentConcept => paymentConcept.id === this.paymentData.paymentConceptId)?.label;
      }
    });

    if (this.paymentData.paymentConceptId) {
      this.paymentService.getPaymentSubConcepts(this.paymentData.paymentConceptId).subscribe(paymentSubConcepts => {
        this.paymentSubConcepts = paymentSubConcepts;
        if (this.paymentData.paymentSubConceptId) {
          this.labelSubConcept = this.paymentSubConcepts
            .find(paymentSybConcept => paymentSybConcept.id === this.paymentData.paymentSubConceptId)?.label;
        }
      });
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
    this.date.emit(this.paymentData.paymentDate);
  }

  formChanged(): void {
    const validPayment = this.isHouseValid() &&
      this.isPaymentModeValid() &&
      this.isPaymentConceptValid() &&
      this.isPaymentAmountValid() &&
      this.isPaymentDateValid() &&
      this.isPaymentSubConceptValid() &&
      this.isPaymentFilesValid();

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
    return this.modelPaymentDate.year >= this.minDate.year && this.modelPaymentDate.year <= this.maxDate.year;
  }

  isPaymentSubConceptValid(): boolean {
    return this.paymentSubConcepts.length === 0 || !!this.paymentData.paymentSubConceptId;
  }

  isPaymentFilesValid(): boolean {
    return this.userHasRoles(['ROLE_ADMIN', 'ROLE_REPRESENTATIVE']) || this.paymentData.files != null;
  }

  prepareStartDate(): void {
    const now = new Date();
    this.modelPaymentDate = {
      year: (this.maxDate.year < now.getFullYear()) ? this.maxDate.year : now.getFullYear(),
      month: (this.maxDate.year < now.getFullYear()) ? 12 : now.getMonth() + 1,
      day: (this.maxDate.year < now.getFullYear()) ? 31 : now.getDate()
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

  prepareFiles(event): void {
    this.paymentData.files = event.target.files;
    this.formChanged();
  }

  userHasRoles(roles: string[]): boolean {
    return this.user.roles.some(r => roles.includes(r));
  }
}
