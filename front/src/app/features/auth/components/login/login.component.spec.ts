import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';
import { LoginComponent } from './login.component';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';
import { Component } from '@angular/core';

@Component({
  template: ''
})
class DummyListComponent { }

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let router: Router;
  let httpMock: HttpTestingController;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [
        LoginComponent,
        DummyListComponent
      ],
      providers: [
        AuthService,
        SessionService
      ],
      imports: [
        RouterTestingModule,
        BrowserAnimationsModule,
        RouterTestingModule.withRoutes([
          { path: 'sessions', component: DummyListComponent },
          { path: 'login', component: LoginComponent }
        ]),
        HttpClientTestingModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule
      ]
    })
      .compileComponents();

    router = TestBed.inject(Router);
    httpMock = TestBed.inject(HttpTestingController);

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should handle successful login', () => {
    const mockResponse: SessionInformation = {
      token: 'mock-token',
      type: 'bearer',
      id: 1,
      username: 'thomas',
      firstName: 'Thomas',
      lastName: 'Mann',
      admin: true
    };

    component.form.setValue({
      email: 'thomas@mountain.com',
      password: 'password'
    });

    const navigateSpy = jest.spyOn(router, 'navigate');
    component.submit();

    const req = httpMock.expectOne('api/auth/login');
    expect(req.request.method).toBe('POST');
    req.flush(mockResponse);

    expect(navigateSpy).toHaveBeenCalledWith(['/sessions']);
  });

  it('should handle login failure', () => {
    component.form.setValue({
      email: 'thomas@mountain.com',
      password: 'wrong-password'
    });

    component.submit();

    const req = httpMock.expectOne('api/auth/login');
    req.flush('Login failed', {
      status: 401,
      statusText: 'Unauthorized'
    });

    expect(component.onError).toBe(true);
  });
});
