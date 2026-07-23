import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { CreateLoanRequest, Loan } from '../../shared/models/loan.models';

const API_URL = 'http://localhost:8080/api';

@Injectable({ providedIn: 'root' })
export class LoanService {
  private readonly http = inject(HttpClient);

  create(request: CreateLoanRequest): Observable<Loan> {
    return this.http.post<Loan>(`${API_URL}/loans`, request);
  }

  getMyLoans(): Observable<Loan[]> {
    return this.http.get<Loan[]>(`${API_URL}/loans/my`);
  }
}
