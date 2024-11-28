package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SessionControllerTest {
  @Mock
  private SessionService sessionService;

  @Mock
  private SessionMapper sessionMapper;

  @InjectMocks
  private SessionController sessionController;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testFindByIdValidIdReturnsSession() {
    Session session = new Session();
    SessionDto sessionDto = new SessionDto();
    when(sessionService.getById(1L)).thenReturn(session);
    when(sessionMapper.toDto(session)).thenReturn(sessionDto);

    ResponseEntity<?> response = sessionController.findById("1");

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(sessionDto, response.getBody());
  }

  @Test
  void testFindByIdInvalidIdReturnsBadRequest() {
    ResponseEntity<?> response = sessionController.findById("invalid");

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  void testFindByIdNonExistentIdReturnsNotFound() {
    when(sessionService.getById(1L)).thenReturn(null);

    ResponseEntity<?> response = sessionController.findById("1");

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  void testFindAllReturnsAllSessions() {
    List<Session> sessions = Arrays.asList(new Session(), new Session());
    List<SessionDto> sessionDtos = Arrays.asList(new SessionDto(), new SessionDto());
    when(sessionService.findAll()).thenReturn(sessions);
    when(sessionMapper.toDto(sessions)).thenReturn(sessionDtos);

    ResponseEntity<?> response = sessionController.findAll();

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(sessionDtos, response.getBody());
  }

  @Test
  void testFindCreateValidSessionReturnsCreatedSession() {
    SessionDto inputDto = new SessionDto();
    Session inputSession = new Session();
    Session createdSession = new Session();
    SessionDto outputDto = new SessionDto();
    when(sessionMapper.toEntity(inputDto)).thenReturn(inputSession);
    when(sessionService.create(inputSession)).thenReturn(createdSession);
    when(sessionMapper.toDto(createdSession)).thenReturn(outputDto);

    ResponseEntity<?> response = sessionController.create(inputDto);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(outputDto, response.getBody());
  }

  @Test
  void testUpdateValidIdAndSessionReturnsUpdatedSession() {
    SessionDto inputDto = new SessionDto();
    Session inputSession = new Session();
    Session updatedSession = new Session();
    SessionDto outputDto = new SessionDto();
    when(sessionMapper.toEntity(inputDto)).thenReturn(inputSession);
    when(sessionService.update(1L, inputSession)).thenReturn(updatedSession);
    when(sessionMapper.toDto(updatedSession)).thenReturn(outputDto);

    ResponseEntity<?> response = sessionController.update("1", inputDto);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(outputDto, response.getBody());
  }

  @Test
  void testUpdateInvalidIdReturnsBadRequest() {
    ResponseEntity<?> response = sessionController.update("invalid", new SessionDto());

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  void testDeleteValidIdReturnsOk() {
    when(sessionService.getById(1L)).thenReturn(new Session());

    ResponseEntity<?> response = sessionController.save("1");

    assertEquals(HttpStatus.OK, response.getStatusCode());
    verify(sessionService).delete(1L);
  }

  @Test
  void testDeleteInvalidIdReturnsBadRequest() {
    ResponseEntity<?> response = sessionController.save("invalid");

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  void testDeleteNonExistentIdReturnsNotFound() {
    when(sessionService.getById(1L)).thenReturn(null);

    ResponseEntity<?> response = sessionController.save("1");

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  void testParticipateValidIdsReturnsOk() {
    ResponseEntity<?> response = sessionController.participate("1", "2");

    assertEquals(HttpStatus.OK, response.getStatusCode());
    verify(sessionService).participate(1L, 2L);
  }

  @Test
  void testParticipateInvalidIdsReturnsBadRequest() {
    ResponseEntity<?> response = sessionController.participate("invalid", "2");

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  void testNoLongerParticipateValidIdsReturnsOk() {
    ResponseEntity<?> response = sessionController.noLongerParticipate("1", "2");

    assertEquals(HttpStatus.OK, response.getStatusCode());
    verify(sessionService).noLongerParticipate(1L, 2L);
  }

  @Test
  void testNoLongerParticipateInvalidIdsReturnsBadRequest() {
    ResponseEntity<?> response = sessionController.noLongerParticipate("invalid", "2");

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }
}
