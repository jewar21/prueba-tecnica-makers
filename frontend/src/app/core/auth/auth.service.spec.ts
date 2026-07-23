import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { AuthService } from './auth.service';

function createJwt(role: 'USER' | 'ADMIN', expiresAt: number): string {
  const encode = (value: object) =>
    btoa(JSON.stringify(value)).replaceAll('+', '-').replaceAll('/', '_').replaceAll('=', '');
  return `${encode({ alg: 'HS256', typ: 'JWT' })}.${encode({ roles: [role], exp: expiresAt })}.signature`;
}

describe('AuthService', () => {
  let service: AuthService;
  let http: HttpTestingController | undefined;

  beforeEach(() => {
    sessionStorage.clear();
    TestBed.configureTestingModule({
      providers: [AuthService, provideHttpClient(), provideHttpClientTesting()],
    });
  });

  afterEach(() => {
    http?.verify();
    sessionStorage.clear();
  });

  function injectService(): void {
    service = TestBed.inject(AuthService);
    http = TestBed.inject(HttpTestingController);
  }

  it('logs in, validates the JWT and stores only the token', () => {
    injectService();
    const token = createJwt('USER', Math.floor(Date.now() / 1000) + 300);

    service.login({ email: 'user@makers.com', password: 'correct-password' }).subscribe();

    const request = http!.expectOne('/api/auth/login');
    expect(request.request.method).toBe('POST');
    expect(request.request.body).toEqual({
      email: 'user@makers.com',
      password: 'correct-password',
    });
    request.flush({ token, role: 'USER' });

    expect(service.token()).toBe(token);
    expect(service.role()).toBe('USER');
    expect(service.isAuthenticated()).toBeTrue();
    expect(sessionStorage.getItem('loans.auth.token')).toBe(token);
    expect(sessionStorage.getItem('loans.auth.session')).toBeNull();
  });

  it('discards an expired stored token', () => {
    sessionStorage.setItem(
      'loans.auth.token',
      createJwt('ADMIN', Math.floor(Date.now() / 1000) - 1),
    );

    injectService();

    expect(service.isAuthenticated()).toBeFalse();
    expect(service.token()).toBeNull();
    expect(service.role()).toBeNull();
    expect(sessionStorage.getItem('loans.auth.token')).toBeNull();
  });

  it('discards a malformed stored token', () => {
    sessionStorage.setItem('loans.auth.token', 'not-a-jwt');

    injectService();

    expect(service.isAuthenticated()).toBeFalse();
    expect(sessionStorage.getItem('loans.auth.token')).toBeNull();
  });
});
