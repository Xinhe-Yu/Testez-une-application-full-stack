package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserTest {
  private Validator validator;

  @BeforeEach
  void setUp() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  void testUserCreation() {
    User user = new User();
    assertNotNull(user);
  }

  @Test
  void testUserBuilderWithValidData() {
    User user = User.builder()
        .id(1L)
        .email("test@example.com")
        .lastName("Doe")
        .firstName("John")
        .password("password123") // Add this line
        .admin(false)
        .build();

    assertNotNull(user);
    assertEquals(1L, user.getId());
    assertEquals("test@example.com", user.getEmail());
    assertEquals("Doe", user.getLastName());
    assertEquals("John", user.getFirstName());
    assertEquals("password123", user.getPassword());
    assertFalse(user.isAdmin());
    Set<javax.validation.ConstraintViolation<User>> violations = validator.validate(user);
    assertTrue(violations.isEmpty());
  }

  @Test
  void testUserBuilderWithNullEmail() {
    // Expecting a NullPointerException when email is null
    assertThrows(NullPointerException.class, () -> {
      User.builder()
          .id(1L)
          .email(null)
          .lastName("Doe")
          .firstName("John")
          .password("password123")
          .admin(false)
          .build();
    });
  }

  @Test
  void testUserBuilderWithNullLastName() {
    // Expecting a NullPointerException when lastName is null
    assertThrows(NullPointerException.class, () -> {
      User.builder()
          .id(1L)
          .email("test@example.com")
          .lastName(null)
          .firstName("John")
          .password("password123")
          .admin(false)
          .build();
    });
  }

  @Test
  void testUserBuilderWithNullFirstName() {
    // Expecting a NullPointerException when firstName is null
    assertThrows(NullPointerException.class, () -> {
      User.builder()
          .id(1L)
          .email("test@example.com")
          .lastName("Doe")
          .firstName(null)
          .password("password123")
          .admin(false)
          .build();
    });
  }

  @Test
  void testUserBuilderWithNullPassword() {
    // Expecting a NullPointerException when password is null
    assertThrows(NullPointerException.class, () -> {
      User.builder()
          .id(1L)
          .email("test@example.com")
          .lastName("Doe")
          .firstName("John")
          .password(null)
          .admin(false)
          .build();
    });
  }

  @Test
  void testUserBuilderWithInvalidEmailFormat() {
    // Testing with an invalid email format
    User user = User.builder()
        .id(1L)
        .email("invalid-email")
        .lastName("Doe")
        .firstName("John")
        .password("password123")
        .admin(false)
        .build();

    Set<javax.validation.ConstraintViolation<User>> violations = validator.validate(user);

    assertFalse(violations.isEmpty());
    assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
  }

  @Test
  void testUserBuilderWithLongLastName() {
    // Testing with a last name that exceeds the maximum length
    String longLastName = "ThisLastNameIsWayTooLongAndShouldFailValidation";

    User user = User.builder()
        .id(1L)
        .email("test@example.com")
        .lastName(longLastName)
        .firstName("John")
        .password("password123")
        .admin(false)
        .build();

    Set<javax.validation.ConstraintViolation<User>> violations = validator.validate(user);

    assertFalse(violations.isEmpty());
    assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("lastName")));
  }

  @Test
  void testUserBuilderWithLongFirstName() {
    // Testing with a first name that exceeds the maximum length
    String longFirstName = "ThisFirstNameIsWayTooLongAndShouldFailValidation";

    User user = User.builder()
        .id(1L)
        .email("test@example.com")
        .lastName("Doe")
        .firstName(longFirstName)
        .password("password123")
        .admin(false)
        .build();

    Set<javax.validation.ConstraintViolation<User>> violations = validator.validate(user);

    assertFalse(violations.isEmpty());
    assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("firstName")));
  }

  @Test
  void testUserBuilderWithLongPassword() {
    // Testing with a password that exceeds the maximum length
    String longPassword = "ThisPasswordIsWayTooLongAndShouldFailValidationBecauseItExceedsTheMaximumAllowedLengthThisPasswordIsWayTooLongAndShouldFailValidationBecauseItExceedsTheMaximumAllowedLengthThisPasswordIsWayTooLongAndShouldFailValidationBecauseItExceedsTheMaximumAllowedLength";

    User user = User.builder()
        .id(1L)
        .email("test@example.com")
        .lastName("Doe")
        .firstName("John")
        .password(longPassword)
        .admin(false)
        .build();

    Set<javax.validation.ConstraintViolation<User>> violations = validator.validate(user);

    assertFalse(violations.isEmpty());
    assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("password")));
  }

  @Test
  void testUserBuilderWithCreatedAtAndUpdatedAt() {
    LocalDateTime now = LocalDateTime.now();
    User user = User.builder()
        .id(1L)
        .email("john.doe@example.com")
        .lastName("Doe")
        .firstName("John")
        .password("password123")
        .admin(false)
        .createdAt(now)
        .updatedAt(now)
        .build();

    assertNotNull(user);
    assertEquals(now, user.getCreatedAt());
    assertEquals(now, user.getUpdatedAt());
  }

  @Test
  void testUserValidation() {
    User user = new User("test@example.com", "Doe", "John", "password123", false);
    Set<javax.validation.ConstraintViolation<User>> violations = validator.validate(user);
    assertTrue(violations.isEmpty());
  }

  @Test
  void testInvalidEmail() {
    User user = new User("invalid-email", "Doe", "John", "password123", false);
    Set<javax.validation.ConstraintViolation<User>> violations = validator.validate(user);
    assertFalse(violations.isEmpty());
    assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
  }

  @Test
  void testNameSizeValidation() {
    User user = new User("test@example.com", "ThisIsAVeryLongLastName", "ThisIsAVeryLongFirstName", "password123",
        false);
    Set<javax.validation.ConstraintViolation<User>> violations = validator.validate(user);
    assertFalse(violations.isEmpty());
    assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("lastName")));
    assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("firstName")));
  }

  @Test
  void testPasswordSizeValidation() {
    User user = new User("test@example.com", "Doe", "John",
        "ThisIsAVeryLongPasswordThatExceedsTheMaximumAllowedLengthThisIsAVeryLongPasswordThatExceedsTheMaximumAllowedLengtThisIsAVeryLongPasswordThatExceedsTheMaximumAllowedLengt",
        false);
    Set<javax.validation.ConstraintViolation<User>> violations = validator.validate(user);
    assertFalse(violations.isEmpty());
    assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("password")));
  }

  @Test
  void testEqualsAndHashCode() {
    User user1 = User.builder()
        .id(1L)
        .email("john.doe@example.com")
        .lastName("Doe")
        .firstName("John")
        .password("password123")
        .admin(false)
        .build();

    User user2 = User.builder()
        .id(1L)
        .email("jane.smith@example.com")
        .lastName("Smith")
        .firstName("Jane")
        .password("securePass456")
        .admin(true)
        .build();

    User user3 = User.builder()
        .id(2L)
        .email("bob.johnson@example.com")
        .lastName("Johnson")
        .firstName("Bob")
        .password("bobsPassword789")
        .admin(false)
        .build();

    User userNullId = User.builder()
        .email("harry.potter@example.com")
        .lastName("Potter")
        .firstName("Harry")
        .password("harrysPassword789")
        .admin(false)
        .build();

    // Test equality
    assertEquals(user1, user2);
    assertNotEquals(user1, user3);

    // Test hashCode
    assertEquals(user1.hashCode(), user2.hashCode());
    assertNotEquals(user1.hashCode(), user3.hashCode());

    // Test equality with null
    assertNotEquals(user1, null);

    // Test equality with self
    assertTrue(user1.equals(user1));

    // Test equality with different class
    assertNotEquals(user1, new Object());

    // Test equality with self
    assertEquals(user1, user1);

    // Test with null id
    assertNotEquals(user1, userNullId);
    assertNotEquals(userNullId, user1);
    assertEquals(userNullId, userNullId);

    // Test hashCode with null id
    assertNotEquals(user1.hashCode(), userNullId.hashCode());
    assertEquals(userNullId.hashCode(), userNullId.hashCode());
  }

  @Test
  void testToString() {
    User user = User.builder()
        .id(1L)
        .email("test@example.com")
        .lastName("Doe")
        .firstName("John")
        .password("password123")
        .admin(false)
        .build();

    String toString = user.toString();
    assertTrue(toString.contains("id=1"));
    assertTrue(toString.contains("email=test@example.com"));
    assertTrue(toString.contains("lastName=Doe"));
    assertTrue(toString.contains("firstName=John"));
    assertTrue(toString.contains("password=password123"));
    assertTrue(toString.contains("admin=false"));
  }

  @Test
  void testUserBuilderToString() {
    User.UserBuilder builder = User.builder()
        .id(1L)
        .email("test@example.com")
        .lastName("Doe")
        .firstName("John")
        .password("password123")
        .admin(false);

    String builderToString = builder.toString(); // Call to UserBuilder's toString()
    assertNotNull(builderToString);
    assertTrue(builderToString.contains("id=1"));
    assertTrue(builderToString.contains("email=test@example.com"));
    assertTrue(builderToString.contains("lastName=Doe"));
    assertTrue(builderToString.contains("firstName=John"));
    assertTrue(builderToString.contains("admin=false"));
  }

  @Test
  void testCreatedAtAndUpdatedAt() {
    User user = new User();
    assertNull(user.getCreatedAt());
    assertNull(user.getUpdatedAt());

    LocalDateTime now = LocalDateTime.now();
    user.setCreatedAt(now);
    user.setUpdatedAt(now);

    assertEquals(now, user.getCreatedAt());
    assertEquals(now, user.getUpdatedAt());
  }

  @Test
  void testSetEmail() {
    User user = new User();
    assertNull(user.getEmail());

    String email = "john.doe@example.com";
    user.setEmail(email);
    assertEquals(email, user.getEmail());

    // Invalid email
    String invalidEmail = "invalid-email";
    user.setEmail(invalidEmail);
    Set<javax.validation.ConstraintViolation<User>> violations = validator.validate(user);
    assertFalse(violations.isEmpty());
    assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));

    // Null email
    assertThrows(NullPointerException.class, () -> user.setEmail(null));
  }

  @Test
  void testSetLastName() {
    User user = new User();
    // Valid last name
    String validLastName = "Doe";
    user.setLastName(validLastName);
    assertEquals(validLastName, user.getLastName());

    // Invalid last name (too long)
    String invalidLastName = "ThisLastNameIsWayTooLong";
    user.setLastName(invalidLastName);
    Set<javax.validation.ConstraintViolation<User>> violations = validator.validate(user);
    assertFalse(violations.isEmpty());
    assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("lastName")));

    // Null last name
    assertThrows(NullPointerException.class, () -> user.setLastName(null));
  }

  @Test
  void testSetFirstName() {
    User user = new User();
    // Valid first name
    String validFirstName = "John";
    user.setFirstName(validFirstName);
    assertEquals(validFirstName, user.getFirstName());

    // Invalid first name (too long)
    String invalidFirstName = "ThisFirstNameIsWayTooLong";
    user.setFirstName(invalidFirstName);
    Set<javax.validation.ConstraintViolation<User>> violations = validator.validate(user);
    assertFalse(violations.isEmpty());
    assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("firstName")));

    // Null first name
    assertThrows(NullPointerException.class, () -> user.setFirstName(null));
  }

  @Test
  void testSetPassword() {
    User user = new User();
    // Valid password
    String validPassword = "password123";
    user.setPassword(validPassword);
    assertEquals(validPassword, user.getPassword());

    // Invalid password (too long)
    String invalidPassword = "ThisPasswordIsWayTooLongAndShouldFailValidationBecauseItExceedsTheMaximumAllowedLengthThisPasswordIsWayTooLongAndShouldFailValidationBecauseItExceedsTheMaximumAllowedLengthThisPasswordIsWayTooLongAndShouldFailValidationBecauseItExceedsTheMaximumAllowedLength";
    user.setPassword(invalidPassword);
    Set<javax.validation.ConstraintViolation<User>> violations = validator.validate(user);
    assertFalse(violations.isEmpty());
    assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("password")));

    // Null password
    assertThrows(NullPointerException.class, () -> user.setPassword(null));
  }

  @Test
  void testSetAdmin() {
    User user = new User();

    // Test setting admin to true
    user.setAdmin(true);
    assertTrue(user.isAdmin());

    // Test setting admin to false
    user.setAdmin(false);
    assertFalse(user.isAdmin());
  }

  @Test
  void testFullConstructorWithValidData() {
    LocalDateTime now = LocalDateTime.now();
    User user = new User(1L, "john.doe@example.com", "Doe", "John", "password123", false, now, now);

    assertEquals("john.doe@example.com", user.getEmail());
    assertEquals("Doe", user.getLastName());
    assertEquals("John", user.getFirstName());
    assertEquals("password123", user.getPassword());
    assertFalse(user.isAdmin());
    assertEquals(now, user.getCreatedAt());
    assertEquals(now, user.getUpdatedAt());

    Set<javax.validation.ConstraintViolation<User>> violations = validator.validate(user);
    assertTrue(violations.isEmpty());
  }

  @Test
  void testFullConstructorWithInvalidEmail() {
    LocalDateTime now = LocalDateTime.now();

    // Invalid email
    User user = new User(1L, "invalid-email", "Doe", "John", "password123", false, now, now);

    Set<javax.validation.ConstraintViolation<User>> violations = validator.validate(user);

    assertFalse(violations.isEmpty());
    assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
  }

  @Test
  void testFullConstructorWithNullEmail() {
    LocalDateTime now = LocalDateTime.now();
    assertThrows(NullPointerException.class, () -> {
      new User(1L, null, "Doe", "John", "password123", false, now, now);
    });
  }

  @Test
  void testFullConstructorWithNullLastName() {
    LocalDateTime now = LocalDateTime.now();
    assertThrows(NullPointerException.class, () -> {
      new User(1L, "john.doe@example.com", null, "John", "password123", false, now, now);
    });
  }

  @Test
  void testFullConstructorWithNullFirstName() {
    LocalDateTime now = LocalDateTime.now();
    assertThrows(NullPointerException.class, () -> {
      new User(1L, "john.doe@example.com", "Doe", null, "password123", false, now, now);
    });
  }

  @Test
  void testFullConstructorWithNullPassword() {
    LocalDateTime now = LocalDateTime.now();
    assertThrows(NullPointerException.class, () -> {
      new User(1L, "john.doe@example.com", "Doe", "John", null, false, now, now);
    });
  }

  @Test
  void testFullConstructorWithLongLastName() {
    LocalDateTime now = LocalDateTime.now();
    String longLastName = "ThisLastNameIsWayTooLong";
    User user = new User(1L, "john.doe@example.com", longLastName, "John", "password123", false, now, now);

    Set<javax.validation.ConstraintViolation<User>> violations = validator.validate(user);
    assertFalse(violations.isEmpty());
    assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("lastName")));
  }

  @Test
  void testFullConstructorWithLongFirstName() {
    LocalDateTime now = LocalDateTime.now();
    String longFirstName = "ThisFirstNameIsWayTooLong";
    User user = new User(1L, "john.doe@example.com", "Doe", longFirstName, "password123", false, now, now);

    Set<javax.validation.ConstraintViolation<User>> violations = validator.validate(user);
    assertFalse(violations.isEmpty());
    assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("firstName")));
  }

  @Test
  void testFullConstructorWithLongPassword() {
    LocalDateTime now = LocalDateTime.now();
    String longPassword = "ThisPasswordIsWayTooLongAndShouldFailValidationBecauseItExceedsTheMaximumAllowedLengthThisPasswordIsWayTooLongAndShouldFailValidationBecauseItExceedsTheMaximumAllowedLengthThisPasswordIsWayTooLongAndShouldFailValidationBecauseItExceedsTheMaximumAllowedLength";

    User user = new User(1L, "john.doe@example.com", "Doe", "John", longPassword, false, now, now);

    Set<javax.validation.ConstraintViolation<User>> violations = validator.validate(user);
    assertFalse(violations.isEmpty());
    assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("password")));
  }

  @Test
  void testFullConstructorWithAdminFlag() {
    LocalDateTime now = LocalDateTime.now();

    // Test with admin set to true
    User adminUser = new User(1L, "admin@example.com", "Admin", "User", "adminPassword123", true, now, now);
    assertTrue(adminUser.isAdmin());

    // Test with admin set to false
    User regularUser = new User(2L, "user@example.com", "Regular", "User", "userPassword123", false, now, now);
    assertFalse(regularUser.isAdmin());
  }

  @Test
  void testUserConstructorWithNullEmail() {
    // Expecting a NullPointerException when email is null
    assertThrows(NullPointerException.class, () -> {
      new User(null, "Doe", "John", "password123", false);
    });
  }

  @Test
  void testUserConstructorWithNullLastName() {
    // Expecting a NullPointerException when lastName is null
    assertThrows(NullPointerException.class, () -> {
      new User("john.doe@example.com", null, "John", "password123", false);
    });
  }

  @Test
  void testUserConstructorWithNullFirstName() {
    // Expecting a NullPointerException when firstName is null
    assertThrows(NullPointerException.class, () -> {
      new User("john.doe@example.com", "Doe", null, "password123", false);
    });
  }

  @Test
  void testUserConstructorWithNullPassword() {
    // Expecting a NullPointerException when password is null
    assertThrows(NullPointerException.class, () -> {
      new User("john.doe@example.com", "Doe", "John", null, false);
    });
  }

  @Test
  void testUserConstructorWithInvalidEmailFormat() {
    // Testing with an invalid email format
    User user = new User("invalid-email", "Doe", "John", "password123", false);
    Set<javax.validation.ConstraintViolation<User>> violations = validator.validate(user);

    assertFalse(violations.isEmpty());
    assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
  }

  @Test
  void testUserConstructorWithLongLastName() {
    // Testing with a last name that exceeds the maximum length
    String longLastName = "ThisLastNameIsWayTooLongAndShouldFailValidation";
    User user = new User("john.doe@example.com", longLastName, "John", "password123", false);

    Set<javax.validation.ConstraintViolation<User>> violations = validator.validate(user);

    assertFalse(violations.isEmpty());
    assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("lastName")));
  }

  @Test
  void testUserConstructorWithLongFirstName() {
    // Testing with a first name that exceeds the maximum length
    String longFirstName = "ThisFirstNameIsWayTooLongAndShouldFailValidation";
    User user = new User("john.doe@example.com", "Doe", longFirstName, "password123", false);

    Set<javax.validation.ConstraintViolation<User>> violations = validator.validate(user);

    assertFalse(violations.isEmpty());
    assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("firstName")));
  }

  @Test
  void testUserConstructorWithLongPassword() {
    // Testing with a password that exceeds the maximum length
    String longPassword = "ThisPasswordIsWayTooLongAndShouldFailValidationBecauseItExceedsTheMaximumAllowedLengthThisPasswordIsWayTooLongAndShouldFailValidationBecauseItExceedsTheMaximumAllowedLengthThisPasswordIsWayTooLongAndShouldFailValidationBecauseItExceedsTheMaximumAllowedLength";

    User user = new User("john.doe@example.com", "Doe", "John", longPassword, false);

    Set<javax.validation.ConstraintViolation<User>> violations = validator.validate(user);

    assertFalse(violations.isEmpty());
    assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("password")));
  }
}
