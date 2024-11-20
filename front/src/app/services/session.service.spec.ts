import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionService } from './session.service';
import { SessionInformation } from '../interfaces/sessionInformation.interface';

describe('SessionService', () => {
  let service: SessionService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SessionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('$isLogged', () => {
    it('should emit the current logged status', (done) => {
      const isLoogedObservable = service.$isLogged();
      isLoogedObservable.subscribe(isLogged => {
        expect(isLogged).toBeFalsy();
        done();
      })
    })
  });

  describe('logIn', () => {
    it('should set isLooged to true and update sessionInformation', () => {
      const mockUser: SessionInformation = {
        token: 'mock-token',
        type: 'bearer',
        id: 1,
        username: 'thomas',
        firstName: 'Thomas',
        lastName: 'Mann',
        admin: true
      };

      service.logIn(mockUser);

      expect(service.isLogged).toBeTruthy();
      expect(service.sessionInformation).toEqual(mockUser);
    })
  });

  describe('logOut', () => {
    it('should set isLogged to false and clear sessionInformation', () => {
      const mockUser: SessionInformation = {
        token: 'mock-token',
        type: 'bearer',
        id: 1,
        username: 'thomas',
        firstName: 'Thomas',
        lastName: 'Mann',
        admin: true
      };

      service.logIn(mockUser);

      service.logOut();

      expect(service.isLogged).toBeFalsy();
      expect(service.sessionInformation).toBeUndefined();
    })
  })
});
