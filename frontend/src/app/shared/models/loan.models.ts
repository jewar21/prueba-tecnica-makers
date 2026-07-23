export type LoanStatus = 'PENDING' | 'APPROVED' | 'REJECTED';

export interface Loan {
  id: number;
  amount: number;
  termMonths: number;
  status: LoanStatus;
  createdAt: string;
  updatedAt: string;
}

export interface CreateLoanRequest {
  amount: number;
  termMonths: number;
}
