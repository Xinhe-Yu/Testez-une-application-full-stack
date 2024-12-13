package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TeacherServiceTest {
  private TeacherRepository teacherRepository;
  private TeacherService teacherService;

  @BeforeEach
  void setUp() {
    teacherRepository = mock(TeacherRepository.class);
    teacherService = new TeacherService(teacherRepository);
  }

  @Test
  void testFindAll() {
    // Arrange
    Teacher teacher1 = new Teacher();
    teacher1.setId(1L);
    Teacher teacher2 = new Teacher();
    teacher2.setId(2L);
    List<Teacher> expectedTeachers = Arrays.asList(teacher1, teacher2);

    when(teacherRepository.findAll()).thenReturn(expectedTeachers);

    // Act
    List<Teacher> actualTeachers = teacherService.findAll();

    // Assert
    assertEquals(expectedTeachers, actualTeachers);
    verify(teacherRepository).findAll();
  }

  @Test
  void testFindById_ExistingTeacher() {
    // Arrange
    Long teacherId = 1L;
    Teacher expectedTeacher = new Teacher();
    expectedTeacher.setId(teacherId);

    when(teacherRepository.findById(teacherId)).thenReturn(Optional.of(expectedTeacher));

    // Act
    Teacher actualTeacher = teacherService.findById(teacherId);

    // Assert
    assertNotNull(actualTeacher);
    assertEquals(expectedTeacher, actualTeacher);
    verify(teacherRepository).findById(teacherId);
  }

  @Test
  void testFindById_NonExistingTeacher() {
    // Arrange
    Long teacherId = 1L;
    when(teacherRepository.findById(teacherId)).thenReturn(Optional.empty());

    // Act
    Teacher actualTeacher = teacherService.findById(teacherId);

    // Assert
    assertNull(actualTeacher);
    verify(teacherRepository).findById(teacherId);
  }
}
