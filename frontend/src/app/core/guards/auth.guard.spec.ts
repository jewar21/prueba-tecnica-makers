import { TestBed } from '@angular/core/testing';
import { provideRouter, Router, UrlTree } from '@angular/router';
import { AuthService } from '../auth/auth.service';
import { authGuard, roleGuard } from './auth.guard';


describe('authentication guards', () => {
  let authService: jasmine.SpyObj<AuthService>;
  let router: Router;

  beforeEach(() => {
    authService = jasmine.createSpyObj<AuthService>('AuthService', ['isAuthenticated', 'hasRole']);
    TestBed.configureTestingModule({
      providers: [
        provideRouter([]),
        { provide: AuthService, useValue: authService },
      ],
    });
    router = TestBed.inject(Router);
  });

  it('redirects an unauthenticated user to login', () => {
    authService.isAuthenticated.and.returnValue(false);

    const result = TestBed.runInInjectionContext(() => authGuard({} as never, {} as never));

    expect(result instanceof UrlTree).toBeTrue();
    expect(router.serializeUrl(result as UrlTree)).toBe('/login');
  });

  it('allows an authenticated user', () => {
    authService.isAuthenticated.and.returnValue(true);

    const result = TestBed.runInInjectionContext(() => authGuard({} as never, {} as never));

    expect(result).toBeTrue();
  });

  it('redirects USER away from an ADMIN route', () => {
    authService.isAuthenticated.and.returnValue(true);
    authService.hasRole.and.returnValue(false);

    const result = TestBed.runInInjectionContext(() => roleGuard('ADMIN')({} as never, {} as never));

    expect(result instanceof UrlTree).toBeTrue();
    expect(router.serializeUrl(result as UrlTree)).toBe('/loans');
  });

  it('allows ADMIN through an ADMIN route', () => {
    authService.isAuthenticated.and.returnValue(true);
    authService.hasRole.and.returnValue(true);

    const result = TestBed.runInInjectionContext(() => roleGuard('ADMIN')({} as never, {} as never));

    expect(result).toBeTrue();
  });
});
