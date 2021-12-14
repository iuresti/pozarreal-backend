export interface PaymentByConcept {
  id: string;
  complete: boolean;
  amount: number;
  validated: boolean;
  conflict: boolean;
}
