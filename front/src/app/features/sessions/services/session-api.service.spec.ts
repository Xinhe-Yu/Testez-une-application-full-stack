import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionApiService } from './session-api.service';
import { Session } from '../interfaces/session.interface';

describe('SessionsService', () => {
  let service: SessionApiService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule
      ],
      providers: [SessionApiService]
    });
    service = TestBed.inject(SessionApiService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });


  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('delete', () => {
    it('should delete a session', () => {
      const sessionId = '1';

      service.delete(sessionId).subscribe(response => {
        expect(response).toBeTruthy();
      });

      const req = httpMock.expectOne(`${service['pathService']}/${sessionId}`);
      expect(req.request.method).toBe('DELETE');
      req.flush({});
    });
  });

  describe('create', () => {
    it('should create a session', () => {
      const newSession: Session = {
        id: 1,
        name: 'yoga',
        description: 'relax your self',
        date: new Date(),
        teacher_id: 1,
        users: [],
        createdAt: new Date(),
        updatedAt: new Date()
      };
      service.create(newSession).subscribe(response => {
        expect(response).toEqual(newSession);
      });

      const req = httpMock.expectOne(service['pathService']);
      expect(req.request.method).toBe('POST');
      expect(req.request.body).toEqual(newSession);
      req.flush(newSession);
    });
  });

  describe('update', () => {
    it('should update an existing session', () => {
      const updatedSession: Session = {
        id: 1,
        name: 'updated yoga',
        description: 'relax yourself',
        date: new Date(),
        teacher_id: 1,
        users: [],
        createdAt: new Date(),
        updatedAt: new Date()
      };
      const sessionId = '1';

      service.update(sessionId, updatedSession).subscribe(response => {
        expect(response).toEqual(updatedSession);
      });

      const req = httpMock.expectOne(`${service['pathService']}/${sessionId}`);
      expect(req.request.method).toBe('PUT');
      expect(req.request.body).toEqual(updatedSession);
      req.flush(updatedSession);
    });
  });

  describe('participate', () => {
    it('should add a user to a session', () => {
      const sessionId = '1';
      const userId = '1';

      service.participate(sessionId, userId).subscribe(response => {
        expect(response).toBeUndefined();
      });

      const req = httpMock.expectOne(`${service['pathService']}/${sessionId}/participate/${userId}`);
      expect(req.request.method).toBe('POST');
      expect(req.request.body).toBeNull();
      req.flush(null);
    });
  });

  describe('unParticipate', () => {
    it('should remove a user from a session', () => {
      const sessionId = '1';
      const userId = '1';

      service.unParticipate(sessionId, userId).subscribe(response => {
        expect(response).toBeUndefined();
      });

      const req = httpMock.expectOne(`${service['pathService']}/${sessionId}/participate/${userId}`);
      expect(req.request.method).toBe('DELETE');
      req.flush(null);
    });
  });

  describe('detail', () => {
    it('should return a session by id', () => {
      const mockSession: Session = {
        id: 1,
        name: 'yoga',
        description: 'relax your self',
        date: new Date('2024-11-20'),
        teacher_id: 1,
        users: [],
      };

      const sessionId = '1';

      service.detail(sessionId).subscribe(session => {
        expect(session).toEqual(mockSession);
      });
      const req = httpMock.expectOne(`${service['pathService']}/${sessionId}`);
      expect(req.request.method).toBe('GET');
      req.flush(mockSession);
    })
  })
});
