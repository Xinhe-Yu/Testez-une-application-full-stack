package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TeacherTest {

  private Validator validator;

  @BeforeEach
  void setUp() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  void testTeacherCreation() {
    Teacher teacher = new Teacher();
    assertNotNull(teacher);
  }

  @Test
  void testTeacherBuilderWithValidData() {
    Teacher teacher = Teacher.builder()
        .id(1L)
        .lastName("Doe")
        .firstName("John")
        .build();

    assertNotNull(teacher);
    assertEquals(1L, teacher.getId());
    assertEquals("Doe", teacher.getLastName());
    assertEquals("John", teacher.getFirstName());
  }

  @Test
  void testTeacherValidation() {
    Teacher teacher = new Teacher();
    Set<ConstraintViolation<Teacher>> violations = validator.validate(teacher);
    assertFalse(violations.isEmpty());

    teacher.setLastName("Doe")
        .setFirstName("John");

    violations = validator.validate(teacher);
    assertTrue(violations.isEmpty());
  }

  @Test
  void testLastNameValidation() {
    Teacher teacher = Teacher.builder()
        .lastName("")
        .firstName("John")
        .build();

    Set<ConstraintViolation<Teacher>> violations = validator.validate(teacher);
    assertFalse(violations.isEmpty());
    assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("lastName")));
  }

  @Test
  void testFirstNameValidation() {
    Teacher teacher = Teacher.builder()
        .lastName("Doe")
        .firstName("")
        .build();

    Set<ConstraintViolation<Teacher>> violations = validator.validate(teacher);
    assertFalse(violations.isEmpty());
    assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("firstName")));
  }

  @Test
  void testNameSizeValidation() {
    Teacher teacher = Teacher.builder()
        .lastName("ThisIsAVeryLongLastName")
        .firstName("ThisIsAVeryLongFirstName")
        .build();

    Set<ConstraintViolation<Teacher>> violations = validator.validate(teacher);
    assertFalse(violations.isEmpty());
    assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("lastName")));
    assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("firstName")));
  }

  @Test
  void testEqualsAndHashCode() {
    Teacher teacher1 = Teacher.builder().id(1L).build();
    Teacher teacher2 = Teacher.builder().id(1L).build();
    Teacher teacher3 = Teacher.builder().id(2L).build();
    Teacher teacherNullId = Teacher.builder().build();

    // Test equality
    assertEquals(teacher1, teacher2);
    assertNotEquals(teacher1, teacher3);

    // Test hashCode
    assertEquals(teacher1.hashCode(), teacher2.hashCode());
    assertNotEquals(teacher1.hashCode(), teacher3.hashCode());

    // Test equality with null
    assertNotEquals(teacher1, null);

    // Test equality with self
    assertTrue(teacher1.equals(teacher1));

    // Test equality with different class
    assertNotEquals(teacher1, new Object());

    // Test equality with self
    assertEquals(teacher1, teacher1);

    // Test with null id
    assertNotEquals(teacher1, teacherNullId);
    assertNotEquals(teacherNullId, teacher1);
    assertEquals(teacherNullId, teacherNullId);

    // Test hashCode with null id
    assertNotEquals(teacher1.hashCode(), teacherNullId.hashCode());
    assertEquals(teacherNullId.hashCode(), teacherNullId.hashCode());
  }

  @Test
  void testToString() {
    Teacher teacher = Teacher.builder()
        .id(1L)
        .lastName("Doe")
        .firstName("John")
        .build();

    String toString = teacher.toString();
    assertTrue(toString.contains("id=1"));
    assertTrue(toString.contains("lastName=Doe"));
    assertTrue(toString.contains("firstName=John"));
  }

  @Test
  void testCreatedAtAndUpdatedAt() {
    Teacher teacher = new Teacher();
    assertNull(teacher.getCreatedAt());
    assertNull(teacher.getUpdatedAt());

    LocalDateTime now = LocalDateTime.now();
    teacher.setCreatedAt(now);
    teacher.setUpdatedAt(now);

    assertEquals(now, teacher.getCreatedAt());
    assertEquals(now, teacher.getUpdatedAt());
  }

  @Test
  void testTeacherBuilderToString() {
    Teacher.TeacherBuilder builder = Teacher.builder()
        .id(1L)
        .lastName("Doe")
        .firstName("John");

    String builderToString = builder.toString();
    assertNotNull(builderToString);
    assertTrue(builderToString.contains("id=1"));
    assertTrue(builderToString.contains("lastName=Doe"));
    assertTrue(builderToString.contains("firstName=John"));
  }

  @Test
  void testTeacherBuilderCreatedAtAndUpdatedAt() {
    LocalDateTime now = LocalDateTime.now();
    Teacher teacher = Teacher.builder()
        .id(1L)
        .lastName("Doe")
        .firstName("John")
        .createdAt(now)
        .updatedAt(now)
        .build();

    assertNotNull(teacher);
    assertEquals(now, teacher.getCreatedAt());
    assertEquals(now, teacher.getUpdatedAt());
  }

  @Test
  void testSetId() {
    Teacher teacher = new Teacher(); // Create a new Teacher instance

    // Test setting a valid ID
    Long validId = 1L;
    teacher.setId(validId);
    assertEquals(validId, teacher.getId());

    // Test setting another valid ID
    Long anotherValidId = 2L;
    teacher.setId(anotherValidId);
    assertEquals(anotherValidId, teacher.getId());
  }
}
