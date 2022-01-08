export interface PaymentFilter {
  street: string;
  house: string;
  startDate: string;
  endDate: string;
  concepts: string[];
  paymentMode: string;
  status: string;
}
