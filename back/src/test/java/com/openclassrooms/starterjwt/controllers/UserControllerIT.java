package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class UserControllerIT {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private JwtUtils jwtUtils;

  private User testUser;
  private String jwtToken;

  @BeforeEach
  void setUp() {
    userRepository.deleteAll();
    testUser = userRepository
        .save(new User("test@example.com", "Doe", "John", passwordEncoder.encode("password"), false));

    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken("test@example.com", "password"));
    jwtToken = jwtUtils.generateJwtToken(authentication);
  }

  @Test
  void testFindById() throws Exception {
    mockMvc.perform(get("/api/user/{id}", testUser.getId())
        .header("Authorization", "Bearer " + jwtToken))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(testUser.getId()))
        .andExpect(jsonPath("$.email").value("test@example.com"))
        .andExpect(jsonPath("$.lastName").value("Doe"))
        .andExpect(jsonPath("$.firstName").value("John"));
  }

  @Test
  void testFindByIdInvalidId() throws Exception {
    mockMvc.perform(get("/api/user/{id}", "invalid")
        .header("Authorization", "Bearer " + jwtToken))
        .andExpect(status().isBadRequest());
  }

  @Test
  void testFindByIdNonExistentId() throws Exception {
    mockMvc.perform(get("/api/user/{id}", 999)
        .header("Authorization", "Bearer " + jwtToken))
        .andExpect(status().isNotFound());
  }

  @Test
  void testDeleteUserSuccessful() throws Exception {
    mockMvc.perform(delete("/api/user/{id}", testUser.getId())
        .header("Authorization", "Bearer " + jwtToken))
        .andExpect(status().isOk());

    userRepository
        .save(new User("another@example.com", "Another", "User", passwordEncoder.encode("password"), false));
    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken("another@example.com", "password"));
    String anotherJwtToken = jwtUtils.generateJwtToken(authentication);

    mockMvc.perform(get("/api/user/{id}", testUser.getId())
        .header("Authorization", "Bearer " + anotherJwtToken))
        .andExpect(status().isNotFound());
  }

  @Test
  void testDeleteUserUnauthorized() throws Exception {
    User anotherUser = userRepository
        .save(new User("another@example.com", "Smith", "Jane", passwordEncoder.encode("password"), false));

    mockMvc.perform(delete("/api/user/{id}", anotherUser.getId())
        .header("Authorization", "Bearer " + jwtToken))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void testDeleteUserNonExistent() throws Exception {
    mockMvc.perform(delete("/api/user/{id}", 999)
        .header("Authorization", "Bearer " + jwtToken))
        .andExpect(status().isNotFound());
  }

  @Test
  void testDeleteUserInvalidId() throws Exception {
    mockMvc.perform(delete("/api/user/{id}", "invalid")
        .header("Authorization", "Bearer " + jwtToken))
        .andExpect(status().isBadRequest());
  }
}
