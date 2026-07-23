import { HttpErrorResponse } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { AuthService } from '../../core/auth/auth.service';
import { LoginComponent } from './login.component';


describe('LoginComponent', () => {
  let fixture: ComponentFixture<LoginComponent>;
  let component: LoginComponent;
  let authService: jasmine.SpyObj<AuthService>;
  let router: Router;

  beforeEach(async () => {
    authService = jasmine.createSpyObj<AuthService>('AuthService', ['login']);
    authService.login.and.returnValue(of({ token: 'signed.jwt.token', role: 'USER' }));

    await TestBed.configureTestingModule({
      imports: [LoginComponent],
      providers: [
        provideRouter([]),
        { provide: AuthService, useValue: authService },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    router = TestBed.inject(Router);
    fixture.detectChanges();
  });

  it('validates email and password with a reactive form', () => {
    expect(component.form.invalid).toBeTrue();

    component.form.setValue({ email: 'user@makers.com', password: 'correct-password' });

    expect(component.form.valid).toBeTrue();
  });

  it('does not submit an invalid form and reveals validation errors', () => {
    component.submit();

    expect(authService.login).not.toHaveBeenCalled();
    expect(component.form.controls.email.touched).toBeTrue();
    expect(component.form.controls.password.touched).toBeTrue();
  });

  it('navigates a user to their loans after login', () => {
    const navigate = spyOn(router, 'navigateByUrl').and.resolveTo(true);
    component.form.setValue({ email: 'user@makers.com', password: 'correct-password' });

    component.submit();

    expect(authService.login).toHaveBeenCalledWith({
      email: 'user@makers.com',
      password: 'correct-password',
    });
    expect(navigate).toHaveBeenCalledWith('/loans');
  });

  it('shows the generic credentials message only for a 401 response', () => {
    authService.login.and.returnValue(
      throwError(() => new HttpErrorResponse({ status: 401, statusText: 'Unauthorized' })),
    );
    component.form.setValue({ email: 'user@makers.com', password: 'wrong-password' });

    component.submit();

    expect(component.errorMessage()).toBe('Correo o contraseña incorrectos.');
    expect(component.isSubmitting()).toBeFalse();
  });

  it('shows an operational message for server errors', () => {
    authService.login.and.returnValue(
      throwError(() => new HttpErrorResponse({ status: 500, statusText: 'Server Error' })),
    );
    component.form.setValue({ email: 'user@makers.com', password: 'correct-password' });

    component.submit();

    expect(component.errorMessage()).toBe('No fue posible iniciar sesión. Inténtalo más tarde.');
  });
});
