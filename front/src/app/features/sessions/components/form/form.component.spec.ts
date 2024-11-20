import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';
import { SessionApiService } from '../../services/session-api.service';
import { of } from 'rxjs';

import { FormComponent } from './form.component';
import { TeacherService } from 'src/app/services/teacher.service';
import { ActivatedRoute, Route, Router } from '@angular/router';
import { create } from 'cypress/types/lodash';
import { Session } from '../../interfaces/session.interface';
import { Teacher } from 'src/app/interfaces/teacher.interface';

describe('FormComponent', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;
  let sessionServiceMock: jest.Mocked<SessionService>;
  let sessionApiServiceMock: jest.Mocked<SessionApiService>;
  let teacherServiceMock: jest.Mocked<TeacherService>;
  let routerMock: { url: string; navigate: jest.Mock };
  let activatedRouteMock: jest.Mocked<ActivatedRoute>;
  let matSnackBarMock: jest.Mocked<MatSnackBar>;

  beforeEach(async () => {
    sessionServiceMock = {
      sessionInformation: { id: 1, admin: false },
      // logOut: jest.fn(),
    } as unknown as jest.Mocked<SessionService>;

    sessionApiServiceMock = {
      detail: jest.fn(),
      create: jest.fn(),
      update: jest.fn()
    } as unknown as jest.Mocked<SessionApiService>;

    teacherServiceMock = {
      all: jest.fn(),
    } as unknown as jest.Mocked<TeacherService>;

    routerMock = {
      navigate: jest.fn(),
      url: '/sessions/create'
    };

    activatedRouteMock = {
      snapshot: {
        paramMap: {
          get: jest.fn().mockReturnValue('1'),
        },
      },
    } as unknown as jest.Mocked<ActivatedRoute>;

    matSnackBarMock = {
      open: jest.fn(),
    } as unknown as jest.Mocked<MatSnackBar>;

    await TestBed.configureTestingModule({

      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule,
        MatSnackBarModule,
        MatSelectModule,
        BrowserAnimationsModule
      ],
      providers: [
        { provide: SessionService, useValue: sessionServiceMock },
        { provide: SessionApiService, useValue: sessionApiServiceMock },
        { provide: TeacherService, useValue: teacherServiceMock },
        { provide: Router, useValue: routerMock },
        { provide: ActivatedRoute, useValue: activatedRouteMock },
        { provide: MatSnackBar, useValue: matSnackBarMock },
      ],
      declarations: [FormComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should init form for create', () => {
    teacherServiceMock.all.mockReturnValue(of([]));
    fixture.detectChanges();

    expect(component.onUpdate).toBeFalsy();
    expect(component.sessionForm).toBeDefined();
  });

  it('should correctly assign form value to session while creating', () => {
    const sessionData = {
      name: 'create yoga',
      description: 'relax your self',
      date: '2024-11-20',
      teacher_id: 1,
    };

    component.sessionForm = new FormGroup({
      name: new FormControl(sessionData.name),
      date: new FormControl(sessionData.date),
      description: new FormControl(sessionData.description),
      teacher_id: new FormControl(sessionData.teacher_id),
    })

    sessionApiServiceMock.create.mockReturnValue(of({} as Session));

    component.submit();

    expect(sessionApiServiceMock.create).toHaveBeenCalledWith(sessionData);
  });

  it('should create a new session', () => {
    fixture.detectChanges();
    const sessionData = {
      name: 'create yoga',
      description: 'relax your self',
      date: '2024-11-20',
      teacher_id: 1,
    };
    component.sessionForm?.setValue(sessionData);

    sessionApiServiceMock.create.mockReturnValue(of({} as Session));
    component.submit();

    expect(sessionApiServiceMock.create).toHaveBeenCalled();
    expect(routerMock.navigate).toHaveBeenCalledWith(['sessions']);
  });

  it('should update an existing sessions', () => {
    const mockSessionId = '1';
    routerMock.url = 'sessions/update/1'
    activatedRouteMock.snapshot.paramMap.get = jest.fn().mockReturnValue(mockSessionId);

    const mockSession: Session = {
      id: 1,
      name: 'yoga',
      description: 'relax your self',
      date: new Date('2024-11-20'),
      teacher_id: 1,
      users: [],
    };

    sessionApiServiceMock.detail.mockReturnValue(of(mockSession));
    teacherServiceMock.all.mockReturnValue(of([]));

    component.ngOnInit();

    const sessionData = {
      name: 'update yoga',
      description: 'relax your self',
      date: '2024-11-20',
      teacher_id: 1,
    };

    component.sessionForm?.setValue(sessionData)
    sessionApiServiceMock.update.mockReturnValue(of({} as Session));

    component.submit();

    expect(sessionApiServiceMock.update).toHaveBeenCalledWith(mockSessionId, sessionData);
    expect(matSnackBarMock.open).toHaveBeenCalledWith('Session updated !', 'Close', { duration: 3000 });
    expect(routerMock.navigate).toHaveBeenCalledWith(['sessions']);
  });

});
