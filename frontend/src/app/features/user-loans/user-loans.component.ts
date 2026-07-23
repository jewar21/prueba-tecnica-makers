import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { DatePipe } from '@angular/common';
import { finalize } from 'rxjs';
import { LoanService } from '../../core/services/loan.service';
import { AuthService } from '../../core/auth/auth.service';
import { Loan, LoanStatus } from '../../shared/models/loan.models';
import { Router } from '@angular/router';

@Component({
  selector: 'app-user-loans',
  imports: [ReactiveFormsModule, DatePipe],
  templateUrl: './user-loans.component.html',
  styleUrl: './user-loans.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class UserLoansComponent {
  private readonly formBuilder = inject(FormBuilder);
  private readonly loanService = inject(LoanService);
  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);

  readonly isSubmitting = signal(false);
  readonly isLoadingLoans = signal(false);
  readonly createError = signal<string | null>(null);
  readonly loans = signal<Loan[]>([]);
  readonly showForm = signal(false);

  readonly form = this.formBuilder.nonNullable.group({
    amount: [0, [Validators.required, Validators.min(0.01)]],
    termMonths: [12, [Validators.required, Validators.min(1), Validators.max(360)]],
  });

  constructor() {
    this.loadLoans();
  }

  loadLoans(): void {
    this.isLoadingLoans.set(true);
    this.loanService.getMyLoans().pipe(finalize(() => this.isLoadingLoans.set(false)))
      .subscribe({
        next: (loans) => this.loans.set(loans),
        error: () => this.loans.set([]),
      });
  }

  toggleForm(): void {
    this.showForm.update((v) => !v);
    if (!this.showForm()) {
      this.createError.set(null);
    }
  }

  submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.createError.set(null);
    this.isSubmitting.set(true);
    this.loanService.create(this.form.getRawValue())
      .pipe(finalize(() => this.isSubmitting.set(false)))
      .subscribe({
        next: () => {
          this.showForm.set(false);
          this.form.reset({ amount: 0, termMonths: 12 });
          this.loadLoans();
        },
        error: () => this.createError.set('No fue posible crear la solicitud. Inténtalo más tarde.'),
      });
  }

  statusLabel(status: LoanStatus): string {
    const labels: Record<LoanStatus, string> = {
      PENDING: 'Pendiente',
      APPROVED: 'Aprobado',
      REJECTED: 'Rechazado',
    };
    return labels[status];
  }

  formatAmount(amount: number): string {
    return new Intl.NumberFormat('es-CO', { style: 'currency', currency: 'COP', minimumFractionDigits: 0, maximumFractionDigits: 0 }).format(amount) + ' COP';
  }

  logout(): void {
    this.authService.logout();
    void this.router.navigateByUrl('/login');
  }
}
