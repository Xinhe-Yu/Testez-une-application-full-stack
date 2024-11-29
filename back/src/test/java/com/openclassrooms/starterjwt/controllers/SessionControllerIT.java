package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.SessionService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class SessionControllerIT {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private SessionRepository sessionRepository;

  @Autowired
  private TeacherRepository teacherRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private SessionService sessionService;

  @Autowired
  private ObjectMapper objectMapper;

  private Session testSession;
  private Teacher testTeacher;
  private User testUser;

  @BeforeEach
  void setUp() {
    sessionRepository.deleteAll();
    teacherRepository.deleteAll();
    userRepository.deleteAll();
    testTeacher = teacherRepository.save(Teacher.builder()
        .lastName("Doe")
        .firstName("John")
        .build());

    testUser = userRepository.save(new User("user@example.com", "User", "Test", "password", false));
    testSession = sessionRepository
        .save(new Session(null, "Test Session", new Date(), "Description", testTeacher, null, null, null));
  }

  @Test
  @WithMockUser
  void testFindById() throws Exception {
    mockMvc.perform(get("/api/session/{id}", testSession.getId()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(testSession.getId()))
        .andExpect(jsonPath("$.name").value("Test Session"));
  }

  @Test
  @WithMockUser
  void testFindAll() throws Exception {
    mockMvc.perform(get("/api/session"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(testSession.getId()))
        .andExpect(jsonPath("$[0].name").value("Test Session"));
  }

  @Test
  @WithMockUser
  void testCreate() throws Exception {
    SessionDto newSession = new SessionDto();
    newSession.setName("New Session");
    newSession.setDate(new Date());
    newSession.setDescription("New Description");
    newSession.setTeacher_id(testTeacher.getId());

    mockMvc.perform(post("/api/session")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(newSession)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("New Session"));
  }

  @Test
  @WithMockUser
  void testUpdate() throws Exception {
    SessionDto updatedSession = new SessionDto();
    updatedSession.setName("Updated Session");
    updatedSession.setDate(new Date());
    updatedSession.setDescription("Updated Description");
    updatedSession.setTeacher_id(testTeacher.getId());

    mockMvc.perform(put("/api/session/{id}", testSession.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(updatedSession)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("Updated Session"));
  }

  @Test
  @WithMockUser
  void testDelete() throws Exception {
    mockMvc.perform(delete("/api/session/{id}", testSession.getId()))
        .andExpect(status().isOk());

    mockMvc.perform(get("/api/session/{id}", testSession.getId()))
        .andExpect(status().isNotFound());
  }

  @Test
  @WithMockUser
  void testParticipate() throws Exception {
    mockMvc.perform(post("/api/session/{id}/participate/{userId}", testSession.getId(), testUser.getId()))
        .andExpect(status().isOk());

    // Refresh the session data
    sessionService.getById(testSession.getId());

    mockMvc.perform(get("/api/session/{id}", testSession.getId()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.users[0]").value(testUser.getId()));
  }

  @Test
  @WithMockUser
  void testNoLongerParticipate() throws Exception {
    // First, make the user participate
    mockMvc.perform(post("/api/session/{id}/participate/{userId}", testSession.getId(), testUser.getId()))
        .andExpect(status().isOk());

    // Then, make the user no longer participate
    mockMvc.perform(delete("/api/session/{id}/participate/{userId}", testSession.getId(), testUser.getId()))
        .andExpect(status().isOk());

    mockMvc.perform(get("/api/session/{id}", testSession.getId()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.users").isEmpty());
  }
}
