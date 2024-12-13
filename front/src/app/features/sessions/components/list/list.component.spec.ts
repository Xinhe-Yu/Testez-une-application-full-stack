import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { RouterTestingModule } from '@angular/router/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { By } from '@angular/platform-browser';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { expect } from '@jest/globals';

import { ListComponent } from './list.component';
import { SessionService } from 'src/app/services/session.service';
import { SessionApiService } from '../../services/session-api.service';
import { Session } from '../../interfaces/session.interface';

describe('ListComponent', () => {
  let component: ListComponent;
  let fixture: ComponentFixture<ListComponent>;
  let httpMock: HttpTestingController;

  const mockSessions: Session[] = [
    {
      id: 1,
      name: 'Yoga',
      description: 'Relaxation session',
      date: new Date('2024-11-20'),
      teacher_id: 1,
      users: [1, 2]
    }
  ];

  describe('Admin user', () => {
    beforeEach(async () => {
      await TestBed.configureTestingModule({
        declarations: [ListComponent],
        imports: [
          HttpClientTestingModule,
          RouterTestingModule,
          MatCardModule,
          MatIconModule,
          NoopAnimationsModule
        ],
        providers: [
          {
            provide: SessionService,
            useValue: {
              sessionInformation: {
                admin: true
              }
            }
          },
          SessionApiService
        ]
      }).compileComponents();

      fixture = TestBed.createComponent(ListComponent);
      component = fixture.componentInstance;
      httpMock = TestBed.inject(HttpTestingController);
    });

    afterEach(() => {
      httpMock.verify();
    });

    it('should show create button for admin', () => {
      fixture.detectChanges();

      const sessionsReq = httpMock.expectOne('api/session');
      sessionsReq.flush(mockSessions);

      fixture.detectChanges();

      const createButton = fixture.debugElement.query(By.css('button[routerLink="create"]'));
      expect(createButton).toBeTruthy();
    });

    it('should show edit button for admin', () => {
      fixture.detectChanges();

      const sessionsReq = httpMock.expectOne('api/session');
      sessionsReq.flush(mockSessions);

      fixture.detectChanges();

      const editButtons = fixture.debugElement.queryAll(By.css('button[mat-raised-button][color="primary"]'));
      const editButton = editButtons.find(button =>
        button.nativeElement.textContent.includes('Edit')
      );

      expect(editButton).toBeTruthy();
    });
  });

  describe('Non-admin user', () => {
    beforeEach(async () => {
      await TestBed.configureTestingModule({
        declarations: [ListComponent],
        imports: [
          HttpClientTestingModule,
          RouterTestingModule,
          MatCardModule,
          MatIconModule,
          NoopAnimationsModule
        ],
        providers: [
          {
            provide: SessionService,
            useValue: {
              sessionInformation: {
                admin: false
              }
            }
          },
          SessionApiService
        ]
      }).compileComponents();

      fixture = TestBed.createComponent(ListComponent);
      component = fixture.componentInstance;
      httpMock = TestBed.inject(HttpTestingController);
    });

    afterEach(() => {
      httpMock.verify();
    });

    it('should not show create button for non-admin', () => {
      fixture.detectChanges();

      const sessionsReq = httpMock.expectOne('api/session');
      sessionsReq.flush(mockSessions);

      fixture.detectChanges();

      const createButton = fixture.debugElement.query(By.css('button[routerLink="create"]'));
      expect(createButton).toBeFalsy();
    });

    it('should not show edit button for non-admin', () => {
      fixture.detectChanges();

      const sessionsReq = httpMock.expectOne('api/session');
      sessionsReq.flush(mockSessions);

      fixture.detectChanges();

      const editButton = fixture.debugElement.query(By.css('button[routerLink="update,1"]'));
      expect(editButton).toBeFalsy();
    });

    it('should show session details', () => {
      fixture.detectChanges();

      const sessionsReq = httpMock.expectOne('api/session');
      sessionsReq.flush(mockSessions);

      fixture.detectChanges();

      const sessionTitle = fixture.debugElement.query(By.css('.item mat-card-title'));
      expect(sessionTitle.nativeElement.textContent.trim()).toBe('Yoga');
    });
  });
});
