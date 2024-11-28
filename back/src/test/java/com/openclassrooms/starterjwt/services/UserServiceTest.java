package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  private UserService userService;

  @BeforeEach
  void setUp() {
    userService = new UserService(userRepository);
  }

  @Test
  void testDelete() {
    Long userId = 1L;

    userService.delete(userId);

    verify(userRepository, times(1)).deleteById(userId);
  }

  @Test
  void testFindByIdWhenUserExists() {
    Long userId = 1L;
    User expectedUser = new User();
    expectedUser.setId(userId);

    when(userRepository.findById(userId)).thenReturn(Optional.of(expectedUser));

    User result = userService.findById(userId);

    assertNotNull(result);
    assertEquals(userId, result.getId());
    verify(userRepository, times(1)).findById(userId);
  }

  @Test
  void testFindByIdWhenUserDoesNotExist() {
    Long userId = 1L;

    when(userRepository.findById(userId)).thenReturn(Optional.empty());

    User result = userService.findById(userId);

    assertNull(result);
    verify(userRepository, times(1)).findById(userId);
  }
}
