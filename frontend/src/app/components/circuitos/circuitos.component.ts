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
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-circuitos',
  templateUrl: './circuitos.component.html',
  styleUrls: ['./circuitos.component.css']
})
export class CircuitosComponent implements OnInit {

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
      }else {
        this.activatedRoute.params.subscribe(params => {
          if (params['streetId']) {
            this.selectedStreetId = params['streetId'];
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

  addPayment(content, bim: number, bimesterPayment: PaymentByConcept, house: House): void {
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
      this.paymentService.save(this.newPayment).subscribe(() => {
        console.log('Saving payment');
        bimesterPayment.amount += this.newPayment.amount;
        bimesterPayment.complete = this.maintenanceFee <= bimesterPayment.amount;
      });
    }, (reason) => {
      console.log('Cancel saving payment');
    });
  }

  onMouseOver(number) {
    number.style.display = 'block';
  }

  onMouseLeave(number) {
    number.style.display = 'none';
  }

  showHouse(id: string) {
    this.router.navigate(["house", id]);
  }
}
