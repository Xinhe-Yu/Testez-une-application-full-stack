import { TestBed } from "@angular/core/testing";
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { AuthService } from "./auth.service";
import { expect } from '@jest/globals';
import { RegisterRequest } from "../interfaces/registerRequest.interface";
import { LoginRequest } from "../interfaces/loginRequest.interface";
import { SessionInformation } from "src/app/interfaces/sessionInformation.interface";

describe('AuthService', () => {
  let service: AuthService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [AuthService]
    });
    service = TestBed.inject(AuthService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('register', () => {
    it('should send a POST request to register endpoint', () => {
      const registerRequest: RegisterRequest = {
        email: 'thomas@mountain.com',
        firstName: 'Thomas',
        lastName: 'Mann',
        password: 'password'
      };

      service.register(registerRequest).subscribe();

      const req = httpMock.expectOne('api/auth/register');
      expect(req.request.method).toBe('POST');
      expect(req.request.body).toEqual(registerRequest);
      req.flush(null);
    })
  });

  describe('login', () => {
    it('should send a POST request to login endpoint and return SessionInformation', () => {
      const loginRequest: LoginRequest = {
        email: 'thomas@mountain.com',
        password: 'password'
      };

      const mockSessionInfo: SessionInformation = {
        token: 'mock-token',
        type: 'bearer',
        id: 1,
        username: 'thomas',
        firstName: 'Thomas',
        lastName: 'Mann',
        admin: true
      };

      service.login(loginRequest).subscribe(response => {
        expect(response).toEqual(mockSessionInfo);
      });

      const req = httpMock.expectOne('api/auth/login');
      expect(req.request.method).toBe('POST');
      expect(req.request.body).toEqual(loginRequest);
      req.flush(mockSessionInfo);
    })
  })
})
