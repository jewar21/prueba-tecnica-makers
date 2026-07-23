import { HttpClient, provideHttpClient, withInterceptors } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { AuthService } from '../auth/auth.service';
import { authInterceptor } from './auth.interceptor';

describe('authInterceptor', () => {
  let httpClient: HttpClient;
  let httpTesting: HttpTestingController;
  let authService: jasmine.SpyObj<AuthService>;

  beforeEach(() => {
    authService = jasmine.createSpyObj<AuthService>('AuthService', ['token', 'logout']);
    authService.token.and.returnValue('signed.jwt.token');
    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(withInterceptors([authInterceptor])),
        provideHttpClientTesting(),
        { provide: AuthService, useValue: authService },
      ],
    });
    httpClient = TestBed.inject(HttpClient);
    httpTesting = TestBed.inject(HttpTestingController);
  });

  afterEach(() => httpTesting.verify());

  it('adds the bearer token to relative API requests', () => {
    httpClient.get('/api/loans').subscribe();

    const request = httpTesting.expectOne('/api/loans');
    expect(request.request.headers.get('Authorization')).toBe('Bearer signed.jwt.token');
    request.flush([]);
  });

  it('does not send the token to another origin', () => {
    httpClient.get('https://example.com/public').subscribe();

    const request = httpTesting.expectOne('https://example.com/public');
    expect(request.request.headers.has('Authorization')).toBeFalse();
    request.flush({});
  });

  it('clears the session when the API rejects a token with 401', () => {
    httpClient.get('/api/loans').subscribe({ error: () => undefined });

    const request = httpTesting.expectOne('/api/loans');
    request.flush({ error: 'No autenticado' }, { status: 401, statusText: 'Unauthorized' });

    expect(authService.logout).toHaveBeenCalled();
  });
});
