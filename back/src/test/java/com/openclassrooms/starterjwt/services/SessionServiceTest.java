package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SessionServiceTest {

  private SessionRepository sessionRepository;
  private UserRepository userRepository;
  private SessionService sessionService;

  @BeforeEach
  void setUp() {
    sessionRepository = mock(SessionRepository.class);
    userRepository = mock(UserRepository.class);
    sessionService = new SessionService(sessionRepository, userRepository);
  }

  @Test
  void testCreateSession() {
    Session session = new Session();
    when(sessionRepository.save(any(Session.class))).thenReturn(session);

    Session createdSession = sessionService.create(session);

    assertNotNull(createdSession);
    verify(sessionRepository).save(session);
  }

  @Test
  void testDeleteSession() {
    Long sessionId = 1L;

    sessionService.delete(sessionId);

    verify(sessionRepository).deleteById(sessionId);
  }

  @Test
  void testFindAllSessions() {
    List<Session> sessions = new ArrayList<>();
    when(sessionRepository.findAll()).thenReturn(sessions);

    List<Session> result = sessionService.findAll();

    assertEquals(sessions, result);
    verify(sessionRepository).findAll();
  }

  @Test
  void testGetByIdFound() {
    Long sessionId = 1L;
    Session session = new Session();
    when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));

    Session result = sessionService.getById(sessionId);

    assertEquals(session, result);
  }

  @Test
  void testGetByIdNotFound() {
    Long sessionId = 1L;
    when(sessionRepository.findById(sessionId)).thenReturn(Optional.empty());

    Session result = sessionService.getById(sessionId);

    assertNull(result);
  }

  @Test
  void testUpdateSession() {
    Long sessionId = 1L;
    Session existingSession = new Session();
    existingSession.setId(sessionId);

    when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(existingSession));
    when(sessionRepository.save(any(Session.class))).thenReturn(existingSession);

    Session updatedSession = new Session();

    Session result = sessionService.update(sessionId, updatedSession);

    assertEquals(existingSession, result);
    verify(sessionRepository).save(updatedSession);
  }

  @Test
  void testParticipateSuccess() {
    Long sessionId = 1L;
    Long userId = 2L;

    User user = new User();
    user.setId(userId);

    Session session = new Session();
    session.setUsers(new ArrayList<>());

    when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
    when(userRepository.findById(userId)).thenReturn(Optional.of(user));

    sessionService.participate(sessionId, userId);

    assertTrue(session.getUsers().contains(user));
    verify(sessionRepository).save(session);
  }

  @Test
  void testParticipateSessionNotFound() {
    Long sessionId = 1L;
    Long userId = 2L;

    when(sessionRepository.findById(sessionId)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> {
      sessionService.participate(sessionId, userId);
    });
  }

  @Test
  void testParticipateUserNotFound() {
    Long sessionId = 1L;

    when(sessionRepository.findById(sessionId)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> {
      sessionService.participate(sessionId, 2L);
    });
  }

  @Test
  void testParticipateAlreadyParticipating() {
    Long sessionId = 1L;
    Long userId = 2L;

    User user = new User();
    user.setId(userId);

    Session session = new Session();
    List<User> users = new ArrayList<>();
    users.add(user);

    session.setUsers(users);

    when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
    when(userRepository.findById(userId)).thenReturn(Optional.of(user));

    assertThrows(BadRequestException.class, () -> {
      sessionService.participate(sessionId, userId);
    });
  }

  @Test
  void testNoLongerParticipateSuccess() {
    Long sessionId = 1L;
    Long userId = 2L;

    User user = new User();
    user.setId(userId);

    Session session = new Session();
    List<User> users = new ArrayList<>();
    users.add(user);

    session.setUsers(users);

    when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));

    sessionService.noLongerParticipate(sessionId, userId);

    assertFalse(session.getUsers().contains(user));
    verify(sessionRepository).save(session);
  }

  @Test
  void testNoLongerParticipateUserNotFound() {
    Long sessionId = 1L;

    when(sessionRepository.findById(sessionId)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> {
      sessionService.noLongerParticipate(sessionId, 2L);
    });
  }

  @Test
  void testNoLongerParticipateUserNotInSession() {
    Long sessionId = 1L;
    Long userId = 2L;

    User user = new User();
    user.setId(userId);

    Session session = new Session();
    List<User> users = new ArrayList<>();

    session.setUsers(users);

    when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));

    assertThrows(BadRequestException.class, () -> {
      sessionService.noLongerParticipate(sessionId, userId);
    });
  }

  @Test
  void testNoLongerParticipateSessionNotFound() {
    Long sessionId = 1L;
    Long userId = 2L;

    when(sessionRepository.findById(sessionId)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> {
      sessionService.noLongerParticipate(sessionId, userId);
    });
  }

  @Test
  void testNoLongerParticipateWithMultipleUsers() {
    Long sessionId = 1L;
    Long userId1 = 1L;
    Long userId2 = 2L;
    Long userId3 = 3L;

    User user1 = new User();
    user1.setId(userId1);
    User user2 = new User();
    user2.setId(userId2);
    User user3 = new User();
    user3.setId(userId3);

    Session session = new Session();
    List<User> users = new ArrayList<>();
    users.add(user1);
    users.add(user2);
    users.add(user3);

    session.setUsers(users);

    when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));

    sessionService.noLongerParticipate(sessionId, userId2);

    assertFalse(session.getUsers().contains(user2));
    assertTrue(session.getUsers().contains(user1));
    assertTrue(session.getUsers().contains(user3));
    assertEquals(2, session.getUsers().size());
    verify(sessionRepository).save(session);
  }
}
