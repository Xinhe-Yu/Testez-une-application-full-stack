import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatCardModule } from '@angular/material/card';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule, } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from '../../../../services/session.service';

import { DetailComponent } from './detail.component';
import { SessionApiService } from '../../services/session-api.service';
import { TeacherService } from 'src/app/services/teacher.service';
import { ActivatedRoute, Router } from '@angular/router';
import { of } from 'rxjs';
import { MatIconModule } from '@angular/material/icon';


describe('DetailComponent', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>;
  let sessionServiceMock: jest.Mocked<SessionService>;
  let sessionApiServiceMock: jest.Mocked<SessionApiService>;
  let teacherServiceMock: jest.Mocked<TeacherService>;
  let routerMock: jest.Mocked<Router>;
  let activatedRouteMock: jest.Mocked<ActivatedRoute>;

  beforeEach(async () => {
    sessionServiceMock = {
      sessionInformation: {
        admin: true,
        id: 1
      }
    } as unknown as jest.Mocked<SessionService>;

    sessionApiServiceMock = {
      detail: jest.fn(),
      delete: jest.fn(),
      participate: jest.fn(),
      unParticipate: jest.fn()
    } as unknown as jest.Mocked<SessionApiService>;

    teacherServiceMock = {
      detail: jest.fn()
    } as unknown as jest.Mocked<TeacherService>;

    routerMock = {
      navigate: jest.fn()
    } as unknown as jest.Mocked<Router>;

    activatedRouteMock = {
      snapshot: {
        paramMap: {
          get: jest.fn().mockReturnValue('1')
        }
      }
    } as unknown as jest.Mocked<ActivatedRoute>;

    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatSnackBarModule,
        ReactiveFormsModule,
        MatCardModule,
        NoopAnimationsModule,
        MatIconModule
      ],
      declarations: [DetailComponent],
      providers: [
        { provide: SessionService, useValue: sessionServiceMock },
        { provide: SessionApiService, useValue: sessionApiServiceMock },
        { provide: TeacherService, useValue: teacherServiceMock },
        { provide: Router, useValue: routerMock },
        { provide: ActivatedRoute, useValue: activatedRouteMock }
      ],
    })
      .compileComponents();
    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should fetch session details on init', () => {
    const mockSession = {
      id: 1,
      name: 'yoga',
      description: 'relax your self',
      date: new Date('2024-11-20'),
      teacher_id: 1,
      users: [],
    };
    const mockTeacher = {
      id: 1,
      lastName: 'Castorp',
      firstName: 'Hans',
      createdAt: new Date(),
      updatedAt: new Date()
    };
    sessionApiServiceMock.detail.mockReturnValue(of(mockSession));
    teacherServiceMock.detail.mockReturnValue(of(mockTeacher));

    fixture.detectChanges();

    expect(sessionApiServiceMock.detail).toHaveBeenCalledWith('1');
    expect(teacherServiceMock.detail).toHaveBeenCalledWith('1');
    expect(component.session).toEqual(mockSession);
    expect(component.teacher).toEqual(mockTeacher);
  });

  it('should delete session', () => {
    sessionApiServiceMock.delete.mockReturnValue(of({}));
    component.delete();
    expect(sessionApiServiceMock.delete).toHaveBeenCalledWith('1');
    expect(routerMock.navigate).toHaveBeenCalledWith(['sessions']);
  });

  it('should participate in session', () => {
    sessionApiServiceMock.participate.mockReturnValue(of());
    component.participate();
    expect(sessionApiServiceMock.participate).toHaveBeenCalledWith('1', '1');
  });

  it('should unparticipate from session', () => {
    sessionApiServiceMock.unParticipate.mockReturnValue(of());
    component.unParticipate();
    expect(sessionApiServiceMock.unParticipate).toHaveBeenCalledWith('1', '1');
  });

  it('should call window.history.back()', () => {
    const historySpy = jest.spyOn(window.history, 'back').mockImplementation(() => { });

    component.back();

    expect(historySpy).toHaveBeenCalled();
    historySpy.mockRestore();
  })
});
