package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SessionTest {
  private Validator validator;

  @Mock
  private Teacher teacher;

  @Mock
  private User user1, user2;

  @BeforeEach
  void setUp() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  void testSessionCreation() {
    Session session = new Session();
    assertNotNull(session);
  }

  @Test
  void testSessionBuilder() {
    Session session = Session.builder()
        .id(1L)
        .name("Test Session")
        .date(new Date())
        .description("Test Description")
        .teacher(teacher)
        .build();

    assertNotNull(session);
    assertEquals(1L, session.getId());
    assertEquals("Test Session", session.getName());
    assertNotNull(session.getDate());
    assertEquals("Test Description", session.getDescription());
    assertEquals(teacher, session.getTeacher());
  }

  @Test
  void testSessionValidation() {
    Session session = new Session();
    Set<ConstraintViolation<Session>> violations = validator.validate(session);
    assertFalse(violations.isEmpty());

    session.setName("Test Session")
        .setDate(new Date())
        .setDescription("Test Description");

    violations = validator.validate(session);
    assertTrue(violations.isEmpty());
  }

  @Test
  void testNameValidation() {
    Session session = Session.builder()
        .name("")
        .date(new Date())
        .description("Test Description")
        .build();

    Set<ConstraintViolation<Session>> violations = validator.validate(session);
    assertFalse(violations.isEmpty());
    assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name")));
  }

  @Test
  void testDescriptionSizeValidation() {
    Session session = Session.builder()
        .name("Test Session")
        .date(new Date())
        .description(repeatString("A", 2501))
        .build();

    Set<ConstraintViolation<Session>> violations = validator.validate(session);
    assertFalse(violations.isEmpty());
    assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("description")));
  }

  @Test
  void testEqualsAndHashCode() {
    Session session1 = Session.builder().id(1L).build();
    Session session2 = Session.builder().id(1L).build();
    Session session3 = Session.builder().id(2L).build();
    Session sessionNullId = Session.builder().build();

    // Test equality
    assertEquals(session1, session2);
    assertNotEquals(session1, session3);

    // Test hashCode
    assertEquals(session1.hashCode(), session2.hashCode());
    assertNotEquals(session1.hashCode(), session3.hashCode());

    // Test equality with null
    assertNotEquals(session1, null);

    // Test equality with self
    assertTrue(session1.equals(session1));

    // Test equality with different class
    assertNotEquals(session1, new Object());

    // Test equality with self
    assertEquals(session1, session1);

    // Test with null id
    assertNotEquals(session1, sessionNullId);
    assertNotEquals(sessionNullId, session1);
    assertEquals(sessionNullId, sessionNullId);

    // Test hashCode with null id
    assertNotEquals(session1.hashCode(), sessionNullId.hashCode());
    assertEquals(sessionNullId.hashCode(), sessionNullId.hashCode());
  }

  @Test
  void testToString() {
    Session session = Session.builder()
        .id(1L)
        .name("Test Session")
        .date(new Date())
        .description("Test Description")
        .build();

    String toString = session.toString();
    assertTrue(toString.contains("id=1"));
    assertTrue(toString.contains("name=Test Session"));
    assertTrue(toString.contains("description=Test Description"));
  }

  @Test
  void testCreatedAtAndUpdatedAt() {
    Session session = new Session();
    assertNull(session.getCreatedAt());
    assertNull(session.getUpdatedAt());

    LocalDateTime now = LocalDateTime.now();
    session.setCreatedAt(now);
    session.setUpdatedAt(now);

    assertEquals(now, session.getCreatedAt());
    assertEquals(now, session.getUpdatedAt());
  }

  @Test
  void testSetId() {
    Session session = new Session();

    Long validId = 1L;
    session.setId(validId);
    assertEquals(validId, session.getId());

    Long anotherValidId = 2L;
    session.setId(anotherValidId);
    assertEquals(anotherValidId, session.getId());
  }

  @Test
  void testSetTeacher() {
    Teacher teacher = new Teacher();
    teacher.setId(1L); // Assuming there's a setId method in Teacher
    teacher.setLastName("Doe");
    teacher.setFirstName("John");

    Session session = new Session();
    session.setTeacher(teacher); // Set the teacher

    assertNotNull(session.getTeacher());
    assertEquals(teacher, session.getTeacher()); // Verify that the teacher is set correctly
  }

  @Test
  void testSetUsers() {
    List<User> users = new ArrayList<>();
    users.add(user1);
    users.add(user2);

    Session session = new Session();
    session.setUsers(users); // Set the list of users

    assertNotNull(session.getUsers());
    assertEquals(2, session.getUsers().size()); // Verify that the users list is set correctly
    assertTrue(session.getUsers().contains(user1)); // Verify that user1 is in the list
    assertTrue(session.getUsers().contains(user2)); // Verify that user2 is in the list
  }

  @Test
  void testSessionBuilderCreatedAtAndUpdatedAt() {
    LocalDateTime now = LocalDateTime.now();
    Session session = Session.builder()
        .id(1L)
        .name("Test Session")
        .date(new Date())
        .description("Test Description")
        .teacher(teacher)
        .createdAt(now)
        .updatedAt(now)
        .build();

    assertNotNull(session);
    assertEquals(now, session.getCreatedAt());
    assertEquals(now, session.getUpdatedAt());
  }

  @Test
  void testSessionBuilderToString() {
    Session.SessionBuilder builder = Session.builder()
        .id(1L)
        .name("Math Class")
        .date(new Date())
        .description("A session for math enthusiasts");

    String builderToString = builder.toString(); // Call to SessionBuilder's toString()
    assertNotNull(builderToString);
    assertTrue(builderToString.contains("id=1"));
    assertTrue(builderToString.contains("name=Math Class"));
    assertTrue(builderToString.contains("description=A session for math enthusiasts"));
  }

  @Test
  void testSessionBuilderWithEmptyUserList() {
    List<User> users = new ArrayList<>();

    Session session = Session.builder()
        .id(1L)
        .name("Art Class")
        .date(new Date())
        .description("A session for art enthusiasts")
        .users(users)
        .build();

    assertNotNull(session.getUsers());
    assertEquals(0, session.getUsers().size());
  }

  private String repeatString(String str, int times) {
    return new String(new char[times]).replace("\0", str);
  }
}
