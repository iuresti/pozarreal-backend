import {Component, OnInit} from '@angular/core';
import {StreetInfo} from '../../model/street-info';
import {Street} from '../../model/street';
import {StreetService} from '../../services/street.service';
import {HouseService} from '../../services/house.service';
import {House} from '../../model/house';
import Swal from 'sweetalert2';
import {Payment} from '../../model/payment';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {PaymentService} from '../../services/payment.service';
import {PaymentByConcept} from '../../model/payment-by-concept';
import {environment} from '../../../environments/environment';
import {ActivatedRoute, Router} from '@angular/router';
import {User} from '../../model/user';
import {SessionService} from '../../services/session.service';

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

  constructor(private streetService: StreetService,
              private sessionService: SessionService,
              private houseService: HouseService,
              private modalService: NgbModal,
              private paymentService: PaymentService,
              private router: Router,
              private activatedRoute: ActivatedRoute) {
  }

  public ngOnInit(): void {

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
    this.streetService.getStreetInfo(this.selectedStreetId).subscribe(streetInfo => {
      this.selectedStreet = streetInfo;
      console.log(this.selectedStreet);
      this.loading = false;
    });
  }

  chipsForHouseEnabled(street: Street, house: House): void {
    Swal.fire({
      title: `¿Deseas ${house.chipsEnabled ? 'activar' : 'desactivar'} todos los chips para ${street.name} ${house.number}?`,
      showDenyButton: true,
      showCancelButton: false,
      confirmButtonText: `Sí`,
      denyButtonText: `No`,
    }).then((result) => {
      if (result.isConfirmed) {
        this.houseService.enableChips(house.id, house.chipsEnabled).subscribe(() => {
          Swal.fire('Guardado!', '', 'success');
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
    this.modalService.open(content, {ariaLabelledBy: 'modal-basic-title'}).result.then((result) => {
      console.log('Saving payment');
      console.log(this.newPayment);
      this.paymentService.save(this.newPayment).subscribe((payment) => {
        console.log('Saving payment');
        bimesterPayment.validated = payment.validated;
        bimesterPayment.amount += this.newPayment.amount;
        bimesterPayment.complete = this.maintenanceFee <= bimesterPayment.amount;
      });
    }, (reason) => {
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
    this.router.navigate(['house', id]);
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
    this.sessionService.getUser().subscribe(user => this.user = user);
    if (!bimesterPayment.validated && bimesterPayment.amount > 0 && this.userHasRoles(['ROLE_ADMIN'])) {
      Swal.fire({
        title: `¿Validar pago por $${bimesterPayment.amount} de la casa ${house.number}?`,
        showDenyButton: true,
        showCancelButton: false,
        confirmButtonText: `Sí`,
        denyButtonText: `No`,
      }).then((result) => {
        if (result.isConfirmed) {
          this.paymentService.validatePayment(bimesterPayment.id).subscribe((payment) => {
            Swal.fire('Validado!', '', 'success').then(console.log);
            bimesterPayment.validated = payment.validated;
          });
        }
      });
    }
  }

  userHasRoles(roles: string[]): boolean {
    return this.user.roles.some(r => roles.includes(r));
  }
}
