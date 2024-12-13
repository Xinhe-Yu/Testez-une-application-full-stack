import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatCardModule } from '@angular/material/card';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from '../../../../services/session.service';
import { DetailComponent } from './detail.component';
import { SessionApiService } from '../../services/session-api.service';
import { TeacherService } from 'src/app/services/teacher.service';
import { ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
import { Session } from '../../interfaces/session.interface';
import { Teacher } from 'src/app/interfaces/teacher.interface';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { Component } from '@angular/core';

@Component({
  template: ''
})
class DummySessionsComponent { }

describe('DetailComponent', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>;
  let router: Router;
  let httpMock: HttpTestingController;

  let mockSession: Session = {
    id: 1,
    name: 'yoga',
    description: 'relax your self',
    date: new Date('2024-11-20'),
    teacher_id: 1,
    users: [3],
  };
  let mockTeacher: Teacher = {
    id: 1,
    lastName: 'Castorp',
    firstName: 'Hans',
    createdAt: new Date(),
    updatedAt: new Date()
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([
          { path: 'sessions', component: DummySessionsComponent }
        ]),
        HttpClientTestingModule,
        MatSnackBarModule,
        ReactiveFormsModule,
        MatCardModule,
        NoopAnimationsModule,
        MatIconModule
      ],
      declarations: [DetailComponent, DummySessionsComponent],
      providers: [
        SessionApiService,
        TeacherService,
        {
          provide: SessionService,
          useValue: {
            sessionInformation: {
              admin: true,
              id: 1
            }
          }
        },
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({
                id: '1'
              })
            }
          }
        }
      ],
    })
      .compileComponents();

    router = TestBed.inject(Router);
    httpMock = TestBed.inject(HttpTestingController);

    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should fetch session details on init', () => {
    fixture.detectChanges();

    const sessionReq = httpMock.expectOne('api/session/1');
    expect(sessionReq.request.method).toBe('GET');
    sessionReq.flush(mockSession);

    const teacherReq = httpMock.expectOne('api/teacher/1');
    expect(teacherReq.request.method).toBe('GET');
    teacherReq.flush(mockTeacher);

    expect(component.session).toEqual(mockSession);
    expect(component.teacher).toEqual(mockTeacher);
  });

  it('should delete session', () => {
    const navigateSpy = jest.spyOn(router, 'navigate');
    component.delete();

    const req = httpMock.expectOne('api/session/1');
    expect(req.request.method).toBe('DELETE');
    req.flush({});

    expect(navigateSpy).toHaveBeenCalledWith(['sessions']);
  });

  it('should participate in session', () => {
    component.participate();

    const participateReq = httpMock.expectOne('api/session/1/participate/1');
    expect(participateReq.request.method).toBe('POST');
    participateReq.flush({});

    const sessionReq = httpMock.expectOne('api/session/1');
    expect(sessionReq.request.method).toBe('GET');
    sessionReq.flush({ ...mockSession, users: [1] });

    const teacherReq = httpMock.expectOne('api/teacher/1');
    expect(teacherReq.request.method).toBe('GET');
    teacherReq.flush(mockTeacher);
  });

  it('should unparticipate from session', () => {
    component.unParticipate();

    const unparticipateReq = httpMock.expectOne('api/session/1/participate/1');
    expect(unparticipateReq.request.method).toBe('DELETE');
    unparticipateReq.flush({});

    const sessionReq = httpMock.expectOne('api/session/1');
    expect(sessionReq.request.method).toBe('GET');
    sessionReq.flush({ ...mockSession, users: [] });

    const teacherReq = httpMock.expectOne('api/teacher/1');
    expect(teacherReq.request.method).toBe('GET');
    teacherReq.flush(mockTeacher);
  });

  it('should call window.history.back()', () => {
    const historySpy = jest.spyOn(window.history, 'back').mockImplementation(() => { });
    component.back();
    expect(historySpy).toHaveBeenCalled();
    historySpy.mockRestore();
  });
});
