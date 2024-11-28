package com.openclassrooms.starterjwt.security.services;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserDetailsServiceImplTest {

  @Mock
  private UserRepository userRepository;

  private UserDetailsServiceImpl userDetailsService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    userDetailsService = new UserDetailsServiceImpl(userRepository);
  }

  @Test
  void loadUserByUsername_UserFound_ReturnsUserDetails() {
    // Given
    String email = "test@example.com";
    User user = new User();
    user.setId(1L);
    user.setEmail(email);
    user.setLastName("Doe");
    user.setFirstName("John");
    user.setPassword("password");

    when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

    // When
    UserDetails userDetails = userDetailsService.loadUserByUsername(email);

    // Then
    assertNotNull(userDetails);
    assertEquals(email, userDetails.getUsername());
    assertEquals("password", userDetails.getPassword());
    assertTrue(userDetails instanceof UserDetailsImpl);
    UserDetailsImpl userDetailsImpl = (UserDetailsImpl) userDetails;
    assertEquals(1L, userDetailsImpl.getId());
    assertEquals("Doe", userDetailsImpl.getLastName());
    assertEquals("John", userDetailsImpl.getFirstName());
  }

  @Test
  void loadUserByUsername_UserNotFound_ThrowsUsernameNotFoundException() {
    // Given
    String email = "nonexistent@example.com";
    when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

    // When & Then
    Exception exception = assertThrows(UsernameNotFoundException.class, () -> {
      userDetailsService.loadUserByUsername(email);
    });

    String expectedMessage = "User Not Found with email: " + email;
    String actualMessage = exception.getMessage();
    assertTrue(actualMessage.contains(expectedMessage));
  }
}
