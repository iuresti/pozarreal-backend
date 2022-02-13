import {PaymentByConcept} from './payment-by-concept';
import {Chip} from './chip';

export interface HouseByUser {
  id: string;
  userId: string;
  houseId: string;
  streetName: string;
  number: string;
  mainHouse: boolean;
  validated: boolean;
  twoMonthsPayments: PaymentByConcept[];
  chips: Chip[];
}
