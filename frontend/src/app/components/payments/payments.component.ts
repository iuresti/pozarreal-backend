import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {PaymentFilter} from '../../model/payment-filter';
import {PaymentView} from '../../model/payment-view';
import {PaymentService} from '../../services/payment.service';
import {NgbModal, NgbModalConfig} from '@ng-bootstrap/ng-bootstrap';
import {Payment} from '../../model/payment';
import Swal from 'sweetalert2';
import {SessionService} from '../../services/session.service';

@Component({
  selector: 'app-payments',
  templateUrl: './payments.component.html',
  styleUrls: ['./payments.component.css'],
  providers: [NgbModalConfig, NgbModal]
})
export class PaymentsComponent implements OnInit {
  @ViewChild('tableElement') html: ElementRef;
  payments: PaymentView[];
  newPayment: Payment;
  lastFilter: PaymentFilter;
  newPaymentReady: boolean;
  loading: boolean;
  isAdmin = false;

  constructor(private paymentService: PaymentService,
              private modalService: NgbModal,
              private sessionService: SessionService,
              config: NgbModalConfig) {
    config.backdrop = 'static';
    config.keyboard = false;
  }

  ngOnInit(): void {
    this.newPaymentReady = false;

    this.sessionService.getUser().subscribe(user => {
      this.isAdmin = user.roles.filter(role => role === 'ROLE_ADMIN').length > 0;
    });
  }

  doSearch(paymentFilter: PaymentFilter): void {
    this.lastFilter = paymentFilter;
    this.loading = true;
    this.paymentService.getPayments(paymentFilter).subscribe(payments => {
      this.payments = payments;
      this.loading = false;
    });
  }

  open(content): void {
    this.newPayment = {} as Payment;
    this.modalService.open(content, {ariaLabelledBy: 'modal-basic-title'}).result.then((result) => {
      console.log('Saving payment');
      console.log(this.newPayment);
      this.paymentService.save(this.newPayment).subscribe(() => {
        if (this.lastFilter) {
          this.doSearch(this.lastFilter);
        }
      });
    }, (reason) => {
      console.log('Cancel saving payment');
    });

  }

  deletePayment(payment: PaymentView): void {
    Swal.fire({
      title: `¿Confirmas la eliminación de este pago para ${payment.streetName} ${payment.houseNumber}?`,
      showDenyButton: true,
      showCancelButton: false,
      confirmButtonText: `Sí`,
      denyButtonText: `No`,
    }).then((result) => {
      if (result.isConfirmed) {
        this.paymentService.delete(payment.id).subscribe(() => {
          this.doSearch(this.lastFilter);
          Swal.fire('Eliminado!', '', 'success');
        });
      }
    });
  }

  editPayment(content, payment: PaymentView): void {
    this.newPayment = {} as Payment;

    this.newPayment.paymentConceptId = payment.paymentConceptId;
    this.newPayment.paymentSubConceptId = payment.paymentSubConceptId;
    this.newPayment.paymentDate = payment.paymentDate;
    this.newPayment.paymentMode = payment.paymentMode;
    this.newPayment.id = payment.id;
    this.newPayment.notes = payment.notes;
    this.newPayment.amount = payment.amount;
    this.newPayment.houseId = payment.houseId;
    this.newPayment.streetId = payment.streetId;

    this.modalService.open(content, {ariaLabelledBy: 'modal-basic-title'}).result.then((result) => {
      console.log('Saving payment');
      console.log(this.newPayment);
      this.paymentService.save(this.newPayment).subscribe(() => {
        if (this.lastFilter) {
          this.doSearch(this.lastFilter);
        }
      });
    }, (reason) => {
      console.log('Cancel saving payment');
    });
  }

  exportToExcel(): void {
    const uri = 'data:application/vnd.ms-excel;base64,';
    const template = '<html xmlns:o="urn:schemas-microsoft-com:office:office" xmlns:x="urn:schemas-microsoft-com:office:excel" lang="es"><head><!--[if gte mso 9]><xml><x:ExcelWorkbook><x:ExcelWorksheets><x:ExcelWorksheet><x:Name>{worksheet}</x:Name><x:WorksheetOptions><x:DisplayGridlines/></x:WorksheetOptions></x:ExcelWorksheet></x:ExcelWorksheets></x:ExcelWorkbook></xml><![endif]--><title></title><meta charset="UTF-8"/></head><body><table>{table}</table></body></html>';

    const base64 = (input) => {
      return window.btoa(unescape(encodeURIComponent(input)));
    };

    const format = (templateHtml, sheetSetting) => {
      return templateHtml.replace(/{(\w+)}/g, (_, table) => sheetSetting[table]);
    };

    const html = this.html.nativeElement.innerHTML;

    const sheetSettings = {
      worksheet: 'Pagos',
      table: html
    };

    const date = new Date();
    const year = date.getFullYear();
    const month = date.getUTCMonth() + 1;
    const day = ((date.getDate() < 10) ? '0' : '') + date.getDate();
    const hour = ((date.getHours() < 10) ? '0' : '') + date.getHours();
    const minutes = ((date.getMinutes() < 10) ? '0' : '') + date.getMinutes();

    const link = document.createElement('a');
    link.download = `pagos-${year}-${month}-${day}-${hour}-${minutes}.xls`;
    link.href = uri + base64(format(template, sheetSettings));
    link.click();
  }

}
