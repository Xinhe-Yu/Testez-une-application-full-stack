import { HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { MatToolbarModule } from '@angular/material/toolbar';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';

import { AppComponent } from './app.component';
import { Router } from '@angular/router';
import { SessionService } from './services/session.service';
import { of } from 'rxjs';


describe('AppComponent', () => {
  let routerMock: jest.Mocked<Router>;
  let sessionServiceMock: jest.Mocked<SessionService>;

  beforeEach(async () => {
    routerMock = {
      navigate: jest.fn()
    } as unknown as jest.Mocked<Router>;

    sessionServiceMock = {
      $isLogged: jest.fn(),
      logOut: jest.fn()
    } as unknown as jest.Mocked<SessionService>;


    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatToolbarModule
      ],
      declarations: [
        AppComponent
      ],
      providers: [
        { provide: Router, useValue: routerMock },
        { provide: SessionService, useValue: sessionServiceMock },
      ]
    }).compileComponents();
  });

  it('should create the app', () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    expect(app).toBeTruthy();
  });

  it('should return isLogged observable from Session Service', () => {
    const mockIsLogged$ = of(true);
    sessionServiceMock.$isLogged.mockReturnValue(mockIsLogged$);

    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;

    const result = app.$isLogged();

    expect(sessionServiceMock.$isLogged).toHaveBeenCalled();
    expect(result).toBe(mockIsLogged$);
  });

  it('should log out and navigate to home page', () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;

    app.logout();
    expect(sessionServiceMock.logOut).toHaveBeenCalled();
    expect(routerMock.navigate).toHaveBeenCalledWith(['']);
  });
});
