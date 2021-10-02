import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {CourseAssistantPayment} from '../../model/course-assistant-payment';
import {NgbDateStruct} from '@ng-bootstrap/ng-bootstrap';
import {DateFormatterService} from '../../services/date-formatter.service';
import {CoursePaymentService} from '../../services/course-payment.service';

@Component({
  selector: 'app-course-add-payment',
  templateUrl: './course-add-payment.component.html',
  styleUrls: ['./course-add-payment.component.css']
})
export class CourseAddPaymentComponent implements OnInit {

  @Input() courseAssistantId: string;
  paymentData: CourseAssistantPayment;
  modelPaymentDate: NgbDateStruct;
  maxDate: NgbDateStruct;
  @Output() saved: EventEmitter<void> = new EventEmitter<void>();

  constructor(private coursePaymentService: CoursePaymentService) {
  }

  ngOnInit(): void {

    this.paymentData = {} as CourseAssistantPayment;
    this.paymentData.courseAssistantId = this.courseAssistantId;

    const now = new Date();

    this.maxDate = {
      year: now.getFullYear(),
      month: now.getMonth() + 1,
      day: now.getDate()
    };

    if (this.paymentData.paymentDate) {
      this.prepareStartDateFromExistentDate();
    } else {
      this.prepareStartDate();
    }

    if (!this.paymentData.id) {
      this.formChanged();
    }
  }

  updateDate(): void {
    this.paymentData.paymentDate = DateFormatterService.formatDate(this.modelPaymentDate);
    this.formChanged();
  }

  formChanged(): void {
    const validPayment = this.isPaymentAmountValid()
      && this.isPaymentDateValid();

  }

  isPaymentAmountValid(): boolean {
    return this.paymentData.amount > 0;
  }

  isPaymentDateValid(): boolean {
    return this.modelPaymentDate.year >= new Date().getFullYear();
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

  savePayment(): void {
    this.coursePaymentService.save(this.paymentData).subscribe(paymentData => {
      this.saved.emit();
    });

  }
}
