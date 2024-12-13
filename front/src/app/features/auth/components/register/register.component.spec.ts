import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { Component } from '@angular/core';
import { expect } from '@jest/globals';
import { RegisterComponent } from './register.component';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { RegisterRequest } from '../../interfaces/registerRequest.interface';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

@Component({
  template: ''
})
class DummyLoginComponent { }

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let authService: AuthService;
  let router: Router;
  let httpMock: HttpTestingController;
  const registerRequest: RegisterRequest = {
    email: 'thomas@mountain.com',
    firstName: 'Thomas',
    lastName: 'Mann',
    password: 'password'
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [
        RegisterComponent,
        DummyLoginComponent
      ],
      imports: [
        RouterTestingModule.withRoutes([
          { path: 'login', component: DummyLoginComponent },
          { path: 'register', component: RegisterComponent }
        ]),
        BrowserAnimationsModule,
        HttpClientTestingModule,
        ReactiveFormsModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ],
      providers: [
        AuthService
      ]
    })
      .compileComponents();

    authService = TestBed.inject(AuthService);
    router = TestBed.inject(Router);
    httpMock = TestBed.inject(HttpTestingController);

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should handle successful registration', () => {
    component.form.setValue(registerRequest);
    const navigateSpy = jest.spyOn(router, 'navigate');

    component.submit();

    const req = httpMock.expectOne('api/auth/register');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(registerRequest);
    req.flush({});

    expect(navigateSpy).toHaveBeenCalledWith(['/login']);
    expect(component.onError).toBeFalsy();
  });

  it('should handle registration failure', () => {
    component.form.setValue(registerRequest);
    const navigateSpy = jest.spyOn(router, 'navigate');

    component.submit();

    const req = httpMock.expectOne('api/auth/register');
    expect(req.request.method).toBe('POST');
    req.flush('Registration failed', {
      status: 400,
      statusText: 'Bad Request'
    });

    expect(navigateSpy).not.toHaveBeenCalled();
    expect(component.onError).toBeTruthy();
  });
});
