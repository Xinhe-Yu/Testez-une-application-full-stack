package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {
  @Mock
  private UserService userService;

  @Mock
  private UserMapper userMapper;

  @Mock
  private SecurityContext securityContext;

  @Mock
  private Authentication authentication;

  @Mock
  private UserDetails userDetails;

  @InjectMocks
  private UserController userController;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    SecurityContextHolder.setContext(securityContext);
  }

  @Test
  void testFindByIdValidIdReturnsUser() {
    User user = new User();
    UserDto userDto = new UserDto();
    when(userService.findById(1L)).thenReturn(user);
    when(userMapper.toDto(user)).thenReturn(userDto);

    ResponseEntity<?> response = userController.findById("1");

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(userDto, response.getBody());
  }

  @Test
  void testFindByIdInvalidIdReturnsBadRequest() {
    ResponseEntity<?> response = userController.findById("invalid");

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  void testFindByIdNonExistentIdReturnsNotFound() {
    when(userService.findById(1L)).thenReturn(null);

    ResponseEntity<?> response = userController.findById("1");

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  void testDeleteUserSuccessful() {
    User user = new User();
    user.setEmail("test@example.com");
    when(userService.findById(1L)).thenReturn(user);
    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(authentication.getPrincipal()).thenReturn(userDetails);
    when(userDetails.getUsername()).thenReturn("test@example.com");

    ResponseEntity<?> response = userController.save("1");

    assertEquals(HttpStatus.OK, response.getStatusCode());
    verify(userService).delete(1L);
  }

  @Test
  void testDeleteUserUnauthorized() {
    User user = new User();
    user.setEmail("test@example.com");
    when(userService.findById(1L)).thenReturn(user);
    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(authentication.getPrincipal()).thenReturn(userDetails);
    when(userDetails.getUsername()).thenReturn("other@example.com");

    ResponseEntity<?> response = userController.save("1");

    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    verify(userService, never()).delete(anyLong());
  }

  @Test
  void testDeleteUserNonExistent() {
    when(userService.findById(1L)).thenReturn(null);

    ResponseEntity<?> response = userController.save("1");

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  void testDeleteUserInvalidId() {
    ResponseEntity<?> response = userController.save("invalid");

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }
}
