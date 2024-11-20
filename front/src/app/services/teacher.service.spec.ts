import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { TeacherService } from './teacher.service';
import { Teacher } from '../interfaces/teacher.interface';

describe('TeacherService', () => {
  let service: TeacherService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule
      ],
      providers: [TeacherService]
    });
    service = TestBed.inject(TeacherService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('all', () => {
    it('should return all teachers', () => {
      const mockTeachers: Teacher[] = [
        {
          id: 1,
          lastName: 'Castorp',
          firstName: 'Hans',
          createdAt: new Date(),
          updatedAt: new Date()
        },
        {
          id: 2,
          lastName: 'Ziemssen',
          firstName: 'Joachim',
          createdAt: new Date(),
          updatedAt: new Date()
        }
      ];
      service.all().subscribe(teachers => {
        expect(teachers).toEqual(mockTeachers)
      });

      const req = httpMock.expectOne(service['pathService']);
      expect(req.request.method).toBe('GET');
      req.flush(mockTeachers);
    })


  })

  describe('detail', () => {
    it('should return a teacher by id', () => {
      const mockTeacher: Teacher = {
        id: 1,
        lastName: 'Castorp',
        firstName: 'Hans',
        createdAt: new Date(),
        updatedAt: new Date()
      };

      const teacherId = '1';

      service.detail(teacherId).subscribe(teacher => {
        expect(teacher).toEqual(mockTeacher);
      });

      const req = httpMock.expectOne(`${service['pathService']}/${teacherId}`);
      expect(req.request.method).toBe('GET');
      req.flush(mockTeacher);
    });
  });
});
