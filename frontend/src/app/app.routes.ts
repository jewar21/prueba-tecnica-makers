import { Routes } from '@angular/router';
import { authGuard, roleGuard } from './core/guards/auth.guard';

export const routes: Routes = [
  {
    path: 'login',
    loadComponent: () => import('./features/login/login.component').then((module) => module.LoginComponent),
  },
  {
    path: 'loans',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./features/user-loans/user-loans-placeholder.component').then(
        (module) => module.UserLoansPlaceholderComponent,
      ),
  },
  {
    path: 'admin/loans',
    canActivate: [roleGuard('ADMIN')],
    loadComponent: () =>
      import('./features/admin-loans/admin-loans-placeholder.component').then(
        (module) => module.AdminLoansPlaceholderComponent,
      ),
  },
  { path: '', pathMatch: 'full', redirectTo: 'login' },
  { path: '**', redirectTo: 'login' },
];
