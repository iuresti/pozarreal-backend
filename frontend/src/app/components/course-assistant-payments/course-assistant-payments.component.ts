import {Component, Input, OnInit} from '@angular/core';
import {CourseAssistantPayment} from '../../model/course-assistant-payment';
import {CoursePaymentService} from '../../services/course-payment.service';
import Swal from 'sweetalert2';
import {NgbDateStruct, NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {DateFormatterService} from '../../services/date-formatter.service';

@Component({
  selector: 'app-course-assistant-payments',
  templateUrl: './course-assistant-payments.component.html',
  styleUrls: ['./course-assistant-payments.component.css']
})
export class CourseAssistantPaymentsComponent implements OnInit {
  loading = false;
  modelPaymentDate: NgbDateStruct;
  maxDate: NgbDateStruct;
  payments: CourseAssistantPayment[] = [];
  newPayment = {} as CourseAssistantPayment;
  paymentForDetail: CourseAssistantPayment;
  @Input() courseAssistantId: string;

  constructor(private coursePaymentService: CoursePaymentService,
              private modalService: NgbModal) {
  }

  ngOnInit(): void {
    const now = new Date();

    this.maxDate = {
      year: now.getFullYear(),
      month: now.getMonth() + 1,
      day: now.getDate()
    };

    this.loadPayments();

    this.modelPaymentDate = {
      year: now.getFullYear(),
      month: now.getMonth() + 1,
      day: now.getDate()
    };
    this.updateDate();
  }

  loadPayments(): void {
    this.loading = true;
    this.coursePaymentService.getAllPayments(this.courseAssistantId).subscribe(payments => {
      this.payments = payments;
      this.loading = false;
    });
  }

  savePayment(): void {
    this.newPayment.courseAssistantId = this.courseAssistantId;
    this.coursePaymentService.save(this.newPayment).subscribe(() => {
      this.newPayment = {} as CourseAssistantPayment;
      this.loadPayments();
    });
  }

  deletePayment(paymentId: string): void {
    Swal.fire({
      title: `¿Confirmas la eliminación de este pago?`,
      showDenyButton: true,
      showCancelButton: false,
      confirmButtonText: `Sí`,
      denyButtonText: `No`,
    }).then((result) => {
      if (result.isConfirmed) {
        this.coursePaymentService.deletePayment(paymentId).subscribe(payment => {
          this.loadPayments();
          Swal.fire('Eliminado!', '', 'success').then(() => {});
        });
      }
    });
  }

  updateDate(): void {
    this.newPayment.paymentDate = DateFormatterService.formatDate(this.modelPaymentDate);
  }

  showPayment(id: string): void {

  }

  showPaymentDetail(content, payment: CourseAssistantPayment): void {
    this.paymentForDetail = payment;
    this.modalService.open(content, {ariaLabelledBy: 'modal-basic-title'}).result.then((result) => {
    });
  }
}
