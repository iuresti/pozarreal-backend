export interface CourseAssistantPayment {
  id: string;
  paymentDate: string;
  courseAssistantId: string;
  concept: string;
  amount: number;
  receiptNumber: string;
  notes: string;
}
