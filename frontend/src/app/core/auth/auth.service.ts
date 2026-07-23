import { HttpClient } from '@angular/common/http';
import { Injectable, signal } from '@angular/core';
import { Observable, map } from 'rxjs';
import { AuthSession, LoginCredentials, Role } from '../../shared/models/auth.models';

const API_URL = '/api';
const TOKEN_KEY = 'loans.auth.token';
const LEGACY_SESSION_KEY = 'loans.auth.session';

interface JwtClaims {
  role: Role;
  expiresAt: number;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly tokenState = signal<string | null>(this.restoreToken());

  constructor(private readonly http: HttpClient) {}

  token(): string | null {
    const token = this.tokenState();
    if (!token || !this.readValidClaims(token)) {
      if (token) {
        this.logout();
      }
      return null;
    }
    return token;
  }

  role(): Role | null {
    const token = this.token();
    return token ? this.readValidClaims(token)?.role ?? null : null;
  }

  isAuthenticated(): boolean {
    return this.token() !== null;
  }

  login(credentials: LoginCredentials): Observable<AuthSession> {
    return this.http.post<AuthSession>(`${API_URL}/auth/login`, credentials).pipe(
      map((session) => {
        const claims = this.readValidClaims(session.token);
        if (!claims || claims.role !== session.role) {
          throw new Error('Invalid authentication token');
        }
        this.tokenState.set(session.token);
        sessionStorage.setItem(TOKEN_KEY, session.token);
        sessionStorage.removeItem(LEGACY_SESSION_KEY);
        return { token: session.token, role: claims.role };
      }),
    );
  }

  logout(): void {
    this.tokenState.set(null);
    sessionStorage.removeItem(TOKEN_KEY);
    sessionStorage.removeItem(LEGACY_SESSION_KEY);
  }

  hasRole(role: Role): boolean {
    return this.role() === role;
  }

  private restoreToken(): string | null {
    const token = sessionStorage.getItem(TOKEN_KEY);
    sessionStorage.removeItem(LEGACY_SESSION_KEY);
    if (!token || !this.readValidClaims(token)) {
      sessionStorage.removeItem(TOKEN_KEY);
      return null;
    }
    return token;
  }

  private readValidClaims(token: string): JwtClaims | null {
    try {
      const parts = token.split('.');
      if (parts.length !== 3) {
        return null;
      }
      const payload = parts[1].replaceAll('-', '+').replaceAll('_', '/');
      const paddedPayload = payload.padEnd(payload.length + ((4 - (payload.length % 4)) % 4), '=');
      const claims = JSON.parse(atob(paddedPayload)) as { exp?: unknown; roles?: unknown };
      const role = Array.isArray(claims.roles) ? claims.roles[0] : null;
      if (
        typeof claims.exp !== 'number' ||
        claims.exp <= Math.floor(Date.now() / 1000) ||
        (role !== 'USER' && role !== 'ADMIN')
      ) {
        return null;
      }
      return { role, expiresAt: claims.exp };
    } catch {
      return null;
    }
  }
}
