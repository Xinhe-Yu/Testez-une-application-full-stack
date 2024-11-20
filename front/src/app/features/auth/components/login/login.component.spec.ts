import { HttpClientModule } from '@angular/common/http';
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
import { of, throwError } from 'rxjs';
import { Router } from '@angular/router';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let authServiceMock: jest.Mocked<AuthService>;
  let sessionServiceMock: jest.Mocked<SessionService>;
  let routerMock: jest.Mocked<Router>;
  // let ngZoneMock: Partial<NgZone>;

  beforeEach(async () => {
    // Mock AuthService
    authServiceMock = {
      login: jest.fn(), // Mocked observable response
    } as unknown as jest.Mocked<AuthService>;

    // Mock SessionService
    sessionServiceMock = {
      logIn: jest.fn(),
    } as unknown as jest.Mocked<SessionService>;

    // Mock Router
    routerMock = {
      navigate: jest.fn(),
    } as unknown as jest.Mocked<Router>;

    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      providers: [
        { provide: AuthService, useValue: authServiceMock },
        { provide: SessionService, useValue: sessionServiceMock },
        { provide: Router, useValue: routerMock },
      ],
      imports: [
        RouterTestingModule,
        BrowserAnimationsModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule]
    })
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call AuthService and navigate on successful login', () => {
    // Arrange
    const mockResponse = {
      token: 'mock-token',
      type: 'bearer',
      id: 1,
      username: 'thomas',
      firstName: 'Thomas',
      lastName: 'Mann',
      admin: true
    };
    authServiceMock.login.mockReturnValue(of(mockResponse));
    component.form.setValue({ email: 'thomas@mountain.com', password: 'password' });

    // Act
    component.submit();

    // Assert
    expect(authServiceMock.login).toHaveBeenCalledWith({ email: 'thomas@mountain.com', password: 'password' });
    expect(sessionServiceMock.logIn).toHaveBeenCalledWith(mockResponse);
    expect(routerMock.navigate).toHaveBeenCalledWith(['/sessions']);
  });

  it('should set onError to true when login fails', () => {
    // Arrange
    const errorResponse = new Error('Login failed');
    authServiceMock.login.mockReturnValue(throwError(() => errorResponse));

    component.form.setValue({ email: 'thomas@mountain.com', password: 'password' });

    // Act
    component.submit();

    // Assert
    expect(authServiceMock.login).toHaveBeenCalledWith({ email: 'thomas@mountain.com', password: 'password' });
    expect(component.onError).toBe(true);
    expect(sessionServiceMock.logIn).not.toHaveBeenCalled();
    expect(routerMock.navigate).not.toHaveBeenCalled();
  });
});
