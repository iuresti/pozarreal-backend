import {PaymentByConcept} from './payment-by-concept';

export interface House {
  id: string;
  number: string;
  chipsEnabled: boolean;
  street: string;
  toggleChips: boolean; // used only for view purposes
  twoMonthsPayments: PaymentByConcept[];
  parkingPenPayment: PaymentByConcept;
}
