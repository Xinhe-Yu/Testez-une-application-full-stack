import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { Component } from '@angular/core';
import { FormComponent } from './form.component';

import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { Router, convertToParamMap } from '@angular/router';
import { Session } from '../../interfaces/session.interface';
import { Teacher } from 'src/app/interfaces/teacher.interface';
import { expect } from '@jest/globals';
import { SessionService } from '../../../../services/session.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  template: ''
})
class DummySessionsComponent { }

describe('FormComponent Init and Create', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;
  let httpMock: HttpTestingController;
  let router: Router;
  let navigateSpy: jest.SpyInstance;

  const mockTeachers: Teacher[] = [{
    id: 1,
    lastName: 'Doe',
    firstName: 'John',
    createdAt: new Date(),
    updatedAt: new Date()
  }];

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([
          { path: 'sessions', component: DummySessionsComponent }
        ]),
        HttpClientTestingModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule,
        MatSnackBarModule,
        MatSelectModule,
        NoopAnimationsModule
      ],
      declarations: [FormComponent, DummySessionsComponent],
      providers: [
        {
          provide: SessionService,
          useValue: {
            sessionInformation: { id: 1, admin: false }
          }
        }
      ]
    }).compileComponents();

    router = TestBed.inject(Router);
    navigateSpy = jest.spyOn(router, 'navigate');
    httpMock = TestBed.inject(HttpTestingController);
    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should init form for create', () => {
    fixture.detectChanges();

    const teachersReq = httpMock.expectOne('api/teacher');
    expect(teachersReq.request.method).toBe('GET');
    teachersReq.flush(mockTeachers);

    expect(component.onUpdate).toBeFalsy();
    expect(component.sessionForm).toBeDefined();
  });

  it('should create a new session', () => {
    fixture.detectChanges();

    const teachersReq = httpMock.expectOne('api/teacher');
    teachersReq.flush(mockTeachers);

    const sessionData = {
      name: 'create yoga',
      description: 'relax your self',
      date: '2024-11-20',
      teacher_id: 1,
    };
    component.sessionForm?.setValue(sessionData);

    component.submit();

    const createReq = httpMock.expectOne('api/session');
    expect(createReq.request.method).toBe('POST');
    expect(createReq.request.body).toEqual(sessionData);
    createReq.flush({});

    expect(navigateSpy).toHaveBeenCalledWith(['sessions']);
  });
});


describe('FormComponent Update', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;
  let httpMock: HttpTestingController;
  let router: Router;
  let navigateSpy: jest.SpyInstance;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([
          { path: 'sessions', component: DummySessionsComponent }
        ]),
        HttpClientTestingModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule,
        MatSnackBarModule,
        MatSelectModule,
        NoopAnimationsModule
      ],
      declarations: [FormComponent, DummySessionsComponent],
      providers: [
        {
          provide: SessionService,
          useValue: {
            sessionInformation: { id: 1, admin: false }
          }
        },

        {
          provide: Router,
          useValue: {
            url: '/sessions/update/1',
            navigate: jest.fn()
          }
        },
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({ id: '1' })
            }
          }
        }
      ]
    }).compileComponents();

    router = TestBed.inject(Router);
    navigateSpy = jest.spyOn(router, 'navigate');
    httpMock = TestBed.inject(HttpTestingController);
    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should update an existing session', () => {
    const mockSession: Session = {
      id: 1,
      name: 'yoga',
      description: 'relax your self',
      date: new Date('2024-11-20'),
      teacher_id: 1,
      users: [],
    };

    fixture.detectChanges();

    const sessionReq = httpMock.expectOne('api/session/1');
    expect(sessionReq.request.method).toBe('GET');
    sessionReq.flush(mockSession);

    const sessionData = {
      name: 'update yoga',
      description: 'relax your self',
      date: '2024-11-20',
      teacher_id: 1,
    };

    component.sessionForm?.setValue(sessionData);
    component.submit();

    const updateReq = httpMock.expectOne('api/session/1');
    expect(updateReq.request.method).toBe('PUT');
    expect(updateReq.request.body).toEqual(sessionData);
    updateReq.flush({});

    expect(navigateSpy).toHaveBeenCalledWith(['sessions']);

  });
});
