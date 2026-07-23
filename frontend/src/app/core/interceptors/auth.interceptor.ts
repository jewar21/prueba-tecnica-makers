import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { catchError, throwError } from 'rxjs';
import { AuthService } from '../auth/auth.service';

const API_PREFIX = '/api';

export const authInterceptor: HttpInterceptorFn = (request, next) => {
  const authService = inject(AuthService);
  const token = authService.token();
  const isApiRequest = request.url === API_PREFIX || request.url.startsWith(`${API_PREFIX}/`);
  const authenticatedRequest =
    token && isApiRequest
      ? request.clone({ setHeaders: { Authorization: `Bearer ${token}` } })
      : request;

  return next(authenticatedRequest).pipe(
    catchError((error: unknown) => {
      if (isApiRequest && error instanceof HttpErrorResponse && error.status === 401) {
        authService.logout();
      }
      return throwError(() => error);
    }),
  );
};
