import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { expect } from '@jest/globals';

import { RegisterComponent } from './register.component';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { RegisterRequest } from '../../interfaces/registerRequest.interface';
import { of, throwError } from 'rxjs';

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let authServiceMock: jest.Mocked<AuthService>;
  let routerMock: jest.Mocked<Router>;

  beforeEach(async () => {
    authServiceMock = {
      register: jest.fn(), // Mocked observable response
    } as unknown as jest.Mocked<AuthService>;

    routerMock = {
      navigate: jest.fn(),
    } as unknown as jest.Mocked<Router>;


    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      imports: [
        BrowserAnimationsModule,
        HttpClientModule,
        ReactiveFormsModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ],
      providers: [
        { provide: AuthService, useValue: authServiceMock },
        { provide: Router, useValue: routerMock }
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call auth.Service.register and navigate to login on successful registration', () => {
    const registerRequest: RegisterRequest = {
      email: 'thomas@mountain.com',
      firstName: 'Thomas',
      lastName: 'Mann',
      password: 'password'
    };
    component.form.setValue(registerRequest);
    authServiceMock.register.mockReturnValue(of(undefined));

    component.submit();

    expect(authServiceMock.register).toHaveBeenCalledWith(registerRequest);
    expect(routerMock.navigate).toHaveBeenCalledWith(['/login']);
    expect(component.onError).toBeFalsy();
  });

  it('should set onError to true on registration failure', () => {
    const registerRequest: RegisterRequest = {
      email: 'thomas@mountain.com',
      firstName: 'Thomas',
      lastName: 'Mann',
      password: 'password'
    };
    component.form.setValue(registerRequest);
    authServiceMock.register.mockReturnValue(throwError(() => new Error('Registration failed')));

    component.submit();
    expect(authServiceMock.register).toHaveBeenCalledWith(registerRequest);
    expect(routerMock.navigate).not.toHaveBeenCalled();
    expect(component.onError).toBeTruthy();
  })
});
