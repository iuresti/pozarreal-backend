import {Component, OnInit} from '@angular/core';
import {StreetInfo} from '../../model/street-info';
import {Street} from '../../model/street';
import {StreetService} from '../../services/street.service';
import {HouseService} from '../../services/house.service';
import {House} from '../../model/house';
import Swal from 'sweetalert2';
import {Payment} from '../../model/payment';
import {NgbDateStruct, NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {PaymentService} from '../../services/payment.service';
import {PaymentByConcept} from '../../model/payment-by-concept';
import {environment} from '../../../environments/environment';
import {ActivatedRoute, Router} from '@angular/router';
import {User} from '../../model/user';
import {SessionService} from '../../services/session.service';
import {UploadFileService} from '../../services/upload-file.service';

@Component({
  selector: 'app-circuitos',
  templateUrl: './circuitos.component.html',
  styleUrls: ['./circuitos.component.css']
})
export class CircuitosComponent implements OnInit {

  user: User;
  house: House;
  title = 'pozarreal';
  selectedStreet: StreetInfo;
  streets: Street[] = [];
  loading: boolean;
  newPaymentReady: boolean;
  newPayment: Payment;
  maintenanceFee = 800;
  selectedStreetId: string;
  mobile: boolean;
  years = [];
  selectedYear: number;
  date: string;

  maxDate: NgbDateStruct;
  minDate: NgbDateStruct;

  constructor(private streetService: StreetService,
              private sessionService: SessionService,
              private houseService: HouseService,
              private modalService: NgbModal,
              private paymentService: PaymentService,
              private uploadFileService: UploadFileService,
              private router: Router,
              private activatedRoute: ActivatedRoute) {
  }

  public ngOnInit(): void {

    const now = new Date();

    this.sessionService.getUser().subscribe(user => this.user = user);

    this.selectedYear = now.getFullYear();

    this.maxDate = {
      year: now.getFullYear(),
      month: now.getMonth() + 1,
      day: now.getDate()
    };

    this.minDate = {
      year: now.getFullYear(),
      month: now.getMonth() + 1,
      day: now.getDate()
    };

    for (let i = this.selectedYear - 4; i <= this.selectedYear; i++) {
      this.years.push(i);
    }

    if (window.screen.width < environment.mobileWidthScreen) {
      this.mobile = true;
    }

    this.streetService.getStreets().subscribe(streets => {
      this.streets = streets;
      if (streets.length === 1) {
        this.selectedStreetId = streets[0].id;
        this.selectStreet();
      } else {
        this.activatedRoute.params.subscribe(params => {
          if (params.streetId) {
            this.selectedStreetId = params.streetId;
            this.selectStreet();
          }
        });
      }
    });
  }

  selectStreet(): void {
    this.loading = true;
    this.selectedStreet = null;
    this.streetService.getStreetInfo(this.selectedStreetId, this.selectedYear).subscribe(streetInfo => {
      this.selectedStreet = streetInfo;
      this.loading = false;
    });
  }

  chipsForHouseEnabled(street: Street, house: House): void {
    Swal.fire({
      title: `??Deseas ${house.chipsEnabled ? 'activar' : 'desactivar'} todos los chips para ${street.name} ${house.number}?`,
      showDenyButton: true,
      showCancelButton: false,
      confirmButtonText: `S??`,
      denyButtonText: `No`,
    }).then((result) => {
      if (result.isConfirmed) {
        this.houseService.enableChips(house.id, house.chipsEnabled).subscribe(() => {
          Swal.fire('Guardado!', '', 'success').then(() => {
          });
        });
      } else {
        house.chipsEnabled = !house.chipsEnabled;
      }
    });
  }

  addPayment(content, bim: number, bimesterPayment: PaymentByConcept, house: House, event: MouseEvent): void {
    event.stopPropagation();
    this.newPayment = {} as Payment;
    this.newPayment.streetId = this.selectedStreet.id;
    this.newPayment.houseId = house.id;
    this.house = {} as House;
    this.house = house;
    this.newPayment.paymentConceptId = 'MAINTENANCE';
    this.newPayment.paymentSubConceptId = 'MAINTENANCE_BIM_' + bim;
    this.newPayment.amount = this.maintenanceFee - bimesterPayment.amount;
    this.modalService.open(content, {ariaLabelledBy: 'modal-basic-title'}).result.then(() => {
      this.paymentService.save(this.newPayment).subscribe(payment => {
        console.log('Saving payment');
        console.log(this.newPayment);

        if (payment.paymentSubConceptId === 'MAINTENANCE_ANNUITY') {
          house.twoMonthsPayments.forEach(p => {
            p.id = payment.id;
            p.validated = payment.validated;
            p.amount = payment.amount;
            p.conflict = payment.conflict;
            p.complete = true;
          });
        }

        bimesterPayment.amount += this.newPayment.amount;
        bimesterPayment.validated = payment.validated;
        bimesterPayment.complete = this.maintenanceFee <= bimesterPayment.amount;
        bimesterPayment.conflict = payment.conflict;

        const files = this.newPayment.files;
        if (files) {
          Array.from(files).forEach((file) => {
            this.uploadFileService.uploadFilesPayment(file, payment.id).subscribe();
          });
        }
      });
    }, () => {
      console.log('Cancel saving payment');
    });
  }

  onMouseOver({style}: HTMLDivElement): void {
    style.display = 'block';
  }

  onMouseLeave({style}: HTMLDivElement): void {
    style.display = 'none';
  }

  showHouse(id: string): void {
    this.router.navigate(['house', id]).then(() => {
    });
  }

  getStyle(bimesterPayment: PaymentByConcept): any {
    if (bimesterPayment.validated && bimesterPayment.complete) {
      return {backgroundColor: '#B6D7A8'};
    }
    if (!bimesterPayment.validated && bimesterPayment.amount > 0) {
      return {backgroundColor: '#FADC00'};
    }
  }

  validatePayment(bimesterPayment: PaymentByConcept, house: House): void {
    if (!bimesterPayment.validated && bimesterPayment.amount > 0 && this.userHasRoles(['ROLE_ADMIN'])) {
      Swal.fire({
        title: `??Validar pago por $${bimesterPayment.amount} de la casa ${house.number}?`,
        showDenyButton: true,
        showCancelButton: false,
        confirmButtonText: `S??`,
        denyButtonText: `No`,
      }).then(result => {
        if (result.isConfirmed) {
          this.paymentService.validatePayment(bimesterPayment.id).subscribe(payment => {

            Swal.fire('Validado!', '', 'success').then(() => {
              if (payment.paymentSubConceptId === 'MAINTENANCE_ANNUITY') {
                house.twoMonthsPayments.forEach(p => p.validated = true);
              }
              bimesterPayment.validated = payment.validated;
            });
          });
        }
      });
    }
  }

  conflictPayment($event: MouseEvent, bimesterPayment: PaymentByConcept, house: House): void {
    if (!bimesterPayment.conflict && this.userHasRoles(['ROLE_REPRESENTATIVE'])) {
      Swal.fire({
        title: `??Hay conflicto en el pago por $${bimesterPayment.amount} de la casa ${house.number}?`,
        showDenyButton: true,
        showCancelButton: false,
        confirmButtonText: `S??`,
        denyButtonText: `No`,
      }).then(result => {
        if (result.isConfirmed) {
          this.paymentService.conflictPayment(bimesterPayment.id).subscribe(payment => {
            Swal.fire('', '', 'success').then(() => bimesterPayment.conflict = payment.conflict);
          });
        }
      });
    }
  }

  changePaymentsOfYear(startOfYear: number): void {
    this.loading = true;
    const now = new Date();

    if (startOfYear < now.getFullYear()) {
      this.maxDate = {
        year: Number(startOfYear),
        day: 31,
        month: 12
      };

      this.minDate = {
        year: Number(startOfYear),
        day: 1,
        month: 1
      };
    } else {
      this.maxDate = {
        year: now.getFullYear(),
        month: now.getMonth() + 1,
        day: now.getDate()
      };

      this.minDate = {
        year: now.getFullYear(),
        month: now.getMonth() + 1,
        day: now.getDate()
      };
    }

    this.streetService.getStreetInfo(this.selectedStreetId, startOfYear).subscribe(streetInfo => {
      this.selectedStreet = streetInfo;
      this.loading = false;
    });
  }

  userHasRoles(roles: string[]): boolean {
    return this.user.roles.some(r => roles.includes(r));
  }

  saveDate(event: string): void {
    this.date = event;
  }
}
