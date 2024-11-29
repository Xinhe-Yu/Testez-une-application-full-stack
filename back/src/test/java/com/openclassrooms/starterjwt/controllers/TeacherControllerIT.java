package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TeacherControllerIT {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private TeacherRepository teacherRepository;

  @Autowired
  private SessionRepository sessionRepository;

  private Teacher testTeacher;

  @BeforeEach
  void setUp() {
    sessionRepository.deleteAll();

    teacherRepository.deleteAll();
    System.out.println("All teachers deleted successfully");

    testTeacher = new Teacher(null, "Doe", "John", null, null);
    testTeacher = teacherRepository.save(testTeacher);
    System.out.println("Test teacher saved successfully with ID: " + testTeacher.getId());
  }

  @Test
  @WithMockUser
  void testFindById() throws Exception {
    mockMvc.perform(get("/api/teacher/{id}", testTeacher.getId()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(testTeacher.getId()))
        .andExpect(jsonPath("$.lastName").value("Doe"))
        .andExpect(jsonPath("$.firstName").value("John"));
  }

  @Test
  @WithMockUser
  void testFindByIdInvalidId() throws Exception {
    mockMvc.perform(get("/api/teacher/{id}", "invalid"))
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithMockUser
  void testFindByIdNonExistentId() throws Exception {
    mockMvc.perform(get("/api/teacher/{id}", 999))
        .andExpect(status().isNotFound());
  }

  @Test
  @WithMockUser
  void testFindAll() throws Exception {
    mockMvc.perform(get("/api/teacher"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(testTeacher.getId()))
        .andExpect(jsonPath("$[0].lastName").value("Doe"))
        .andExpect(jsonPath("$[0].firstName").value("John"));
  }
}
