export interface Payment {
  id: string;
  streetId: string;
  houseId: string;
  paymentDate: string;
  amount: number;
  paymentConceptId: string;
  paymentSubConceptId: string;
  paymentMode: string;
  notes: string;
  validated: boolean;
  conflict: boolean;
  files: FileList;
}
